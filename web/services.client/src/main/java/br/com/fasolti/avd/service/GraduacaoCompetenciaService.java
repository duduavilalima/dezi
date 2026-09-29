package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.GraduacaoCompetenciaTO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GraduacaoCompetenciaService extends AvdService<GraduacaoCompetenciaTO, GraduacaoCompetenciaTO> {

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	void updateCoresGraduacaoCompetenciaByModelo(Long modeloGraduacaoId);

	List<GraduacaoCompetenciaTO> listByModeloGraduacaoId(Long modeloGraduacaoId);

	GraduacaoCompetenciaTO getByModeloGraduacaoIdAndIdGraduacao(Long modeloGraduacaoId, Long graduacaoId);

	GraduacaoCompetenciaTO getByModeloGraduacaoIdAndIdGraduacaoProjections(Long modeloGraduacaoId, Long graduacaoId);

	GraduacaoCompetenciaTO obterGraduacaoCompetenciaProjections(Long modeloGraduacaoId, Long graduacaoId);

}
