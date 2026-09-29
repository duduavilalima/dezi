import angular from 'angular';
import treeControl from 'angular-tree-control';
import 'angular-tree-control/css/tree-control.css'; // TODO Encapsular
import 'angular-tree-control/context-menu'; //TODO Precisa dessa sujeira para o treeControl funcionar, encapsular isso
import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular

import dialogUtils from 'utils/dialog.utils';
import modeloGraduacaoService from 'services/modelograduacao';

import filter from 'lib/filter';

import formPage from 'components/form-page';
import formTable from 'components/form-table';
import formCard from 'components/form-card';
import labeledInput from 'components/labeled-input';

import modelosGraduacaoListCtrl from './modelosgraduacao.list';
import modelosGraduacaoViewCtrl from './modelosgraduacao.view';
import modelosGraduacaoEditCtrl from './modelosgraduacao.edit';

import DialogGraduacao from './dialogs/dialog_graduacao.dialogs';

import './modelosgraduacao.list.html';
import './modelosgraduacao.view.html';
import './modelosgraduacao.edit.html';

import './dialogs/dialog_graduacao.html';

export default angular.module('app.modelosgraduacao', [
	treeControl,
	filter,
	formPage,
	formCard,
	formTable,
	labeledInput,
	mdDataTable,
	dialogUtils,
	modeloGraduacaoService,
])
	.service('DialogGraduacao', DialogGraduacao)
	.controller('ModelosGraduacaoListCtrl', modelosGraduacaoListCtrl)
	.controller('ModelosGraduacaoViewCtrl', modelosGraduacaoViewCtrl)
	.controller('ModelosGraduacaoEditCtrl', modelosGraduacaoEditCtrl)
	.name;
