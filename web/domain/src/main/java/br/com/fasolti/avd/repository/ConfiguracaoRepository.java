package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Configuracao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfiguracaoRepository extends QueryDslCrudRepository<Configuracao> {
	List<Configuracao> findByFuncionarioIdOrderByIdDesc(Long funcionarioId);
}
