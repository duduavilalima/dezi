import { Component, EventEmitter, Input, Output } from "@angular/core";
import { AvaliacaoCompetenciaTO } from "../../avaliacao.transfer";


@Component({
	selector: "app-competencia-progress",
	templateUrl: "competencia-progress.html",
})
export class CompetenciaProgress {
	@Output() selectedCompetencia = new EventEmitter<AvaliacaoCompetenciaTO>();

	private _listaCompetencias: AvaliacaoCompetenciaTO[];

	constructor() {}

	@Input()
	set listaCompetencias(value: AvaliacaoCompetenciaTO[]) {
		this._listaCompetencias = value;
	}

	get listaCompetencias() {
		return this._listaCompetencias;
	}

	onClickCompetencia = (competencia: AvaliacaoCompetenciaTO) => {
		this.selectedCompetencia.emit(competencia);
	};
}
