export default class MdTableParams {
	constructor(extraParams) {
		this.limit = 5,
		this.page = 1,
		this.limitOptions = [5, 10, 15, 20, 25, 30, 35, 40, 45, 50],
		this.labels = {
			page: 'Página:',
			rowsPerPage: 'Registros por página:',
			of: 'de',
		};
		Object.assign(this, extraParams);
	}

	get limitSkip() {
		return (this.page - 1) * this.limit;
	}

	paginate(list) {
		const { page, limit } = this;
		const begin = (page - 1) * limit;
		const end = begin + limit;
		return list.slice(begin, end);
	}
}
