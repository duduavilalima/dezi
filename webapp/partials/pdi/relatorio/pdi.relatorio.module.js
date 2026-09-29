import angular from 'angular';
import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular
import filters from 'lib/filter';
import dialogs from 'utils/dialog.utils';

//Services
import avaliacaoService from 'services/avaliacao';
import fotoService from 'services/foto';
import funcionarioService from 'services/funcionario';
import setorService from 'services/departamento';
import cargoService from 'services/cargo';
import competenciaService from 'services/competencia';
import pdiService from 'services/pdi';

//Ctrl
import pdiRelatorioCtrl from './pdi.relatorio';

//HTML
import './pdi.relatorio.html';

export default angular
	.module('app.relatorio', [
		mdDataTable,
		filters,
		dialogs,
		avaliacaoService,
		fotoService,
		funcionarioService,
		setorService,
		cargoService,
		competenciaService,
		pdiService
	])
	.controller('PDIRelatorioCtrl', pdiRelatorioCtrl)
	.name;
