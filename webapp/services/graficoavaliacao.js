import angular from 'angular';
import ngResource from 'angular-resource';

export default angular.module('services.graficoavaliacao', [ngResource])
	.factory('GraficoAvaliacaoResource', graficoAvaliacaoFactory)
	.name;

function graficoAvaliacaoFactory($resource) {
	return $resource('${restServer}/graficoavaliacao/:id', null, {
		chartRadarByAvaliacao: {
			method: 'POST',
			url: '${restServer}/graficoavaliacao/chartradarbyavaliacao'
		},
		chartRadarAvaliacaoByColaborador: {
			method: 'POST',
			url: '${restServer}/graficoavaliacao/chartradaravaliacaobycolaborador'
		},
		chartRadarAvaliacaoByCargo: {
			method: 'POST',
			url: '${restServer}/graficoavaliacao/chartradaravaliacaobycargo'
		}
	});
}
