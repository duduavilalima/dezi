package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.AvalDimComDet;
import br.com.fasolti.avd.repository.AvalDimComDetRepository;
import br.com.fasolti.avd.service.AvalDimComDetService;
import br.com.fasolti.avd.transfer.AvalDimComDetTO;
import br.com.fasolti.service.util.BaseAvdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvalDimComDetServiceImpl extends BaseAvdService<AvalDimComDet, AvalDimComDetTO, AvalDimComDetTO>
	implements AvalDimComDetService {

	@Autowired
	private AvalDimComDetRepository avalDimComDetRepository;

	@Autowired
	public AvalDimComDetServiceImpl(AvalDimComDetRepository avalDimComDetRepository) {
		super(avalDimComDetRepository);
	}

	@Override
	public AvalDimComDetTO findAvalDimCompDetByDetalhamentoAndAvaliacao(Long idAvaliacao, Long idDetalhamento) {
		return map(avalDimComDetRepository.findByIdAvaliacaoAndIdCompDet(idAvaliacao, idDetalhamento));
	}
}
