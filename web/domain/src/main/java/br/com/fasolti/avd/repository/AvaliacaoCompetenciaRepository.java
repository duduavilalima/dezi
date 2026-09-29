package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.AvaliacaoCompetencia;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoCompetenciaRepository extends QueryDslCrudRepository<AvaliacaoCompetencia> {

//	List<AvaliacaoCompetencia> findByParticipanteId(Long participanteId);

//	List<AvaliacaoCompetencia> findByCompetenciaDetalhamentoId(Long competenciaDetalhamentoId);

	AvaliacaoCompetencia findByAvaliacaoIdAndIdCompetencia(Long avaliacaoId, Long competenciaId);

	List<AvaliacaoCompetencia> findByIdCompetencia(Long competenciaId);
}
