import { toName, NameLike } from "../utils/typescript";

const base = "fasolti/dezi";

export const STORAGE_PATH = {
	base,
	for: (name: NameLike) => `${base}/${toName(name)}`,
};
