package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.CargoTO;
import br.com.fasolti.avd.transfer.CompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.CompetenciaTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.types.Situacao;

import java.util.List;
import java.util.Set;

public interface CompetenciaDetalhamentoService
	extends AvdService<CompetenciaDetalhamentoTO, CompetenciaDetalhamentoTO> {

	List<CompetenciaDetalhamentoTO> listByCompetencia(Long id);

	List<CompetenciaDetalhamentoTO> listByParticipante(ParticipanteTO participante);

	Set<CompetenciaDetalhamentoTO> listByCompetenciaAndCargoAndSituacao(CompetenciaTO competencia, CargoTO cargo, Situacao situacao);

	List<CompetenciaDetalhamentoTO> listActiveByCompetencia(Long competenciaId);

	CompetenciaDetalhamentoTO obterCompetenciaDetalhamentoProjections(Long idCompDet);
}
