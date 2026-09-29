import { Constructor } from "./typescript";

export type Something = boolean | string | number | symbol | object;
export type Nothing = undefined | null;
export type AsSomething<T> = T extends Something ? T : never;

export function isSomething<T = Something>(value: any): value is AsSomething<T> {
	return value !== undefined && value !== null;
}

export function is<T>(constructor: Constructor<T>) {
	return (value: any): value is T => value instanceof constructor;
}

export function isNumber(value: any): value is number {
	return typeof value === "number";
}

export function isString(value: any): value is string {
	return typeof value === "string";
}

export function isBoolean(value: any): value is boolean {
	return typeof value === "boolean";
}

export function isObject<T extends object = object>(value: any): value is T {
	return typeof value === "object" && !!value;
}

export function ensure<T>(isValid: (value: any) => value is T, message = "Incorrect JavaScript type.") {
	return <R = T>(value: any, converter?: (value: T) => R): R => {
		if (isValid(value)) {
			if (converter) return converter(value);
			return value as any;
		}
		throw new TypeError(message);
	};
}

export function ensureOr<T>(isValid: (value: any) => value is T, fallback: T) {
	return (value: any): T => {
		if (isValid(value)) return value;
		return fallback;
	};
}

export function ensureBoolean(value: any, message = "Value must be a boolean"): boolean {
	if (isBoolean(value)) return value;
	throw new TypeError(message);
}

export function ensureSomething<T = Something>(value: any, message = "Value must be a boolean"): AsSomething<T> {
	if (isSomething<T>(value)) return value;
	throw new TypeError(message);
}

export function ensureObject<T extends object | Nothing = any>(value: T | Nothing, message?: string): NonNullable<T>;
export function ensureObject<T extends object = object, R extends object = T>(value: T, converter: (value: T) => R): R;
export function ensureObject<T extends object = any, R extends object = T>(value: any, converter: (value: T) => R): R;
export function ensureObject<T extends object = any, R extends object = T>(
	value: any,
	converter?: string | ((value: T) => R)
): T | R {
	const message = typeof converter === "string" ? converter : "Value must be an object";
	converter = typeof converter === "function" ? converter : (value: T): R => value as any;
	if (isObject<T>(value)) {
		if (converter) return converter(value);
		return value;
	}
	throw new TypeError(message);
}

export function booleanOr(value: any, fallback: boolean): boolean {
	if (isBoolean(value)) return value;
	return fallback;
}

export function ensureBooleanOr(value: any, fallback: boolean, message = "Value must be a boolean"): boolean {
	if (isBoolean(value)) return value;
	if (!isSomething(value)) throw new TypeError(message);
	return fallback;
}

export const ensureString = ensure(isString);
export const ensureStringOr = (fallback: string) => ensureOr(isString, fallback);
export const ensureStringOrEmpty = ensureStringOr("");
export const ensureNumber = ensure(isNumber);
export const ensureDate = ensure(is(Date));
