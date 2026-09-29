package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.CargoCompetencia;

@Repository
public interface CargoCompetenciaRepository extends QueryDslCustomerRepository<CargoCompetencia, Long> {
	
}