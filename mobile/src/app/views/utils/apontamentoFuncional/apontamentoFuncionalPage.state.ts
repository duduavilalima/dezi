import { ensureObject } from '../../../utils/guards';
import { RequiredSlice } from '../../../utils/typescript';

import {
	FeedforwardTO,
	FeedforwardTipoCompetenciaTO,
	FeedforwardCompetenciaTO,
	FeedforwardFuncionarioTO,
	FeedforwardCreateTO,
	FeedforwardFilterTO,
} from './transfer/feedforward.transfer';
import {
	ApontamentoTO,
	ApontamentoTipoCompetenciaTO,
	ApontamentoCompetenciaTO,
	ApontamentoFuncionarioTO,
	ApontamentoCreateTO,
	ApontamentoFilterTO,
} from './transfer/apontamento.transfer';

export type FeedforwardPageState = ApontamentoFuncionalPageState<
	FeedforwardTO,
	FeedforwardTipoCompetenciaTO,
	FeedforwardCompetenciaTO,
	FeedforwardFuncionarioTO,
	FeedforwardCreateTO,
	FeedforwardFilterTO
>;

export type ApontamentoPageState = ApontamentoFuncionalPageState<
	ApontamentoTO,
	ApontamentoTipoCompetenciaTO,
	ApontamentoCompetenciaTO,
	ApontamentoFuncionarioTO,
	ApontamentoCreateTO,
	ApontamentoFilterTO
>;

interface ApontamentoFuncionalResource<
	TApontamentoFuncionalTO extends FeedforwardTO | ApontamentoTO,
	TTipoCompetenciaTO extends FeedforwardTipoCompetenciaTO | ApontamentoTipoCompetenciaTO,
	TCompetenciaTO extends FeedforwardCompetenciaTO | ApontamentoCompetenciaTO,
	TFuncionarioTO extends FeedforwardFuncionarioTO | ApontamentoFuncionarioTO,
	TCreateInput extends FeedforwardCreateTO | ApontamentoCreateTO,
	TFilterTO extends FeedforwardFilterTO | ApontamentoFilterTO,
> {
	listAvaliado(): Promise<TFuncionarioTO[]>;
	listTipoCompetenciaByAvaliadoId(avaliadoId: number): Promise<TTipoCompetenciaTO[]>;
	listCompetenciaByTipoCompetenciaId(tipoCompetenciaId: number): Promise<TCompetenciaTO[]>;
	list(filter: RequiredSlice<FeedforwardFilterTO, "avaliadoId" | "tipoCompetenciaId">): Promise<TApontamentoFuncionalTO[]>;
	create(input: TCreateInput): Promise<any>;
}

export class ApontamentoFuncionalPageState<
	TApontamentoFuncionalTO extends FeedforwardTO | ApontamentoTO,
	TTipoCompetenciaTO extends FeedforwardTipoCompetenciaTO | ApontamentoTipoCompetenciaTO,
	TCompetenciaTO extends FeedforwardCompetenciaTO | ApontamentoCompetenciaTO,
	TFuncionarioTO extends FeedforwardFuncionarioTO | ApontamentoFuncionarioTO,
	TCreateInput extends FeedforwardCreateTO | ApontamentoCreateTO,
	TFilterInput extends FeedforwardFilterTO | ApontamentoFilterTO,
