import { Injectable } from "@angular/core";

import { ensureString } from "../../utils/guards";
import { Platform } from "ionic-angular";

@Injectable()
export class LocalStorage {

	volatile: true;

	private storage = window.localStorage;

	get(key: string) {
		if (this.includes(key)) {
			const json = ensureString(this.storage.getItem(key));
			return JSON.parse(json);
		}
		return undefined;
	}

	set(key: string, value: any) {
		this.storage.setItem(key, JSON.stringify(value));
	}

	includes(key: string) {
		return this.keys().indexOf(key) >= 0;
	}

	remove(key: string): void {
		this.storage.removeItem(key);
	}

	clear(): void {
		this.storage.clear();
	}

	length(): number {
		return this.storage.length;
	}

	keys(): string[] {
		const keys: string[] = [];
		for (let i = 0; i < this.storage.length; i++) {
			const key = ensureString(this.storage.key(i));
			keys.push(key);
		}
		return keys;
	}
}

@Injectable()
export class AsyncLocalStorage {

	volatile: true;

	constructor(
		private platform: Platform,
		private localStorage: LocalStorage,
	) {}

	async ready(): Promise<any> {
		return this.platform.ready();
	}

	async get(key: string): Promise<any> {
		await this.ready();
		return this.localStorage.get(key);
	}

	async set(key: string, value: any): Promise<any> {
		await this.ready();
		return this.localStorage.set(key, value);
	}

	async includes(key: string): Promise<boolean> {
		await this.ready();
		return this.localStorage.includes(key);
	}

	async remove(key: string): Promise<any> {
		await this.ready();
		return this.localStorage.remove(key);
	}

	async clear(): Promise<void> {
		await this.ready();
		return this.localStorage.clear();
	}

	async length(): Promise<number> {
		await this.ready();
		return this.localStorage.length();
	}

	async keys(): Promise<string[]> {
		await this.ready();
		return this.localStorage.keys();
	}
}
