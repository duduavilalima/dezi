import { Component, Input, ViewChild } from "@angular/core";

import { Slides } from "ionic-angular";
import { AvaliacaoTipoCompetenciaTO } from "../../avaliacao.transfer";


@Component({
	selector: "app-tipo-competencia-info",
	templateUrl: "tipo-competencia-info.html",
})
export class TipoCompetenciaInfo {
	@ViewChild(Slides) slides: Slides;

	private _avTipoCompetencia: AvaliacaoTipoCompetenciaTO;
	public qtdRespostas: number = 0;
	public qtdPerguntas: number = 0;
	public percentual: number = 0;
	constructor() {}

	get avTipoCompetencia() {
		return this._avTipoCompetencia;
	}

	@Input()
	set avTipoCompetencia(value: AvaliacaoTipoCompetenciaTO) {
		this._avTipoCompetencia = value;
		//this.calcPercentHandler(this._avTipoCompetencia);
	}

	/* calcPercentHandler = (avaliacaoTipoCompetencia: AvaliacaoTipoCompetenciaTO) => {
		let qtdPreenchidas: number = 0;
		(avaliacaoTipoCompetencia.listaCompetencias || []).forEach(competencia => {
			if (competencia.qtdPerguntas && competencia.qtdPerguntas === competencia.qtdRespostas) {
				qtdPreenchidas++;
			}
		});
		this.qtdRespostas = qtdPreenchidas;
		this.qtdPerguntas = avaliacaoTipoCompetencia.listaCompetencias!.length;

		this.percentual = Math.round((qtdPreenchidas * 100) / avaliacaoTipoCompetencia.listaCompetencias!.length);
	}; */
}
