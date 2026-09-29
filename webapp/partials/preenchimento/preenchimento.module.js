import angular from 'angular';
import filters from 'lib/filter';
import dialogs from 'utils/dialog.utils';
import listController from './listController';

import './avaliacoes/preenchimento.list.avaliacoes.html';
import './avaliacoes/preenchimento.list.secao.competencias.html';
import './avaliacoes/preenchimento.list.secao.funcionarios.html';

import './consensos/preenchimento.list.consensos.html';
import './consensos/preenchimento.list.secao.gestores.html';
import './consensos/preenchimento.list.secao.subordinados.html';

import './dialogs/login_consenso.html';
import './dialogs/dialog_apontamento_funcional.html';
import './dialogs/login_consenso.html';

import detailCompetencia from './avaliacoes/porcompetencia/detailCompetencia';
import './avaliacoes/porcompetencia/detail.competencia.html';

import detailController from './detalhamento/preenchimento.detail';
import './detalhamento/preenchimento.detail.html';

import Participante from 'services/participante';
import UserService from 'services/user.service';
import CompetenciaAvaliador from 'services/preenchimento';
import CompetenciaPreenchimento from 'services/preenchimento';
import ApontamentoFuncionalResource from 'services/apontamento';
import preenchimentoComponent from './detalhamento/preenchimento.component';

export default angular.module('app.preenchimento', [
	filters,
	dialogs,
	Participante,
	UserService,
	CompetenciaAvaliador,
	ApontamentoFuncionalResource,
	preenchimentoComponent,
	CompetenciaPreenchimento
])
	.controller('PreenchimentoDetailController', detailController)
	.controller('PreenchimentoListController', listController)
	.controller('PreenchimentoDetailCompetencia', detailCompetencia)
	.filter('trustHtml',function($sce){
		return function(html){
			return $sce.trustAsHtml(html);
		};
	})
	.name;

