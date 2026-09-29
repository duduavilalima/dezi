import angular from 'angular';

import toolbarController from './toolbar.controller';

import configuracaoService from 'services/configuracao';
import fotoService from 'services/foto';
import funcionarioService from 'services/funcionario';
import utils from 'lib/utils';

import './toolbar.html';
import './toolbar.scss';

export default angular.module('app.toolbar', [
	configuracaoService,
	fotoService,
	funcionarioService,
	utils
])
	.config(configTranslate)
	.controller('ToolbarController', toolbarController)
	.name;

function configTranslate($translatePartialLoaderProvider) {
	$translatePartialLoaderProvider.addPart('toolbar');
}
