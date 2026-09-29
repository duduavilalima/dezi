import EnjoyHint from 'lib/enjoyhint/enjoyhint';

function EnjoyHintHelper(modo, campos) {
	this.modo = modo;
	this.campos = campos;
	this.etapas = [];
}

const nextButton = {
	className: 'myNext',
	text: 'Próximo'
};

const skipButton = {
	className: 'mySkip',
	text: 'Fechar'
};

const endButton = {
	className: 'myNext',
	text: 'Fechar'
};

const defaults = {
	scroll: false
};

const prepararOnBeforeStart = function(query) {
	return function () {
		$('html,#content').animate({
			scrollTop: $(query).offset().top
		}, 300);
	};
};

EnjoyHintHelper.prototype.prepararEtapa = function(nomeCampo, opcoes) {
	const etapa = opcoes || {};
	const campo = this.campo(nomeCampo);

	etapa[campo.evento] = etapa[campo.evento] || campo.descricao;
	etapa.nextButton = etapa.nextButton || nextButton;
	etapa.skipButton = etapa.skipButton || skipButton;
	if (campo.scroll) {
		etapa.onBeforeStart = etapa.onBeforeStart || prepararOnBeforeStart(campo.query);
	}

	return etapa;
};

EnjoyHintHelper.prototype.campo = function(nomeCampo) {
	const info = this.campos[nomeCampo];
	if (!info) return null;

	const nomeEvento = (info.eventos || {})[this.modo] || info.evento;
	const campo = {};

	campo.query = (info.queries || {})[this.modo] || info.query;
	campo.descricao = info.descricao;
	campo.evento = nomeEvento || 'next ' + (campo.query);
	campo.scroll = info.scroll ? info.scroll : defaults.scroll;

	return campo;
};

EnjoyHintHelper.prototype.prepararEtapasParaExecucao = function() {
	const etapas = this.etapas.slice();
	if (etapas.length > 0) {
		etapas[etapas.length - 1]['nextButton'] = endButton;
		delete etapas[etapas.length - 1]['skipButton'];
		etapas[etapas.length - 1]['showSkip'] = false;
	}

	return etapas;
};

EnjoyHintHelper.prototype.etapa = function(nomeCampo, opcoes) {
	const etapa = this.prepararEtapa(nomeCampo, opcoes);

	this.etapas.push(etapa);

	return this;
};

EnjoyHintHelper.prototype.prepararEnjoyHint = function() {
	const etapas = this.prepararEtapasParaExecucao();

	if (etapas.length == 0) return null;

	const enjoyHint = new EnjoyHint();
	enjoyHint.set(etapas);

	return enjoyHint;
};

EnjoyHintHelper.prototype.executar = function() {
	this.prepararEnjoyHint().run();

	return this;
};

EnjoyHintHelper.prototype.limpar = function() {
	this.etapas = [];
};

export default EnjoyHintHelper;
