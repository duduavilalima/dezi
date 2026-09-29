import angular from 'angular';
import treeControl from 'angular-tree-control';
import 'angular-tree-control/context-menu'; //TODO Precisa dessa sujeira para o treeControl funcionar, encapsular isso
import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular

import dialogUtils from 'utils/dialog.utils';
import departamentoService from 'services/departamento';

import filter from 'lib/filter';
import formCard from 'components/form-card';
import formTable from 'components/form-table';
import labeledInput from 'components/labeled-input';

import departamentosListCtrl from './departamentos.list';
import departamentosViewCtrl from './departamentos.view';
import departamentosEditCtrl from './departamentos.edit';

import './departamentos.list.html';
import './departamentos.view.html';
import './departamentos.edit.html';

export default angular.module('app.departamentos', [
	treeControl,
	filter,
	formCard,
	formTable,
	labeledInput,
	mdDataTable,
	dialogUtils,
	departamentoService
])
	.controller('DepartamentosListCtrl', departamentosListCtrl)
	.controller('DepartamentosViewCtrl', departamentosViewCtrl)
	.controller('DepartamentosEditCtrl', departamentosEditCtrl)
	.name;