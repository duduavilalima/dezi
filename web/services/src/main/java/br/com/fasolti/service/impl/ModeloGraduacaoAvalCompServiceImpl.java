package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.ModeloGraduacaoAvalCompRepository;
import br.com.fasolti.avd.service.ModeloGraduacaoAvalCompService;
import br.com.fasolti.avd.transfer.ModeloGraduacaoAvalCompTO;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModeloGraduacaoAvalCompServiceImpl
	extends BaseAvdService<ModeloGraduacaoAvalComp, ModeloGraduacaoAvalCompTO, ModeloGraduacaoAvalCompTO>
	implements ModeloGraduacaoAvalCompService {

	private final QModeloGraduacaoAvalComp qModeloGraduacaoAvalComp = QModeloGraduacaoAvalComp.modeloGraduacaoAvalComp;

	private final QAvaliacaoCompetencia qAvaliacaoCompetencia = QAvaliacaoCompetencia.avaliacaoCompetencia;

	private final QAvaliacaoCompDet qAvaliacaoCompDet = QAvaliacaoCompDet.avaliacaoCompDet;

	private final QModeloGraduacaoComp qModeloGraduacaoComp = QModeloGraduacaoComp.modeloGraduacaoComp;

	@Autowired
	public ModeloGraduacaoAvalCompServiceImpl(ModeloGraduacaoAvalCompRepository repository) {
		super(repository);
	}

	@Override
	public ModeloGraduacaoAvalCompTO obterModeloGraduacaoAvalCompProjections(Long avaliacaoCompetenciaId, Long modeloGraduacaoId, Long avaliacaoCompDetId) {

		BooleanBuilder predicate = new BooleanBuilder();

		if (avaliacaoCompetenciaId != null)
			predicate.and(qModeloGraduacaoAvalComp.avaliacaoCompetencia.id.eq(avaliacaoCompetenciaId));

		if (modeloGraduacaoId != null)
			predicate.and(qModeloGraduacaoAvalComp.modeloGraduacaoComp.idModeloGraduacao.eq(modeloGraduacaoId));

		if (avaliacaoCompDetId != null) {
			predicate.and(qModeloGraduacaoAvalComp.avaliacaoCompDet.id.eq(avaliacaoCompDetId));
		}

		JPAQuery<ModeloGraduacaoAvalComp> modelGradAvalCompQuery = select();
		modelGradAvalCompQuery.where(predicate);
		modelGradAvalCompQuery.select(Projections.fields(
			ModeloGraduacaoAvalComp.class,
			qModeloGraduacaoAvalComp.id,
			Projections.fields(ModeloGraduacaoComp.class, qModeloGraduacaoComp.id).as("modeloGraduacaoComp"),
			Projections.fields(AvaliacaoCompetencia.class, qAvaliacaoCompetencia.id).as("avaliacaoCompetencia"),
			Projections.fields(AvaliacaoCompDet.class, qAvaliacaoCompDet.id).as("avaliacaoCompDet")
		));

		return map(modelGradAvalCompQuery.fetchOne());
	}
}
