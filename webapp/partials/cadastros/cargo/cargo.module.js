import angular from 'angular';
import treeControl from 'angular-tree-control';
import 'angular-tree-control/context-menu'; //TODO Precisa dessa sujeira para o treeControl funcionar, encapsular isso
import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular

import dialogUtils from 'utils/dialog.utils';
import cargoService from 'services/cargo';
import filter from 'lib/filter';

import formCard from 'components/form-card';
import formTable from 'components/form-table';
import labeledInput from 'components/labeled-input';

import cargoListController from './cargo.list';
import cargoViewController from './cargo.view';
import cargoEditController from './cargo.edit';
import associarCompetenciaCtrl from './associar.competencia';

import cargoDialogs from './dialogs/cargo.dialogs';
import DialogAssociarCompetencia from './dialogs/dialogAssociarCompetencia';

import './cargo.list.html';
import './cargo.view.html';
import './cargo.edit.html';
import './associar.competencia.html';

export default angular.module('app.cargo', [
	treeControl,
	filter,
	mdDataTable,
	formCard,
	formTable,
	labeledInput,
	dialogUtils,
	cargoService,
	cargoDialogs,
	DialogAssociarCompetencia
])
	.controller('CargoListController', cargoListController)
	.controller('CargoViewController', cargoViewController)
	.controller('CargoEditController', cargoEditController)
	.controller('AssociarCompetenciaCtrl', associarCompetenciaCtrl)
	.name;
