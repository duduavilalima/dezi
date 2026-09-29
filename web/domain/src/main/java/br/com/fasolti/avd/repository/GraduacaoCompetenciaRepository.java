package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.GraduacaoCompetencia;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraduacaoCompetenciaRepository extends QueryDslCrudRepository<GraduacaoCompetencia> {

	GraduacaoCompetencia findByModeloGraduacaoIdAndIdGraduacao(Long modeloGraduacaoId, Long graduacaoId);

	List<GraduacaoCompetencia> findByModeloGraduacaoId(Long modeloGraduacaoId);

}
