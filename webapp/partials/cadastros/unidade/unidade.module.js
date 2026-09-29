import angular from 'angular';
import treeControl from 'angular-tree-control';
import 'angular-tree-control/context-menu'; //TODO Precisa dessa sujeira para o treeControl funcionar, encapsular isso
import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular

import dialogUtils from 'utils/dialog.utils';
import unidadeService from 'services/unidade';

import formCard from 'components/form-card';
import formTable from 'components/form-table';
import labeledInput from 'components/labeled-input';

import filter from 'lib/filter';
import unidadeDialogs from './unidade.subunidade';
import unidadeListCtrl from './unidade.list';
import unidadeViewCtrl from './unidade.view';
import unidadeEditCtrl from './unidade.edit';

import './unidade.list.html';
import './unidade.view.html';
import './unidade.edit.html';
import './unidade.subunidade.html';

export default angular.module('app.unidade', [
	treeControl,
	formTable,
	formCard,
	labeledInput,
	filter,
	mdDataTable,
	dialogUtils,
	unidadeService,
	unidadeDialogs,
])
	.controller('UnidadeListCtrl', unidadeListCtrl)
	.controller('UnidadeViewCtrl', unidadeViewCtrl)
	.controller('UnidadeEditCtrl', unidadeEditCtrl)
	.name;
