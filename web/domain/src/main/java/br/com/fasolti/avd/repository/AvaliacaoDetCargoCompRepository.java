package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.AvaliacaoDetCargoComp;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoDetCargoCompRepository extends QueryDslCrudRepository<AvaliacaoDetCargoComp> {

	List<AvaliacaoDetCargoComp> findByAvaliacaoCompDetId(Long avaliacaoCompDetId);

}
