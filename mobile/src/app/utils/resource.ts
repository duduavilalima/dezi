import { HttpHeaders, HttpParams, HttpResponse } from "@angular/common/http";
import { Observable } from "rxjs/Observable";
import { isObject } from "util";
import { ensureNumber } from "./guards";
import { JsonValue } from "./json";
import { DeepPartial, Omit } from "./typescript";

export enum ResourceAuthStatus {
	ANY,
	AUTHENTICATED,
	// TO_AUTHENTICATED,
	NON_AUTHENTICATED,
	// TO_NON_AUTHENTICATED,
}

export enum ResourceCommandStatus {
	CREATED = "CREATED",
	STARTED = "STARTED",
	SUCCEEDED = "SUCCEEDED",
	FAILED = "FAILED",
}

export type HttpResourceMethodName = "get" | "post" | "put" | "delete";
export type ResourceActionName = "get" | "list" | "search" | "create" | "update" | "remove";

export enum HttpResourceMethod {
	GET = "get",
	POST = "post",
	PUT = "put",
	DELETE = "delete",
}

export enum ResourceAction {
	GET = "get",
	LIST = "list",
	SEARCH = "search",
	CREATE = "create",
	UPDATE = "update",
	REMOVE = "remove",
}

export type ResourceActionToHttpMap = { [resourceMethod in ResourceActionName]: HttpResourceMethodName };

export interface EntityResource {
	id?: number;
}

export interface EntityResourceFilterInput {
	id?: number;
}

export type EntityResourceRef<E extends EntityResource> = Required<Pick<E, "id">>;
export type EntityResourceId<E extends EntityResource> = EntityResourceRef<E>["id"];
export type EntityResourceData<E extends EntityResource> = Omit<E, "id">;
export type EntityResourceFilter<E extends EntityResource> = DeepPartial<E>;
export type EntityResourceRefLike<E extends EntityResource> = EntityResourceId<E> | EntityResourceRef<E>;

export interface HttpResourceActionPathInfo {
	name: string;
	config: HttpResourceActionConfig<any>;
	resource: HttpResourceDescriptor<any, any, any>;
}

export interface HttpResourceActionConfig<TParams> {
	path?: string | ((params: TParams, action: HttpResourceActionPathInfo) => string);
	method?: HttpResourceMethodName | HttpResourceMethod;
	requiredAuthStatus?: ResourceAuthStatus;
	headers?:
		| { [param: string]: string | string[] | undefined }
		| ((params: TParams, info: HttpResourceActionPathInfo) => { [param: string]: string | undefined });
	params?:
		| { [param: string]: string | undefined }
		| ((params: TParams, info: HttpResourceActionPathInfo) => { [param: string]: string | undefined });
	payload?: (params: TParams, info: HttpResourceActionPathInfo) => JsonValue;
	response?: (responseBody: JsonValue, info: HttpResourceActionPathInfo) => any;
}

export type HttpResourceActionConfigMap<
	TEntity extends EntityResource,
	TCreateInput extends object,
	TFilterInput extends EntityResourceFilterInput
> = {
	[resourceMethod in ResourceActionName]?: HttpResourceActionConfig<
		resourceMethod extends "create"
			? TCreateInput
			: resourceMethod extends "update"
			? TEntity
			: resourceMethod extends "remove" | "get"
			? Pick<TEntity, "id">
			: TFilterInput
	>
};

export interface HttpResourceRequestParams<TRequest> {
	method: string;
	url: string;
	options: {
		body?: TRequest | JsonValue;
		headers:
			| HttpHeaders
			| {
					[header: string]: string | string[];
			  };
		params:
			| HttpParams
			| {
					[header: string]: string;
			  };
	};
}

export class ResourceError extends Error {
	constructor(message: string, public resourceName: string) {
		super(message);
	}
}

export class HttpResourceError extends ResourceError {
	constructor(message: string, resourceName: string, public response: HttpResponse<any>) {
		super(message, resourceName);
	}
}

export interface HttpResourceClient<TResponse, TParams = any, TRequest = any> {
	createHttpRequest?(
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	): HttpResourceRequestParams<TRequest>;
	httpRequest?(
		command: HttpResourceCommand<TResponse, TParams, TRequest>
	): Observable<HttpResourceCommand<TResponse>>;
}

export interface HttpResourceDescriptor<
	TEntity extends EntityResource,
	TCreateInput extends object = EntityResourceData<TEntity>,
	TFilterInput extends EntityResourceFilterInput = EntityResourceFilter<EntityResource>
