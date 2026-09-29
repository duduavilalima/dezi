import angular from 'angular';
import treeControl from 'angular-tree-control';
import 'angular-tree-control/css/tree-control.css'; // TODO Encapsular
import 'angular-tree-control/context-menu'; //TODO Precisa dessa sujeira para o treeControl funcionar, encapsular isso
import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular

import dialogUtils from 'utils/dialog.utils';
import notifyUtils from 'utils/notify.utils';
import tipoCompetenciaService from 'services/tipocompetencia';
import formCard from 'components/form-card';
import formTable from 'components/form-table';

import filter from 'lib/filter';
import tipoCompetenciaListCtrl from './tipodecompetencias.list';
import tipoCompetenciaEditCtrl from './tipodecompetencias.edit';
import dialogTipoCompetenciaService from './dialogs/dialog_tipocompetencia.dialogs';

import './tipodecompetencias.list.html';
import './tipodecompetencias.edit.html';
import './dialogs/dialog_tipocompetencia.html';

export default angular.module('app.tipocompetencia', [
	treeControl,
	filter,
	formCard,
	formTable,
	mdDataTable,
	dialogUtils,
	notifyUtils,
	tipoCompetenciaService,
])
	.service('DialogTipoCompetencia', dialogTipoCompetenciaService)
	.controller('TipoCompetenciaListCtrl', tipoCompetenciaListCtrl)
	.controller('TipoCompetenciaEditCtrl', tipoCompetenciaEditCtrl)
	.name;
