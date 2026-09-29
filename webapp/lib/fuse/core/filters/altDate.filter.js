import moment from 'moment';

export function altDate() {
	return function (value) {
		var diff = Date.now() - new Date(value);

		if (diff < (60 * 60 * 1000)) {
			/**
			 * If in a hour
			 * e.g. "2 minutes ago"
			 */
			return moment(value).fromNow();
		} else if (diff < (60 * 60 * 24 * 1000)) {
			/*
			* If in the day
			* e.g. "11:23"
			*/
			return moment(value).format('HH:mm');
		} else if (diff < (60 * 60 * 24 * 7 * 1000)) {
			/*
			* If in week
			* e.g "Tuesday"
			*/
			return moment(value).format('dddd');
		} else {
			/*
			* If more than a week
			* e.g. 03/29/2016
			*/
			return moment(value).calendar();
		}
	};
}
