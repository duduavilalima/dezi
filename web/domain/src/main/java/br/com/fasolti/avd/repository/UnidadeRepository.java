package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Unidade;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnidadeRepository extends QueryDslCrudRepository<Unidade> {

	List<Unidade> findByEmpresaIdAndNomeIgnoreCase(Long empresaId, String nome);

	Unidade findByEmpresaIdAndNome(Long empresaId, String nome);

}
