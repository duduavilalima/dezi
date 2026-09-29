package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.AvalPesoCargoComp;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;

import java.util.List;

public interface AvalPesoCargoCompRepository extends QueryDslCrudRepository<AvalPesoCargoComp> {

	List<AvalPesoCargoComp> findByIdPreenchimento(Long idPreenchimento);

}
