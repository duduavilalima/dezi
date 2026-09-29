package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.DimensaoCompetenciaDetalhamento;
import br.com.fasolti.avd.repository.DimensaoCompetenciaDetalhamentoRepository;
import br.com.fasolti.avd.service.DimensaoCompetenciaDetalhamentoService;
import br.com.fasolti.avd.transfer.DimensaoCompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.DimensaoTO;
import br.com.fasolti.service.util.BaseAvdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DimensaoCompetenciaDetalhamentoServiceImpl
	extends BaseAvdService<DimensaoCompetenciaDetalhamento, DimensaoCompetenciaDetalhamentoTO, DimensaoCompetenciaDetalhamentoTO>
	implements DimensaoCompetenciaDetalhamentoService {

	@Autowired
	private DimensaoCompetenciaDetalhamentoRepository dimensaoCompDetRepository;

	@Autowired
	public DimensaoCompetenciaDetalhamentoServiceImpl(DimensaoCompetenciaDetalhamentoRepository repository) {
		super(repository);
	}

	@Override
	public List<DimensaoCompetenciaDetalhamentoTO> obterDetalhamentosPorDimensao(DimensaoTO dimensao) {
		return map(dimensaoCompDetRepository.findByDimensaoId(dimensao.getId()));
	}

	@Override
	public DimensaoCompetenciaDetalhamentoTO obterDimensaoDetalhamentoPorCompDet(Long idCompetenciaDetalhemento) {
		return map(dimensaoCompDetRepository.findByCompetenciaDetalhamentoId(idCompetenciaDetalhemento));
	}
}
