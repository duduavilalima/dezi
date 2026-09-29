package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.Departamento;

@Repository
public interface DepartamentoRepository extends QueryDslCustomerRepository<Departamento, Long> {

    Departamento findByEmpresaIdAndNome(Long empresaId, String nome);
}