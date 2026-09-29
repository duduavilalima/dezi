package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.CargoTipoCompetencia;

@Repository
public interface CargoTipoCompetenciaRepository extends QueryDslCustomerRepository<CargoTipoCompetencia, Long> {
	
}