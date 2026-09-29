import angular from 'angular';

import formInputs from 'components/form-input.module';

import configureColaboradorCard from './colaborador.card';

const module = angular.module('app.colaborador.components', [formInputs]);

configureColaboradorCard(module);

export default module.name;
