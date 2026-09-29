package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.AvalDimComDet;
import br.com.fasolti.avd.transfer.AvalDimComDetTO;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvalDimComDetRepository extends QueryDslCrudRepository<AvalDimComDet> {

	AvalDimComDet findByIdAvaliacaoAndIdCompDet(Long idAvaliacao, Long idDetalhamento);

}
