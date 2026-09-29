import { Component, Input } from "@angular/core";

@Component({
	selector: 'app-descricao-competencia',
	templateUrl: 'descricao-competencia.component.html',
})
export class DescricaoCompetenciaComponent {
	private _direcionamentoCompetencia: string;
	private _descricaoCompetencia: string;

	public isExpandable: boolean = false;
	public iconName: string = 'arrow-down';

	constructor() {
	}

	@Input()
	set descricaoCompetencia(value: string) {
		this._descricaoCompetencia = value;
	}

	get descricaoCompetencia() {
		return this._descricaoCompetencia;
	}

	@Input()
	set direcionamentoCompetencia(value: string) {
		this._direcionamentoCompetencia = value;
	}

	get direcionamentoCompetencia() {
		return this._direcionamentoCompetencia;
	}

	onExpandText() {
		if(this.isExpandable) {
			this.isExpandable = false;
			this.iconName = 'arrow-down';
		}
		else {
			this.isExpandable = true;
			this.iconName = 'arrow-up';
		}
	}

}

