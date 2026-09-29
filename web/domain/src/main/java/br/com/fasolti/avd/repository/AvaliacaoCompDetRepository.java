package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.AvaliacaoCompDet;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoCompDetRepository extends QueryDslCrudRepository<AvaliacaoCompDet> {

	AvaliacaoCompDet findByAvaliacaoCompetenciaIdAndIdCompetenciaDetalhamento(Long avaliacaoCompetenciaId, Long competenciaDetalhamentoId);

	List<AvaliacaoCompDet> findByAvaliacaoCompetenciaId(Long avaliacaoCompetenciaId);
}
