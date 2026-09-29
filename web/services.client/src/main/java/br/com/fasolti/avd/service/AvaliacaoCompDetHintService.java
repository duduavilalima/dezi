package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.AvaliacaoCompDetHintTO;

import java.util.List;

public interface AvaliacaoCompDetHintService extends AvdService<AvaliacaoCompDetHintTO, AvaliacaoCompDetHintTO> {

	List<AvaliacaoCompDetHintTO> listByAvaliacaoCompDetId(Long avaliacaoCompDetId);

	List<AvaliacaoCompDetHintTO> listByAvaliacaoCompDetIdProjections(Long avaliacaoCompDetId);

	AvaliacaoCompDetHintTO obterAvaliacaoCompDetHintProjections(Long avaliacaoCompDetId, Long hintId);

    List<AvaliacaoCompDetHintTO> findByGraduacaoId(Long graduacaoId);
}
