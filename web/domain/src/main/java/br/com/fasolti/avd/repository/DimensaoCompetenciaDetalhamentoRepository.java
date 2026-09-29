package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.DimensaoCompetenciaDetalhamento;
import br.com.fasolti.avd.transfer.DimensaoCompetenciaDetalhamentoTO;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DimensaoCompetenciaDetalhamentoRepository extends QueryDslCrudRepository<DimensaoCompetenciaDetalhamento> {

	List<DimensaoCompetenciaDetalhamento> findByDimensaoId(Long idDimensao);

	DimensaoCompetenciaDetalhamento findByCompetenciaDetalhamentoId(Long idCompetenciaDetalhemento);
}
