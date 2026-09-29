import angular from 'angular';
import treeControl from 'angular-tree-control';
import 'angular-tree-control/context-menu'; //TODO Precisa dessa sujeira para o treeControl funcionar, encapsular isso
import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular

import dialogUtils from 'utils/dialog.utils';

import competenciaService from 'services/competencia';
import ModeloGraduacaoService from 'services/modelograduacao';

import filter from 'lib/filter';

import competenciaListCtrl from './competencias.list';
import competenciaViewCtrl from './competencia.view';
import competenciaNewCtrl from './competencia.new';
import competenciaEditCtrl from './competencia.edit';
import switchDialogInput from 'components/switch-dialog-input';

import './competencias.list.html';
import './competencia.html';
import './competencia.new.html';
import './competencia.view.html';
import './competencia.edit.html';
import './dialogs/dialog.detalhamento.html';
import './dialogs/dialog.tipo.de.competencia.html';

export default angular.module('app.competencia', [
	treeControl,
	filter,
	switchDialogInput,
	mdDataTable,
	dialogUtils,
	competenciaService,
	ModeloGraduacaoService
])
	.controller('CompetenciaListCtrl', competenciaListCtrl)
	.controller('CompetenciaNewCtrl', competenciaNewCtrl)
	.controller('CompetenciaEditCtrl', competenciaEditCtrl)
	.controller('CompetenciaViewCtrl', competenciaViewCtrl)
	.name;

