package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.GraduacaoCompetencia;
import br.com.fasolti.avd.domain.ModeloGraduacaoAvalComp;
import br.com.fasolti.avd.domain.ModeloGraduacaoComp;
import br.com.fasolti.avd.domain.QModeloGraduacaoComp;
import br.com.fasolti.avd.repository.GraduacaoCompetenciaRepository;
import br.com.fasolti.avd.repository.ModeloGraduacaoAvalCompRepository;
import br.com.fasolti.avd.repository.ModeloGraduacaoCompRepository;
import br.com.fasolti.avd.service.ModeloGraduacaoCompService;
import br.com.fasolti.avd.transfer.GraduacaoCompetenciaTO;
import br.com.fasolti.avd.transfer.ModeloGraduacaoCompTO;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ModeloGraduacaoCompServiceImpl
	extends BaseAvdService<ModeloGraduacaoComp, ModeloGraduacaoCompTO, ModeloGraduacaoCompTO>
	implements ModeloGraduacaoCompService {

	private final QModeloGraduacaoComp qModeloGraduacaoComp = QModeloGraduacaoComp.modeloGraduacaoComp;

	@Autowired
	private Mapper mapper;

	@Autowired
	private GraduacaoCompetenciaRepository graduacaoCompetenciaRepository;

	@Autowired
	private ModeloGraduacaoAvalCompRepository modeloGraduacaoAvalCompRepository;

	@Autowired
	private ModeloGraduacaoCompRepository modeloGraduacaoCompRepository;


	@Autowired
	public ModeloGraduacaoCompServiceImpl(ModeloGraduacaoCompRepository modeloGraduacaoCompRepository) {
		super(modeloGraduacaoCompRepository);
	}

	@Override
	public ModeloGraduacaoCompTO getByAvaliacaoCompetenciaId(Long avaliacaoCompetenciaId) {
		ModeloGraduacaoAvalComp modeloGraduacaoAvalComp = modeloGraduacaoAvalCompRepository.findByAvaliacaoCompetenciaId(avaliacaoCompetenciaId);

		if (modeloGraduacaoAvalComp != null && modeloGraduacaoAvalComp.getModeloGraduacaoComp() != null)
			return map(modeloGraduacaoAvalComp.getModeloGraduacaoComp());

		return null;
	}

	@Override
	//FIXME: Podem vir mais de um ModeloGraduacaoComp nessa chamada ao repositorio (o modelo pode ser usado em mais de uma avaliacao)
	public ModeloGraduacaoCompTO getByIdModeloGraduacao(Long idModeloGraduacao) {
		ModeloGraduacaoComp modeloGraduacaoComp = modeloGraduacaoCompRepository.findByIdModeloGraduacao(idModeloGraduacao);

		if (modeloGraduacaoComp != null )
			return map(modeloGraduacaoComp);

		return null;
	}

	@Override
	public ModeloGraduacaoCompTO fullObject(ModeloGraduacaoCompTO modeloGraduacao) {
		List<GraduacaoCompetencia> listaGraduacoes = graduacaoCompetenciaRepository.findByModeloGraduacaoId(modeloGraduacao.getId());
		if (modeloGraduacao.getGraduacoes()==null)
			modeloGraduacao.setGraduacoes(new ArrayList<>());
		listaGraduacoes.forEach(x -> {
			modeloGraduacao.getGraduacoes().add(mapper.map(x, GraduacaoCompetenciaTO.class));
		});
		return modeloGraduacao;
	}

	@Override
	public ModeloGraduacaoCompTO obterModeloGraduacaoCompProjections(Long idModeloGraduacao, Long idAvaliacao) {
		return map(
			select(
				Projections.fields(ModeloGraduacaoComp.class, qModeloGraduacaoComp.id, qModeloGraduacaoComp.idAvaliacao
			)
		).where(qModeloGraduacaoComp.idModeloGraduacao.eq(idModeloGraduacao)
				.and(qModeloGraduacaoComp.idAvaliacao.eq(idAvaliacao))).fetchOne());
	}
}
