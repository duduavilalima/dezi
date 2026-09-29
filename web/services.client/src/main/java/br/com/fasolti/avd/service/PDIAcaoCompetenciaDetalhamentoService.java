package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.PDIAcaoCompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.search.PDIAcaoCompetenciaDetalhamentoSearchTO;

import java.util.List;

public interface PDIAcaoCompetenciaDetalhamentoService extends AvdService<PDIAcaoCompetenciaDetalhamentoTO, PDIAcaoCompetenciaDetalhamentoSearchTO> {
	List<PDIAcaoCompetenciaDetalhamentoTO> listByPDIAcao(Long pdiAcaoId);

	List<PDIAcaoCompetenciaDetalhamentoTO> listarPorPDIacaoAvaliado(Long IdFuncionario);
	List<PDIAcaoCompetenciaDetalhamentoTO> listarAcaoPDICompetenciaaDetalhamento(Long IdPdiAcao);
}
