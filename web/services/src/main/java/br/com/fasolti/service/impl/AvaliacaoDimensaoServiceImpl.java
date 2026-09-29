package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.AvaliacaoDimensao;
import br.com.fasolti.avd.repository.AvaliacaoDimensaoRepository;
import br.com.fasolti.avd.service.AvaliacaoDimensaoService;
import br.com.fasolti.avd.transfer.AvaliacaoDimensaoTO;
import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.service.util.BaseAvdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvaliacaoDimensaoServiceImpl extends BaseAvdService<AvaliacaoDimensao, AvaliacaoDimensaoTO, AvaliacaoDimensaoTO>
	implements AvaliacaoDimensaoService {

	@Autowired
	private AvaliacaoDimensaoRepository avaliacaoDimensaoRepository;

	@Autowired
	public AvaliacaoDimensaoServiceImpl(AvaliacaoDimensaoRepository repository) {
		super(repository);
	}

	@Override
	public List<AvaliacaoDimensaoTO> obterDimensaoPorAvaliacao(AvaliacaoTO avaliacaoTO) {
		return map(avaliacaoDimensaoRepository.findByAvaliacaoId(avaliacaoTO.getId()));
	}
}
