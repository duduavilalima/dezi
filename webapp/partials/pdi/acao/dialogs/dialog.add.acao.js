import SituacaoPDI from '../../../model/SituacaoPDI';

/* @ngInject */
export default function dialogAddAcao(
	$scope,
	$mdDialog,
	title,
	listCampanhas,
	listParticipantes,
	listCompetencias,
	funcionario,
	acao,
	fn_addAcao) {
	$scope.showCampanhas = listCampanhas.length > 0 ? true : false;
	$scope.showAvaliacoes = false;
	$scope.handleChange = (field, value) => {
		$scope.object[field] = value;
	};

	$scope.handleChangeCampanha = (name, value) => {
		const avaliacoes = $scope.listParticipantes.filter( part => part.avaliacao.campanha.id === value.id).map(part => part.avaliacao);
		$scope.listAvaliacoes = [];
		avaliacoes.forEach(av => {
			if(!$scope.listAvaliacoes.find(scopeAv => scopeAv.id === av.id)) {
				$scope.listAvaliacoes.push(av);
			}
		});
		$scope.isAvaliacaoRequired = true;
		$scope.showAvaliacoes = $scope.listAvaliacoes.length > 0 ? true : false;
	};

	//AUTOCOMPLETE DA COMPETENCIA
	//TODO: Ver se tem necessidade de usar um regexp para remoção de acentos e afins
	$scope.querySearchCompetencia = query =>
		$scope.listCompetencias
			.filter(competencia => competencia.descricaoAbreviada.toLowerCase().startsWith(query.toLowerCase()));

	$scope.onRemoveChip = () => {
		onChangeCompetenciaDetalhamento();
	};

	//setando os valores selecionados no autocomplete da competencia
	$scope.selectedItemCompetenciaChange = item => {
		if (item && !$scope.object.chipsCompetencias.find(comp => comp.id === item.id)) {
			$scope.object.chipsCompetencias.push(item);
		}

		onChangeCompetenciaDetalhamento();
	};

	const onChangeCompetenciaDetalhamento = () => {
		if($scope.object.chipsCompetencias.length === 1) {
			$scope.isDisabledDetalhamento = false;
			$scope.listDetalhamentos = $scope.object.chipsCompetencias[0].detalhamentos;
		} else {
			$scope.listDetalhamentos = undefined;
			$scope.object.competenciaDetalhamento = undefined;
			$scope.isDisabledDetalhamento = true;
		}
	};

	$scope.cancel = () => {
		$mdDialog.cancel();
	};

	const verifyDatas = () => {
		const dataPrevInicio = new Date($scope.object.dataPrevistaInicio);
		const dataPrevFim = new Date($scope.object.dataPrevistaFim);

		if(dataPrevInicio > dataPrevFim) {
			return false;
		}

		return true;
	};

	const transformCompetencias = () => {
		const competencias = [];
		$scope.object.chipsCompetencias.forEach(comp => {
			let findComp;
			if(acao && acao.pdiAcaoCompetencias) {
				findComp = acao.pdiAcaoCompetencias.find(pdiAcaoComp => pdiAcaoComp.competencia.id === comp.id);
				if(findComp) {
					if($scope.object.chipsCompetencias.length > 1 ) {
						findComp.competenciaDetalhamento = undefined;
					} else if ($scope.object.competenciaDetalhamento) {
						findComp.competenciaDetalhamento = $scope.object.competenciaDetalhamento;
					}

					competencias.push(findComp);
				}
			}

			if(!findComp) {
				competencias.push({
					pdiAcao: {id: $scope.object.id},
					competencia: comp,
					competenciaDetalhamento: $scope.object.competenciaDetalhamento
				});
			}
		});
		return competencias;
	};

	$scope.saveAcao = () => {
		if(!verifyDatas()) {
			return $scope.$emit('notify-error', 'Data prevista final não pode ser menor que a inicial.');
		}

		let participante;
		if($scope.object.avaliacao) {
			participante = $scope.listParticipantes.find(part => part.avaliacao.id === $scope.object.avaliacao.id);
		}

		const object = {
			id: $scope.object.id,
			situacao: acao? acao.situacao : SituacaoPDI.EM_APROVACAO,
			acaoParaDesenvolvimento: $scope.object.acaoParaDesenvolvimento,
			dataPrevistaInicio: $scope.object.dataPrevistaInicio,
			dataPrevistaFim: $scope.object.dataPrevistaFim,
			dataInicio: acao? acao.dataInicio : undefined,
			funcionario: $scope.funcionario,
			participante: participante,
			pdiAcaoCompetencias: transformCompetencias()
		};

		//TODO: Verificar duplicidade na lista de competencias, ou entao, na hora de adicionar
		/* if(competenciaSelecionada.length > 1) {
			return $scope.$emit('notify-error', `A competência ${competenciaSelecionada[0].competencia.descricaoAbreviada} está duplicada!`);
		}*/

		fn_addAcao(object);
		$mdDialog.hide();
	};

	function loadLists() {
		$scope.dataAtual = new Date();
		$scope.dataMinimaPrevista = new Date();
		$scope.isDisabledDetalhamento = true;
		$scope.isAvaliacaoRequired = false;
		$scope.listCampanhas = listCampanhas;
		$scope.listParticipantes = listParticipantes;
		$scope.listCompetencias = listCompetencias;
		$scope.funcionario = funcionario;
		$scope.listAvaliacoes = undefined;
		$scope.listDetalhamentos = undefined;
	}

	function loadObject() {
		$scope.title = title;
		const loadAcao = (acao) => {
			$scope.object = {
				id: acao.id,
				acaoParaDesenvolvimento: acao.acaoParaDesenvolvimento,
				dataPrevistaInicio: acao.dataPrevistaInicio,
				dataPrevistaFim: acao.dataPrevistaFim,
				campanha: acao.participante? acao.participante.avaliacao.campanha : undefined,
				avaliacao: acao.participante? acao.participante.avaliacao : undefined,
				chipsCompetencias: acao.pdiAcaoCompetencias.map(acaoComp => acaoComp.competencia) || [],
				competenciaDetalhamento: undefined
			};
			if(acao.pdiAcaoCompetencias && acao.pdiAcaoCompetencias.length === 1) {
				$scope.listDetalhamentos = acao.pdiAcaoCompetencias[0].competencia.detalhamentos;

				$scope.object.competenciaDetalhamento = acao.pdiAcaoCompetencias[0].competenciaDetalhamento;
				$scope.isDisabledDetalhamento = false;
			}

			if(acao.participante) {
				$scope.listAvaliacoes = $scope
					.listParticipantes
					.filter( part => part.avaliacao.campanha.id === acao.participante.avaliacao.campanha.id)
					.map(part => part.avaliacao);
			}
		};

		if(acao) {
			loadAcao(acao);
		} else {
			$scope.object = {
				chipsCompetencias: []
			};
		}

		if ($scope.object.id &&  $scope.object.id > 0) {
			var data = new Date($scope.object.dataPrevistaInicio);
			$scope.dataAtual = data<$scope.dataAtual?new Date(data):$scope.dataAtual;
		}

		$scope.dataMinimaPrevista = new Date($scope.object.dataPrevistaInicio);
	}

	loadLists();
	loadObject();
}
