package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.CargoTipoCompetenciaTO;

import java.util.List;

public interface CargoTipoCompetenciaService extends AvdService<CargoTipoCompetenciaTO, CargoTipoCompetenciaTO> {

	List<CargoTipoCompetenciaTO> findAllCargoTipoCompetenciaByCargo(Long idCargo);

	CargoTipoCompetenciaTO findCargoTipoCompetenciaByCargoAndTipoCompetencia(Long idCargo, Long idCompetencia);

    List<CargoTipoCompetenciaTO> findAllCargoTipoCompetenciaByTipoCompetencia(Long tipoCompetenciaId);

	void inativarCargoTipoCompetenciaByCargo(Long cargoId, Long tipoCompetenciaId);

	void inativarCargoTipoCompetencia(Long cargoId, Long tipoCompetenciaId);

	void inativarCargoTipoCompetenciaByTipoCompetencia(Long tipoCompetenciaId);

	List<CargoTipoCompetenciaTO> findAllCargoTipoCompetenciaByCargoAndAtivos(Long cargoId);

	List<CargoTipoCompetenciaTO> buscarTodosTiposAtivos();
}
