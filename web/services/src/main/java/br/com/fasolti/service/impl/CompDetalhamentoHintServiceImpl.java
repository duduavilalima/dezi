package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.CompDetalhamentoHintRepository;
import br.com.fasolti.avd.service.CompDetalhamentoHintService;
import br.com.fasolti.avd.service.GraduacaoService;
import br.com.fasolti.avd.transfer.CompDetalhamentoHintTO;
import br.com.fasolti.avd.transfer.CompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.GraduacaoTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CompDetalhamentoHintServiceImpl
	extends BaseAvdService<CompDetalhamentoHint, CompDetalhamentoHintTO, CompDetalhamentoHintTO>
	implements CompDetalhamentoHintService {

	private final QCompDetalhamentoHint qCompDetalhamentoHint = QCompDetalhamentoHint.compDetalhamentoHint;

	private final QGraduacao qGraduacao = QGraduacao.graduacao;

	@Autowired
	private CompDetalhamentoHintRepository compDetalhamentoHintRepository;

	@Autowired
	private GraduacaoService graduacaoService;

	@Autowired
	public CompDetalhamentoHintServiceImpl(CompDetalhamentoHintRepository repository) {
		super(repository);
	}

	@Override
	public List<CompDetalhamentoHintTO> listByCompetenciaDetalhamento(CompetenciaDetalhamentoTO competenciaDetalhamento) {
		List<GraduacaoTO> graduacoes = graduacaoService.findAllGraduacoesByModeloAndSituacao(competenciaDetalhamento.getModeloGraduacao(), Situacao.ATIVA);
		List<CompDetalhamentoHintTO> compDetalhamentoHintTOList = findAllCompetenciaDetalhamentoHintAndModeloGraduacao(competenciaDetalhamento.getId(), competenciaDetalhamento.getModeloGraduacao().getId());

		graduacoes.forEach(graduacaoTO -> {
			boolean match = compDetalhamentoHintTOList
				.stream()
				.anyMatch(compDetHint -> compDetHint.getGraduacao().getId().equals(graduacaoTO.getId()));

			if(!match) {
				CompDetalhamentoHintTO compDetalhamentoHintTO = createCompDetalhamentoHintTO(competenciaDetalhamento, graduacaoTO);
				compDetalhamentoHintTOList.add(compDetalhamentoHintTO);
			}

		});

		Collections.sort(compDetalhamentoHintTOList);
		return compDetalhamentoHintTOList;
	}

	private CompDetalhamentoHintTO createCompDetalhamentoHintTO(CompetenciaDetalhamentoTO competenciaDetalhamentoTO, GraduacaoTO graduacaoTO) {
		CompDetalhamentoHintTO compDetalhamentoHintTO = new CompDetalhamentoHintTO();
		compDetalhamentoHintTO.setGraduacao(graduacaoTO);
		compDetalhamentoHintTO.setCompetenciaDetalhamento(competenciaDetalhamentoTO);
		compDetalhamentoHintTO.setComentarioObrigatorio(graduacaoTO.getComentarioObrigatorio());
		compDetalhamentoHintTO.setHint(graduacaoTO.getHint());
		return compDetalhamentoHintTO;
	}

	private List<CompDetalhamentoHintTO> findAllCompetenciaDetalhamentoHint(Long idCompDetalhamento) {
		return map(
			select(
				Projections.fields(
					CompDetalhamentoHint.class,
					qCompDetalhamentoHint.id,
					qCompDetalhamentoHint.hint,
					qCompDetalhamentoHint.comentarioObrigatorio,
					Projections.fields(
						CompetenciaDetalhamento.class,
						qCompDetalhamentoHint.competenciaDetalhamento.id,
						qCompDetalhamentoHint.competenciaDetalhamento.descricao,
						qCompDetalhamentoHint.competenciaDetalhamento.ordem,
						qCompDetalhamentoHint.competenciaDetalhamento.situacao
					).as("competenciaDetalhamento"),
					Projections.fields(
						Graduacao.class,
						qCompDetalhamentoHint.graduacao.id,
						qCompDetalhamentoHint.graduacao.label,
						qCompDetalhamentoHint.graduacao.hint,
						qCompDetalhamentoHint.graduacao.ponto,
						qCompDetalhamentoHint.graduacao.ordem
					).as("graduacao")
				)
			)
				.join(qCompDetalhamentoHint.competenciaDetalhamento, QCompetenciaDetalhamento.competenciaDetalhamento)
				.join(qCompDetalhamentoHint.graduacao, QGraduacao.graduacao)
				.where(qCompDetalhamentoHint.competenciaDetalhamento.id.eq(idCompDetalhamento))
				.fetch()
		);
	}

	private List<CompDetalhamentoHintTO> findAllCompetenciaDetalhamentoHintAndModeloGraduacao(Long idCompDetalhamento, Long idModeloGraduacao) {
		QCompetenciaDetalhamento qCompetenciaDetalhamento = QCompetenciaDetalhamento.competenciaDetalhamento;
		QGraduacao qGraduacao = QGraduacao.graduacao;
		QModeloGraduacao qModeloGraduacao = QModeloGraduacao.modeloGraduacao;

		return map(
			select(
				Projections.fields(
					CompDetalhamentoHint.class,
					qCompDetalhamentoHint.id,
					qCompDetalhamentoHint.hint,
					qCompDetalhamentoHint.comentarioObrigatorio,
					Projections.fields(
						CompetenciaDetalhamento.class,
						qCompDetalhamentoHint.competenciaDetalhamento.id,
						qCompDetalhamentoHint.competenciaDetalhamento.descricao,
						qCompDetalhamentoHint.competenciaDetalhamento.ordem,
						qCompDetalhamentoHint.competenciaDetalhamento.situacao
					).as("competenciaDetalhamento"),
					Projections.fields(
						Graduacao.class,
						qCompDetalhamentoHint.graduacao.id,
						qCompDetalhamentoHint.graduacao.label,
						qCompDetalhamentoHint.graduacao.hint,
						qCompDetalhamentoHint.graduacao.ponto,
						qCompDetalhamentoHint.graduacao.ordem,
						qCompDetalhamentoHint.graduacao.situacao
					).as("graduacao")
				)
			)
				.join(qCompDetalhamentoHint.competenciaDetalhamento, qCompetenciaDetalhamento)
				.join(qCompDetalhamentoHint.graduacao, qGraduacao)
				.join(qGraduacao.modeloGraduacao, qModeloGraduacao)
				.where(
					qCompetenciaDetalhamento.id.eq(idCompDetalhamento)
					.and(qGraduacao.situacao.eq(Situacao.ATIVA))
					.and(qModeloGraduacao.id.eq(idModeloGraduacao))
				)
				.fetch()
		);
	}

	@Override
	public List<CompDetalhamentoHintTO> listByGraduacao(GraduacaoTO graduacao) {
		return map(compDetalhamentoHintRepository.findByGraduacaoId(graduacao.getId()));
	}

	@Override
	public List<CompDetalhamentoHintTO> obterCompDetalhamentoHintProjections(Long idCompDet) {
		JPAQuery<CompDetalhamentoHint> hintQuery = select();
		hintQuery.where(qCompDetalhamentoHint.competenciaDetalhamento.id.eq(idCompDet));
		hintQuery.join(qCompDetalhamentoHint.graduacao, qGraduacao);
		hintQuery.select(Projections.fields(
			CompDetalhamentoHint.class,
			qCompDetalhamentoHint.id,
			qCompDetalhamentoHint.hint,
			qCompDetalhamentoHint.comentarioObrigatorio,
			Projections.fields(Graduacao.class, qGraduacao.id).as("graduacao")
		));

		return map(hintQuery.fetch());
	}

	@Override
	public List<CompDetalhamentoHintTO> obterCompDetalhamentoHintAndGraduacaoProjections(Long idCompDet) {
		JPAQuery<CompDetalhamentoHint> hintQuery = select();
		hintQuery.where(qCompDetalhamentoHint.competenciaDetalhamento.id.eq(idCompDet));
		hintQuery.join(qCompDetalhamentoHint.graduacao, qGraduacao);
		hintQuery.select(Projections.fields(
			CompDetalhamentoHint.class,
			qCompDetalhamentoHint.id,
			qCompDetalhamentoHint.hint,
			qCompDetalhamentoHint.comentarioObrigatorio,
			Projections.fields(Graduacao.class,
				qGraduacao.id,
				qGraduacao.ordem,
				qGraduacao.label,
				qGraduacao.ponto,
				qGraduacao.situacao
			).as("graduacao")
		));

		return map(hintQuery.fetch());
	}

}
