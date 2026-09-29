package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.DepartamentoGestor;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartamentoGestorRepository extends QueryDslCrudRepository<DepartamentoGestor> {

	List<DepartamentoGestor> findByFuncionarioId(Long idGestor);

	@Modifying
	@Query(value="delete from departamento_gestor where id_gestor = ?1",  nativeQuery=true)
	void deleteByFuncionarioId(Long idGestor);
}