> {
	// Selection
	avaliado?: TFuncionarioTO;
	tipoCompetencia?: TTipoCompetenciaTO;

	// Options
	avaliados: TFuncionarioTO[] = [];
	tiposCompetencia: TTipoCompetenciaTO[] = [];
	apontamentosFuncionais?: TApontamentoFuncionalTO[] = [];

	protected reenterMessage = undefined;

	// Cache
	private tiposCompetenciaByAvaliadoId: { [avaliadoId: number]: TTipoCompetenciaTO[] } = {};
	private apontamentosFuncionaisByIds: { [avaliadoTipoCompetenciaId: string]: TApontamentoFuncionalTO[] } = {};

	// Sequencing
	private lastPromise: Promise<any> = Promise.resolve();

	constructor(
		private apontamentoFuncionalResource: ApontamentoFuncionalResource<
			TApontamentoFuncionalTO,
			TTipoCompetenciaTO,
			TCompetenciaTO,
			TFuncionarioTO,
			TCreateInput,
			TFilterInput
		>,
	) {}

	selectAvaliado(avaliado?: TFuncionarioTO) {
		this.lastPromise = this.lastPromise.then(async () => {
			if (!avaliado) {
				this.avaliado = undefined;
				this.apontamentosFuncionais = undefined;
				this.selectTipoCompetencia(undefined);
			} else {
				this.avaliado = avaliado;
				this.tiposCompetencia = await this.listTipoCompetencia();
				this.selectTipoCompetencia(this.tiposCompetencia[0]);
			}
		});
		return this.lastPromise;
	}

	selectTipoCompetencia(tipoCompetencia?: TTipoCompetenciaTO) {
		this.lastPromise = this.lastPromise.then(async () => {
			if (!tipoCompetencia) {
				this.tipoCompetencia = undefined;
				this.apontamentosFuncionais = [];
			} else {
				this.tipoCompetencia = tipoCompetencia;
				// Para ativar a re-renderização da lista de feedforwards
				this.apontamentosFuncionais = undefined;
				this.apontamentosFuncionais = await this.listApontamentoFuncional();
			}
		});
		return this.lastPromise;
	}

	async enterPage() {
		this.avaliado = undefined;
		this.tipoCompetencia = undefined;
		this.tiposCompetencia = [];
		this.apontamentosFuncionais = undefined;
		this.tiposCompetenciaByAvaliadoId = {};
		this.apontamentosFuncionaisByIds = {};

		this.avaliados = await this.apontamentoFuncionalResource.listAvaliado();
		await this.selectAvaliado(this.avaliados[0]);
	}

	async reenterPage() {
		const key = this.getApontamentoFuncionalCacheId();
		delete this.apontamentosFuncionaisByIds[key];
		this.lastPromise = this.lastPromise.then(async () => {
			this.selectTipoCompetencia(this.tipoCompetencia);
		});
	}

	private getAvaliadoId(avaliado?: TFuncionarioTO) {
		return ensureObject(avaliado || this.avaliado).id;
	}

	private getTipoCompetenciaId(tipoCompetencia?: TTipoCompetenciaTO) {
		return ensureObject(tipoCompetencia || this.tipoCompetencia).id;
	}

	private async listTipoCompetencia(avaliado?: TFuncionarioTO) {
		const avaliadoId = this.getAvaliadoId(avaliado);
		let tiposCompetencia = this.tiposCompetenciaByAvaliadoId[avaliadoId];
		if (!tiposCompetencia) {
			tiposCompetencia = await this.apontamentoFuncionalResource.listTipoCompetenciaByAvaliadoId(avaliadoId);
			this.tiposCompetenciaByAvaliadoId[avaliadoId] = tiposCompetencia;
		}
		return tiposCompetencia;
	}

	private async listApontamentoFuncional(tipoCompetencia?: TTipoCompetenciaTO, avaliado?: TFuncionarioTO) {
		const tipoCompetenciaId = this.getTipoCompetenciaId(tipoCompetencia);
		const avaliadoId = this.getAvaliadoId(avaliado);
		const key = this.getApontamentoFuncionalCacheId(tipoCompetenciaId, avaliadoId);
		let apontamentosFuncionais = this.apontamentosFuncionaisByIds[key];
		if (!apontamentosFuncionais) {
			apontamentosFuncionais = await this.apontamentoFuncionalResource.list({
				avaliadoId,
				tipoCompetenciaId,
			});
			this.apontamentosFuncionaisByIds[key] = apontamentosFuncionais;
		}
		return apontamentosFuncionais;
	}

	private getApontamentoFuncionalCacheId(tipoCompetenciaId?: number, avaliadoId?: number) {
		tipoCompetenciaId = tipoCompetenciaId || this.getTipoCompetenciaId();
		avaliadoId = avaliadoId || this.getAvaliadoId();
		const key = `${tipoCompetenciaId}:${avaliadoId}`;
		return key;
	}
}

export function createFeedforwardPageState(resource: ApontamentoFuncionalResource<
	FeedforwardTO,
	FeedforwardTipoCompetenciaTO,
	FeedforwardCompetenciaTO,
	FeedforwardFuncionarioTO,
	FeedforwardCreateTO,
	FeedforwardFilterTO
>) {
	return new ApontamentoFuncionalPageState(resource);
}

export function createApontamentoPageState(resource: ApontamentoFuncionalResource<
	ApontamentoTO,
	ApontamentoTipoCompetenciaTO,
	ApontamentoCompetenciaTO,
	ApontamentoFuncionarioTO,
	ApontamentoCreateTO,
	ApontamentoFilterTO
>) {
	return new ApontamentoFuncionalPageState(resource);
}
