package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Departamento;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartamentoRepository extends QueryDslCrudRepository<Departamento> {

	List<Departamento> findByEmpresaIdAndSituacao(Long empresaId, Situacao situacao);

	Departamento findByEmpresaIdAndNome(Long empresaId, String nome);

	Departamento findByEmpresaIdAndIdDepartamentoCliente(Long empresaId, String idCliente);

	Departamento findByEmpresaIdAndIdDepartamentoClienteIgnoreCase(Long empresaId, String idDepartamentoCliente);

	List<Departamento> findByDepartamentoPaiIdAndSituacao(Long departamentoPaiId, Situacao situacao);
}
