import angular from 'angular';

import DialogFuncionarioFormacaoService from './colaborador_formacao.dialog';
import DialogFuncionarioIdiomaService from './colaborador_idioma.dialog';
import DialogFuncionarioFotoService from './colaborador_foto.dialog';
import imgCropper from 'angular-img-cropper';

import './colaborador_foto.dialog.html';

export default angular.module('app.colaborador.dialogs', [imgCropper])
	.service('DialogFuncionarioFormacao', DialogFuncionarioFormacaoService)
	.service('DialogFuncionarioIdioma', DialogFuncionarioIdiomaService)
	.service('DialogFuncionarioFoto', DialogFuncionarioFotoService)
	.name;
