import { BehaviorSubject } from "rxjs";
import { PartialObserver } from "rxjs/Observer";
import { resolveState, StateChangeRequest, StateChangeResolution, StateTransformer } from "./store";


export interface AsyncStateStorageLike {
	volatile?: boolean;
	ready(): Promise<any>;
	get(key: string): Promise<any>;
	set(key: string, value: any): Promise<any>;
	includes(key: string): Promise<boolean>;
	remove(key: string): Promise<any>;
	clear(): Promise<void>;
	length(): Promise<number>;
	keys(): Promise<string[]>;
}

export interface AsyncVolatileStateStorageLike extends AsyncStateStorageLike {
	volatile?: true;
}

export abstract class AsyncStore<TState> {
	initialized = false;

	private readyPromise: Promise<any>;
	private readyResolver: () => any;
	private readyRejector: (reason: any) => any;
	// private state$: Observable<TState>;
	private _state$: BehaviorSubject<TState>;

	constructor() {
		this._state$ = new BehaviorSubject<TState>(this.createEmptyState());
		this.readyPromise = new Promise((resolve, reject) => {
			this.readyResolver = resolve;
			this.readyRejector = reject;
		});
	}

	protected abstract createEmptyState(): TState;

	ready() {
		return this.readyPromise;
	}

	subscribe(observer: PartialObserver<TState>) {
		// this.ensureInitialized();
		return this._state$.subscribe(observer);
	}

	async getState() {
		this.ensureInitialized();
		return this._state$.getValue();
	}

	protected async setState(requestedState: TState | StateTransformer<TState>) {
		this.ensureInitialized();
		const oldState = await this.getState();
		requestedState = resolveState(requestedState, oldState);
		const { newState, ignore } = await this.filterStateChange({
			oldState,
			requestedState,
			newState: requestedState,
		});
		if (!ignore) {
			this._state$.next(newState);
		}
	}

	protected async clear() {
		await this.setState(await this.createInitialState());
	}

	protected async createInitialState() {
		return this.createEmptyState();
	}

	protected async getInitialState() {
		return this.createInitialState();
	}

	protected async filterStateChange(request: StateChangeRequest<TState>): Promise<StateChangeResolution<TState>> {
		return { ...request, ignore: false };
	}

	protected ensureInitialized(initialized = true) {
		if (!!this.initialized !== !!initialized) {
			throw new Error(`Service "${this.constructor.name}" is ${initialized ? "not yet" : "already"} initialized`);
		}
	}

	protected async initialize(): Promise<any> {
		try {
			this.ensureInitialized(false);
			const initialState = await this.getInitialState();
			if (!Object.is(initialState, this._state$.getValue())) {
				this._state$.next(initialState);
			}
			this.initialized = true;
			this.readyResolver();
		} catch (err) {
			this.readyRejector(err);
			throw err;
		}
	}

	protected async reinitialize(initialState: TState) {
		this.initialized = false;
		this._state$.next(initialState);
		this.initialized = true;
	}
}

export abstract class PersistentAsyncStore<TState> extends AsyncStore<TState> {
	protected stateStorage: AsyncStateStorageLike;
	protected storagePath: string;

	protected async getInitialState() {
		const storagePath = this.storagePath;
		await this.stateStorage.ready();
		let initialState: TState;
		if (this.stateStorage.includes(storagePath)) {
			initialState = await this.stateStorage.get(storagePath);
		} else {
			initialState = await this.createInitialState();
			await this.stateStorage.set(storagePath, initialState);
		}
		return initialState;
	}

	protected async filterStateChange(request: StateChangeRequest<TState>) {
		const { newState } = request;
		await this.stateStorage.set(this.storagePath, newState);
		return { ...request, ignore: false };
	}
}

export abstract class VolatileAsyncStore<TState> extends PersistentAsyncStore<TState> {
	protected stateStorage: AsyncVolatileStateStorageLike;

	async getState() {
		const storagePath = this.storagePath;
		const included = await this.stateStorage.includes(storagePath);
		if (!included) {
			const state = await this.getInitialState();
			await this.reinitialize(state);
		}
		return super.getState();
	}
}
