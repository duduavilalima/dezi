package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Conceito;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;

import java.util.List;

public interface ConceitoRepository extends QueryDslCrudRepository<Conceito> {

	List<Conceito> findByEmpresaId(Long empresaId);
}
