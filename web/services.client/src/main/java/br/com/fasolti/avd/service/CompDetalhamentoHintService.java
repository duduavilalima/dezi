package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.CompDetalhamentoHintTO;
import br.com.fasolti.avd.transfer.CompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.GraduacaoTO;

import java.util.List;

public interface CompDetalhamentoHintService
	extends AvdService<CompDetalhamentoHintTO, CompDetalhamentoHintTO> {

	List<CompDetalhamentoHintTO> listByCompetenciaDetalhamento(CompetenciaDetalhamentoTO competenciaDetalhamento);

	List<CompDetalhamentoHintTO> listByGraduacao (GraduacaoTO graduacao);

	List<CompDetalhamentoHintTO> obterCompDetalhamentoHintProjections(Long idCompDet);

	List<CompDetalhamentoHintTO> obterCompDetalhamentoHintAndGraduacaoProjections(Long idCompDet);
}
