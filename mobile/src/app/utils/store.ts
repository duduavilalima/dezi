import { Observable, BehaviorSubject } from "rxjs";

import { PartialObserver } from "rxjs/Observer";

export enum StoreStatus {
	CREATED,
	INITIALIZING,
	REINITIALIZING,
	READY,
}

export type StateTransformer<TState, TPrevious extends TState = TState> = (current: TPrevious) => TState;
export type StateInput<TState, TPrevious extends TState = TState> = TState | StateTransformer<TState, TPrevious>;

export interface StateStorageLike {
	volatile?: boolean;
	get(key: string): any;
	set(key: string, value: any): void;
	includes(key: string): boolean;
	remove(key: string): void;
	clear(): void;
	length(): number;
	keys(): string[];
}

export interface VolatileStateStorageLike extends StateStorageLike {
	volatile?: true;
}

export interface StateChangeRequest<TState> {
	requestedState: TState;
	oldState: TState;
	newState: TState;
}

export interface StateChangeResolution<TState> {
	requestedState: TState;
	oldState: TState;
	newState: TState;
	ignore: boolean;
}

export abstract class Store<TState> {
	status: StoreStatus;

	private state: TState;
	private state$: Observable<TState>;
	private _state$: BehaviorSubject<TState>;

	protected abstract createInitialState(): TState;

	subscribe(observer: PartialObserver<TState>) {
		this.ensureReady();
		return this.state$.subscribe(observer);
	}

	getState() {
		this.ensureReady();
		return this.state;
	}

	isReady() {
		return StoreStatus.READY === this.status;
	}

	ready() {
		if (!this.isReady()) {
			if (this.canInitialize()) this.initialize();
		}
	}

	protected async setState(requestedState: TState | StateTransformer<TState>) {
		this.ensureReady();
		const oldState = this.getState();
		requestedState = resolveState(requestedState, oldState);
		const { newState, ignore } = this.filterStateChange({ oldState, requestedState, newState: requestedState });
		if (!ignore) {
			this.state = newState;
			this._state$.next(newState);
		}
	}

	protected clear() {
		this.setState(this.createInitialState());
	}

	protected getInitialState() {
		return this.createInitialState();
	}

	protected getReinitializedState() {
		return this.getInitialState();
	}

	protected filterStateChange(request: StateChangeRequest<TState>): StateChangeResolution<TState> {
		return { ...request, ignore: false };
	}

	protected ensureReady(initialized = true) {
		const initializedStatus = StoreStatus.READY === this.status;
		if (initializedStatus !== !!initialized) {
			throw new Error(
				`Service "${this.constructor.name}" is ${!!initialized ? "not yet" : "already"} initialized`
			);
		}
	}

	protected reinitialize() {
		this.status = StoreStatus.REINITIALIZING;
		this._state$.next(this.getReinitializedState());
		this.status = StoreStatus.READY;
	}

	protected initialize() {
		this.ensureReady(false);
		const initialState = this.getInitialState();
		this.state = initialState;
		this._state$ = new BehaviorSubject(initialState);
		this.state$ = this._state$.asObservable();
		this.status = StoreStatus.READY;
	}

	protected canInitialize() {
		return StoreStatus.CREATED === this.status;
	}

	protected canReinitialize() {
		return StoreStatus.READY === this.status;
	}
}

export function resolveState<TState>(state: StateInput<TState>, oldState: TState) {
	if (state instanceof Function) {
		state = state(oldState);
	}
	return state;
}

export abstract class PersistentStore<TState> extends Store<TState> {
	protected stateStorage: StateStorageLike;
	protected storagePath: string;

	protected getInitialState() {
		const storagePath = this.storagePath;
		let initialState: TState;
		if (this.stateStorage.includes(storagePath)) {
			initialState = this.stateStorage.get(storagePath);
		} else {
			initialState = this.createInitialState();
			this.stateStorage.set(storagePath, initialState);
		}
		return initialState;
	}

	protected filterStateChange(request: StateChangeRequest<TState>) {
		const { newState } = request;
		this.stateStorage.set(this.storagePath, newState);
		return { ...request, ignore: false };
	}
}

export abstract class VolatileStore<TState> extends PersistentStore<TState> {
	protected stateStorage: VolatileStateStorageLike;

	getState() {
		const storagePath = this.storagePath;
		if (!this.stateStorage.includes(storagePath)) {
			this.reinitialize();
		}
		return super.getState();
	}
}
