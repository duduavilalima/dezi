import { Omit } from "./typescript";

/**
 * Representa o _ slice_ básico das entidades de negócio manipuladas na aplicação.
 *
 * Uma entidade só é efetivamente válida se possuir mais de um campo. Ou seja, ela NÃO pode possuir
 * APENAS o campo ID, pois isso significaria que o estado da entidade contempla apenas a
 * chave substituta (i.e. _surrogate ID_), ou que não há nenhuma chave natural/candidata disponível,
 * o que não faz muito sentido em termos de modelagem de dados.
 */
export interface Entity {
	id: number;
}

/**
 * Constrói um tipo-objeto que é uma mera referência a uma entidade (i.e. possui APENAS o ID).
 */
export type EntityRef<E extends Entity> = Required<Pick<E, "id">>;

/**
 * Constrói o tipo do ID da entidade.
 */
export type EntityId<E extends Entity> = EntityRef<E>["id"];

/**
 * Constrói um tipo-objeto contento todos os campos da entidade EXCETO o ID.
 *
 * Para ser usado em métodos de criação (e.g. INSERT) de entidades, quando a entidade ainda
 * não "existe". Representa os dados de criação da entidade; se estes dados precisam conter
 * campos e/ou valores que não necessariamente farão parte da entidade após a ação de
 * inclusão/persistência (e.g. CAPTCHA), considere criar um tipo específico para a ação
 * de persistência em questão.
 */
export type EntityData<E extends Entity> = Omit<E, "id">;

/**
 * Constrói um tipo que aceita tanto uma referência quanto um ID de entidade.
 */
export type EntityRefLike<E extends Entity> = EntityId<E> | EntityRef<E>;

export type EntityLike<E extends Entity> = E | EntityRef<E>;

export function isEntityId(value: any): value is EntityId<any> {
	return typeof value === "number";
}

export function isEntityRef(value: any): value is EntityRef<any> {
	return typeof value === "object" && !!value && "id" in value && isEntityId(value.id);
}

export function isEntity(value: any): value is Entity {
	return isEntityRef(value) && Object.keys(value).length > 1;
}
