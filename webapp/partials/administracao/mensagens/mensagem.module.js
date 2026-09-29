import angular from 'angular';

import mensagemService from 'services/mensagem.service';
import admMensagemController from './mensagem.controller';

import './mensagemadm.html';

export default angular.module('app.adm.mensagem', [mensagemService])
	.controller('AdmMensagemCtrl', admMensagemController)
	.name;
