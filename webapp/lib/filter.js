import angular from 'angular';
import moment from 'moment';

//TODO Refatorar os nomes horrorosos desses filtros
export default angular.module('app.filter', [])
	.filter('multFilter', multFilterProvider)
	.filter('MessageFilter', messageFilterProvider)
	.filter('MessageExibirFilter', messageExibirFilterProvider)
	.filter('avaliacoesFilter', avaliacoesFilterProvider)
	.filter('avaliacoesRevisoresFilter', avaliacoesRevisoresFilterProvider)
	.filter('campanhasFilter', campanhasFilterProvider)
	.filter('competenciasFilter', competenciasFilterProvider)
	.filter('avaliacoesFiltroConsensoGestorApenas', avaliacoesFiltroConsensoGestorApenasProvider)
	.filter('departamentosFilter', departamentosFilterProvider)
	.filter('cursosespecializacaoFilter', cursosespecializacaoFilterProvider)
	.filter('cursosgraduacaoFilter', cursosgraduacaoFilterProvider)
	.filter('escolaridadeFilter', escolaridadeFilterProvider)
	.filter('idiomasFilter', idiomasFilterProvider)
	.filter('tiposcursoFilter', tiposcursoFilterProvider)
	.filter('funcionariosFilter', funcionariosFilterProvider)
	.filter('modelosGraduacaoFilter', modelosGraduacaoFilterProvider)
	.filter('filtroAbaAvaliados', filtroAbaAvaliadosProvider)
	.filter('preenchimentoPorCompetenciasFilter', preenchimentoPorCompetenciasFilterProvider)
	.filter('preenchimentoPorCompetenciasGestoesFilter', preenchimentoPorCompetenciasGestoesFilterProvider)
	.filter('preenchimentoPorCompetenciasSubordinadosFilter', preenchimentoPorCompetenciasSubordinadosFilterProvider)
	.filter('preenchimentoPorCompetenciasParesFilter', preenchimentoPorCompetenciasParesFilterProvider)
	.filter('filtroAvaliadoresAba', filtroAvaliadoresAbaProvider)
	.filter('avaliacoesFiltroPreenchimentoFilter', avaliacoesFiltroPreenchimentoFilterProvider)
	.filter('avaliacoesFiltroPreenchimentoConsensoFilter', avaliacoesFiltroPreenchimentoConsensoFilterProvider)
	.filter('avaliacoesFiltroConsensoSubordinadosApenas', avaliacoesFiltroConsensoSubordinadosApenasProvider)
	.filter('tipoDeCompetenciasFilter', tipoDeCompetenciasFilterProvider)
	.filter('pdiFilter', pdiFilter)
	.name;

function multFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText.campanha) && angular.isUndefined(searchText.empresa)) {
				return input;
			}

			for (var x = 0; x < input.length; x++) {
				if (angular.isDefined(searchText.campanha) && angular.isDefined(searchText.empresa)) {
					if (input[x].descricao === searchText.campanha.descricao && input[x].empresa.nome === searchText.empresa.nome) {
						returnArray.push(input[x]);
					}
				} else if (angular.isDefined(searchText.empresa)) {
					if (input[x].empresa.nome === searchText.empresa.nome) {
						returnArray.push(input[x]);
					}
				} else if (angular.isDefined(searchText.campanha)) {
					if (input[x].descricao === searchText.campanha.descricao) {
						returnArray.push(input[x]);
					}
				}
			}

			return returnArray;
		}
	};
}

function messageFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].cargo.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].departamento.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function messageExibirFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].funcionarioDestino.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].funcionarioDestino.cargo.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].funcionarioDestino.departamento.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function avaliacoesFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].avaliacao.campanha.descricao.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].avaliacao.descricaoAbreviada.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].avaliador.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function avaliacoesRevisoresFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var revisor = input[x].revisor != null ? input[x].revisor : { nome: '' };
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].avaliacao.campanha.descricao.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].avaliacao.descricaoAbreviada.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| revisor.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].avaliado.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function avaliacoesFiltroConsensoGestorApenasProvider() {
	return function (input, user) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(user)) {
				return input;
			}

			for (var x = 0; x < input.length; x++) {
				if (input[x].consensoIniciado || input[x].consensoLiberado) {
					if (input[x].avaliado.id == user.id) {
						returnArray.push(input[x]);
					}
				}
			}

			return returnArray;
		}
	};
}

function campanhasFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					const achou =
						input[x].descricao.toLowerCase().indexOf(searchTextSplit[y]) !== -1 ||
						input[x].empresa.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1;
					if (achou) count++;
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function competenciasFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].descricaoAbreviada.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].tipo.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].situacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function departamentosFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].idDepartamentoCliente.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].situacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function cursosespecializacaoFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					var preencheOutrosCursosString = input[x].preencheOutrosCursos ? 'Sim' : 'Não';

					if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| preencheOutrosCursosString.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].situacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function cursosgraduacaoFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					var preencheOutrosCursosString = input[x].preencheOutrosCursos ? 'Sim' : 'Não';

					if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| preencheOutrosCursosString.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].situacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function escolaridadeFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					var preencheTipoCursoString = input[x].preencheTipoCurso ? 'Sim' : 'Não';
					var preencheOutrosCursosString = input[x].preencheOutrosCursos ? 'Sim' : 'Não';

					if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| preencheTipoCursoString.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| preencheOutrosCursosString.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].situacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function idiomasFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].situacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function tiposcursoFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					var preencheGraduacaoString = input[x].preencheGraduacao ? 'Sim' : 'Não';
					var preencheEspecializacaoString = input[x].preencheEspecializacao ? 'Sim' : 'Não';

					if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| preencheGraduacaoString.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| preencheEspecializacaoString.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].situacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function funcionariosFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].departamento.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].cargo.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].situacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].situacaoFuncional.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function modelosGraduacaoFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].descricaoModelo.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].situacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function filtroAbaAvaliadosProvider() {
	return function (input, searchText, exibirSomenteSelecionados) {
		if (exibirSomenteSelecionados && angular.isUndefined(searchText)) {
			searchText = '';
		}

		if (input != null) {
			var returnArray = [];
			var returnArraySomenteSelecionados = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].avaliacao && input[x].avaliacao.preencheAv4) {
						if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1 ||
							input[x].cargo.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1 ||
							input[x].avaliador.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1 ||
							input[x].departamento.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1 ||
							input[x].revisor.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1 ||
							input[x].situacaoFuncional.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
							count++;
						}
					} else {
						var dtCargo = '';
						var dtAdmisaoDe = '';

						if (angular.isString(searchTextSplit[y])) {
							var pesquisa = '' + searchTextSplit[y];
							var resultado = pesquisa.includes('t03:00:00.000+0000');
							if (resultado) {
								dtAdmisaoDe = moment(searchTextSplit[y]).toDate();
								var dataCargo = input[x].dataCargo.substring(0, 10) + 't03:00:00.000+0000';
								dtCargo = moment(dataCargo).toDate();
							}
						}

						if (dtCargo == '' && dtAdmisaoDe == '') {
							dtCargo = 'a';
							dtAdmisaoDe = 'b';
						}

						if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1 ||
							input[x].cargo.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1 ||
							input[x].departamento.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1 ||
							dtCargo >= dtAdmisaoDe ||
							input[x].situacaoFuncional.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
							count++;
						}
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}

				if (exibirSomenteSelecionados && input[x].selected) {
					if (count == searchTextSplit.length) {
						returnArraySomenteSelecionados.push(input[x]);
					}
				}
			}

			if (exibirSomenteSelecionados) {
				return returnArraySomenteSelecionados;
			} else {
				return returnArray;
			}
		}
	};
}

function preenchimentoPorCompetenciasFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].descricaoAbreviada.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].tipo.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].avaliacao.descricaoAbreviada.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function preenchimentoPorCompetenciasGestoesFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].descricaoCompetencia.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].descricaoAvaliacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].tipoCompetencia.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function preenchimentoPorCompetenciasSubordinadosFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].descricaoCompetencia.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].descricaoAvaliacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].tipoCompetencia.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function preenchimentoPorCompetenciasParesFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].descricaoCompetencia.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].descricaoAvaliacao.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].tipoCompetencia.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function filtroAvaliadoresAbaProvider() {
	return function (input, searchText, exibirNaoRevisados) {
		if (exibirNaoRevisados && angular.isUndefined(searchText)) {
			searchText = '';
		}

		if (input != null) {
			var returnArray = [];
			var returnArrayNaoRevisados = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				/*Tive que usar um replace quando a palavra tiver 'ç' pois no objeto ela vem com 'c' ai o filtro não pega*/
				var stFuncional = input[x].situacaoFuncional;
				var stFuncionalCaracterEspecial = stFuncional.replace('CA', 'ÇA');

				for (var y = 0; y < searchTextSplit.length; y++) {
					if (angular.isDefined(input[x].gestor)) {
						if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
							|| input[x].cargo.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
							|| input[x].departamento.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
							|| input[x].gestor.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
							|| stFuncionalCaracterEspecial.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
							count++;
						}
					} else {
						if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
							|| input[x].cargo.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
							|| input[x].departamento.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
							|| stFuncionalCaracterEspecial.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
							count++;
						}
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}

				if (exibirNaoRevisados && input[x].revisado == false) {
					if (count == searchTextSplit.length) {
						returnArrayNaoRevisados.push(input[x]);
					}
				}
			}

			if (exibirNaoRevisados) {
				return returnArrayNaoRevisados;
			} else {
				return returnArray;
			}
		}
	};
}

