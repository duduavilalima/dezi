package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.AvaliacaoDetCargoCompTO;

public interface AvaliacaoDetCargoCompService
	extends AvdService<AvaliacaoDetCargoCompTO, AvaliacaoDetCargoCompTO> {

	AvaliacaoDetCargoCompTO obterAvaliacaoDetCargoCompProjections(Long avaliacaoCompDetId, Long detCargoCompId);

}
