import { Component, Input, OnDestroy, OnInit } from "@angular/core";
import { Events } from "ionic-angular";
import { TOPIC } from "../../../../../../constants/topics";
import { AvaliacaoPreenchimentoTO } from "../../../../avaliacao.transfer";

@Component({
	selector: "app-avatar-avaliados",
	templateUrl: "avatar-avaliados.component.html",
})
export class AvatarAvaliadosComponent implements OnInit, OnDestroy {
	private _preenchimentoList: AvaliacaoPreenchimentoTO[];

	constructor(private events: Events) { }

	ngOnInit() {
		this.events.subscribe(TOPIC.preenchimento.avatar.list, (value: AvaliacaoPreenchimentoTO[]) => {
			this.deepSortList(value);
		});
	}

	get preenchimentoList() {
		return this._preenchimentoList;
	}

	@Input()
	set preenchimentoList(value: AvaliacaoPreenchimentoTO[]) {
		this.deepSortList(value);
	}

	jsonCopy(src) {
		return JSON.parse(JSON.stringify(src));
	}

	deepSortList(value: AvaliacaoPreenchimentoTO[]) {
		let preenchimentoSorted: AvaliacaoPreenchimentoTO[] = [];
		value.forEach(pr => {
			preenchimentoSorted.push(this.jsonCopy(pr));
		});
		preenchimentoSorted = preenchimentoSorted.sort((p1, p2) => {
			/* Verifica se os dois participantes tem nota */
			if (p1.graduacao && p2.graduacao) {
				if (p1.graduacao!.ponto && p2.graduacao!.ponto) {
					if (p1.graduacao!.ponto! === p2.graduacao!.ponto!) {
						return 0;
					}
					if (p1.graduacao!.ponto! < p2.graduacao!.ponto!) {
						return 1;
					} else {
						return -1;
					}
					/* Verifica se o primeiro participante nao tem nota, coloca-o no final da lista */
				}
				if (!p1.graduacao!.ponto! && p2.graduacao!.ponto!) {
					return 1;
				}
				/* Verifica se o segundo participante nao tem nota, coloca-o no final da lista */
				if (p1.graduacao!.ponto! && !p2.graduacao!.ponto!) {
					return -1;
				} else {
					return 0;
				}
			} else {
				return 0;
			};
		});
		this._preenchimentoList = preenchimentoSorted;
	}

	ngOnDestroy() {
		this.events.unsubscribe(TOPIC.preenchimento.avatar.list);
	}
}
