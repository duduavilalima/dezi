import angular from 'angular';

import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular

import parametroService from 'services/parametro.service';
import admParametrosController from './parametros.controller';

import './parametros.html';

export default angular.module('app.adm.parametros', [mdDataTable, parametroService])
	.controller('AdmParametrosController', admParametrosController)
	.name;
