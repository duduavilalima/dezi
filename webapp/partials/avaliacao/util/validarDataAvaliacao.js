export default function validarData($scope, obj, novaAv) {
	const dataAtual = new Date();
	const hoje = new Date(dataAtual.getFullYear(), dataAtual.getMonth(), dataAtual.getDate());

	if (obj.preencheAv1 && obj.limiteAv1 && novaAv) {
		if (obj.limiteAv1 < hoje) {
			$scope.$emit('notify-error', 'A data limite da AutoAvaliação (AV1) deve ser maior ou igual a data atual', 'Atenção!');
			return false;
		}
	}

	if (obj.preencheAv2 && obj.limiteAv2 && novaAv) {
		if (obj.limiteAv2 < hoje) {
			$scope.$emit('notify-error', 'A data limite da Avaliação (AV2) deve ser maior ou igual a data atual', 'Atenção!');
			return false;
		}
	}

	if (obj.preencheAv3 && obj.limiteAv3 && novaAv) {
		if (obj.limiteAv3 < hoje) {
			$scope.$emit('notify-error', 'A data limite do Consenso (AV3) deve ser maior ou igual a data atual', 'Atenção!');
			return false;
		}
	}

	if (obj.preencheAv4 && obj.limiteAv4 && novaAv) {
		if (obj.limiteAv4 < hoje) {
			$scope.$emit('notify-error', 'A data limite da Revisão (AV4) deve ser maior ou igual a data atual', 'Atenção!');
			return false;
		}
	}

	if (obj.preencheAv3 && (obj.limiteAv3 < obj.limiteAv2)) {
		$scope.$emit('notify-error', 'A data limite do Consenso (AV3) deve ser maior ou igual a data da Avaliação (AV2)', 'Atenção!');
		return false;
	}

	if (obj.preencheAv3 && (obj.limiteAv3 < obj.limiteAv1)) {
		$scope.$emit('notify-error', 'A data limite do Consenso (AV3) deve ser maior ou igual a data da AutoAvaliação (AV1)', 'Atenção!');
		return false;
	}

	if (obj.preencheAv1 && (obj.limiteAv1 < obj.limiteAv2)) {
		$scope.$emit('notify-error', 'A data limite da AutoAvaliação (AV1) deve ser maior ou igual a data da Avaliação (AV2)', 'Atenção!');
		return false;
	}

	if (obj.preencheAv3 && (obj.limiteAv3 < obj.limiteAv2)) {
		$scope.$emit('notify-error', 'A data limite do Consenso (AV3) deve ser maior ou igual a data da Avaliação (AV2)', 'Atenção!');
		return false;
	}

	if (obj.preencheAv3 && (obj.limiteAv3 < obj.limiteAv1)) {
		$scope.$emit('notify-error', 'A data limite do Consenso (AV3) deve ser maior ou igual a data da AutoAvaliação (AV1)', 'Atenção!');
		return false;
	}

	if (obj.preencheAv1 && (obj.limiteAv1 < obj.limiteAv2)) {
		$scope.$emit('notify-error', 'A data limite da AutoAvaliação (AV1) deve ser maior ou igual a data da Avaliação (AV2)', 'Atenção!');
		return false;
	}

	return true;
}
