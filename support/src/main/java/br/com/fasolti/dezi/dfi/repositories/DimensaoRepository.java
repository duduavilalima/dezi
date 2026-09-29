package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.Dimensao;

@Repository
public interface DimensaoRepository extends QueryDslCustomerRepository<Dimensao, Long> {
	
}