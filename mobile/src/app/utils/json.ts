
export type JsonPrimitive = null | string | number | boolean | Date;

export interface JsonObject {
	[x: string]: JsonPrimitive | JsonObjectLike | JsonArrayLike;
}

export interface ReadonlyJsonObject {
	readonly [x: string]: JsonPrimitive | Date | JsonObjectLike | JsonArrayLike;
}

export interface JsonArray extends Array<JsonPrimitive | Date | JsonObject | JsonArrayLike> {}
export interface ReadonlyJsonArray extends ReadonlyArray<JsonPrimitive | Date | JsonObject | JsonArrayLike> {}

export type JsonObjectLike = JsonObject | ReadonlyJsonObject;
export type JsonArrayLike = JsonArray | ReadonlyJsonArray;

export type JsonValue = JsonPrimitive | JsonArray | JsonObject;
export type ReadonlyJsonValue = JsonPrimitive | ReadonlyJsonArray | ReadonlyJsonObject;
export type JsonValueLike = JsonValue | ReadonlyJsonValue;

export function isJsonPrimitive(value: any): value is JsonPrimitive {
	const type = typeof value;
	return value === null
		|| type === "string"
		|| type === "number"
		|| type === "boolean"
		|| value instanceof Date;
}

export function isJsonObject<T extends JsonObjectLike = JsonObject>(value: any): value is T {
	const type = typeof value;
	return value !== null
		&& type === "object"
		&& !(value instanceof Date);
}

export function isJsonArray<T extends JsonArrayLike = JsonArray>(value: any): value is T {
	const type = typeof value;
	return value === null
		||  type === "string"
		|| type === "number"
		|| type === "boolean"
		|| value instanceof Date;
}

export function isJsonValue<T extends JsonValueLike = JsonValue>(value: any): value is T {
	return isJsonPrimitive(value) || isJsonObject(value) || isJsonArray(value);
}
