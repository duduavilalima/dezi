import { Injectable } from "@angular/core";
import { TranslateService } from "@ngx-translate/core";

export type TranslatorValues = {
	[name: string]: any;
};

@Injectable()
export class Translator {

	constructor(
		private translateService: TranslateService,
	) {}

	syncGet(name: string): string;
	syncGet(name: string[]): string[];
	syncGet(name: string, params: TranslatorValues): string;
	syncGet(name: string[], params: TranslatorValues): string[];
	syncGet(name: string | string[], params?: TranslatorValues): string | string[] {
		return this.translateService.instant(name, params);
	}

	async get(name: string): Promise<string>;
	async get(name: string[]): Promise<string[]>;
	async get(name: string, params: TranslatorValues): Promise<string>;
	async get(name: string[], params: TranslatorValues): Promise<string[]>;
	async get(name: string[]): Promise<string[]>;
	async get(name: string | string[], params?: TranslatorValues): Promise<string | string[]> {
		return this.translateService.get(name, params).toPromise();
	}
}
