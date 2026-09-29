import { Component, Input, OnInit, Output, EventEmitter } from "@angular/core";

@Component({
	selector: "app-page-indicator",
	templateUrl: "app-page-indicator.component.html",
})
export class PageIndicatorComponent implements OnInit {
	private _paginatorLength: number;
	private _activePage: number;
	public pageAsArray: number[];
	@Output() selectedPage = new EventEmitter<number>();
	constructor() {}

	ngOnInit() {
		this.pageAsArray = Array.from({ length: this._paginatorLength }, (_, k) => k);
		this._activePage = 0;
	}

	get paginatorLength() {
		return this._paginatorLength;
	}

	@Input()
	set paginatorLength(value: number) {
		this._paginatorLength = value;
	}

	get activePage() {
		return this._activePage;
	}

	@Input()
	set activePage(value: number) {
		this._activePage = value;
	}

	onChangePage(page: number) {
		this.selectedPage.emit(page);
	}
}
