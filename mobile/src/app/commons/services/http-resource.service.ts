import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observer } from "rxjs";
import { Observable } from "rxjs/Observable";
import { removeUndefinedProperties } from "../../utils/collections";
import { isJsonValue, JsonValue } from "../../utils/json";
import {
	HttpResourceActionCallbackInfo,
	HttpResourceActionConfig,
	HttpResourceActionPathInfo,
	HttpResourceCommand,
	HttpResourceCommandInput,
	HttpResourceDescriptor,
	HttpResourceError,
	HttpResourceRequestParams,
	isFinishedHttpResourceCommand,
	mapSucceededHttpResourceCommandResult,
	ResourceAction,
	ResourceActionToHttpMap,
	ResourceAuthStatus,
	ResourceCommandStatus,
	ResourceError,
} from "../../utils/resource";
import { AuthStore } from "../stores/auth.store";
import { EnvState, EnvStore } from "../stores/env.store";

const httpResourceMethods: ResourceActionToHttpMap = {
	get: "get",
	list: "get",
	create: "post",
	update: "put",
	remove: "delete",
	search: "post",
};

@Injectable()
export class HttpResourceService {
	defaultResourceAuthStatus = ResourceAuthStatus.ANY;
	defaultPerformAuthAction = false;
	defaultUrlPrefix = "";

	constructor(private authStore: AuthStore, private angularHttpClient: HttpClient, private envStore: EnvStore) {
		this.initialize();
	}

	request<TResponse, TParams = any, TRequest = any>(
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	): Observable<HttpResourceCommand<TResponse, TParams, TRequest>> {
		return Observable.create((observer: Observer<HttpResourceCommand<TResponse, TParams, TRequest>>) => {
			try {
				this.ensureAuthStatus(input);
				const command: HttpResourceCommand<TResponse, TParams, TRequest> = {
					params: input.params,
					action: input.action,
					resource: input.resource,
					httpRequestParams: this.httpClient.createHttpRequest(input),

					id: this.generateId(),
					status: ResourceCommandStatus.CREATED,
				};
				this.httpClient.httpRequest(command).subscribe({
					next: result => {
						observer.next(result);
					},
					error: err => {
						observer.error(err);
					},
					complete: () => {
						observer.complete();
					},
				});
			} catch (err) {
				observer.error(err);
				observer.complete();
			}
		});
	}

	successfulResponse<TResponse, TParams = any, TRequest = any>(
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	) {
		return this.request(input)
			.filter(value => isFinishedHttpResourceCommand(value))
			.map(command => mapSucceededHttpResourceCommandResult(command));
	}

	response<TResponse, TParams = any, TRequest = any>(input: HttpResourceCommandInput<TResponse, TParams, TRequest>) {
		return this.request(input).map(result => result.response);
	}

	private onEnvChange = (ENV: EnvState) => {
		// if (!ENV) return;
		this.defaultUrlPrefix = ENV.apiBaseUrl;
	};

	private async initialize() {
		this.envStore.subscribe({ next: this.onEnvChange });
	}

	private httpClient = {
		createHttpRequest: <TResponse, TParams = any, TRequest = any>(
			input: HttpResourceCommandInput<TResponse, TParams, TRequest>
		): HttpResourceRequestParams<TRequest> => {
			const actionConfig = this.getActionConfig(input);
			if (input.httpClient && input.httpClient.createHttpRequest) {
				return input.httpClient.createHttpRequest(input);
			}
			return {
				url: this.createHttpRequestUrl(input),
				method: this.getHttpRequestMethod(input),
				options: {
					body: this.createHttpRequestBody(actionConfig, input),
					headers: this.createHttpRequestHeaders(actionConfig, input) || new HttpHeaders(),
					params: this.createHttpRequestParams(actionConfig, input) || new HttpParams(),
				},
			};
		},

		httpRequest: <TResponse, TParams = any, TRequest = any>(
			command: HttpResourceCommand<TResponse, TParams, TRequest>
		) => {
			const { httpRequestParams } = command;
			const { url, method, options } = httpRequestParams;
			command.status = ResourceCommandStatus.STARTED;
			const observable = this.angularHttpClient
				.request(method, url, {
					...options,
					body: options.body ? JSON.stringify(options.body) : undefined,
					observe: "response",
					responseType: "json",
					reportProgress: false,
				})
				.catch<Object, any>((err, observable) => {
					const resourceName = command.resource.name;
					throw new HttpResourceError(
						`Um erro ocorreu ao realizar a ação "${command.action}" (método HTTP: "${method}")` +
							` via Angular HttpClient para o recurso "${resourceName}": "${err.statusText}"` +
							` (provavelmente, culpa do CORS!)`,
						resourceName,
						err
					);
				})
				.map(response => this.transformHttpResponse(response, command))
				.map(response => this.transformCommand(response, command));
			return observable;
		},
	};

