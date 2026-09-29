import moment from 'moment';
import 'moment/min/locales.min.js';

export default function dataUtilFactory() {
	const locale = window.navigator.userLanguage || window.navigator.language;
	moment.locale(locale === 'pt'? 'pt-BR' : locale);
	const momentUtil = {
		converter: (strData) => {
			return strData ? moment(strData) : null;
		},
		format: (strData, type) => {
			// 'L' = DD/MM/YYYY, 'LL' = DD de MMM de YYYY, 'll' = DD de MM de YYYY
			return moment(strData).format(type); 
		},
		add: (strData,num,hourOrDay) => {
			return moment(strData).add(num, hourOrDay).calendar();
		},
		subtract: (strData,num,hourOrDay) => {
			return moment(strData).subtract(num, hourOrDay).calendar();
		},
		startOf: (strData,hourOrDay) => {
			return moment(strData).startOf(hourOrDay).fromNow();
		},
		endOf: (strData,hourOrDay) => {
			return moment(strData).endOf(hourOrDay).fromNow(); 
		},
		nowHour: () => {
			return moment().format('LT');
		},
		nowHourAndSec: () => {
			return moment().format('LTS');
		}
	};
	return momentUtil;
}