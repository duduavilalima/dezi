import angular from 'angular';

import '../templates/avaliacao.tpl.html';
import '../templates/competencia.tpl.html';
import '../templates/detalhamento.tpl.html';
import '../dialogs/ad-avaliacao-dialog.dlg.html';

import adCompetenciaCtrl from '../templates/adCompetenciaCtrl';
import adDetalhamentoCtrl from '../templates/adDetalhamentoCtrl';
import adavaliacaoCtrl from '../templates/adavaliacaoCtrl';

export default angular.module('app.preenchimentoComponent', [])

	.component('adAvaliacao', {
		templateUrl: 'avaliacao.tpl.html',
		controller: adavaliacaoCtrl,
		bindings: {
			stage: '<',
			competencias: '<',
			pdi: '<',
			comentarioFinalAvaliado: '<',
			comentarioFinalAvaliador: '<',
			consensoLiberado: '<',
			progresso: '=',
			onSave: '&',
		},
	})

	.component('adCompetencia', {
		templateUrl: 'competencia.tpl.html',
		require: {
			avaliacaoCtrl: '^adAvaliacao'
		},
		controller: adCompetenciaCtrl,
		bindings: {
			competencia: '<',
			hasNext: '@',
			onSave: '&'
		},
	})

	.component('adDetalhamento', {
		templateUrl: 'detalhamento.tpl.html',
		require: {
			aCompetenciaCtrl: '^adCompetencia'
		},
		controller: adDetalhamentoCtrl,
		bindings: {
			stage: '@',
			detalhamento: '<',
			graduacoes: '<',
			onChange: '&',
			index: '<'
		},
	})

	.component('adAvIndicator', {
		template: '<div ng-class="{ av1: $ctrl.av1 == $ctrl.option, av2: $ctrl.av2 == $ctrl.option }"></div>',
		bindings: {
			option: '<',
			av1: '<',
			av2: '<'
		}
	})
	.name;
