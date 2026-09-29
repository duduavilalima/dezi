package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.LogAuth;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogAuthRepository extends QueryDslCrudRepository<LogAuth> {

}
