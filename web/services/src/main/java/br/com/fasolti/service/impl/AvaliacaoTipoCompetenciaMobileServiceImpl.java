package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.AvaliacaoTipoCompetencia;
import br.com.fasolti.avd.domain.QAvaliacaoTipoCompetencia;
import br.com.fasolti.avd.domain.QTipoCompetencia;
import br.com.fasolti.avd.repository.AvaliacaoTipoCompetenciaRepository;
import br.com.fasolti.avd.service.AvaliacaoTipoCompetenciaMobileService;
import br.com.fasolti.avd.service.AvaliacaoTipoCompetenciaService;
import br.com.fasolti.avd.transfer.mobile.AvaliacaoTipoCompetenciaMobileTO;
import br.com.fasolti.service.util.BaseDeziService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AvaliacaoTipoCompetenciaMobileServiceImpl
	extends BaseDeziService<AvaliacaoTipoCompetencia, AvaliacaoTipoCompetenciaMobileTO>
	implements AvaliacaoTipoCompetenciaMobileService {


	private final QAvaliacaoTipoCompetencia qAvaliacaoTipoCompetencia = QAvaliacaoTipoCompetencia.avaliacaoTipoCompetencia;
	private final QTipoCompetencia qTipoCompetencia = QTipoCompetencia.tipoCompetencia;

	@Autowired
	private AvaliacaoTipoCompetenciaService avaliacaoTipoCompetenciaService;

	@Autowired
	public AvaliacaoTipoCompetenciaMobileServiceImpl(AvaliacaoTipoCompetenciaRepository repository) {
		super(repository);
	}



}
