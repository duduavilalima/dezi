import angular from 'angular';
import cookies from 'angular-cookies';
import toastr from 'angular-toastr';
import dialogs from 'utils/dialog.utils';

import publicService from 'services/public.service';
import AuthService from 'services/auth';

import forgotController from './forgot.controller';

import './forgot-password.html';

export default angular.module('app.login', [
	cookies,
	dialogs,
	toastr,
	publicService,
	AuthService
])
	.controller('ForgotController', forgotController)
	.name;
