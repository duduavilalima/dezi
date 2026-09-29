import angular from 'angular';
import filter from 'lib/filter';
import pentahoService from 'services/pentaho';
import biParametroUsuarioService from 'services/biparametrousuario';

import historicosBIListCtrl from './historicos.list';

import './historicos.list.html';

export default angular.module('app.historicos', [
	filter,
	pentahoService,
	biParametroUsuarioService
])
	.controller('HistoricosBIListCtrl', historicosBIListCtrl)
	.name;
