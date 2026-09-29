package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.ApontamentoFuncional;
import br.com.fasolti.avd.domain.TipoCompetencia;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApontamentoFuncionalRepository extends QueryDslCrudRepository<ApontamentoFuncional> {

	List<ApontamentoFuncional> findByAvaliadoId(Long avaliadoId);

	List<ApontamentoFuncional> findByAvaliadoIdAndAvaliadorId(Long avaliadoId, Long avaliadorId);

	List<ApontamentoFuncional> findByAvaliadoIdAndFeedforwardIsNotNull(Long id);
}
