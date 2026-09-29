package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.AvalDimComDetTO;

public interface AvalDimComDetService extends AvdService<AvalDimComDetTO, AvalDimComDetTO> {

	AvalDimComDetTO findAvalDimCompDetByDetalhamentoAndAvaliacao(Long idAvaliacao, Long idDetalhamento);
}
