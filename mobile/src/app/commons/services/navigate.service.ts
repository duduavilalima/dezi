import { Injectable } from "@angular/core";
import { NavController } from "ionic-angular";
import { CopyConstructor } from "../../utils/typescript";

export type NavigablePageInputParams<N extends NavigablePage<any>> = N extends NavigablePage<infer I> ? I : never;

export type NavigablePageParams<N extends NavigablePage<any>> = N extends NavigablePage<any, infer P> ? P : never;

export interface NavigablePage<TParamsInput extends object = {}, TParams extends object = {}> {
	new (...args: any[]): any;
	Params?: CopyConstructor<TParams, TParamsInput>;
	params?: TParams | ((params: TParamsInput) => TParams);
}

@Injectable()
export class Navigate {
	constructor(private navController: NavController) {}

	back() {
		return this.navController.pop();
	}

	toPage<
		TPage extends NavigablePage<any>,
		TParamsInput extends NavigablePageInputParams<TPage> = NavigablePageInputParams<TPage>,
		TParams extends NavigablePageParams<TPage> = NavigablePageParams<TPage>
	>(Page: TPage, paramsInput: TParamsInput, opts?: any) {
		const params = this.preparePageParams<TParamsInput, TParams>(Page as any, paramsInput);
		return this.navController.push(Page, params, opts);
	}

	private preparePageParams<TParamsInput extends object, TParams extends object>(
		Page: NavigablePage<TParamsInput, TParams>,
		params: TParamsInput
	): TParams {
		if (Page.Params) {
			return new Page.Params(params);
		}
		if (Page.params instanceof Function) {
			return Page.params(params);
		}
		if (Page.params) {
			const defaultParams: object = Page.params || {};
			const customParams: object = params || {};
			return {
				...defaultParams,
				...customParams,
			} as TParams;
		}
		console.debug(`Page "${Page.name}" does not have a params parser. Defaulting TParams to empty object.`);
		return params as any;
	}
}
