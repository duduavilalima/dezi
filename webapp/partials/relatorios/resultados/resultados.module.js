import angular from 'angular';

import campanhaService from 'services/campanha';
import pentahoService from 'services/pentaho';
import biParametroUsuarioService from 'services/biparametrousuario';

import resultadoBIController from './resultados.list';

import './resultados.list.html';

export default angular.module('app.resultados', [
	campanhaService,
	pentahoService,
	biParametroUsuarioService
])
	.controller('ResultadosBIListCtrl', resultadoBIController)
	.name;
