package br.com.fasolti.dezi.dfi.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.Cargo;

@Repository
public interface CargoRepository extends QueryDslCustomerRepository<Cargo, Long> {
	
	List<Cargo> findByEmpresaId(Long id);
	Cargo findByEmpresaIdAndNome(Long id, String nome);
	
}