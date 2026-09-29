import angular from 'angular';
import moment from 'moment';

import ngMaterial from 'angular-material';
import ngResource from 'angular-resource';

import mdDataTable from 'angular-material-data-table';

import uirouter from 'angular-ui-router';
import translate from 'angular-translate';
import translatePartial from 'angular-translate-loader-partial';
import vAccordion from 'v-accordion';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import 'slick-carousel';
import 'angular-slick-carousel';

import 'jquery-datetimepicker/build/jquery.datetimepicker.full.min.js';

// Toastr
import toastr from 'angular-toastr';

// Fonts
import 'typeface-montserrat';

//style
import 'lib/datetimepicker/jquery.datetimepicker.css';
import 'v-accordion/dist/v-accordion.css';
import 'angular-toastr/dist/angular-toastr.css';
import 'angular-material-data-table/dist/md-data-table.css';
import './style/main.scss';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular
import '@fortawesome/fontawesome-free/css/all.css';

// Fuse
import fuse from 'fuse/core/core.module';
import fuseScroll from 'fuse/core/directives/ms-scroll/ms-scroll.directive';

// Configs
import utils from 'lib/utils';
import ROUTES from 'utils/routes';

import {
	initStates,
	configStates,
	resourcesConfig,
	httpConfig,
	fuseConfig,
	ariaConfig,
	toastrConfig,
	authStateConfig
} from './app.config';

import appController from './app.controller';

// Componentes
import navigation from 'partials/navigation/navigation.module';
import toolbar from 'partials/toolbar/toolbar.module';
import perfil from 'partials/toolbar/perfil/perfil.route';
import mensagens from 'partials/toolbar/mensagens/mensagens.route';

// Views - Login
import login from 'partials/login/login.route';
import forgot from 'partials/forgot/forgot.route';

// Views - Main
import main from 'partials/main.route';
import acompanhamentos from 'partials/acompanhamentos/acompanhamentos.route';
import contato from 'partials/pages/contato/contato.module';
import campanha from 'partials/campanha/campanha.route';
import avaliacao from 'partials/avaliacao/avaliacao.route';
import preenchimento from 'partials/preenchimento/preenchimento.route';

// Views - Administração
import admMain from 'partials/administracao/administracao.module';
import admMensagem from 'partials/administracao/mensagens/mensagem.route';
import admHost from 'partials/administracao/host/host.route';
import admParametros from 'partials/administracao/parametros/parametros.route';

//Views - Main - Relatorios
import relatorios from 'partials/relatorios/relatorios.module';
import resultados from 'partials/relatorios/resultados/resultados.route';
import historicos from 'partials/relatorios/historicos/historicos.route';

// Views - Main - Cadastros
import cadastros from 'partials/cadastros/cadastros.module';
import funcionario from 'partials/cadastros/funcionarios/funcionario.route';
import unidade from 'partials/cadastros/unidade/unidade.route';
import modelograduacao from 'partials/cadastros/modelosgraduacao/modelosgraduacao.route';
import tipoCompetencia from 'partials/cadastros/tipodecompetencias/tipodecompetencias.route';
import competencia from 'partials/cadastros/competencias/competencia.route';
// import dimensao from 'partials/cadastros/dimensao/dimensao.route';
import departamento from 'partials/cadastros/departamentos/departamento.route';
import cargo from 'partials/cadastros/cargo/cargo.route';

// Views - Main - Cadastros - Formação e Idiomas
// import formacoesidiomas from 'partials/cadastros/formacoes_idiomas/formacoesidiomas.module';
// import cursoespecializacao from './partials/cadastros/formacoes_idiomas/cursosespecializacao/cursosespecializacao.route';
// import cursograduacao from './partials/cadastros/formacoes_idiomas/cursograduacao/cursograduacao.route';
// import escolaridade from './partials/cadastros/formacoes_idiomas/escolaridade/escolaridade.route';
// import idioma from './partials/cadastros/formacoes_idiomas/idioma/idioma.route';
// import tipocurso from './partials/cadastros/formacoes_idiomas/tiposcurso/tiposcurso.route';