	private async ensureAuthStatus<TResponse, TParams = any, TRequest = any>(
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	) {
		const actionConfig = this.getActionConfig(input);
		const authStatus = this.getAuthStatus(actionConfig, input.resource);
		switch (authStatus) {
			case ResourceAuthStatus.ANY:
				return;
			case ResourceAuthStatus.AUTHENTICATED:
				if (this.authStore.isAuthenticated()) return;
				throw new ResourceError(
					`O recurso REST ${input.resource.name} requer autenticação, mas o usuário não está autenticado.`,
					input.resource.name
				);
			case ResourceAuthStatus.NON_AUTHENTICATED:
				if (!this.authStore.isAuthenticated()) return;
				throw new ResourceError(
					`O recurso REST ${input.resource.name} requer anonimato, mas o usuário está autenticado.`,
					input.resource.name
				);
		}
	}

	private getActionConfig<TResponse, TParams = any, TRequest = any>(
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	) {
		const { resource, action } = input;
		const actionConfig = resource.actions[action];
		if (!actionConfig) {
			throw new Error(`O recurso REST "${resource.name}" não suporta a ação "${action}" que foi solicitada.`);
		}
		return actionConfig;
	}

	private getAuthStatus(
		actionConfig: HttpResourceActionConfig<any>,
		resource: HttpResourceDescriptor<any, any, any>
	) {
		let authStatus: ResourceAuthStatus;
		if ("requiredAuthStatus" in actionConfig && actionConfig.requiredAuthStatus) {
			authStatus = actionConfig.requiredAuthStatus;
		} else if ("requiredAuthStatus" in resource && resource.requiredAuthStatus) {
			authStatus = resource.requiredAuthStatus;
		} else {
			authStatus = this.defaultResourceAuthStatus;
		}
		return authStatus;
	}

	private getDefaultActionPath = (params: any, actionInfo: HttpResourceActionPathInfo) => {
		switch (actionInfo.name) {
			case ResourceAction.LIST:
				return "all";
			case ResourceAction.SEARCH:
				return "search";
			case ResourceAction.GET:
			case ResourceAction.UPDATE:
			case ResourceAction.REMOVE:
				if (!params.id) {
					throw new Error(
						`Faltando ID para a ação ${actionInfo.name} no recurso ${actionInfo.resource.name}.`
					);
				}
				return String(params.id);
		}
		return "";
	};

	private createHttpRequestUrl<TResponse, TParams = any, TRequest = any>(
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	): string {
		const { resource, params } = input;
		const actionConfig = this.getActionConfig(input);
		const urlPrefix = resource.urlPrefix || this.defaultUrlPrefix;
		const basePath = resource.basePath ? `/${resource.basePath}` : "";
		const actionInfo = {
			resource,
			basePath: resource.basePath || "",
			name: input.action,
			config: actionConfig,
		};
		const actionPath =
			typeof actionConfig.path === "string"
				? actionConfig.path
				: (actionConfig.path || this.getDefaultActionPath)(params || ({} as any), actionInfo) || "";
		const path = actionPath ? `/${actionPath}` : "";
		return `${urlPrefix}${basePath}${path}`;
	}

	private getHttpRequestMethod<TResponse, TParams = any, TRequest = any>(
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	): string {
		const { action } = input;
		const actionConfig = this.getActionConfig(input);
		const method = actionConfig.method || httpResourceMethods[action];
		return method;
	}

	private createHttpRequestBody<TResponse, TParams = any, TRequest = any>(
		actionConfig: HttpResourceActionConfig<any>,
		command: HttpResourceCommandInput<TResponse, TParams, TRequest>
	): TRequest | JsonValue | undefined {
		if (command.payload instanceof Function) {
			return command.payload(command.params, command);
		} else if (isJsonValue(command.payload)) {
			return command.payload;
		} else if (actionConfig.payload) {
			const info = this.createActionInfo(actionConfig, command);
			return actionConfig.payload(command.params, info);
		}
		switch (command.action) {
			case ResourceAction.SEARCH:
				return command.params as any;
			case ResourceAction.CREATE:
			case ResourceAction.UPDATE:
				if (!isJsonValue(command.params)) {
					throw new Error(
						`Faltando corpo da requisição para a ação "${command.action}" no recurso HTTP "${
							command.resource.name
						}".`
					);
				}
				return command.params;
		}
		return command.params as any;
	}

