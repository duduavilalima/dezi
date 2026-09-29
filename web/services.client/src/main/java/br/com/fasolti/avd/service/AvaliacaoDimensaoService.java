package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.AvaliacaoDimensaoTO;
import br.com.fasolti.avd.transfer.AvaliacaoTO;

import java.util.List;

public interface AvaliacaoDimensaoService extends AvdService<AvaliacaoDimensaoTO, AvaliacaoDimensaoTO> {

	List<AvaliacaoDimensaoTO> obterDimensaoPorAvaliacao(AvaliacaoTO avaliacaoTO);
}