// Viewa - PDI
import pdi from 'partials/pdi/pdi.module';
import pdiAcao from './partials/pdi/acao/pdi.route';
import pdiRelatorio from './partials/pdi/relatorio/pdi.relatorio.route';

// Serviços
import authService from 'services/auth';
import funcionarioService from 'services/funcionario';
import mensageriaService from 'services/mensageria';
import translateInterpolation from './services/translateInterpolation';
import userService from 'services/user.service';
import homeOfficeService from './services/homeoffice';
// Diversos
import textAngular from 'utils/textangular.utils';

//Diretives
import dateTimePicker from 'lib/datetimepicker/datetimepicker';

//Views - HomeOffice
import homeOfficeRoute from './partials/homeoffice/homeoffice.route';
import homeOfficeModule from './partials/homeoffice/homeoffice.module.js';

angular.module('app', [
	ngMaterial,
	ngResource,
	vAccordion,
	mdDataTable,
	'slickCarousel',
	dateTimePicker,

	translate,
	translatePartial,
	uirouter,
	toastr,

	fuse,
	fuseScroll,

	textAngular,

	utils,
	navigation,
	toolbar,
	contato,

	// Views
	login,
	forgot,
	main,
	acompanhamentos,
	admMain,
	admMensagem,
	admHost,
	admParametros,
	campanha,
	avaliacao,
	perfil,
	mensagens,
	preenchimento,
	cadastros,
	modelograduacao,
	tipoCompetencia,
	competencia,
	funcionario,
	unidade,
	// dimensao,
	departamento,
	cargo,
	// formacoesidiomas,
	// cursoespecializacao,
	// cursograduacao,
	// escolaridade,
	// idioma,
	// tipocurso,
	relatorios,
	resultados,
	historicos,
	pdi,
	pdiAcao,
	pdiRelatorio,
	homeOfficeRoute,
	homeOfficeModule,

	// Services
	authService,
	funcionarioService,
	mensageriaService,
	userService,
	homeOfficeService,
	translateInterpolation
])
	.constant('$ROUTES', ROUTES)
	.config(fuseConfig)
	.config(translateConfig)
	.config(resourcesConfig)
	.config(httpConfig)
	.config(ariaConfig)
	.config(configStates)
	.config(toastrConfig)
	.config(mdDateLocaleProvider)
	.controller('AppController', appController)
	.run(initStates)
	.run(authStateConfig)
	.name;

function mdDateLocaleProvider($mdDateLocaleProvider) {
	//FIXME: Criar internacionalização
	$mdDateLocaleProvider.months = ['Janeiro', 'Fevereiro', 'Março', 'Abril','Maio','Junho','Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'];
	$mdDateLocaleProvider.shortMonths = ['Jan', 'Fev', 'Mar', 'Abr','Maio','Jun','Jul','Ago','Set','Out','Nov','Dez'];
	$mdDateLocaleProvider.days = ['Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta', 'Sábado', 'Domingo'];
	$mdDateLocaleProvider.shortDays = ['Seg', 'Ter', 'Qua','Qui', 'Sex', 'Sab', 'Dom'];

	//FIXME: Melhorar obter locale
	const locale = window.navigator.userLanguage || window.navigator.language;
	moment.locale(locale === 'pt' ? 'pt-BR' : locale);

	$mdDateLocaleProvider.parseDate = dateString => {
		const m = moment(dateString, 'DD/MM/YYYY', true);
		return m.isValid() ? m.toDate() : new Date(NaN);
	};

	$mdDateLocaleProvider.formatDate = date => {
		const m = moment(date);
		return m.isValid() ? m.format('DD/MM/YYYY') : '';
	};
}

function translateConfig($translateProvider) {
	//TODO: Tentar jogar isso para o webpack (testar https://fitbit.github.io/angular-translate-loader/)
	$translateProvider.useLoader('$translatePartialLoader', {
		urlTemplate: 'i18n/{part}/{lang}.json'
	});
	$translateProvider.preferredLanguage('pt_BR');
	$translateProvider.useSanitizeValueStrategy('escape');
}
