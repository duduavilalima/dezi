package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.AvaliacaoCompetenciaRepository;
import br.com.fasolti.avd.repository.ParticipanteRepository;
import br.com.fasolti.avd.service.AvaliacaoCompetenciaMobileService;
import br.com.fasolti.avd.service.ParticipanteMobileService;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.transfer.mobile.AvaliacaoCompetenciaMobileTO;
import br.com.fasolti.avd.transfer.mobile.AvaliacaoMobileTO;
import br.com.fasolti.avd.transfer.mobile.ParticipanteMobileTO;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseDeziService;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by pedro.barata on 24/06/19.
 */

@Service
public class AvaliacaoCompetenciaMobileServiceImpl extends BaseDeziService<AvaliacaoCompetencia, AvaliacaoCompetenciaMobileTO> implements AvaliacaoCompetenciaMobileService {

	private static final QAvaliacao qAvaliacao = QAvaliacao.avaliacao;
	private static final QAvaliacaoCompetencia qAvaliacaoCompetencia = QAvaliacaoCompetencia.avaliacaoCompetencia;
	private static final QParticipante qParticipante = QParticipante.participante;
	private static final QTipoCompetencia qTipoCompetencia = QTipoCompetencia.tipoCompetencia;
	@Autowired
	private Mapper mapper;

	@Autowired
	public AvaliacaoCompetenciaMobileServiceImpl(AvaliacaoCompetenciaRepository avaliacaoCompetenciaRepository) {
		super(avaliacaoCompetenciaRepository);
	}

	@Override
	protected ServiceFilter<AvaliacaoCompetencia> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless

		UserTO funcionario = getLoggedUser();

		switch (funcionario.getTipoPerfil()) {
			case ADMINISTRADOR:
			case GESTOR_RH:
				return ServiceFilter.create(qAvaliacaoCompetencia, qAvaliacaoCompetencia.avaliacao.campanha.empresa.id.eq(funcionario.getEmpresa().getId()));
//			case CONSULTOR_RH: //TODO Ver todas as empresas da qual ele é gestor
			case GESTOR_AREA:
			default:
				/*TODO: Como fazer o filtro de funcionario aqui?*/
				return ServiceFilter.create(qAvaliacaoCompetencia, null);
				/*return ServiceFilter.create(qParticipante,
					qParticipante.avaliador.id.eq(funcionario.getId())
						.or(qParticipante.avaliado.id.eq(funcionario.getId())));*/

		}
	}

	private static final QBean<AvaliacaoCompetencia> AVALIACAO_COMP_PROJECTION = Projections.fields(
		AvaliacaoCompetencia.class,
		qAvaliacaoCompetencia.id,
		qAvaliacaoCompetencia.descricaoAbreviada,
		qAvaliacaoCompetencia.direcionamentoGestor
	);

	private JPAQuery<AvaliacaoCompetencia> avaliacaoCompetenciaQuery() {
		JPAQuery<AvaliacaoCompetencia> query = select(AVALIACAO_COMP_PROJECTION);
		return query;
	}


	/*Pelo fato da avaliação nao "enxergar" o participante, foi feito o processo inverso,
	 * ao invés deste método estar na avaliação, está aqui em participante.*/
	@Override
	public List<AvaliacaoCompetenciaMobileTO> listByTipoCompetenciaId(Long tipoCompetenciaId) {
		JPAQuery<AvaliacaoCompetencia> query = avaliacaoCompetenciaQuery()
			.where(
				qTipoCompetencia.id.eq(tipoCompetenciaId)
			);
		List<AvaliacaoCompetenciaMobileTO> participanteList = map(query.fetch());
		return participanteList;
	}

}