> {
	name: string;
	basePath?: string;
	urlPrefix?: string;
	requiredAuthStatus?: ResourceAuthStatus;
	actions: HttpResourceActionConfigMap<TEntity, TCreateInput, TFilterInput>;
}

export interface HttpResourceActionCallbackInfo<TResponse, TParams, TRequest> {
	name: string;
	config: HttpResourceActionConfig<any>;
	resource: HttpResourceDescriptor<any>;
}

export interface HttpResourceCommandInput<TResponse, TParams = any, TRequest = any> {
	action: ResourceActionName;
	resource: HttpResourceDescriptor<any, any, any>;
	params?: TParams;
	payload?: TRequest;
	httpParams?:
		| { [header: string]: string | undefined }
		| ((
				currentHttpParams: { [param: string]: string },
				params: TParams,
				input: HttpResourceActionCallbackInfo<TResponse, TParams, TRequest>
		  ) => { [param: string]: string | undefined });
	httpHeaders?:
		| { [header: string]: string | string[] | undefined }
		| ((
				currentHttpHeaders: { [header: string]: string | string[] },
				params: TParams,
				input: HttpResourceActionCallbackInfo<TResponse, TParams, TRequest>
		  ) => { [header: string]: string | string[] | undefined });
	httpClient?: HttpResourceClient<TResponse, TParams, TRequest>;
}

export interface HttpResourceCommand<TResponse, TParams = any, TRequest = any>
	extends Omit<HttpResourceCommandInput<TResponse, TParams, TRequest>, "httpParams"> {
	id: string;
	status: ResourceCommandStatus;
	response?: TResponse;
	httpRequestParams: HttpResourceRequestParams<TRequest>;
	httpClient?: HttpResourceClient<TResponse, TParams, TRequest>;
}

export interface FinishedHttpResourceCommand<TResponse, TParams = any, TRequest = any>
	extends HttpResourceCommand<TResponse, TParams, TRequest> {
	status: ResourceCommandStatus.SUCCEEDED | ResourceCommandStatus.FAILED;
}

export interface HttpResourceExecutor {
	request<TResponse, TParams = any, TRequest = any>(
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	): Observable<HttpResourceCommand<TResponse, TParams, TRequest>>;
}

export function isFinishedHttpResourceCommand(
	resource: HttpResourceCommand<any>
): resource is FinishedHttpResourceCommand<any> {
	const finishedStatuses = [ResourceCommandStatus.SUCCEEDED, ResourceCommandStatus.FAILED];
	return finishedStatuses.indexOf(resource.status) >= 0;
}

export function mapSucceededHttpResourceCommandResult<TResponse>(command: HttpResourceCommand<TResponse>) {
	if (ResourceCommandStatus.SUCCEEDED !== command.status) {
		throw new Error(
			`Comando "${command.constructor.name}" para o recurso "${
				command.resource.name
			}" não foi finalizado com sucesso.`
		);
	}
	return command.response;
}

export function toEntityResourceId<E extends EntityResource>(refLike: EntityResourceRefLike<E>): EntityResourceId<E> {
	if (typeof refLike === "number") return refLike;
	if (typeof refLike === "object") return ensureNumber(refLike);
	throw new Error("ID não pode ser nulo.");
}

export function toEntityResourceRef<E extends EntityResource>(refLike: EntityResourceRefLike<E>): EntityResourceRef<E> {
	if (typeof refLike === "number") return { id: refLike };
	if (typeof refLike === "object") return { id: refLike.id };
	throw new Error("ID não pode ser nulo.");
}

export type HttpResourceDescriptorFor<T> = T extends ReadonlyHttpEntityResource<infer A, infer B, infer C>
	? HttpResourceDescriptor<A, B, C>
	: never;

export abstract class ReadonlyHttpEntityResource<
	TEntity extends EntityResource,
	TCreateInput extends object = EntityResourceData<TEntity>,
	TFilterInput extends EntityResourceFilterInput = EntityResourceFilter<EntityResource>
