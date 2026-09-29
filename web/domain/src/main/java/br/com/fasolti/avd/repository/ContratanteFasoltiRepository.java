package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.ContratanteFasolti;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContratanteFasoltiRepository extends QueryDslCrudRepository<ContratanteFasolti> {

}
