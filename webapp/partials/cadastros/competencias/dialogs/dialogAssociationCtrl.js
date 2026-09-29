import angular from 'angular';

/* @ngInject */
export default function DialogAssociationCtrl(
	$scope, 
	$mdDialog, 
	title, 
	object, 
	msg, 
	acao, 
	listHints, 
	fnSave,
	fnCancel, 
	resourceAssociation,
	ModeloGraduacao,
	Graduacao
) {
	const $dialog = this;
	$dialog.object = object;
	$dialog.listHints = listHints;
	
	if ($dialog.object.id == null) {
		$dialog.object = {};
		
		resourceAssociation.get().$promise
			.then(data => {
				$dialog.object = data;
				$dialog.object.competenciaDetalhamentosCalibrahint = [];
				findModelosGraduacoes();
			})
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	} else {
		resourceAssociation.get({ id: $dialog.object.id }).$promise
			.then(data => {
				$dialog.object = data;
				$dialog.object.modeloGraduacao = angular.copy($dialog.object.modeloGraduacao);
				$dialog.idModeloAntigo = $dialog.object.modeloGraduacao.id;
				findModelosGraduacoes();
				loadHints();
			})
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	}

	$dialog.title = title;
	$dialog.msg = msg;
	$dialog.acao = acao;

	const createCompetenciaDetalhamentoHint = (graduacao, competenciaDetalhamento) => {
		const novoHint = {};
		const novaCompetenciaDetalhamento = angular.copy(competenciaDetalhamento);
		delete novaCompetenciaDetalhamento.competenciaDetalhamentosCalibrahint;

		novoHint.competenciaDetalhamento = novaCompetenciaDetalhamento;
		novoHint.graduacao = graduacao;
		novoHint.hint = graduacao.hint;
		return novoHint;
	};

	$dialog.changeModeloGraduacao = idModelo => {
		if (angular.isUndefined($dialog.idModeloAntigo) || idModelo != $dialog.idModeloAntigo) {
			createHintByGraduacao(idModelo);
		} else {
			loadHints();
		}
	};

	const findModelosGraduacoes = () => {
		const filtro = {};
		filtro.situacao = 'ATIVA';
		ModeloGraduacao.buscar(null,filtro).$promise
			.then(dataModelos => {
				$dialog.listModeloGraduacao = [];
				$dialog.listModeloGraduacao = dataModelos;
			})
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};

	const createHintByGraduacao = idModelo => {
		const modelo = {};
		modelo.id = idModelo;
		Graduacao.graduacoesAtivasPorModelo({ modeloGraduacao: modelo }).$promise
			.then(graduacoes => {
				$dialog.object.competenciaDetalhamentosCalibrahint = [];
				angular.forEach(graduacoes, graduacao => {
					if (graduacao.situacao == 'ATIVA') {
						const hint = createCompetenciaDetalhamentoHint(graduacao, $dialog.object);
						$dialog.object.modeloGraduacao = modelo;
						$dialog.object.competenciaDetalhamentosCalibrahint.push(hint);
					}
				});
			})
			.catch(response => {
				$scope.$emit('notify-critical', response);
			});
	};

	const loadHints = () => {
		if (angular.isDefined($dialog.listHints)) {
			$dialog.object.competenciaDetalhamentosCalibrahint = [];
			angular.forEach($dialog.listHints, hint => {
				if (angular.isUndefined(hint.comentarioObrigatorio)) hint.comentarioObrigatorio = false;
				$dialog.object.competenciaDetalhamentosCalibrahint.push(hint);
			});
		}
	};

	$dialog.hide = () => {$mdDialog.hide();};

	$dialog.cancel = () => {fnCancel($dialog.hide);};

	$dialog.save = () => {fnSave($dialog.object, $dialog.hide, $dialog.msg, $dialog.acao);};
}