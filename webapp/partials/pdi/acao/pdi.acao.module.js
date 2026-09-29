import angular from 'angular';
import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular
import filters from 'lib/filter';
import dialogs from 'utils/dialog.utils';
import labeledInput from 'components/labeled-input';

//Services
import avaliacaoService from 'services/avaliacao';
import fotoService from 'services/foto';
import funcionarioService from 'services/funcionario';
import cargoService from 'services/cargo';
import competenciaService from 'services/competencia';
import pdiService from 'services/pdi';

//Ctrl
import pdiListCtrl from './pdi.list';
import pdiAcaoList from './pdi.acao.list';
import pdiAcaoRelatorio from './pdi.relatorio.individual';
import PDIViewModelService from 'services/pdi.view-model';
//HTML
import './pdi.list.html';
import './pdi.acao.list.html';
import './tabs/em.aprovacao.html';
import './tabs/aprovado.html';
import './tabs/em.andamento.html';
import './tabs/finalizado.html';
import './pdi.relatorio.individual.html';

export default angular
	.module('app.pdiacao', [
		mdDataTable,
		filters,
		dialogs,
		labeledInput,
		avaliacaoService,
		PDIViewModelService,
		fotoService,
		funcionarioService,
		pdiService,
		cargoService,
		competenciaService,
	])
	.controller('PDIListCtrl', pdiListCtrl)
	.controller('PDIAcaoListCtrl',pdiAcaoList)
	.controller('PDIAcaoRelatorioCtrl',pdiAcaoRelatorio)
	.name;
