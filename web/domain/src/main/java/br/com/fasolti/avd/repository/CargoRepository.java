package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Cargo;
import br.com.fasolti.avd.domain.TipoCompetencia;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoRepository extends QueryDslCrudRepository<Cargo> {

	List<Cargo> findByEmpresaIdAndSituacao(Long empresaId, Situacao situacao);

	Cargo findByEmpresaIdAndIdCargoClienteIgnoreCase(Long empresaId, String IdCargoCliente);

	List<Cargo> findByEmpresaIdAndNomeIgnoreCase(Long empresaId, String nome);

	Cargo findByEmpresaIdAndNome(Long empresaId, String nome);

//	List<Cargo> findByCargoPaiIdAndIdEmpresa(Long cargoId, Long empresaId);
}
