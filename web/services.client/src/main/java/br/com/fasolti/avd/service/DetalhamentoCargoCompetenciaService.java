package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.CargoCompetenciaTO;
import br.com.fasolti.avd.transfer.CompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.CompetenciaTO;
import br.com.fasolti.avd.transfer.DetalhamentoCargoCompetenciaTO;
import br.com.fasolti.avd.types.Situacao;

import java.util.List;
import java.util.Optional;

public interface DetalhamentoCargoCompetenciaService
	extends AvdService<DetalhamentoCargoCompetenciaTO, DetalhamentoCargoCompetenciaTO> {

	void updateDetalhamentoCCList(CargoCompetenciaTO cargoCompetenciaTO);

	List<DetalhamentoCargoCompetenciaTO> findOrCreateDetalhamentosCCByCompetencia(Long competenciaId, Optional<Long> cargoCompetenciaId);

	List<DetalhamentoCargoCompetenciaTO> findOrCreateDetalhamentosCC(Long competenciaId, Long cargoCompetenciaId);

	List<DetalhamentoCargoCompetenciaTO> findAllDetalhamentosCCByCargoCompetencia(Long idCargoCompetencia);

	List<DetalhamentoCargoCompetenciaTO> findAllDetalhamentosCCByCompetenciaDetalhamento(Long compDetalhamentoId);

	DetalhamentoCargoCompetenciaTO findDetalhamentoCCByCargoCompetenciaAndCompetenciaDetalhamento(Long idCargoCompetencia, Long competenciaDetalhamentoId);

	void removerDetalhamentosCCListByCargoCompetencia(CargoCompetenciaTO object);

	void removerDetalhamentosCCListByCompetenciaDetalhamento(CompetenciaDetalhamentoTO competenciaDetalhamentoTO);

	DetalhamentoCargoCompetenciaTO obterDetalhamentoCCProjections(Long idDetCC);


	List<DetalhamentoCargoCompetenciaTO> findDetalhamentosAplicaveisAoCargo(Long idCargo, Situacao idSituacao);
}