	private createHttpRequestHeaders<TResponse, TParams = any, TRequest = any>(
		actionConfig: HttpResourceActionConfig<any>,
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	): HttpHeaders | undefined {
		const defaultHeaders: { [header: string]: string | string[] } = {
			"Content-Type": "application/json",
		};
		if (this.requiresAuthHeaders(actionConfig, input) && !!this.authStore.authenticated()) {
			const token = this.authStore.getToken();
			defaultHeaders["X-Auth-Token"] = token;
		}

		let configHeaders: { [header: string]: string | string[] };
		if (actionConfig.headers instanceof Function) {
			const info = this.createActionInfo(actionConfig, input);
			configHeaders = removeUndefinedProperties(actionConfig.headers(input.params || ({} as any), info));
		} else if (!!actionConfig.headers) {
			configHeaders = removeUndefinedProperties(actionConfig.headers);
		} else {
			configHeaders = {};
		}

		const precalculatedHeaders: { [header: string]: string | string[] } = {
			...defaultHeaders,
			...configHeaders,
		};
		let effectiveHeaders: { [header: string]: string | string[] };

		if (input.httpHeaders instanceof Function) {
			const info = this.createCommandCallbackInfo(actionConfig, input);
			effectiveHeaders = removeUndefinedProperties(
				input.httpHeaders(precalculatedHeaders, input.params || ({} as any), info)
			);
		} else if (!!input.httpHeaders) {
			effectiveHeaders = removeUndefinedProperties(input.httpHeaders);
		} else {
			effectiveHeaders = precalculatedHeaders;
		}

		return new HttpHeaders(effectiveHeaders);
	}

	private createHttpRequestParams<TResponse, TParams = any, TRequest = any>(
		actionConfig: HttpResourceActionConfig<any>,
		command: HttpResourceCommandInput<TResponse, TParams, TRequest>
	): HttpParams {
		let configHttpParams: { [param: string]: string };
		if (actionConfig.params instanceof Function) {
			const info = this.createActionInfo(actionConfig, command);
			configHttpParams = removeUndefinedProperties(actionConfig.params(command.params || {}, info));
		} else if (!!actionConfig.params) {
			configHttpParams = removeUndefinedProperties(actionConfig.params);
		} else {
			configHttpParams = command.params || {};
		}

		let effectiveParams: { [param: string]: string };
		if (command.httpParams instanceof Function) {
			const info = this.createCommandCallbackInfo(actionConfig, command);
			effectiveParams = removeUndefinedProperties(
				command.httpParams(configHttpParams, command.params || ({} as any), info)
			);
		} else if (!!command.httpParams) {
			effectiveParams = removeUndefinedProperties({
				...configHttpParams,
				...command.httpParams,
			});
		} else {
			effectiveParams = configHttpParams;
		}

		return new HttpParams({
			fromObject: effectiveParams,
		});
	}

	private generateId() {
		return (
			"_" +
			Math.random()
				.toString(36)
				.substr(2, 9)
		);
	}

	private transformHttpResponse<TResponse, TParams = any, TRequest = any>(
		response: HttpResponse<any>,
		command: HttpResourceCommand<TResponse, TParams, TRequest>
	) {
		const actionConfig = this.getActionConfig(command);
		if (actionConfig.response) {
			const info = this.createActionInfo(actionConfig, command);
			return response.clone({
				body: actionConfig.response(response.body, info),
			});
		}
		return response;
	}

	private transformCommand<TResponse, TParams = any, TRequest = any>(
		request: HttpResponse<any>,
		command: HttpResourceCommand<TResponse, TParams, TRequest>
	): HttpResourceCommand<TResponse, TParams, TRequest> {
		command.response = request.body;
		if (request.ok) {
			command.status = ResourceCommandStatus.SUCCEEDED;
		} else {
			command.status = ResourceCommandStatus.FAILED;
		}
		return command;
	}

	private requiresAuthHeaders<TResponse, TParams = any, TRequest = any>(
		actionConfig: HttpResourceActionConfig<any>,
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	) {
		const requiredAuthStatus = this.getAuthStatus(actionConfig, input.resource);
		return ResourceAuthStatus.AUTHENTICATED === requiredAuthStatus;
	}

	private createActionInfo<TResponse, TParams = any, TRequest = any>(
		actionConfig: HttpResourceActionConfig<any>,
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	) {
		const info: HttpResourceActionPathInfo = {
			name: input.action,
			config: actionConfig,
			resource: input.resource,
		};
		return info;
	}

	private createCommandCallbackInfo<TResponse, TParams = any, TRequest = any>(
		actionConfig: HttpResourceActionConfig<any>,
		input: HttpResourceCommandInput<TResponse, TParams, TRequest>
	) {
		const info: HttpResourceActionCallbackInfo<TResponse, TParams, TRequest> = {
			name: input.action,
			config: actionConfig,
			resource: input.resource,
		};
		return info;
	}
}
