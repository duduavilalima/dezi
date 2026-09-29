package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.DadoPreenchimentoTO;
import br.com.fasolti.avd.transfer.DadoPreenchimentoTotaisTO;
import br.com.fasolti.commons.client.service.Service;

import java.util.List;

public interface DadoPreenchimentoService extends Service<DadoPreenchimentoTO> {
	DadoPreenchimentoTotaisTO totalizar(Long campanhaId, Long avaliacaoId, Long departamentoId, Long responsavelId);

	List<DadoPreenchimentoTO> listarResponsaveis(Long campanhaId, Long avaliacaoId, Long departamentoId);

	List<DadoPreenchimentoTO> listarPendencias(Long campanhaId, Long avaliacaoId, Long departamentoId);
}
