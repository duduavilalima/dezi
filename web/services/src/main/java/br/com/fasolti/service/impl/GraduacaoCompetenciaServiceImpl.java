package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.GraduacaoCompetencia;
import br.com.fasolti.avd.domain.ModeloGraduacaoComp;
import br.com.fasolti.avd.domain.QGraduacaoCompetencia;
import br.com.fasolti.avd.domain.QModeloGraduacaoComp;
import br.com.fasolti.avd.repository.GraduacaoCompetenciaRepository;
import br.com.fasolti.avd.service.GraduacaoCompetenciaService;
import br.com.fasolti.avd.transfer.GraduacaoCompetenciaTO;
import br.com.fasolti.avd.transfer.GraduacaoTO;
import br.com.fasolti.avd.types.GraduacaoCorEnum;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GraduacaoCompetenciaServiceImpl
	extends BaseAvdService<GraduacaoCompetencia, GraduacaoCompetenciaTO, GraduacaoCompetenciaTO>
	implements GraduacaoCompetenciaService{

	private final QGraduacaoCompetencia qGraduacaoCompetencia = QGraduacaoCompetencia.graduacaoCompetencia;
	private final QModeloGraduacaoComp qModeloGraduacaoComp = QModeloGraduacaoComp.modeloGraduacaoComp;

	@Autowired
	private GraduacaoCompetenciaRepository graduacaoCompetenciaRepository;

	@Autowired
	public GraduacaoCompetenciaServiceImpl(GraduacaoCompetenciaRepository graduacaoCompetenciaRepository) {
		super(graduacaoCompetenciaRepository);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public void updateCoresGraduacaoCompetenciaByModelo(Long modeloGraduacaoId) {
		List<GraduacaoCompetenciaTO> graduacaoCompetenciaTOList = listByModeloGraduacaoId(modeloGraduacaoId);
		graduacaoCompetenciaTOList.forEach(graduacaoCompetenciaTO -> {
			graduacaoCompetenciaTO.setCor(GraduacaoCorEnum.valueOf((graduacaoCompetenciaTO.getOrdem()==null || graduacaoCompetenciaTO.getOrdem() > 10)?11:graduacaoCompetenciaTO.getOrdem()).getNome());
			saveOrUpdate(graduacaoCompetenciaTO);
		});
	}

	@Override
	public List<GraduacaoCompetenciaTO> listByModeloGraduacaoId(Long modeloGraduacaoId) {
		return map(graduacaoCompetenciaRepository.findByModeloGraduacaoId(modeloGraduacaoId));
	}

	@Override
	public GraduacaoCompetenciaTO getByModeloGraduacaoIdAndIdGraduacao(Long modeloGraduacaoId, Long graduacaoId) {
		return map(graduacaoCompetenciaRepository.findByModeloGraduacaoIdAndIdGraduacao(modeloGraduacaoId, graduacaoId));
	}

	@Override
	public GraduacaoCompetenciaTO getByModeloGraduacaoIdAndIdGraduacaoProjections(Long modeloGraduacaoId, Long graduacaoId) {
		JPAQuery<GraduacaoCompetencia> gradCompQuery = select();
		gradCompQuery.where(
			qGraduacaoCompetencia.modeloGraduacao.id.eq(modeloGraduacaoId)
			.and(qGraduacaoCompetencia.idGraduacao.eq(graduacaoId)));
		gradCompQuery.select(Projections.fields(
			GraduacaoCompetencia.class,
			qGraduacaoCompetencia.id,
			qGraduacaoCompetencia.idGraduacao,
			qGraduacaoCompetencia.ordem,
			qGraduacaoCompetencia.label,
			qGraduacaoCompetencia.hint,
			qGraduacaoCompetencia.ponto,
			qGraduacaoCompetencia.cor
		));

		return map(gradCompQuery.fetchOne());
	}

	@Override
	public GraduacaoCompetenciaTO obterGraduacaoCompetenciaProjections(Long modeloGraduacaoId, Long graduacaoId) {
		JPAQuery<GraduacaoCompetencia> gradCompQuery = select();
		gradCompQuery.where(qGraduacaoCompetencia.modeloGraduacao.id.eq(modeloGraduacaoId)
			.and(qGraduacaoCompetencia.idGraduacao.eq(graduacaoId)));
		gradCompQuery.select(Projections.fields(
			GraduacaoCompetencia.class,
			qGraduacaoCompetencia.id
		));

		return map(gradCompQuery.fetchOne());
	}

}
