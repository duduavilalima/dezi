package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.ParticipanteRepository;
import br.com.fasolti.avd.service.ParticipanteMobileService;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.transfer.mobile.AvaliacaoMobileTO;
import br.com.fasolti.avd.transfer.mobile.ParticipanteMobileTO;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseDeziService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pedro.barata on 24/06/19.
 */

@Service
public class ParticipanteMobileServiceImpl extends BaseDeziService<Participante, ParticipanteMobileTO> implements ParticipanteMobileService {

	private static final QAvaliacao qAvaliacao = QAvaliacao.avaliacao;
	private static final QParticipante qParticipante = QParticipante.participante;
	private static final QTipoCompetencia qTipoCompetencia = QTipoCompetencia.tipoCompetencia;
	private static final QAvaliacaoTipoCompetencia qAvaliacaoTipoCompetencia = QAvaliacaoTipoCompetencia.avaliacaoTipoCompetencia;


	@Autowired
	public ParticipanteMobileServiceImpl(ParticipanteRepository participanteRepository) {
		super(participanteRepository);
	}

	@Override
	protected ServiceFilter<Participante> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless

		// Vide AvaliacaoCompDetResource#listByAvaliacaoCompetencia se for necessário equiparar o Mobile ao Web.
		// Aparentemente, alterações aqui devem ser replicadas em PreenchimentoMobileServiceImpl#getServiceFilter também.

		UserTO user = getLoggedUser();
		Long funcionarioId = user.getId();
		Date now = new Date();

		BooleanExpression isParticipanteAvaliador = qParticipante.avaliador.id.eq(funcionarioId);
		BooleanExpression isParticipanteAvaliado = qParticipante.avaliado.id.eq(funcionarioId);
		BooleanExpression isParticipanteGestor = qParticipante.perfilAgente.eq(TipoAvaliacaoPerfil.GESTOR);
		BooleanExpression isParticipantePreencheConsenso = qParticipante.limiteAv3.isNotNull();
		BooleanExpression isAvaliacaoValida = qAvaliacao.situacao.notIn(SituacaoAvaliacao.CONCLUIDA);

		// Seguindo a mesma regra do AvaliacaoCompDetServiceImpl#listByAvaliacaoCompetenciaAndAvaliadorWithPreenchimentoAndHint
		BooleanExpression isAutoAvaliacaoPermitida =
			isParticipanteAvaliado
				.and(qAvaliacao.preencheAv1.isTrue())
				.and(qParticipante.limiteAv1.isNotNull())
				.and(qParticipante.limiteAv1.goe(now));

		BooleanExpression isAvaliacaoPrincipalPermitida =
			isParticipanteAvaliador
				.and(qAvaliacao.preencheAv2.isTrue())
				.and(qParticipante.limiteAv2.isNotNull())
				.and(qParticipante.limiteAv2.goe(now));

		BooleanExpression isAvaliadoGestor =
			isParticipanteAvaliado
				.and(isParticipantePreencheConsenso)
				.and(isParticipanteGestor)
				.and(isAutoAvaliacaoPermitida);

		BooleanExpression byAvaliacao = isAutoAvaliacaoPermitida.or(isAvaliacaoPrincipalPermitida);
		BooleanExpression byGestor = isAvaliadoGestor;

		BooleanExpression filter = isAvaliacaoValida.and(byAvaliacao.or(byGestor));
		return ServiceFilter.create(qParticipante, filter);
	}

	private static final QBean<Avaliacao> AVALIACAO_PROJECTION = Projections.fields(
		Avaliacao.class,
		qAvaliacao.id,
		qAvaliacao.descricaoAbreviada);

	private JPAQuery<Avaliacao> avaliacaoQuery() {
		JPAQuery<Avaliacao> query = select(AVALIACAO_PROJECTION).distinct()
			.join(qParticipante.avaliacao, qAvaliacao);
		return query;
	}

	/*Pelo fato da avaliação nao "enxergar" o participante, foi feito o processo inverso,
	 * ao invés deste método estar na avaliação, está aqui em participante.*/
	@Override
	public List<AvaliacaoMobileTO> obterAvaliacaoMobileList() {
		JPAQuery<Avaliacao> query = avaliacaoQuery().where(
			qAvaliacao.situacao.eq(SituacaoAvaliacao.LIBERADA)
		);

		return query.fetch()
			.stream()
			.map(avaliacao -> {
				AvaliacaoMobileTO avaliacaoMobileTO = new AvaliacaoMobileTO();
				avaliacaoMobileTO.setId(avaliacao.getId());
				avaliacaoMobileTO.setDescricaoAbreviada(avaliacao.getDescricaoAbreviada());
				return avaliacaoMobileTO;
			}).collect(Collectors.toList());
	}

}
