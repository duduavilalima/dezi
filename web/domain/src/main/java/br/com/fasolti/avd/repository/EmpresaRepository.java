package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpresaRepository extends QueryDslCrudRepository<Empresa> {

	List<Empresa> findEmpresaByContratanteId(Long contratanteId);

	List<Empresa> findBySituacao(Situacao situacao);

	Empresa findByHostAndSituacao(String host, Situacao ativa);
}
