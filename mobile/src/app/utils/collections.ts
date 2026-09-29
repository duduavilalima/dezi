export function keysOf<T>(value: T) {
	return Object.keys(value) as (keyof T)[];
}

export function removeDuplicatesById<T extends { id: any } = { id: any }>(elements: ReadonlyArray<T>) {
	const ids = elements.map(elem => elem.id);
	return elements.filter((value, index) => index === ids.indexOf(value.id));
}

export function removeUndefinedProperties<T, R extends {
	[K in keyof T]: NonNullable<T[K]>;
} = {
	[K in keyof T]: NonNullable<T[K]>;
}>(
	value: T,
): R {
	for (let key of Object.keys(value)) {
		if (value[key] === undefined) delete value[key];
	}
	return value as any;
}

export function toString(value: undefined | null): undefined;
export function toString<T>(value: T | undefined | null): string | undefined;
export function toString<T>(value: T): string;
export function toString(value: any): string | undefined;
export function toString(value: any): string | undefined {
	return value !== undefined && value !== null ? String(value) : undefined;
}
