import angular from 'angular';
import imgCropper from 'lib/angular-img-cropper/src/angular-img-cropper';
import vAccordion from 'v-accordion';

import filters from 'lib/filter';

import formCard from 'components/form-card';
import formTable from 'components/form-table';
import labeledInput from 'components/labeled-input';

import avaliacaoService from 'services/avaliacao';
import cargoService from 'services/cargo';
import userService from 'services/user.service';
import configuracaoService from 'services/configuracao';
import departamentoService from 'services/departamento';
import empresaService from 'services/empresa.service';
import formacaoService from 'services/formacao';
import fotoService from 'services/foto';
import funcionarioService from 'services/funcionario';
import situacaoService from 'services/situacao';
import tipoService from 'services/tipo';
import unidadeService from 'services/unidade';

import notifyService from 'utils/notify.utils';
import dialogsService from 'utils/dialog.utils';

import funcionarioEditCtrl from './funcionario.edit';
import funcionarioListCtrl from './funcionario.list';
import funcionarioViewCtrl from './funcionario.view';

import 'img/avatars/anon.png';

import './funcionario.list.html';
import './funcionario.edit.html';
import './funcionario.view.html';

import funcionarioDialogs from './dialogs/colaborador.dialogs';
import funcionarioComponents from './components/colaborador.components';

export default angular
	.module('app.colaborador', [
		formCard,
		formTable,
		labeledInput,
		vAccordion,
		imgCropper,
		filters,
		avaliacaoService,
		cargoService,
		configuracaoService,
		departamentoService,
		empresaService,
		formacaoService,
		fotoService,
		funcionarioService,
		situacaoService,
		tipoService,
		unidadeService,
		funcionarioDialogs,
		userService,
		notifyService,
		dialogsService,
		funcionarioComponents,
	])
	.controller('FuncionarioListController', funcionarioListCtrl)
	.controller('FuncionarioEditController', funcionarioEditCtrl)
	.controller('FuncionarioViewController', funcionarioViewCtrl)
	.name;
