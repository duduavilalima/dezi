package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.CompDetalhamentoHint;

@Repository
public interface CompDetalhamentoHintRepository extends QueryDslCustomerRepository<CompDetalhamentoHint, Long> {
	
}