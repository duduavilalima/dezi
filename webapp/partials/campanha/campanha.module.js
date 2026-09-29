import angular from 'angular';
import mdDataTable from 'angular-material-data-table';
import 'angular-material-data-table/dist/md-data-table.css'; //TODO Encapsular
import filters from 'lib/filter';
import dialogs from 'utils/dialog.utils';

import campanhaService from 'services/campanha';
import avaliacaoService from 'services/avaliacao';
import empresaService from 'services/empresa.service';
import tipoCompetenciaService from 'services/tipocompetencia';
import tipoService from 'services/tipo';

import campanhaListCtrl from './campanha.list';
import campanhaViewCtrl from './campanha.view';
import campanhaNewCtrl from './campanha.new';
import campanhaEditCtrl from './campanha.edit';

import './campanha.list.html';
import './campanha.view.html';
import './campanha.new.html';
import './campanha.edit.html';

export default angular
	.module('app.campanha', [
		mdDataTable,
		filters,
		dialogs,
		campanhaService,
		empresaService,
		avaliacaoService,
		tipoCompetenciaService,
		tipoService
	])
	.controller('CampanhaListCtrl', campanhaListCtrl)
	.controller('CampanhaViewCtrl', campanhaViewCtrl)
	.controller('CampanhaNewCtrl', campanhaNewCtrl)
	.controller('CampanhaEditCtrl', campanhaEditCtrl)
	.name;
