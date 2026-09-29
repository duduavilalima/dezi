package br.com.fasolti.dezi.dfi.repositories;

import java.util.List;

import br.com.fasolti.avd.domain.Competencia;

public interface CompetenciaRepository extends QueryDslCustomerRepository<Competencia, Long> {
	
	List<Competencia> findByEmpresaId(Long id);

}
