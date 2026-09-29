package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Mensagem;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemRepository extends QueryDslCrudRepository<Mensagem> {
}
