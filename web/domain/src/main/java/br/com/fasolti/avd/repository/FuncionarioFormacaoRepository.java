package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.FuncionarioFormacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuncionarioFormacaoRepository extends QueryDslCrudRepository<FuncionarioFormacao> {
}
