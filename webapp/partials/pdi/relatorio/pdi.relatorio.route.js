import angular from 'angular';
import stateUtils from 'utils/state.utils';
import menuUtils from 'utils/menu.utils';
import ROUTES from 'utils/routes';
import { SituacaoPDIInfo } from '../../model/SituacaoPDI';
import Situacao from '../../model/Situacao';

export default angular.module('routes.pdirelatorio', [stateUtils,menuUtils])
	.run(menuConfig)
	.config(routeConfig)
	.name;

function menuConfig(MenuUtils) {
	const item = {
		stateName: 'pdi.pdirelatorio',
		title: 'Relatórios',
		group: false,
		icon: 'icon-clipboard-text',
		state: ROUTES.MAIN.PDI.RELATORIO.LIST.name,
		translate: 'PDI.MENU_RELATORIO',
		weight: 4,
		roles: ['ROLE_ADMINISTRADOR', 'ROLE_CONSULTOR_RH', 'ROLE_GESTOR_RH', 'ROLE_GESTOR_AREA', 'ROLE_FUNCIONARIO']
	};

	MenuUtils.addMenu(item);
}

function routeConfig($lazyStateProvider) {
	const moduleLoader = () => import('./pdi.relatorio.module');

	/* @ngInject */
	function listRoles(UserService) {
		return UserService.roles().$promise;
	}

	/* @ngInject */
	const listFuncionarios = (Funcionario) => Funcionario.buscar(null, {situacao: 'ATIVA'})
		.$promise.then((funcionarios) => {
			funcionarios.filter(funcionario => {
				return funcionario.situacao !== Situacao.INATIVA;
			})
				.sort((a, b) => {
					if (a.nome < b.nome) return -1;
					if (a.nome > b.nome) return 1;
					return 0;
				});
			return funcionarios;
		});

	/* @ngInject */
	const listCargos = (Cargo) => Cargo.all()
		.$promise.then((cargos) => {
			cargos.filter(cargo => {
				return cargo.situacao !== Situacao.INATIVA;
			})
				.sort((a, b) => {
					if (a.nome < b.nome) return -1;
					if (a.nome > b.nome) return 1;
					return 0;
				});
			return cargos;
		});

	/* @ngInject */
	const listDepartamentos = (Departamento) => Departamento.all()
		.$promise.then((departamentos) => {
			departamentos.filter(departamento => {
				return departamento.situacao !== Situacao.INATIVA;
			})
				.sort((a, b) => {
					if (a.nome < b.nome) return -1;
					if (a.nome > b.nome) return 1;
					return 0;
				});
			return departamentos;
		});

	/* @ngInject */
	const listarCompetencia = (CargoCompetencia) => CargoCompetencia.all()
		.$promise.then((cargoCompetencias) => {
			const competencias = cargoCompetencias.map(cargoComp => cargoComp.competencia);
			competencias.filter(competencia => {
				return competencia.situacao !== Situacao.INATIVA;
			})
				.sort((a, b) => {
					if (a.descricaoAbreviada < b.descricaoAbreviada) return -1;
					if (a.descricaoAbreviada > b.descricaoAbreviada) return 1;
					return 0;
				});
			return competencias;
		});

	/* Obtém todas as avaliações do avaliado */
	/* @ngInject */
	const listAvaliacoes = (Avaliacao) =>
		Avaliacao.obterAvaliacoesPDI()
			.$promise.then((avaliacoes) => {
				const campanhas = [];
				avaliacoes.forEach(avaliacao => {
					const campanhaExist = campanhas.find(camp => {
						if(camp) {
							return	avaliacao.campanha.id === camp.id;
						}
					});
					if(campanhaExist) {
						delete avaliacao.campanha;
						campanhaExist.avaliacoes.push(avaliacao);
					} else {
						const campanha = avaliacao.campanha;
						campanha.avaliacoes = [];
						delete avaliacao.campanha;
						campanha.avaliacoes.push(avaliacao);
						campanhas.push(campanha);
					}
				});

				return campanhas;
			});

	/* @ngInject */
	const getSituacaoPDI = () => {
		const situacaoList = [...SituacaoPDIInfo.names];
		return situacaoList;
	};

	const controllerAlias = '$ctrl';

	$lazyStateProvider
		.state({
			abstract: true,
			moduleLoader,
			stateName: ROUTES.MAIN.PDI.RELATORIO.name,
			url: '/pdi/relatorio',
			template: '<ui-view></ui-view>',
		})
		.state({
			moduleLoader,
			stateName: ROUTES.MAIN.PDI.RELATORIO.LIST.name,
			url: '/list',
			templateUrl: 'pdi.relatorio.html',
			controller: 'PDIRelatorioCtrl',
			controllerAs: controllerAlias,
			resolve: {
				listFuncionarios: listFuncionarios,
				listSetores: listDepartamentos,
				listCargos: listCargos,
				listCompetencia: listarCompetencia,
				listSituacao: getSituacaoPDI,
				listCampanhas: listAvaliacoes,
				roles: listRoles
			}
		});
}
