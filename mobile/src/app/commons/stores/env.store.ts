import { Injectable } from "@angular/core";
import { environment } from "../../../environments/environment";
import { environments, EnvName } from "../../../environments/environment.base";
import { STORAGE_PATH } from "../../constants/storage";
import { AsyncStore } from "../../utils/async-store";

type EnvValueConverter<TOut, TIn = any> = (value: TIn, found: boolean) => TOut;

export type EnvState = typeof environment & {
	updatedAt: Date;
	createdAt: Date;
};

@Injectable()
export class EnvStore extends AsyncStore<EnvState> {
	protected storagePath = STORAGE_PATH.for(EnvStore);

	constructor() {
		super();
		this.initialize();
	}

	async get<TType = EnvState>(converter?: EnvValueConverter<TType>): Promise<TType>;
	async get<TType = any>(key: string, converter?: (value: any) => TType): Promise<TType | undefined>;
	async get<TType = any>(
		key?: string | EnvValueConverter<TType>,
		converter?: EnvValueConverter<TType>
	): Promise<EnvState | TType | undefined> {
		await this.ready();
		converter = converter || ((value: any) => value);
		const config: EnvState = await this.getState();
		if (key instanceof Function) {
			return key(config, true);
		}
		if (key) {
			const subKeys = key.split(".");
			let result: any = config;
			for (const subKey of subKeys) {
				if (result === undefined) {
					return converter(undefined, false);
				}
				result = result[subKey];
			}
			return converter(result, true);
		}
		return config;
	}

	async select(envName: EnvName): Promise<EnvState> {
		const config = environments[envName];
		const currentState = await this.getState();
		const newState = this.createUpdatedState(config, currentState);
		await this.setState(newState);
		return newState;
	}

	protected createEmptyState() {
		// const now = new Date();
		const initialState = this.createUpdatedState(environment, undefined);
		return initialState;
	}

	private createUpdatedState(base: typeof environment, oldState: Partial<EnvState> | undefined) {
		oldState = oldState || {};
		const updatedAt = new Date();
		return {
			...oldState,
			...base,
			updatedAt,
			createdAt: oldState.createdAt || updatedAt,
		};
	}
}
