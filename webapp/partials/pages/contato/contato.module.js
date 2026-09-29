import angular from 'angular';

import contatoService from '../../../services/contato';
import contatoController from './contato.controller';

export default angular.module('app.contato', [contatoService])
	.controller('ContatoController', contatoController)
	.name;
