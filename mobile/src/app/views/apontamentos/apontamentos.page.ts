import { Component, Injector } from "@angular/core";

import { ApontamentoResource } from './apontamentos.resource';
import {
	ApontamentoFuncionarioTO,
	ApontamentoTipoCompetenciaTO,
} from '../utils/apontamentoFuncional/transfer/apontamento.transfer';
import {
	ApontamentoPageState,
	createApontamentoPageState,
} from '../utils/apontamentoFuncional/apontamentoFuncionalPage.state';
import { ProtectedPage } from '../utils/page';
import { NovoApontamentoPage } from './novo-apontamento/novo-apontamento.page';

@Component({
	selector: "app-apontamentos",
	templateUrl: "./apontamentos.page.html",
})
export class ApontamentosPage extends ProtectedPage {

	protected reenterMessage = undefined;

	private state: ApontamentoPageState;

	constructor(
		injector: Injector,
		apontamentoResource: ApontamentoResource,
	) {
		super(injector);
		this.state = createApontamentoPageState(apontamentoResource);
	}

	get avaliado() { return this.state.avaliado; }
	get tipoCompetencia() { return this.state.tipoCompetencia; }
	get avaliados() { return this.state.avaliados; }
	get tiposCompetencia() { return this.state.tiposCompetencia; }
	get apontamentos() { return this.state.apontamentosFuncionais; }

	onSelectedAvaliado = async (avaliado?: ApontamentoFuncionarioTO) => {
		await this.state.selectAvaliado(avaliado);
	}

	onSelectedTipoCompetencia = async (tipoCompetencia?: ApontamentoTipoCompetenciaTO) => {
		await this.state.selectTipoCompetencia(tipoCompetencia);
	}

	onClickNovoApontamento = () => {
		this.navController.push(NovoApontamentoPage, {
			avaliado: this.avaliado!,
			tipoCompetencia: this.tipoCompetencia!,
		});
	};

	protected async onEnter() {
		await this.state.enterPage();
	}

	protected async onReenter() {
		await this.state.reenterPage();
	}
}
