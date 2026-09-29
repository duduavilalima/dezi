package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.CompDetalhamentoHint;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompDetalhamentoHintRepository extends QueryDslCrudRepository<CompDetalhamentoHint> {

	List<CompDetalhamentoHint> findByCompetenciaDetalhamentoId(Long competenciaDetalhamentoId);

	List<CompDetalhamentoHint> findByGraduacaoId(Long graduacaoId);

}
