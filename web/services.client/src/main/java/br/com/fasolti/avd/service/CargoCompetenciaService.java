package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.CargoCompetenciaTO;

import java.util.List;

public interface CargoCompetenciaService extends AvdService<CargoCompetenciaTO, CargoCompetenciaTO> {

	List<CargoCompetenciaTO> findAllCargoCompetenciaByCargo(Long idCargo);

	//List<CargoCompetenciaTO> obterCargoCompetenciaPorTipoCompetencia(Long idTipoCompetencia);

	List<CargoCompetenciaTO> findCargoCompetenciaByCargoAndTipoCompetenciaAtivos(Long idCargo, Long idTipoCompetencia);

	List<CargoCompetenciaTO> findCargoCompetenciaByCompetenciaAtivos(Long idCompetencia);

	CargoCompetenciaTO findCargoCompetenciaByCargoAndCompetencia(Long idCargo, Long idCompetencia);

	List<CargoCompetenciaTO> findAllCargoCompetenciaByCargoAndAtivos(Long cargoId);

	List<CargoCompetenciaTO> obterCargoCompetenciaPorCargoAndTipoCompetencia(Long idCargo, Long idTipoCOmpetencia);

	void inativarCargoCompetenciaByCargoAndTipoCompetencia(Long idCargo, Long idTipoCompetencia);
}
