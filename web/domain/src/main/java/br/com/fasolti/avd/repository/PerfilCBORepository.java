package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.PerfilCBO;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilCBORepository extends QueryDslCrudRepository<PerfilCBO> {
	List<PerfilCBO> findByOcupacaoId(Long ocupacaoId);
}
