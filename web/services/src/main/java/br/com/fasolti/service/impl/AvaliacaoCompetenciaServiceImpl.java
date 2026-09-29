package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Avaliacao;
import br.com.fasolti.avd.domain.AvaliacaoCompetencia;
import br.com.fasolti.avd.domain.QAvaliacao;
import br.com.fasolti.avd.domain.QAvaliacaoCompetencia;
import br.com.fasolti.avd.repository.AvaliacaoCompetenciaRepository;
import br.com.fasolti.avd.repository.PreenchimentoRepository;
import br.com.fasolti.avd.service.AvaliacaoCompetenciaService;
import br.com.fasolti.avd.service.PreenchimentoService;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class AvaliacaoCompetenciaServiceImpl
	extends BaseAvdService<AvaliacaoCompetencia, AvaliacaoCompetenciaTO, AvaliacaoCompetenciaTO>
	implements AvaliacaoCompetenciaService {

	private static final QAvaliacaoCompetencia qAvaliacaoCompetencia = QAvaliacaoCompetencia.avaliacaoCompetencia;

	private static final QAvaliacao qAvaliacao = QAvaliacao.avaliacao;

	@Autowired
	private AvaliacaoCompetenciaRepository avaliacaoCompetenciaRepository;

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	private PreenchimentoRepository preenchimentoRepository;

	@Autowired
	public AvaliacaoCompetenciaServiceImpl(AvaliacaoCompetenciaRepository repository) {
		super(repository);
	}

	@Override
	public List<AvaliacaoCompetenciaTO> listByParticipante(ParticipanteTO participante) {
		/*List<AvaliacaoCompetenciaTO> avaliacaoCompetencias = map(avaliacaoCompetenciaRepository.findByParticipanteId(participante.getId()));

		for (AvaliacaoCompetenciaTO avaliacaoCompetencia : avaliacaoCompetencias) {
			Preenchimento preenchimento = preenchimentoRepository.findByAvaliacaoCompDetId(avaliacaoCompetencia.getId());
			*//*if (preenchimento != null) // FIXME O mapDefault deve prever null
				avaliacaoCompetencia.setPreenchimento((PreenchimentoTO) mapDefault(preenchimento));*//*
		}

		return avaliacaoCompetencias;*/
		return null;
	}

	@Override
	public List<AvaliacaoCompetenciaTO> listByCompetenciaDetalhamento(CompetenciaDetalhamentoTO competenciaDetalhamento) {
		/*List<AvaliacaoCompetenciaTO> avaliacaoCompetencias = map(avaliacaoCompetenciaRepository.findByCompetenciaDetalhamentoId(competenciaDetalhamento.getId()));

		for (AvaliacaoCompetenciaTO avaliacaoCompetencia : avaliacaoCompetencias) {
			Preenchimento preenchimento = preenchimentoRepository.findByAvaliacaoCompDetId(avaliacaoCompetencia.getId());
			*//*if (preenchimento != null) // FIXME O mapDefault deve prever null
				avaliacaoCompetencia.setPreenchimento((PreenchimentoTO) mapDefault(preenchimento));*//*
		}

		return avaliacaoCompetencias;*/
		return null;
	}

	@Override
	public List<AvaliacaoCompetenciaTO> listByAvaliador(FuncionarioTO funcionario) {
		BooleanBuilder builder = new BooleanBuilder();
		/*builder.and(QAvaliacaoCompetencia.avaliacaoCompetencia.participante.avaliador.id.in(funcionario.getId()));
		builder.or(QAvaliacaoCompetencia.avaliacaoCompetencia.participante.revisor.id.in(funcionario.getId()));*/
		return map(avaliacaoCompetenciaRepository.findAll(builder));
	}

	@Override
	public List<AvaliacaoCompetenciaTO> listByAvaliacaoAndAvaliadorAndCompetencia(AvaliacaoTO avaliacao, FuncionarioTO funcionario, CompetenciaTO competencia) {
		BooleanBuilder builder = new BooleanBuilder();
		/*builder.and(QAvaliacaoCompetencia.avaliacaoCompetencia.competenciaDetalhamento.competencia.id.in(competencia.getId()));
		builder.and(QAvaliacaoCompetencia.avaliacaoCompetencia.participante.avaliacao.id.in(avaliacao.getId()));
		builder.and( QAvaliacaoCompetencia.avaliacaoCompetencia.participante.avaliador.id.in(funcionario.getId())
			         .or(QAvaliacaoCompetencia.avaliacaoCompetencia.participante.revisor.id.in(funcionario.getId())) );*/

		return map(avaliacaoCompetenciaRepository.findAll(builder));
	}

	@Override
	public List<AvaliacaoCompetenciaTO> listByParticipanteAndDetalhamento(ParticipanteTO participante, Set<CompetenciaDetalhamentoTO> detalhamentos) {
		List<AvaliacaoCompetenciaTO> listaAvaliacaoCompetencia = new ArrayList<AvaliacaoCompetenciaTO>();

		if (detalhamentos == null || detalhamentos.isEmpty())
			return null;

		detalhamentos.forEach(detalhamento -> {
			if(Situacao.ATIVA.equals(detalhamento.getSituacao())){
				AvaliacaoCompetenciaTO ac = new AvaliacaoCompetenciaTO();
				/*ac.setParticipante(participante);
				ac.setCompetenciaDetalhamento(detalhamento);

				if(participante.getLimiteAv1() != null)
					ac.setPreenchidaAV1(Boolean.FALSE);

				if(participante.getLimiteAv2() != null)
					ac.setPreenchidaAV2(Boolean.FALSE);

				if(participante.getLimiteAv3() != null)
					ac.setPreenchidaAV3(Boolean.FALSE);*/

				listaAvaliacaoCompetencia.add(ac);
			}
		});

		return listaAvaliacaoCompetencia;
	}

	@Override
	public AvaliacaoCompetenciaTO getByAvaliacaoIdAndIdCompetencia(Long avaliacaoId, Long competenciaId) {
		return map(avaliacaoCompetenciaRepository.findByAvaliacaoIdAndIdCompetencia(avaliacaoId, competenciaId));
	}

	@Override
	public List<AvaliacaoCompetenciaTO> listByIdCompetencia(Long id) {
		return map(
			select(
				Projections.fields(
					AvaliacaoCompetencia.class,
					qAvaliacaoCompetencia.id,
					Projections.fields(
						Avaliacao.class,
						qAvaliacaoCompetencia.avaliacao.id,
						qAvaliacaoCompetencia.avaliacao.descricao
					).as("avaliacao")
				)
			)
				.leftJoin(qAvaliacaoCompetencia.avaliacao, QAvaliacao.avaliacao)
				.where(qAvaliacaoCompetencia.idCompetencia.eq(id))
				.fetch()
		);
	}

	@Override
	public AvaliacaoCompetenciaTO obterAvaliacaoCompetenciaProjections(Long avaliacaoId, Long competenciaId) {
		JPAQuery<AvaliacaoCompetencia> avalCompQuery = select();
		avalCompQuery.where(qAvaliacaoCompetencia.avaliacao.id.eq(avaliacaoId)
			.and(qAvaliacaoCompetencia.idCompetencia.eq(competenciaId)));
		avalCompQuery.select(Projections.fields(
			AvaliacaoCompetencia.class,
			qAvaliacaoCompetencia.id,
			qAvaliacaoCompetencia.idCompetencia,
			Projections.fields(Avaliacao.class, qAvaliacao.id).as("avaliacao")
		));

		return map(avalCompQuery.fetchOne());
	}
}
