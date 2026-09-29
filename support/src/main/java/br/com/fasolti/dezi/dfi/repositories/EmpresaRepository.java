package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.Empresa;

@Repository
public interface EmpresaRepository extends QueryDslCustomerRepository<Empresa, Long> {
	
	Empresa findByNome(String nome);

}