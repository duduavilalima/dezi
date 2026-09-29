import angular from 'angular';

import labeledText from './form-input/labeled-text';
import labeledSelectEnum from './form-input/labeled-select-enum';
import labeledDatepicker from './form-input/labeled-datepicker';
import labeledSelect from './form-input/labeled-select';
import labeledSelectBoolean from './form-input/labeled-select-boolean';

export default angular.module('comps.formInput', [
	labeledText,
	labeledSelectEnum,
	labeledDatepicker,
	labeledSelect,
	labeledSelectBoolean,
]);
