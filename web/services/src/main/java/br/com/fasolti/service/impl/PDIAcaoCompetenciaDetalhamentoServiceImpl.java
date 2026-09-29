package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.PDIAcaoCompetenciaDetalhamentoRepository;
import br.com.fasolti.avd.service.CompetenciaDetalhamentoService;
import br.com.fasolti.avd.service.PDIAcaoCompetenciaDetalhamentoService;
import br.com.fasolti.avd.service.PDIAcaoService;
import br.com.fasolti.avd.service.ParticipanteService;
import br.com.fasolti.avd.transfer.PDIAcaoCompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.PDIAcaoTO;
import br.com.fasolti.avd.transfer.search.PDIAcaoCompetenciaDetalhamentoSearchTO;
import br.com.fasolti.service.util.BaseAvdService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PDIAcaoCompetenciaDetalhamentoServiceImpl extends BaseAvdService<PDIAcaoCompetenciaDetalhamento, PDIAcaoCompetenciaDetalhamentoTO, PDIAcaoCompetenciaDetalhamentoSearchTO> implements PDIAcaoCompetenciaDetalhamentoService {

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private PDIAcaoCompetenciaDetalhamentoRepository pdiAcaoCompDetRepository;

	@Autowired
	private ParticipanteService participanteService;

	@Autowired
	private CompetenciaDetalhamentoService competenciaDetalhamentoService;

	@Autowired
	private PDIAcaoService pdiAcaoService;

	private final QPDIAcaoCompetenciaDetalhamento qpdiAcaoCompDet = QPDIAcaoCompetenciaDetalhamento.pDIAcaoCompetenciaDetalhamento;
	private final QPDIAcao qpdiAcao = QPDIAcao.pDIAcao;
	private final QParticipante qParticipante = QParticipante.participante;
	private final QFuncionario qFuncionario = QFuncionario.funcionario;
	private final QCompetencia qCompetencia = QCompetencia.competencia;
	private final QCompetenciaDetalhamento qCompetenciaDetalhamento = QCompetenciaDetalhamento.competenciaDetalhamento;

	@Autowired
	public PDIAcaoCompetenciaDetalhamentoServiceImpl(PDIAcaoCompetenciaDetalhamentoRepository pdiAcaoCompDetRepository) {
		super(pdiAcaoCompDetRepository);
	}

	private String mapToJson(Map<String, String> templateValues) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(templateValues);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private List<PDIAcaoTO> listaAcaoPdi(Long IdFuncionario) {
		return pdiAcaoService.listarAcaoPDI(IdFuncionario);
	}

	private final QBean<PDIAcaoCompetenciaDetalhamento> PDI_ACAO_COMPETENCIA_RELATORIO = Projections.fields(
		PDIAcaoCompetenciaDetalhamento.class,
		qpdiAcaoCompDet.id,
		Projections.fields(
			PDIAcao.class,
			qpdiAcao.id,
			qpdiAcao.acaoParaDesenvolvimento,
			qpdiAcao.dataInicio,
			qpdiAcao.dataFim,
			qpdiAcao.situacao,
			Projections.fields(
				Participante.class,
				qParticipante.id,
				Projections.fields(
					Funcionario.class,
					qFuncionario.id,
					qFuncionario.nome,
					qFuncionario.matricula,
					qFuncionario.cargo,
					qFuncionario.departamento
				).as("avaliado")
			).as("participante")
		).as("pdiAcao"),
		Projections.fields(
			Competencia.class,
			qCompetencia.id,
			qCompetencia.descricaoAbreviada
		).as("competencia")
	);

	public List<PDIAcaoCompetenciaDetalhamentoTO> listarAcaoPDICompetenciaaDetalhamento(Long IdPdiAcao) {
		JPAQuery<PDIAcaoCompetenciaDetalhamento> query = select(
			Projections.fields(
				PDIAcaoCompetenciaDetalhamento.class,
				qpdiAcaoCompDet.id,
				Projections.fields(
					PDIAcao.class,
					qpdiAcaoCompDet.pdiAcao.id
				).as(qpdiAcaoCompDet.pdiAcao),
				Projections.fields(
					Competencia.class,
					qpdiAcaoCompDet.competencia.id,
					qpdiAcaoCompDet.competencia.descricaoAbreviada
				).as(qpdiAcaoCompDet.competencia),
				Projections.fields(
					CompetenciaDetalhamento.class,
					qpdiAcaoCompDet.competenciaDetalhamento.id,
					qpdiAcaoCompDet.competenciaDetalhamento.descricao
					).as(qpdiAcaoCompDet.competenciaDetalhamento)
			)
		)
			.leftJoin(qpdiAcaoCompDet.pdiAcao, qpdiAcao)
			.leftJoin(qpdiAcaoCompDet.competencia, qCompetencia)
			.leftJoin(qpdiAcaoCompDet.competenciaDetalhamento, qCompetenciaDetalhamento)
			.where(qpdiAcao.id.eq(IdPdiAcao));

		//FIXME: Trazer a lista de detalhamentos da competencia direto
		//FIXME: Está trazendo a competenciaDetalhamento com um objeto vazio {}
		List<PDIAcaoCompetenciaDetalhamentoTO> pdiAcaoCompetenciaDetalhamentoTOList = map(query.fetch());
		pdiAcaoCompetenciaDetalhamentoTOList.forEach(competenciaDetalhamentoTO -> {
			if(competenciaDetalhamentoTO.getCompetenciaDetalhamento().getId() == null) {
				competenciaDetalhamentoTO.setCompetenciaDetalhamento(null);
			}

			competenciaDetalhamentoTO.getCompetencia()
				.setDetalhamentos(competenciaDetalhamentoService.listByCompetencia(competenciaDetalhamentoTO.getCompetencia().getId()));
		});
		return pdiAcaoCompetenciaDetalhamentoTOList;
	}

	@Override
	public List<PDIAcaoCompetenciaDetalhamentoTO> listByPDIAcao(Long pdiAcaoId) {
		JPAQuery<PDIAcaoCompetenciaDetalhamento> query = select(
			Projections.fields(
				PDIAcaoCompetenciaDetalhamento.class,
				qpdiAcaoCompDet.id,
				Projections.fields(
					Competencia.class,
					qpdiAcaoCompDet.competencia.id,
					qpdiAcaoCompDet.competencia.descricaoAbreviada
				).as(qpdiAcaoCompDet.competencia)
			)
		)
			.leftJoin(qpdiAcaoCompDet.pdiAcao, qpdiAcao)
			.leftJoin(qpdiAcaoCompDet.competencia, qCompetencia)
			.where(qpdiAcaoCompDet.pdiAcao.id.eq(pdiAcaoId));
		return map(query.fetch());
	}

	@Override
	public List<PDIAcaoCompetenciaDetalhamentoTO> listarPorPDIacaoAvaliado(Long IdFuncionario) {
		List<PDIAcaoTO> pdiList = listaAcaoPdi(IdFuncionario);
		List<PDIAcaoCompetenciaDetalhamentoTO> pdiCompDetList = new ArrayList<>();
		List<PDIAcaoCompetenciaDetalhamentoTO> listTemp = new ArrayList<>();
		for (PDIAcaoTO pdiAcao : pdiList) {
			listTemp = listarAcaoPDICompetenciaaDetalhamento(pdiAcao.getId());
			if (listTemp.isEmpty()) {
				listTemp.add(new PDIAcaoCompetenciaDetalhamentoTO.Builder(pdiAcao).build());
			} else {
				// FIXME PDIAçao esta vindo null, corrigir depois refatora
				List<PDIAcaoCompetenciaDetalhamentoTO> list = new ArrayList<>();
				for (PDIAcaoCompetenciaDetalhamentoTO pacd : listTemp) {
					pacd.setPdiAcao(pdiAcao);
					list.add(pacd);
				}
				listTemp = list;
			}
			pdiCompDetList.addAll(listTemp);
		}
		return pdiCompDetList;
	}


	@Override
	protected void buildSearchQuery(JPAQuery<PDIAcaoCompetenciaDetalhamento> query) {
		query.select(PDI_ACAO_COMPETENCIA_RELATORIO)
			.leftJoin(qpdiAcaoCompDet.pdiAcao, qpdiAcao)
			.leftJoin(qpdiAcaoCompDet.competencia, qCompetencia)
			.leftJoin(qpdiAcaoCompDet.competenciaDetalhamento, qCompetenciaDetalhamento)
			.innerJoin(qpdiAcao.participante, qParticipante)
			.innerJoin(qParticipante.avaliado, qFuncionario);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, PDIAcaoCompetenciaDetalhamentoSearchTO exampleObj) {
		/*Avaliado*/
		if (exampleObj.getPdiAcao().getParticipante().getAvaliado().getId() != null) {
			builder.and(qParticipante.avaliado.id.eq(exampleObj.getPdiAcao().getParticipante().getAvaliado().getId()));
		}
		/*Setor (depto)*/
		if (exampleObj.getPdiAcao().getParticipante().getAvaliado().getDepartamento().getId() != null) {
			builder.and(qParticipante.avaliado.departamento.id.eq(exampleObj.getPdiAcao().getParticipante().getAvaliado().getDepartamento().getId()));
		}
		/*Cargo*/
		if (exampleObj.getPdiAcao().getParticipante().getAvaliado().getCargo().getId() != null) {
			builder.and(qParticipante.avaliado.cargo.id.eq(exampleObj.getPdiAcao().getParticipante().getAvaliado().getCargo().getId()));
		}

		/*Competencia*/
		if (exampleObj.getCompetenciaDetalhamento().getCompetencia().getId() != null) {
			builder.and(qCompetencia.id.eq(exampleObj.getCompetenciaDetalhamento().getCompetencia().getId()));
		}
		/*Situacao*/
		if (exampleObj.getPdiAcao().getSituacao() != null && exampleObj.getPdiAcao().getSituacao().getCode() != null) {
			builder.and(qpdiAcao.situacao.eq(exampleObj.getPdiAcao().getSituacao()));
		}
		/*Data inicial*/
		if (exampleObj.getPdiAcao().getDataInicio() != null) {
			builder.and(qpdiAcao.dataInicio.goe(exampleObj.getPdiAcao().getDataInicio()));
		}
		/*Data final*/
		if (exampleObj.getPdiAcao().getDataFim() != null) {
			builder.and(qpdiAcao.dataFim.loe(exampleObj.getPdiAcao().getDataFim()));
		}
	}

/*
	@Override
	protected void doAfterSearch(List<Campanha> entities, List<CampanhaTO> result) throws ServiceException {
		calcularTotalAvaliacoes(result);
	}
*/


}
