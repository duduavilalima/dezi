import angular from 'angular';
import chartJs from 'angular-chart.js';

import acompanhamentoService from 'services/acompanhamento';
import dadosPreenchimentoService from 'services/dados-preenchimento';
import avaliacaoService from 'services/avaliacao';
import campanhaService from 'services/campanha';
import departamentoService from 'services/departamento';
import unidadeService from 'services/unidade';
import dialogUtils from 'utils/dialog.utils';
import filter from 'lib/filter';
import codecUtils from 'utils/codec.utils';

import formCard from 'components/form-card';
import formTable from 'components/form-table';
import labeledInput from 'components/labeled-input';

import acompanhamentosController from './acompanhamentos';
import acompanhamentosViewController from './acompanhamentos.view';
import acompanhamentosPreenchimentoController from './acompanhamentos.preenchimento';
import acompanhamentosDialogService from './dialogs/acompanhamentos.dialogs';

import './acompanhamentos.html';
import './acompanhamentos.view.html';
import './acompanhamentos.preenchimento.html';

export default angular.module('app.acompanhamentos', [
	chartJs,
	filter,
	formCard,
	formTable,
	labeledInput,
	acompanhamentoService,
	dadosPreenchimentoService,
	avaliacaoService,
	campanhaService,
	departamentoService,
	unidadeService,
	dialogUtils,
	codecUtils
])
	.controller('AcompanhamentosCtrl', acompanhamentosController)
	.controller('AcompanhamentosViewCtrl', acompanhamentosViewController)
	.controller('AcompanhamentosPreenchimentoCtrl', acompanhamentosPreenchimentoController)
	.service('AcompanhamentosDialogService', acompanhamentosDialogService)
	.name;