function avaliacoesFiltroPreenchimentoFilterProvider() {
	return function (input, filtro, user) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(filtro)) {
				return input;
			}

			for (var x = 0; x < input.length; x++) {
				if (filtro.naoPreenchida) {
					if (input[x].idCompetencia) {
						if (input[x].percentualPreenchimento == 0) returnArray.push(input[x]);
					} else if (input[x].avaliado.id == user.id) {
						if (input[x].avaliacao.limiteAv1 != null
							&& input[x].limiteAv1 != null
							&& input[x].dataInicioAv1 == null
							&& input[x].dataConclusaoAv1 == null
						) {
							returnArray.push(input[x]);
						}
					} else {
						if (input[x].avaliacao.limiteAv2 != null
							&& input[x].limiteAv2 != null
							&& input[x].dataInicioAv2 == null
							&& input[x].dataConclusaoAv2 == null
						) {
							returnArray.push(input[x]);
						}
					}
				}

				if (filtro.parcial) {
					if (input[x].idCompetencia) {
						if (input[x].percentualPreenchimento > 0 && input[x].percentualPreenchimento < 100) returnArray.push(input[x]);
					} else if (input[x].avaliado.id == user.id) {
						if (input[x].avaliacao.limiteAv1 != null
							&& input[x].limiteAv1 != null
							&& input[x].dataInicioAv1 != null
							&& input[x].dataConclusaoAv1 == null
						) {
							returnArray.push(input[x]);
						}
					} else {
						if (input[x].avaliacao.limiteAv2 != null
							&& input[x].limiteAv2 != null
							&& input[x].dataInicioAv2 != null
							&& input[x].dataConclusaoAv2 == null
						) {
							returnArray.push(input[x]);
						}
					}
				}

				if (filtro.preenchida) {
					if (input[x].idCompetencia) {
						if (input[x].percentualPreenchimento == 100) returnArray.push(input[x]);
					} else if (input[x].avaliado.id == user.id) {
						if (input[x].avaliacao.limiteAv1 != null
							&& input[x].limiteAv1 != null
							&& input[x].dataInicioAv1 != null
							&& input[x].dataConclusaoAv1 != null
						) {
							returnArray.push(input[x]);
						}
					} else {
						if (input[x].avaliacao.limiteAv2 != null
							&& input[x].limiteAv2 != null
							&& input[x].dataInicioAv2 != null
							&& input[x].dataConclusaoAv2 != null
						) {
							returnArray.push(input[x]);
						}
					}
				}

				if (!filtro.naoPreenchida && !filtro.parcial && !filtro.preenchida) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function avaliacoesFiltroPreenchimentoConsensoFilterProvider() {
	return function (input, filtro) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(filtro)) {
				return input;
			}

			for (var x = 0; x < input.length; x++) {
				if (filtro.naoPreenchida) {
					if (input[x].avaliacao.limiteAv3 != null
						&& input[x].limiteAv3 != null
						&& input[x].dataInicioAv3 == null
						&& input[x].dataConclusaoAv3 == null
					) {
						returnArray.push(input[x]);
					}
				}

				if (filtro.parcial) {
					if (input[x].avaliacao.limiteAv3 != null
						&& input[x].limiteAv3 != null
						&& input[x].dataInicioAv3 != null
						&& input[x].dataConclusaoAv3 == null
					) {
						returnArray.push(input[x]);
					}
				}

				if (filtro.preenchida) {
					if (input[x].avaliacao.limiteAv3 != null
						&& input[x].limiteAv3 != null
						&& input[x].dataInicioAv3 != null
						&& input[x].dataConclusaoAv3 != null
					) {
						returnArray.push(input[x]);
					}
				}

				if (!filtro.naoPreenchida && !filtro.parcial && !filtro.preenchida) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function avaliacoesFiltroConsensoSubordinadosApenasProvider() {
	return function (input, user) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(user)) {
				return input;
			}

			for (var x = 0; x < input.length; x++) {
				if (input[x].consensoIniciado || input[x].consensoLiberado) {
					if (input[x].avaliador.id == user.id) {
						returnArray.push(input[x]);
					}
				}
			}

			return returnArray;
		}
	};
}

function tipoDeCompetenciasFilterProvider() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');
			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (angular.isDefined(input[x].paiTO)) {
						if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
							|| input[x].descricao.toLowerCase().indexOf(searchTextSplit[y]) !== -1
							|| input[x].paiTO.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
							count++;
						}
					} else if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].descricao.toLowerCase().indexOf(searchTextSplit[y]) !== -1) {
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}

function pdiFilter() {
	return function (input, searchText) {
		if (input != null) {
			var returnArray = [];

			if (angular.isUndefined(searchText)) {
				return input;
			}

			var searchTextSplit = searchText.toLowerCase().split(' ');

			for (var x = 0; x < input.length; x++) {
				var count = 0;
				for (var y = 0; y < searchTextSplit.length; y++) {
					if (input[x].nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].departamento.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].cargo.nome.toLowerCase().indexOf(searchTextSplit[y]) !== -1
						|| input[x].situacaoFuncional.toLowerCase().indexOf(searchTextSplit[y]) !== -1){
						count++;
					}
				}

				if (count == searchTextSplit.length) {
					returnArray.push(input[x]);
				}
			}

			return returnArray;
		}
	};
}
