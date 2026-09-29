import angular from 'angular';
import treeControl from 'angular-tree-control';
import 'angular-tree-control/context-menu'; //TODO Precisa dessa sujeira para o treeControl funcionar, encapsular isso
import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular

import dialogUtils from 'utils/dialog.utils';
import formacaoService from 'services/formacao';
import filter from 'lib/filter';

import formPage from 'components/form-page';
import formCard from 'components/form-card';
import labeledInput from 'components/labeled-input';
import switchDialogInput from 'components/switch-dialog-input';

import cursoGraduacaoListController from './cursograduacao.list';
import cursoGraduacaoViewController from './cursograduacao.view';
import cursoGraduacaoEditController from './cursograduacao.edit';

import './cursograduacao.list.html';
import './cursograduacao.view.html';
import './cursograduacao.edit.html';

export default angular.module('app.cursograduacao', [
	treeControl,
	filter,
	mdDataTable,
	formPage,
	formCard,
	labeledInput,
	switchDialogInput,
	dialogUtils,
	formacaoService
])
	.controller('CursoGraduacaoListController', cursoGraduacaoListController)
	.controller('CursoGraduacaoViewController', cursoGraduacaoViewController)
	.controller('CursoGraduacaoEditController', cursoGraduacaoEditController)
	.name;
