export interface Constructor<T> {
	new (...args: any[]): T;
	prototype: T;
}

export type InferInstance<C extends Constructor<any>> =
	C extends Constructor<infer I> ? I : never;

export interface CopyConstructor<T, A> {
	new (params: A): T;
}

export type RequiredSlice<T, K extends keyof T> = T & Required<Pick<T, K>>;

export type PartialSlice<T, K extends keyof T> = Pick<T, Exclude<keyof T, K>> & Partial<Pick<T, K>>;

export type Omit<T, K extends keyof T> = Pick<T, Exclude<keyof T, K>>;

export type DeepPartial<T> = {
	[P in keyof T]?: T[P] extends Array<infer U>
		? Array<DeepPartial<U>>
		: T[P] extends ReadonlyArray<infer U>
		? ReadonlyArray<DeepPartial<U>>
		: DeepPartial<T[P]>
};

export type NameLike = string | Named;

export interface Named {
	name: string;
}

export function toName(named: NameLike) {
	return typeof named === "string" ? named : named.name;
}
