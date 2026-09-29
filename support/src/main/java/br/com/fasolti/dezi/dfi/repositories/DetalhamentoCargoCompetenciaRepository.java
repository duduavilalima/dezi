package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.DetalhamentoCargoCompetencia;

@Repository
public interface DetalhamentoCargoCompetenciaRepository extends QueryDslCustomerRepository<DetalhamentoCargoCompetencia, Long> {
	
}