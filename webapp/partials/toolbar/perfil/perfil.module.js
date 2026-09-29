import angular from 'angular';

import perfilCadastroCtrl from './perfil';
import SituacaoService from 'services/situacao';
import UserService from 'services/user.service';
import FormacaoService from 'services/formacao';
import avaliacaoService from 'services/avaliacao';

import './perfil.html';

export default angular
	.module('app.perfil', [
		avaliacaoService,
		SituacaoService,
		UserService,
		FormacaoService,
	])
	.controller('PerfilCadastroCtrl', perfilCadastroCtrl)
	.name;
