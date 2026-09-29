package br.com.fasolti.dezi.dfi.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.CompetenciaDetalhamento;

@Repository
public interface CompetenciaDetalhamentoRepository extends QueryDslCustomerRepository<CompetenciaDetalhamento, Long> {
	
	List<CompetenciaDetalhamento> findBycriador(String criador);

}