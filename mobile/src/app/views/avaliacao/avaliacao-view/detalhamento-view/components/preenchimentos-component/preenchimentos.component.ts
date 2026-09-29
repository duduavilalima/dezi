import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { Platform } from "ionic-angular";
import { AvaliacaoGraduacaoTO, AvaliacaoPreenchimentoTO, TipoClientTO } from "../../../../avaliacao.transfer";

@Component({
	selector: "app-preenchimentos",
	templateUrl: "preenchimentos.component.html",
})
export class PreenchimentosComponent implements OnInit {
	private _preenchimentoList: AvaliacaoPreenchimentoTO[];
	private _graduacaoList: AvaliacaoGraduacaoTO[];

	@Output() changedPreenchimento = new EventEmitter<AvaliacaoPreenchimentoTO>();

	public graduacaoSize: number;
	public selectedGraduacao: number = 0;
	public height: number;
	private client: TipoClientTO;
	constructor(private platform: Platform) { }

	ngOnInit() {
		this.graduacaoSize = this._graduacaoList.length;

		this.platform.ready().then(() => {
			this.height = this.platform.height();
			this.getTipoClient();
		});
	}

	@Input()
	set preenchimentoList(value: AvaliacaoPreenchimentoTO[]) {
		this._preenchimentoList = value;
		this._preenchimentoList.forEach(preench => preench.tipoClient = this.client);
	}

	get preenchimentoList() {
		return this._preenchimentoList;
	}

	@Input()
	set graduacaoList(value: AvaliacaoGraduacaoTO[]) {
		this._graduacaoList = value;
	}

	get graduacaoList() {
		return this._graduacaoList;
	}

	onChangeGraduacao(event, preenchimento: AvaliacaoPreenchimentoTO) {
		let graduacao: AvaliacaoGraduacaoTO | undefined = this._graduacaoList.find(grad => grad.ordem === event.value);
		if (!graduacao) {
			graduacao = {
				hint: "Não avaliado",
				label: "Não avaliado",
				ordem: 0,
				ponto: undefined
			};
		}
		preenchimento.graduacao = graduacao;
		this.changedPreenchimento.emit(preenchimento);
	}

	getTipoClient() {
		if (this.platform.is("mobile")) {
			this.client = TipoClientTO.ANDROID;
		} else if (this.platform.is("ios")) {
			this.client = TipoClientTO.IOS;
		}
	}
}
