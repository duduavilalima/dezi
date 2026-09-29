package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.ParticipanteRepository;
import br.com.fasolti.avd.repository.PreenchimentoRepository;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.search.ParticipanteSearchTO;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ParticipanteServiceImpl
	extends BaseAvdService<Participante, ParticipanteTO, ParticipanteSearchTO>
	implements ParticipanteService {

	private final QParticipante qParticipante = QParticipante.participante;
	private final QAvaliacao qAvaliacao = QAvaliacao.avaliacao;
	private final QFuncionario qFuncionario = QFuncionario.funcionario;
	private final QDepartamento qDepartamento = QDepartamento.departamento;

	@Autowired
	private ParticipanteRepository participanteRepository;

	@Autowired
	private PreenchimentoRepository preenchimentoRepository;

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	private GraduacaoCompetenciaService graduacaoCompetenciaService;

	@Autowired
	private ModeloGraduacaoCompService modeloGraduacaoCompService;

	@Autowired
	private ParticipanteEvolucaoService participanteEvolucaoService;

	@Autowired
	public ParticipanteServiceImpl(ParticipanteRepository participanteRepository) {
		super(participanteRepository);
	}

	@Override
	protected ServiceFilter<Participante> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		Long myId = securityService.getUser().getId();

		mandatoryFilter
			.or(qParticipante.avaliador.id.eq(myId))
			.or(qParticipante.avaliado.id.eq(myId));

		return ServiceFilter.create(qParticipante, mandatoryFilter);
	}

	@Override
	public List<ParticipanteTO> listAllAvailable() {
		JPAQuery<Participante> participanteJPAQuery = select();
		participanteJPAQuery.where(qParticipante.avaliacao.situacao.eq(SituacaoAvaliacao.LIBERADA));
		participanteJPAQuery.join(qParticipante.avaliacao, qAvaliacao);
		participanteJPAQuery.join(qParticipante.avaliador, qFuncionario);
		participanteJPAQuery.join(qParticipante.avaliado, qFuncionario);
		participanteJPAQuery.join(qFuncionario.departamento, qDepartamento);
		participanteJPAQuery.select(Projections.fields(
			Participante.class, qParticipante.id,
			qParticipante.comentarioFinalAvaliado,
			qParticipante.comentarioFinalAvaliador,
			qParticipante.consensoLiberado,
			qParticipante.revisaoLiberada,
			qParticipante.revisado,
			qParticipante.pdi,
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
			qParticipante.avaliado,
			qParticipante.avaliador,
			qParticipante.limiteAv1,
			qParticipante.limiteAv2,
			qParticipante.limiteAv3,
			qParticipante.preenchidaAV1,
			qParticipante.preenchidaAV2,
			qParticipante.preenchidaAV3,
			qParticipante.consensoIniciado,
			qParticipante.revisaoIniciada,
			qParticipante.dataInicioAv1,
			qParticipante.dataInicioAv2,
			qParticipante.dataInicioAv3,
			qParticipante.dataConclusaoAv1,
			qParticipante.dataConclusaoAv2,
			qParticipante.dataConclusaoAv3,
			qParticipante.perfilAgente,
			qParticipante.idCargoAvaliado,
			qParticipante.nomeCargoAvaliado,
			qParticipante.idDeptoAvaliado,
			qParticipante.nomeDeptoAvaliado
		));

		return map(participanteJPAQuery.fetch());
	}

	@Override
	protected void doBeforeUpdate(Participante entity, ParticipanteTO to) throws ServiceException {
		// FIXME: No framework, não está passando o valoda FALSE.
		if (to.getPreenchidaAV1() != null) {
			entity.setPreenchidaAV1(to.getPreenchidaAV1());
		}
		if (to.getPreenchidaAV2() != null) {
			entity.setPreenchidaAV1(to.getPreenchidaAV2());
		}
		if (to.getPreenchidaAV3() != null) {
			entity.setPreenchidaAV1(to.getPreenchidaAV3());
		}
		if (to.getPreenchidaAV4() != null) {
			entity.setPreenchidaAV1(to.getPreenchidaAV4());
		}
	}

	@Override
	public List<ParticipanteTO> obterParticipantesPorAvaliacaoAndEmpresa(Long idAvaliacao) {
		return map(participanteRepository.findByAvaliacaoIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(idAvaliacao, getLoggedUser().getEmpresa().getId()));
	}

	@Override
	public List<ParticipanteTO> obterParticipantesPorAvaliadorAndAvaliacao(Long idAvaliador, Long idAvaliacao) {
		return map(participanteRepository.findByAvaliadorIdAndAvaliacaoId(idAvaliador, idAvaliacao));
	}

	@Override
	public List<ParticipanteTO> obterParticipantesPorCampanhaAndEmpresa(Long idCampanha) {
		return map(participanteRepository.findByAvaliacaoCampanhaIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(idCampanha, getLoggedUser().getEmpresa().getId()));
	}

	@Override
	public List<ParticipanteTO> obterParticipantesPorAvaliadorUnidadeAndEmpresa(Long idUnidade) {
		return map(participanteRepository.findByAvaliadorUnidadeIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(idUnidade, getLoggedUser().getEmpresa().getId()));
	}

	@Override
	public List<ParticipanteTO> obterParticipantesPorAvaliadorDepartamentoAndEmpresa(Long idDepartamento) {
		return map(participanteRepository.findByAvaliadorDepartamentoIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(idDepartamento, getLoggedUser().getEmpresa().getId()));
	}

	@Override
	public List<ParticipanteTO> obterParticipantesPorAvaliacaoAndUnidadeAndEmpresa(Long idAvaliacao, Long idUnidade) {
		return map(participanteRepository.findByAvaliacaoIdAndAvaliadorUnidadeIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(idAvaliacao, idUnidade, getLoggedUser().getEmpresa().getId()));
	}

	@Override
	public List<ParticipanteTO> obterParticipantesPorAvaliacaoAndUnidadeAndDepartamentoAndEmpresa(Long idAvaliacao, Long idUnidade, Long idDepartamento) {
		return map(participanteRepository.findByAvaliacaoIdAndAvaliadorUnidadeIdAndAvaliadorDepartamentoIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(idAvaliacao, idUnidade, idDepartamento, getLoggedUser().getEmpresa().getId()));
	}

	@Override
	public List<ParticipanteTO> obterParticipantesPorAvaliacaoAndDepartamentoAndEmpresa(Long idAvaliacao, Long idDepartamento) {
		return map(participanteRepository.findByAvaliacaoIdAndAvaliadorDepartamentoIdAndAvaliacaoCampanhaEmpresaIdOrderByAvaliadorNome(idAvaliacao, idDepartamento, getLoggedUser().getEmpresa().getId()));
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public CompetenciaPreenchimentoTO salvarCompetencia(CompetenciaPreenchimentoTO competencia, Long participanteId) {
		ParticipanteTO participante = load(participanteId);
		List<AvaliacaoCompDetTO> detalhamentos = competencia.getDetalhamentos();

		Boolean temAv1 = Boolean.FALSE;
		Boolean temAv2 = Boolean.FALSE;
		Boolean temAv3 = Boolean.FALSE;
		Boolean finalizou = Boolean.FALSE;
		Date hoje = new Date();

		for (AvaliacaoCompDetTO detalhamento : detalhamentos) {
			finalizou = detalhamento.getFinalizou() != null ? detalhamento.getFinalizou() : false;

			PreenchimentoTO preenchimento = detalhamento.getPreenchimentos().get(0);

			ModeloGraduacaoCompTO modeloGraduacaoComp = modeloGraduacaoCompService.obterModeloGraduacaoCompProjections(preenchimento.getAvaliacaoCompDet().getIdModeloGraduacao(), preenchimento.getParticipante().getAvaliacao().getId());

			if (preenchimento.getGraduacaoAv1Id() != null) {
				temAv1 = Boolean.TRUE;
				GraduacaoCompetenciaTO graduacaoCompAv1 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacaoProjections(modeloGraduacaoComp.getId(), preenchimento.getGraduacaoAv1Id());//getByModeloGraduacaoIdAndIdGraduacao
				preenchimento.setGraduacaoCompAv1Id(graduacaoCompAv1.getId());
			}
			if (preenchimento.getGraduacaoAv2Id() != null) {
				temAv2 = Boolean.TRUE;
				GraduacaoCompetenciaTO graduacaoCompAv2 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacaoProjections(modeloGraduacaoComp.getId(), preenchimento.getGraduacaoAv2Id());//getByModeloGraduacaoIdAndIdGraduacao
				preenchimento.setGraduacaoCompAv2Id(graduacaoCompAv2.getId());
			}
			if (preenchimento.getGraduacaoAv3Id() != null) {
				temAv3 = Boolean.TRUE;
				GraduacaoCompetenciaTO graduacaoCompAv3 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacaoProjections(modeloGraduacaoComp.getId(), preenchimento.getGraduacaoAv3Id());//getByModeloGraduacaoIdAndIdGraduacao
				preenchimento.setGraduacaoCompAv3Id(graduacaoCompAv3.getId());
			}
			preenchimento.setDataPreenchimento(hoje.getTime());
			preenchimento.setDataGravacao(hoje.getTime());

			preenchimentoService.saveOrUpdate(preenchimento);
		}

		if (temAv1 && participante.getAvaliacao().getPreencheAv1() && participante.getDataInicioAv1() == null) {
			participante.setDataInicioAv1(new Date());
		}
		if (temAv2 && participante.getAvaliacao().getPreencheAv2() && participante.getDataInicioAv2() == null) {
			participante.setDataInicioAv2(new Date());
		}
		if (temAv3 && participante.getAvaliacao().getPreencheAv3() && participante.getDataInicioAv3() == null) {
			participante.setDataInicioAv3(new Date());
		}

		// Verifica se está tudo preenchido
		ParticipanteEvolucaoTO participanteEvolucao = participanteEvolucaoService.calcularEvolucao(participante);

		long total = participanteEvolucao.getTotal();
		long preenchimentoAv1 = participanteEvolucao.getPreenchimentoAv1();
		long preenchimentoAv2 = participanteEvolucao.getPreenchimentoAv2();
		long preenchimentoAv3 = participanteEvolucao.getPreenchimentoAv3();

		participante.setPreenchidaAV1(total == preenchimentoAv1);
		participante.setPreenchidaAV2(total == preenchimentoAv2);
		participante.setPreenchidaAV3(total == preenchimentoAv3);

		//ESTE CÓDIGO É NECESSÁRIO SEM ELE O GRÁFICO DO PAINEL DE CONTROLE NÃO FUNCIONA
		if (participante.getPreenchidaAV1())
			participante.setDataConclusaoAv1(new Date());

		if (participante.getPreenchidaAV2())
			participante.setDataConclusaoAv2(new Date());

		if (participante.getPreenchidaAV3())
			participante.setDataConclusaoAv3(new Date());

		//código usado pelo gráfico do painel de controle - fim

		if (participante.getConsensoLiberado() != null && participante.getConsensoLiberado() != true && finalizou == true) {
			participante.setConsensoLiberado(participante.getPreenchidaAV1() && participante.getPreenchidaAV2());
		}

		updateCamposPDI(participante, competencia);

		saveOrUpdate(participante);
		return competencia;
	}

	private void updateCamposPDI(ParticipanteTO participante, CompetenciaPreenchimentoTO competencia) {
		if (participante.getPdi() == null) {
			participante.setPdi(competencia.getAcaoPDI());
		} else if (!participante.getPdi().equals(competencia.getAcaoPDI())) {
			participante.setPdi(competencia.getAcaoPDI());
		}

		if (participante.getComentarioFinalAvaliado() == null) {
			participante.setComentarioFinalAvaliado(competencia.getComentarioFinalAvaliado());
		} else if (!participante.getComentarioFinalAvaliado().equals(competencia.getComentarioFinalAvaliado())) {
			participante.setComentarioFinalAvaliado(competencia.getComentarioFinalAvaliado());
		}

		if (participante.getComentarioFinalAvaliador() == null) {
			participante.setComentarioFinalAvaliador(competencia.getComentarioFinalAvaliador());
		} else if (!participante.getComentarioFinalAvaliador().equals(competencia.getComentarioFinalAvaliador())) {
			participante.setComentarioFinalAvaliador(competencia.getComentarioFinalAvaliador());
		}
	}
}
