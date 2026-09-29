package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.Unidade;


@Repository
public interface UnidadeRepository extends QueryDslCustomerRepository<Unidade, Long> {

}