package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.Funcionario;

@Repository
public interface FuncionarioRepository extends QueryDslCustomerRepository<Funcionario, Long> {

}