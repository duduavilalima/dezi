import angular from 'angular';
import dialogs from 'utils/dialog.utils';
import homeOfficeService from 'services/homeoffice';
import MunicipioPublic from '../../services/public.service';

import homeOfficeFormCtrl from './homeoffice.form';

import './homeoffice.form.html';
import './homeoffice.form.scss';

export default angular
	.module('app.homeoffice', [
		dialogs,
		homeOfficeService,
		MunicipioPublic
	])
	.controller('HomeOfficeFormCtrl', homeOfficeFormCtrl)
	.name;
