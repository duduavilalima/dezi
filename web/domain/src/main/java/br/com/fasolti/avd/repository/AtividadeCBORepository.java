package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.AtividadeCBO;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeCBORepository extends QueryDslCrudRepository<AtividadeCBO> {
	List<AtividadeCBO> findByPerfilId(Long perfilId);
}
