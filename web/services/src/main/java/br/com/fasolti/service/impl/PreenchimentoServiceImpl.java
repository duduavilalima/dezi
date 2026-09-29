package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.AvaliacaoCompetenciaRepository;
import br.com.fasolti.avd.repository.PreenchimentoRepository;
import br.com.fasolti.avd.service.PreenchimentoService;
import br.com.fasolti.avd.transfer.AvaliacaoCompetenciaTO;
import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.CargoTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.PreenchimentoTO;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class PreenchimentoServiceImpl extends BaseAvdService<Preenchimento, PreenchimentoTO, PreenchimentoTO> implements PreenchimentoService {

	private static final QPreenchimento qPreenchimento = QPreenchimento.preenchimento;
	private static final QParticipante qParticipante = QParticipante.participante;
	private static final QAvaliacaoCompDet qAvaliacaoCompDet = QAvaliacaoCompDet.avaliacaoCompDet;
	private static final QAvaliacaoCompetencia qAvaliacaoCompetencia = QAvaliacaoCompetencia.avaliacaoCompetencia;
	private final QAvaliacao qAvaliacao = QAvaliacao.avaliacao;
	private final QFuncionario qFuncionario = QFuncionario.funcionario;
	private final QGraduacao qGraduacao = QGraduacao.graduacao;
	private final QCargo qCargo = QCargo.cargo;

	@Autowired
	private AvaliacaoCompetenciaRepository avaliacaoCompetenciaRepository;

	@Autowired
	private PreenchimentoRepository preenchimentoRepository;

	@Autowired
	public PreenchimentoServiceImpl(PreenchimentoRepository preenchimentoRepository) {
		super(preenchimentoRepository);
	}

	private List<Preenchimento> findByParticipante(Long participanteId) {
		return preenchimentoRepository.findByParticipanteId(participanteId);
	}

	public List<PreenchimentoTO> preenchimentosByParticipante(Long participanteId) {
		return map(preenchimentoRepository.findByParticipanteId(participanteId));
	}

	@Override
	public List<PreenchimentoTO> findByAvaliacao(AvaliacaoTO avaliacao) {
		return map(preenchimentoRepository.findByAvaliacao(avaliacao.getId()));
	}

	@Override
	public List<PreenchimentoTO> listByParticipante(ParticipanteTO participante) throws ServiceException {
		return map(findByParticipante(participante.getId()));
	}

	@Override
	@Transactional
	public List<PreenchimentoTO> reset(ParticipanteTO participante) throws ServiceException {
		/*List<Preenchimento> preenchimentos = findByParticipante(participante.getId());
		repository.delete(preenchimentos);

		List<AvaliacaoCompetencia> detalhamentos = avaliacaoCompetenciaRepository.findByParticipanteId(participante.getId());

		// FIXME Não pode chamar o save diretamente - trocar tudo para TO e chamar do serviço
		for (AvaliacaoCompetencia detalhamento : detalhamentos) {
			Preenchimento p = new Preenchimento();
			p.setAvaliacaoCompDet(new AvaliacaoCompDet());
//			p.setAvaliacaoCompetencia(detalhamento);
			repository.save(p);
		}

		// Recarrega a lista
		preenchimentos = findByParticipante(participante.getId());

		return map(preenchimentos);*/
		return null;
	}

	@Override
	public List<PreenchimentoTO> findByCargo(CargoTO cargo) {
		return map(preenchimentoRepository.findByCargoParticipante(cargo.getId()));
	}

	@Override
	public PreenchimentoTO findByAvaliacaoCompetencia(AvaliacaoCompetenciaTO avaliacaoCompetencia) {
		List<Preenchimento> listaPreenchimento = preenchimentoRepository.findByAvaliacaoCompDetId(avaliacaoCompetencia.getId());
		if (listaPreenchimento != null && !listaPreenchimento.isEmpty())
			return map(listaPreenchimento.get(0));
		return null;
	}

	@Override
	public List<PreenchimentoTO> listByAvaliacaoCompDetId(Long avaliacaoCompetenciaId) {
		return map(preenchimentoRepository.findByAvaliacaoCompDetId(avaliacaoCompetenciaId));
	}

	@Override
	public List<PreenchimentoTO> listByAvaliacaoCompDetAndAvaliador(Long avaliacaoCompetenciaId, Long avaliadorId) {
		return map(preenchimentoRepository.findByAvaliacaoCompDetIdAndParticipanteAvaliadorId(avaliacaoCompetenciaId, avaliadorId));
	}

	@Override
	public List<PreenchimentoTO> listByAvaliacaoCompDetAndAvaliado(Long avaliacaoCompetenciaId, Long avaliadoId) {
		return map(preenchimentoRepository.findByAvaliacaoCompDetIdAndParticipanteAvaliadoId(avaliacaoCompetenciaId, avaliadoId));
	}

	@Override
	public Long count(BooleanExpression eq) {
		return super.count(eq);
	}

	@Override
	public PreenchimentoTO obterPreenchimentoProjections(Long participanteId, Long avaliacaoCompetenciaId) {
		return map(select(Projections.fields(
			Preenchimento.class,
			qPreenchimento.id
			)).where(qPreenchimento.participante.id.eq(participanteId)
				.and(qPreenchimento.avaliacaoCompDet.id.eq(avaliacaoCompetenciaId))).fetchOne()
		);
	}

	@Override
	public List<PreenchimentoTO> obterListaDePreenchimentoDoParticipante(ParticipanteTO participanteTO) {
		return map(select().where(qPreenchimento.participante.id.eq(participanteTO.getId())).fetch());
	}

	@Override
	public List<PreenchimentoTO> listByAvaliadoAndLimiteAndCompetenciaProjections(Long avaliadoId, Date data, SituacaoAvaliacao situacao, Long avaliacaoCompetenciaId) {
		JPAQuery<Preenchimento> preenchimentoQuery = select();
		preenchimentoQuery.where(qPreenchimento.participante.avaliado.id.eq(avaliadoId),
			qPreenchimento.participante.limiteAv1.isNotNull(),
			qPreenchimento.participante.limiteAv1.goe(data),
			qPreenchimento.avaliacaoCompDet.avaliacaoCompetencia.id.eq(avaliacaoCompetenciaId),
			qPreenchimento.participante.avaliacao.situacao.notIn(situacao));
		preenchimentoQuery.join(qPreenchimento.participante, qParticipante);
		preenchimentoQuery.join(qPreenchimento.avaliacaoCompDet, qAvaliacaoCompDet);
		preenchimentoQuery.join(qParticipante.avaliador, qFuncionario);
		preenchimentoQuery.join(qParticipante.avaliado, qFuncionario);
		preenchimentoQuery.join(qParticipante.avaliacao, qAvaliacao);
		preenchimentoQuery.leftJoin(qPreenchimento.graduacaoAv1, qGraduacao);
		preenchimentoQuery.leftJoin(qPreenchimento.graduacaoAv2, qGraduacao);
		preenchimentoQuery.leftJoin(qPreenchimento.graduacaoAv3, qGraduacao);
		preenchimentoQuery.select(Projections.fields(
			Preenchimento.class,
			qPreenchimento.id,
			Projections.fields(AvaliacaoCompDet.class, qAvaliacaoCompDet.id, qAvaliacaoCompDet.ordem, qAvaliacaoCompDet.descricao, qAvaliacaoCompDet.idModeloGraduacao).as("avaliacaoCompDet"),
			Projections.fields(Participante.class, qParticipante.id,
				qParticipante.comentarioFinalAvaliado,
				qParticipante.comentarioFinalAvaliador,
				qParticipante.consensoLiberado,
				qParticipante.revisaoLiberada,
				qParticipante.revisado,
				qParticipante.pdi,
				qParticipante.avaliado,
				qParticipante.avaliador,
				Projections.fields(
					Avaliacao.class,
					qAvaliacao.id,
					qAvaliacao.descricaoAbreviada,
					qAvaliacao.campanha,
					qAvaliacao.situacao,
					qAvaliacao.preencheAv1,
					qAvaliacao.preencheAv2,
					qAvaliacao.preencheAv3,
					qAvaliacao.gestores,
					qAvaliacao.subordinados,
					qAvaliacao.pares,
					qAvaliacao.limiteAv1,
					qAvaliacao.limiteAv2,
					qAvaliacao.limiteAv3
				).as("avaliacao"),
				qParticipante.limiteAv1,
				qParticipante.limiteAv2,
				qParticipante.limiteAv3,
				qParticipante.preenchidaAV1,
				qParticipante.preenchidaAV2,
				qParticipante.preenchidaAV3,
				qParticipante.consensoIniciado,
				qParticipante.revisaoIniciada,
				qParticipante.dataConclusaoAv1,
				qParticipante.dataConclusaoAv2,
				qParticipante.dataConclusaoAv3,
				qParticipante.perfilAgente,
				qParticipante.idCargoAvaliado,
				qParticipante.nomeCargoAvaliado,
				qParticipante.idDeptoAvaliado,
				qParticipante.nomeDeptoAvaliado
			).as("participante"),
			qPreenchimento.graduacaoAv1,
			qPreenchimento.graduacaoAv2,
			qPreenchimento.graduacaoAv3,
			qPreenchimento.concordanciaRevisor,
			qPreenchimento.justificativaRevisor,
			qPreenchimento.observacaoAv1,
			qPreenchimento.observacaoAv2,
			qPreenchimento.observacaoAv3,
			qPreenchimento.graduacaoCompAv1Id,
			qPreenchimento.graduacaoCompAv2Id,
			qPreenchimento.graduacaoCompAv3Id,
			qPreenchimento.dataPreenchimento,
			qPreenchimento.dataGravacao,
			qPreenchimento.tipoClient
		));

		return map(preenchimentoQuery.fetch());
	}

	@Override
	public List<PreenchimentoTO> listByAvaliadorAndLimiteAndCompetenciaProjections(Long avaliadoId, Date data, SituacaoAvaliacao situacao, Long avaliacaoCompetenciaId) {
		JPAQuery<Preenchimento> preenchimentoQuery = select();
		preenchimentoQuery.where(qPreenchimento.participante.avaliador.id.eq(avaliadoId),
			qPreenchimento.participante.limiteAv2.isNotNull(),
			qPreenchimento.participante.limiteAv2.goe(data),
			qPreenchimento.avaliacaoCompDet.avaliacaoCompetencia.id.eq(avaliacaoCompetenciaId),
			qPreenchimento.participante.avaliacao.situacao.notIn(situacao));
		preenchimentoQuery.join(qPreenchimento.participante, qParticipante);
		preenchimentoQuery.join(qPreenchimento.avaliacaoCompDet, qAvaliacaoCompDet);
		preenchimentoQuery.join(qParticipante.avaliador, qFuncionario);
		preenchimentoQuery.join(qParticipante.avaliado, qFuncionario);
		preenchimentoQuery.join(qParticipante.avaliacao, qAvaliacao);
		preenchimentoQuery.leftJoin(qPreenchimento.graduacaoAv1, qGraduacao);
		preenchimentoQuery.leftJoin(qPreenchimento.graduacaoAv2, qGraduacao);
		preenchimentoQuery.leftJoin(qPreenchimento.graduacaoAv3, qGraduacao);
		preenchimentoQuery.select(Projections.fields(
			Preenchimento.class,
			qPreenchimento.id,
			Projections.fields(AvaliacaoCompDet.class, qAvaliacaoCompDet.id, qAvaliacaoCompDet.ordem, qAvaliacaoCompDet.descricao, qAvaliacaoCompDet.idModeloGraduacao).as("avaliacaoCompDet"),
			Projections.fields(Participante.class, qParticipante.id,
				qParticipante.comentarioFinalAvaliado,
				qParticipante.comentarioFinalAvaliador,
				qParticipante.consensoLiberado,
				qParticipante.revisaoLiberada,
				qParticipante.revisado,
				qParticipante.pdi,
				qParticipante.avaliado,
				qParticipante.avaliador,
				Projections.fields(
					Avaliacao.class,
					qAvaliacao.id,
					qAvaliacao.descricaoAbreviada,
					qAvaliacao.campanha,
					qAvaliacao.situacao,
					qAvaliacao.preencheAv1,
					qAvaliacao.preencheAv2,
					qAvaliacao.preencheAv3,
					qAvaliacao.gestores,
					qAvaliacao.subordinados,
					qAvaliacao.pares,
					qAvaliacao.limiteAv1,
					qAvaliacao.limiteAv2,
					qAvaliacao.limiteAv3
				).as("avaliacao"),
				qParticipante.limiteAv1,
				qParticipante.limiteAv2,
				qParticipante.limiteAv3,
				qParticipante.preenchidaAV1,
				qParticipante.preenchidaAV2,
				qParticipante.preenchidaAV3,
				qParticipante.consensoIniciado,
				qParticipante.revisaoIniciada,
				qParticipante.dataConclusaoAv1,
				qParticipante.dataConclusaoAv2,
				qParticipante.dataConclusaoAv3,
				qParticipante.perfilAgente,
				qParticipante.idCargoAvaliado,
				qParticipante.nomeCargoAvaliado,
				qParticipante.idDeptoAvaliado,
				qParticipante.nomeDeptoAvaliado
			).as("participante"),
			qPreenchimento.graduacaoAv1,
			qPreenchimento.graduacaoAv2,
			qPreenchimento.graduacaoAv3,
			qPreenchimento.concordanciaRevisor,
			qPreenchimento.justificativaRevisor,
			qPreenchimento.observacaoAv1,
			qPreenchimento.observacaoAv2,
			qPreenchimento.observacaoAv3,
			qPreenchimento.graduacaoCompAv1Id,
			qPreenchimento.graduacaoCompAv2Id,
			qPreenchimento.graduacaoCompAv3Id,
			qPreenchimento.dataPreenchimento,
			qPreenchimento.dataGravacao,
			qPreenchimento.tipoClient
		));

		return map(preenchimentoQuery.fetch());
	}

	@Override
	public List<PreenchimentoTO> listByParticipanteSemAvalPesoCargoComp(Long idParticipante) {
		QAvalPesoCargoComp qAvalPesoCargoComp = QAvalPesoCargoComp.avalPesoCargoComp;

		JPAQuery<Preenchimento> queryPreenchimento = select(
			Projections.fields(
				Preenchimento.class,
				qPreenchimento.id,
				Projections.fields(
					AvaliacaoCompDet.class,
					qPreenchimento.avaliacaoCompDet.id,
					Projections.fields(
						AvaliacaoCompetencia.class,
						qPreenchimento.avaliacaoCompDet.avaliacaoCompetencia.id,
						qPreenchimento.avaliacaoCompDet.avaliacaoCompetencia.idCompetencia,
						qPreenchimento.avaliacaoCompDet.avaliacaoCompetencia.idTipoCompetencia
					).as("avaliacaoCompetencia")
				).as("avaliacaoCompDet")
			)
		);

		queryPreenchimento
			.where(
				qPreenchimento.participante.id.eq(idParticipante)
				.and(qPreenchimento.id.notIn(JPAExpressions.select(qAvalPesoCargoComp.idPreenchimento).from(qAvalPesoCargoComp)))
			);

		return map(queryPreenchimento.fetch());
	}
}
