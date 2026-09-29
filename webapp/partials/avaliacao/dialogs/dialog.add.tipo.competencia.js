/* @ngInject */
export default function dialogTipoCompetenciaCtrl(
	$scope,
	$mdDialog,
	listaTiposCompetenciaOriginal,
	listaTiposCompetenciaSelecionadas,
	objetoTela,
	title,
	toolBarClassDialog,
	TipoCompetencia
) {
	const $dialog = this;
	$dialog.tipoCompetenciaList = [];
	$dialog.listaTiposCompetenciaOriginal = listaTiposCompetenciaOriginal;
	$dialog.listaTiposCompetenciaSelecionadas = listaTiposCompetenciaSelecionadas;
	$dialog.title = title;
	$dialog.toolBarClassDialog = toolBarClassDialog;
	$dialog.object = objetoTela;

	$dialog.comboSubTipo = false;

	function carregarComboTipoCompetencia() {
		return new Promise((resolve, reject) => {
			TipoCompetencia.obterSemPai().$promise
				.then(data => {
					$dialog.listDeTipos = [];
					data.forEach(item => {
						let tem = false;

						$dialog.listaTiposCompetenciaSelecionadas.forEach(value => {
							if (item.id === value.id) {
								tem = true;
							}
						});

						if (!tem) {
							TipoCompetencia.obterFilhos({id: item.id}).$promise
								.then(subTipos => {
									let contador = 0;
									
									if (subTipos.length > 0) {
										subTipos.forEach(subtipo => {
											$dialog.listaTiposCompetenciaSelecionadas.forEach(tipoSelecionado => {
												if (subtipo.id === tipoSelecionado.id) {
													contador++;
												}
											});
										});
									}

									if (contador < subTipos.length) {
										$dialog.listDeTipos.push(item);
									} else if (subTipos.length == 0) {
										$dialog.listDeTipos.push(item);
									}
								}).catch(response => {
									$scope.$emit('notify-critical', response);
								});
						}
					});
					resolve(true);
				}).catch(response => {
					reject(response);
					$scope.$emit('notify-critical', response);
				});
		});
	}

	function carregarComboSubTipo(objeto) {
		if(objeto.tipoCompetencia) {
			TipoCompetencia.obterFilhos({id: objeto.tipoCompetencia.id}).$promise
				.then(subTipos => {
					$dialog.listDeSubTipos = [];
					if (subTipos.length > 0) {
						$dialog.comboSubTipo = true;
						subTipos.forEach(subtipo => {
							let achou = false;
							$dialog.listaTiposCompetenciaSelecionadas.forEach( tipoSelecionado => {
								if (subtipo.id === tipoSelecionado.id) {
									achou = true;
								}
							});
							if (!achou) {
								$dialog.listDeSubTipos.push(subtipo);
							}
						});
					}
				}).catch(response => {
					$scope.$emit('notify-critical', response);
				});
		}
	}

	carregarComboTipoCompetencia();

	$dialog.onChangeSubTipo = () => {
		$dialog.comboSubTipo = false;
		carregarComboSubTipo($dialog.object);
	};

	$dialog.hide = () => {
		$mdDialog.hide();
	};

	$dialog.cancel = () => {
		$mdDialog.cancel();
	};

	$dialog.confirmar = () => {
		if ($dialog.comboSubTipo == true) {
			$dialog.object.tipoCompetencia = $dialog.object.subTipoCompetencia;
		}

		$dialog.object.tipoCompetencia.percentualAutoavaliacao = 0;
		$dialog.object.tipoCompetencia.gestor = false;
		$dialog.object.tipoCompetencia.percentualGestor = 0;
		$dialog.object.tipoCompetencia.subordinados = false;
		$dialog.object.tipoCompetencia.percentualSubordinados = 0;
		$dialog.object.tipoCompetencia.pares = false;
		$dialog.object.tipoCompetencia.percentualPares = 0;
		$dialog.object.tipoCompetencia.desabilitarCampo = false;

		if (objetoTela.autoavaliacao && !objetoTela.gestores && !objetoTela.subordinados && !objetoTela.pares) {
			$dialog.object.tipoCompetencia.autoavaliacao = true;
			$dialog.object.tipoCompetencia.percentualAutoavaliacao = 0;
			$dialog.object.tipoCompetencia.desabilitarCampo = true;
		} else if (!objetoTela.autoavaliacao && objetoTela.gestores && !objetoTela.subordinados && !objetoTela.pares) {
			$dialog.object.tipoCompetencia.gestor = true;
			$dialog.object.tipoCompetencia.percentualGestor = 0;
			$dialog.object.tipoCompetencia.desabilitarCampo = true;
		} else if (!objetoTela.autoavaliacao && !objetoTela.gestores && objetoTela.subordinados && !objetoTela.pares) {
			$dialog.object.tipoCompetencia.subordinados = true;
			$dialog.object.tipoCompetencia.percentualSubordinados = 0;
			$dialog.object.tipoCompetencia.desabilitarCampo = true;
		} else if (!objetoTela.autoavaliacao && !objetoTela.gestores && !objetoTela.subordinados && objetoTela.pares) {
			$dialog.object.tipoCompetencia.pares = true;
			$dialog.object.tipoCompetencia.percentualPares = 0;
			$dialog.object.tipoCompetencia.desabilitarCampo = true;
		}

		$dialog.listaTiposCompetenciaSelecionadas.push($dialog.object.tipoCompetencia);
		$mdDialog.cancel();
	};
}
