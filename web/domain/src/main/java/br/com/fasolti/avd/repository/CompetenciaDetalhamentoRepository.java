package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.CompetenciaDetalhamento;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetenciaDetalhamentoRepository extends QueryDslCrudRepository<CompetenciaDetalhamento> {

	List<CompetenciaDetalhamento> findByCompetenciaId(Long competenciaId);

	List<CompetenciaDetalhamento> findByCompetenciaIdAndSituacao(Long competenciaId, Situacao situacao);

	CompetenciaDetalhamento findByCompetenciaIdAndDescricao(Long competenciaId, String descricao);
}
