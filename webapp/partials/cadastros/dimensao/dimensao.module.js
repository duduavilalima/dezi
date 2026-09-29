import angular from 'angular';
import treeControl from 'angular-tree-control';
import 'angular-tree-control/context-menu'; //TODO Precisa dessa sujeira para o treeControl funcionar, encapsular isso
import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular

import dialogUtils from 'utils/dialog.utils';
import dimensaoService from 'services/dimensao';

import filter from 'lib/filter';
import dimensaoListCtrl from './dimensao.ctrl.list';

import './dimensao_list.html';
import './dimensao_entity.html';
import './dimensao_entity2.html';

export default angular.module('app.dimensao', [
	treeControl,
	filter,
	mdDataTable,
	dialogUtils,
	dimensaoService
])
	.controller('DimensaoListCtrl', dimensaoListCtrl)
	.name;