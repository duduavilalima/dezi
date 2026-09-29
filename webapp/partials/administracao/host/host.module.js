import angular from 'angular';

import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular

import empresaService from 'services/empresa.service';
import admHostController from './host.controller';

import './host.html';

export default angular.module('app.adm.parametros', [mdDataTable, empresaService])
	.controller('AdmHostController', admHostController)
	.name;
