import angular from 'angular';
import filters from 'lib/filter';
import dialogs from 'utils/dialog.utils';

import campanhaService from 'services/campanha';
import avaliacaoService from 'services/avaliacao';
import avaliacaoViewModelService from '../../services/avaliacao.view-model';
import empresaService from 'services/empresa.service';
import tipoCompetenciaService from 'services/tipocompetencia';
import SituacaoService from 'services/situacao';

import './avaliacao.new.html';
import './avaliacao.edit.html';
import './avaliacao.view.html';
import './avaliacao.mensagem.html';

import abaParticipantesCadastroCtrl from './partials/aba.participantes';
import abaPendenciasCadastroCtrl from './partials/aba.pendencias';
import abaAvaliadoresViewCtrl from './partials/aba.avaliadores';
import abaMensagensCtrl from './partials/aba.mensagens';
import abaResumoCtrl from './partials/aba.resumo';
import avaliacaoEditCtrl from './partials/aba.resumo.edit';
import avaliacaoNewCtrl from './partials/aba.resumo.new';
import MensagemCadastroCtrl from './avaliacao.mensagem';
import avaliacaoViewCtrl from './avaliacao.view';

import './partials/aba.resumo.view.html';
import './partials/aba.resumo.edit.html';
import './partials/aba.resumo.new.html';
import './partials/aba.participantes.html';
import './partials/aba.pendencias.html';
import './partials/aba.avaliadores.html';
import './partials/aba.mensagens.html';
import './avaliacao.mensagem.html';

import {
	ValidarParticipantesDialog,
	ImportarParesDialog,
	ValidarPendenciasDialog } from './dialogs/dialogs.aba-participante';

import { AdicionarAvaliadorDialog, LiberarAbaAvaliadoresDialog } from './dialogs/dialogs.aba-avaliadores';

import './dialogs/dialog.validar.html';
import './dialogs/dialog.importar-pares.html';
import './dialogs/dialog.validar-pendencias.html';
import './dialogs/dialog.add-avaliadores.html';

export default angular
	.module('app.avaliacao', [
		filters,
		dialogs,
		avaliacaoService,
		avaliacaoViewModelService,
		campanhaService,
		empresaService,
		tipoCompetenciaService,
		SituacaoService
	])
	.controller('AvaliacaoViewCtrl', avaliacaoViewCtrl)
	.controller('AvaliacaoEditCtrl', avaliacaoEditCtrl)
	.controller('AvaliacaoNewCtrl', avaliacaoNewCtrl)
	.controller('AbaParticipantesViewCtrl', abaParticipantesCadastroCtrl)
	.controller('AbaPendenciasCadastroCtrl', abaPendenciasCadastroCtrl)
	.controller('AbaAvaliadoresViewCtrl', abaAvaliadoresViewCtrl)
	.controller('AbaMensagensCtrl', abaMensagensCtrl)
	.controller('AbaResumoCtrl', abaResumoCtrl)
	.controller('MensagemCadastroCtrl', MensagemCadastroCtrl)
	.controller('ValidarParticipantesDialog', ValidarParticipantesDialog)
	.controller('ImportarParesDialog', ImportarParesDialog)
	.controller('ValidarPendenciasDialog', ValidarPendenciasDialog)
	.controller('AdicionarAvaliadorDialog', AdicionarAvaliadorDialog)
	.controller('LiberarAbaAvaliadoresDialog', LiberarAbaAvaliadoresDialog)
	.name;
