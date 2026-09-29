package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.PendenciaTO;

import java.util.List;

public interface PendenciaService extends AvdService<PendenciaTO, PendenciaTO> {

	List<PendenciaTO> findByAvaliacaoId(Long idAvaliacao);

}
