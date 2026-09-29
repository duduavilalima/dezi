package br.com.fasolti.dezi.dfi.repositories;

import java.util.List;

import br.com.fasolti.avd.domain.TipoCompetencia;

public interface TipoCompetenciaRepository extends QueryDslCustomerRepository<TipoCompetencia, Long> {
	
	List<TipoCompetencia> findByIdEmpresa(Long id);

}
