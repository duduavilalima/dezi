package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.CargoTipoCompetencia;
import br.com.fasolti.avd.transfer.CargoTipoCompetenciaTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoTipoCompetenciaRepository extends QueryDslCrudRepository<CargoTipoCompetencia> {

	List<CargoTipoCompetencia> findByCargoId(Long cargoId);

	List<CargoTipoCompetencia> findByCargoIdAndSituacao(Long cargoId, Situacao situacao);

	CargoTipoCompetencia findByCargoIdAndTipoCompetenciaId(Long cargoId, Long tipoCompetenciaId);

	List<CargoTipoCompetenciaTO> findBySituacao(Situacao ativa);

	@Query(value = "select count(ctc.id) from cargo_tipo_competencia ctc where ctc.id_cargo = ?1 and ctc.id_situacao = ?2 ", nativeQuery = true)
	Integer countPerfilAgenteByAvaliacao(Long idCargo, Integer idSituacao);
}
