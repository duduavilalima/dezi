import angular from 'angular';
import cookies from 'angular-cookies';
import toastr from 'angular-toastr';
import dialogs from 'utils/dialog.utils';

import publicService from '../../services/public.service';
import userService from '../../services/user.service';

import loginController from './login.controller';

import './login.scss';
import './login.html';

export default angular.module('app.login', [
	cookies,
	dialogs,
	toastr,
	publicService,
	userService
])
	.controller('LoginController', loginController)
	.name;
