package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.CargoCompetencia;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoCompetenciaRepository extends QueryDslCrudRepository<CargoCompetencia> {

	List<CargoCompetencia> findByCargoId(Long cargoId);

	List<CargoCompetencia> findByCargoIdAndSituacao(Long cargoId, Situacao situacao);

	CargoCompetencia findByCargoIdAndCompetenciaId(Long cargoId, Long competenciaId);

	List<CargoCompetencia> findByCargoIdAndCompetenciaTipoId(Long idCargo, Long idTipoCompetencia);
}