> {
	protected abstract httpResource: HttpResourceExecutor;
	protected descriptor: HttpResourceDescriptor<TEntity, TCreateInput, TFilterInput>;

	protected objectToEntity(value: { [field: string]: any }): TEntity {
		return value as any;
	}

	async get(refLike: EntityResourceRefLike<TEntity>): Promise<TEntity | undefined> {
		return await this.httpResource
			.request(await this.getCommand(refLike))
			.filter(value => isFinishedHttpResourceCommand(value))
			.map(command => mapSucceededHttpResourceCommandResult(command))
			.map(this.convertJsonObjectToEntity)
			.toPromise();
	}

	async getOne(id: number): Promise<TEntity | undefined> {
		const entity = await this.get(id);
		if (!entity) throw new Error(`Não existe nenhum(a) "${this.descriptor.name}" com ID "${id}".`);
		return entity;
	}

	async list(params?: TFilterInput): Promise<TEntity[]> {
		return await this.httpResource
			.request(await this.listCommand(params))
			.filter(value => isFinishedHttpResourceCommand(value))
			.map(command => mapSucceededHttpResourceCommandResult(command) || [])
			.map(this.convertJsonListToEntities)
			.toPromise();
	}

	async search(input: TFilterInput) {
		return await this.httpResource
			.request(await this.searchCommand(input))
			.filter(value => isFinishedHttpResourceCommand(value))
			.map(command => mapSucceededHttpResourceCommandResult(command) || [])
			.map(this.convertJsonListToEntities)
			.toPromise();
	}

	protected async getCommand(refLike: EntityResourceRefLike<TEntity>): Promise<HttpResourceCommandInput<TEntity>> {
		const id = toEntityResourceId(refLike);
		return {
			action: ResourceAction.GET,
			resource: this.descriptor,
			params: { id },
		};
	}

	protected async listCommand(params?: TFilterInput): Promise<HttpResourceCommandInput<TEntity[]>> {
		return {
			params,
			action: ResourceAction.LIST,
			resource: this.descriptor,
		};
	}

	protected async searchCommand(input: TFilterInput): Promise<HttpResourceCommandInput<TEntity[]>> {
		return {
			action: ResourceAction.SEARCH,
			resource: this.descriptor,
			params: input,
			payload: input,
		};
	}

	protected convertJsonObjectToEntity = (entity: any): TEntity => {
		if (!isObject(entity)) {
			throw new TypeError(
				`Tipo de valor "${typeof entity}" inválido retornado para chamada à método de recurso HTTP!`
			);
		}
		if (!!this.objectToEntity) return this.objectToEntity(entity);
		return entity;
	};

	protected convertJsonListToEntities = (entity: any[]): TEntity[] => {
		if (this.objectToEntity) return entity.map(this.objectToEntity);
		return entity;
	};
}

export abstract class MutableHttpEntityResource<
	TEntity extends EntityResource,
	TCreateInput extends EntityResourceData<TEntity> = EntityResourceData<TEntity>,
	TFilterInput extends EntityResourceFilterInput = EntityResourceFilter<EntityResource>
> extends ReadonlyHttpEntityResource<TEntity, TCreateInput, TFilterInput> {
	async create(input: TCreateInput): Promise<any> {
		return await this.httpResource
			.request(await this.createCommand(input))
			.filter(value => isFinishedHttpResourceCommand(value))
			.map(command => mapSucceededHttpResourceCommandResult(command))
			.map(this.convertJsonObjectToEntity)
			.toPromise();
	}

	async update(input: TEntity): Promise<any> {
		return await this.httpResource
			.request(await this.updateCommand(input))
			.filter(value => isFinishedHttpResourceCommand(value))
			.map(command => mapSucceededHttpResourceCommandResult(command))
			.map(this.convertJsonObjectToEntity)
			.toPromise();
	}

	async remove(refLike: EntityResourceRefLike<TEntity>): Promise<any> {
		return await this.httpResource
			.request(await this.removeCommand(refLike))
			.filter(value => isFinishedHttpResourceCommand(value))
			.map(command => mapSucceededHttpResourceCommandResult(command))
			.map(this.convertJsonObjectToEntity)
			.toPromise();
	}

	protected async createCommand(input: TCreateInput): Promise<HttpResourceCommandInput<any>> {
		return {
			action: ResourceAction.CREATE,
			resource: this.descriptor,
			params: { input },
			payload: input,
		};
	}

	protected async updateCommand(input: TEntity): Promise<HttpResourceCommandInput<any>> {
		return {
			action: ResourceAction.UPDATE,
			resource: this.descriptor,
			params: { input },
			payload: input,
		};
	}

	protected async removeCommand(refLike: EntityResourceRefLike<TEntity>): Promise<HttpResourceCommandInput<any>> {
		const id = toEntityResourceId(refLike);
		return {
			action: ResourceAction.REMOVE,
			resource: this.descriptor,
			params: { id },
		};
	}
}
