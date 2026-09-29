package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.*;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.search.ParticipanteSearchTO;
import br.com.fasolti.avd.transfer.simple.AvaliacaoSimpleTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.avd.types.SituacaoPreenchimento;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.avd.types.TipoClient;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AvaliacaoParticipanteServiceImpl
	extends BaseAvdService<Participante, ParticipanteTO, ParticipanteSearchTO>
	implements AvaliacaoParticipanteService {

	private static final QParticipante qParticipante = QParticipante.participante;
	private static final QAvaliacao qAvaliacao = QAvaliacao.avaliacao;
	private static final QCampanha qCampanha = QCampanha.campanha;
	private static final QEmpresa qEmpresa = QEmpresa.empresa;

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private AvaliacaoCompetenciaService avaliacaoCompetenciaService;

	@Autowired
	private CargoCompetenciaService cargoCompetenciaService;

	@Autowired
	private CompetenciaDetalhamentoService competenciaDetalhamentoService;

	@Autowired
	private CompetenciaService competenciaService;

	@Autowired
	private Mapper mapper;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private AvaliacaoTipoCompetenciaRepository avaliacaoTipoCompetenciaRepository;

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@Autowired
	private CompetenciaDetalhamentoRepository competenciaDetalhamentoRepository;

	@Autowired
	private AvaliacaoCompDetRepository avaliacaoCompDetRepository;

	@Autowired
	private AvaliacaoCompDetHintRepository avaliacaoCompDetHintRepository;

	@Autowired
	private CompDetalhamentoHintRepository compDetalhamentoHintRepository;

	@Autowired
	private DetalhamentoCargoCompetenciaRepository detalhamentoCargoCompetenciaRepository;

	@Autowired
	private AvaliacaoDetCargoCompRepository avaliacaoDetCargoCompRepository;

	@Autowired
	private CompetenciaRepository competenciaRepository;

	@Autowired
	private GraduacaoCompetenciaRepository graduacaoCompetenciaRepository;

	@Autowired
	private ModeloGraduacaoRepository modeloGraduacaoRepository;

	@Autowired
	private ModeloGraduacaoAvalCompRepository modeloGraduacaoAvalCompRepository;

	@Autowired
	private ModeloGraduacaoCompRepository modeloGraduacaoCompRepository;

	@Autowired
	private GraduacaoRepository graduacaoRepository;

	@Autowired
	private AvaliacaoParticipanteRepository avaliacaoParticipanteRepository;

	@Autowired
	private AvaliacaoCompetenciaRepository avaliacaoCompetenciaRepository;

	@Autowired
	private PreenchimentoRepository preenchimentoRepository;

	@Autowired
	private AvaliacaoTipoCompetenciaService avaliacaoTipoCompetenciaService;

	@Autowired
	private AvaliacaoCompDetService avaliacaoCompDetService;

	@Autowired
	private DetalhamentoCargoCompetenciaService detalhamentoCargoCompetenciaService;

	@Autowired
	private AvaliacaoDetCargoCompService avaliacaoDetCargoCompService;

	@Autowired
	private CompDetalhamentoHintService compDetalhamentoHintService;

	@Autowired
	private AvaliacaoCompDetHintService avaliacaoCompDetHintService;

	@Autowired
	private ModeloGraduacaoAvalCompService modeloGraduacaoAvalCompService;

	@Autowired
	private ModeloGraduacaoCompService modeloGraduacaoCompService;

	@Autowired
	private ModeloGraduacaoService modeloGraduacaoService;

	@Autowired
	private GraduacaoService graduacaoService;

	@Autowired
	private GraduacaoCompetenciaService graduacaoCompetenciaService;

	@Autowired
	private AvalDimComDetService avalDimComDetService;

	@Autowired
	private DimensaoCompetenciaDetalhamentoService dimensaoCompetenciaDetalhamentoService;

	@Autowired
	private AvalDimComDetRepository avalDimComDetRepository;

	@Autowired
	private CargoTipoCompetenciaRepository cargoTipoCompetenciaRepository;

	@Autowired
	private AvalPesoCargoCompRepository avalPesoCargoCompRepository;

	@Autowired(required = false)
	private Logger logger;


	@Autowired
	public AvaliacaoParticipanteServiceImpl(AvaliacaoParticipanteRepository avaliacaoParticipanteRepository) {
		super(avaliacaoParticipanteRepository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, ParticipanteSearchTO object) {
		if (object.getAvaliacao() != null && object.getAvaliacao().getId() != null) {
			builder.and(qParticipante.avaliacao.id.eq(object.getAvaliacao().getId()));
		}


		/*Está sendo usado pelos 4 checkboxs que estão em cima da lista de participantes
		da aba de análise. O usuário pode filtrar os participantes selecionando os checkboxs.
		Autoavaliação -> preenchidaAV1, Avaliação -> preenchidaAV2,
		Consenso -> preenchidaAV3 e Revisão -> preenchidaAV4*/

		if (object.getPreenchidaAV1() != null && object.getPreenchidaAV1() == true) {
			builder.and(qParticipante.preenchidaAV1.eq(object.getPreenchidaAV1()));
		}

		if (object.getPreenchidaAV2() != null && object.getPreenchidaAV2() == true) {
			builder.and(qParticipante.preenchidaAV2.eq(object.getPreenchidaAV2()));
		}

		if (object.getPreenchidaAV3() != null && object.getPreenchidaAV3() == true) {
			builder.and(qParticipante.preenchidaAV3.eq(object.getPreenchidaAV3()));
		}

		if (object.getPreenchidaAV4() != null && object.getPreenchidaAV4() == true) {
			builder.and(qParticipante.preenchidaAV4.eq(object.getPreenchidaAV4()));
		}
	}

	@Override
	protected void doBeforeSave(ParticipanteTO object) throws ServiceException {
		AvaliacaoSimpleTO avaliacao = mapSimple(object.getAvaliacao());

		FuncionarioTO avaliado = mapper.map(funcionarioRepository.findOne(object.getAvaliado().getId()), FuncionarioTO.class);
		object.setAvaliado(mapSimple(avaliado));

		if (object.getAvaliador() != null) {
			FuncionarioTO avaliador = mapper.map(funcionarioRepository.findOne(object.getAvaliador().getId()), FuncionarioTO.class);
			object.setAvaliador(mapSimple(avaliador));
		}

		if (SituacaoAvaliacao.LIBERADA.equals(object.getAvaliacao().getStatus())) {
			object.setRevisado(true);
		}

		prepare(avaliacao, object);
		validate(avaliacao, object);
	}

	@Override
	protected void doAfterSave(Participante entity, ParticipanteTO object) throws ServiceException {

	}

	@Override
	protected void doBeforeUpdate(ParticipanteTO object) throws ServiceException {
		AvaliacaoSimpleTO avaliacao = mapSimple(object.getAvaliacao());
		prepare(avaliacao, object);

		FuncionarioTO avaliado = funcionarioService.obterFuncionarioProjections(object.getAvaliado().getId());
		object.setAvaliado(mapSimple(avaliado));

		if (object.getAvaliador() != null) {
			FuncionarioTO avaliador = funcionarioService.obterFuncionarioProjections(object.getAvaliador().getId());
			object.setAvaliador(mapSimple(avaliador));
		}

		validate(avaliacao, object);
	}

	@Override
	protected void doBeforeUpdate(Participante entity, ParticipanteTO object) throws ServiceException {

		entity.setAvaliado(mapper.map(funcionarioService.obterFuncionarioProjections(object.getAvaliado().getId()), Funcionario.class));

		if (object.getAvaliador() != null && object.getAvaliador().getId() != null)
			entity.setAvaliador(mapper.map(funcionarioService.obterFuncionarioProjections(object.getAvaliador().getId()), Funcionario.class));

		if (object.getRevisor() != null && object.getRevisor().getId() != null)
			entity.setRevisor(mapper.map(funcionarioService.obterFuncionarioProjections(object.getRevisor().getId()), Funcionario.class));
	}

	@Override
	protected void doBeforeDelete(ParticipanteTO participante) throws br.com.fasolti.commons.lang.ServiceException {
		if (SituacaoAvaliacao.LIBERADA.equals(participante.getAvaliacao().getStatus())) {
			removerTodasPerguntasDoParticipante(participante);
		}
	}

	@Override
	public Long obterTotalAvaliacoesPorAvaliacao(AvaliacaoTO avaliacao) {
		return avaliacaoParticipanteRepository.count(qParticipante.avaliacao.id.eq(avaliacao.getId()));
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<ParticipanteTO> obterListaParticipantesPorAvaliacao(AvaliacaoTO avaliacao) {
		//return map(avaliacaoParticipanteRepository.findByAvaliacaoId(avaliacao.getId()));
		JPAQuery<Participante> participanteJPAQuery = select();
		participanteJPAQuery.where(qParticipante.avaliacao.id.eq(avaliacao.getId()));
		participanteJPAQuery.join(qParticipante.avaliacao, qAvaliacao);
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

		List<Participante> lista = participanteJPAQuery.fetch();
		return map(lista);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ParticipanteTO liberaAvaliacaoConsenso(ParticipanteTO participante) {
		participante.setPreenchidaAV3(null);
		participante.setPreenchidaAV4(null);
		participante.setConsensoLiberado(true);
		participante.setComentarioFinalAvaliado(null);
		participante.setComentarioFinalAvaliador(null);
		participante.setConsensoIniciado(false);
		participante.setRevisaoLiberada(false);
		participante.setComentarioFinalRevisor(null);
		participante.setComentarioFinalAvaliador(null);
		participante.setRevisaoIniciada(false);

		List<PreenchimentoTO> listPreenchimento = preenchimentoService.listByParticipante(participante);

		for (PreenchimentoTO preenchimento : listPreenchimento) {

			ModeloGraduacaoCompTO modeloGraduacaoComp = modeloGraduacaoCompService.obterModeloGraduacaoCompProjections(preenchimento.getAvaliacaoCompDet().getIdModeloGraduacao(), preenchimento.getParticipante().getAvaliacao().getId());

			if (preenchimento.getGraduacaoAv1Id() != null) {
				GraduacaoCompetenciaTO graduacaoCompAv1 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacao(modeloGraduacaoComp.getId(), preenchimento.getGraduacaoAv1Id());
				preenchimento.setGraduacaoCompAv1Id(graduacaoCompAv1.getId());
			}

			if (preenchimento.getGraduacaoAv2Id() != null) {
				GraduacaoCompetenciaTO graduacaoCompAv2 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacao(modeloGraduacaoComp.getId(), preenchimento.getGraduacaoAv2Id());
				preenchimento.setGraduacaoCompAv2Id(graduacaoCompAv2.getId());
			}

			if (preenchimento.getGraduacaoAv3Id() != null) {
				GraduacaoCompetenciaTO graduacaoCompAv3 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacao(modeloGraduacaoComp.getId(), preenchimento.getGraduacaoAv3Id());
				preenchimento.setGraduacaoCompAv3Id(graduacaoCompAv3.getId());
			}

			preenchimento.setConcordanciaRevisor(false);
			preenchimento.setJustificativaRevisor(null);
		}

		preenchimentoService.update(listPreenchimento);

		if (SituacaoPreenchimento.TOTALMENTE_PREENCH == participante.getAvaliacao().getSituacaoPreenchimento()) {
			AvaliacaoTO avaliacao = avaliacaoService.getById(participante.getAvaliacao().getId());
			avaliacao.setSituacaoPreenchimento(SituacaoPreenchimento.PARCIALMENTE_PREENCH);
			avaliacaoService.update(avaliacao, avaliacao.getId());
			participante.setAvaliacao(avaliacao);
		}

		return update(participante, participante.getId());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ParticipanteTO atualizaAvaliadorAndRevisor(ParticipanteTO participante) {
		/* FIXME Este método era usado quando existia a aba de analise na tela de avaliação. Verificar a permanência deste método, talvez possa ser usado aba de avaliadores da tela de avaliação. */

		participante.setConsensoLiberado(false);
		participante.setComentarioFinalAvaliado(null);
		participante.setComentarioFinalAvaliador(null);
		participante.setConsensoIniciado(false);
		participante.setRevisaoIniciada(false);
		participante.setComentarioFinalRevisor(null);
		participante.setRevisaoIniciada(false);
		participante.setPreenchidaAV2(false);
		participante.setPreenchidaAV3(false);
		participante.setPreenchidaAV4(false);
		participante.setDataInicioAv2(null);
		participante.setDataInicioAv3(null);
		participante.setDataInicioAv4(null);
		participante.setDataConclusaoAv2(null);
		participante.setDataConclusaoAv3(null);
		participante.setDataConclusaoAv4(null);

		List<AvaliacaoCompetenciaTO> listAvaliacaoCompetencia = avaliacaoCompetenciaService.listByParticipante(participante);
		for (AvaliacaoCompetenciaTO avaliacaoCompetencia : listAvaliacaoCompetencia) {
			avaliacaoCompetencia.setDataAtualizacao(new Date());
			avaliacaoCompetencia.setAtualizador(getLoggedUser().getMatricula());
			avaliacaoCompetenciaService.saveOrUpdate(avaliacaoCompetencia);
		}


		List<PreenchimentoTO> listPreenchimento = preenchimentoService.listByParticipante(participante);
		for (PreenchimentoTO preenchimento : listPreenchimento) {
			preenchimento.setGraduacaoAv2Id(null);
			preenchimento.setObservacaoAv2(null);
			preenchimento.setGraduacaoAv3Id(null);
			preenchimento.setObservacaoAv3(null);
			preenchimento.setConcordanciaRevisor(false);
			preenchimento.setJustificativaRevisor(null);
			ModeloGraduacaoCompTO modeloGraduacaoComp = modeloGraduacaoCompService.obterModeloGraduacaoCompProjections(preenchimento.getAvaliacaoCompDet().getIdModeloGraduacao(), preenchimento.getParticipante().getAvaliacao().getId());

			if (preenchimento.getGraduacaoAv1Id() != null) {
				GraduacaoCompetenciaTO graduacaoCompAv1 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacao(modeloGraduacaoComp.getId(), preenchimento.getGraduacaoAv1Id());
				preenchimento.setGraduacaoCompAv1Id(graduacaoCompAv1.getId());
			}

			if (preenchimento.getGraduacaoAv2Id() != null) {
				GraduacaoCompetenciaTO graduacaoCompAv2 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacao(modeloGraduacaoComp.getId(), preenchimento.getGraduacaoAv2Id());
				preenchimento.setGraduacaoCompAv2Id(graduacaoCompAv2.getId());
			}

			if (preenchimento.getGraduacaoAv3Id() != null) {
				GraduacaoCompetenciaTO graduacaoCompAv3 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacao(modeloGraduacaoComp.getId(), preenchimento.getGraduacaoAv3Id());
				preenchimento.setGraduacaoCompAv3Id(graduacaoCompAv3.getId());
			}

			Preenchimento preenchimentoEntity = mapDefault(preenchimento);

			//FIXME: ERRO GRAVE, SÓ FUNCIONOU CHAMANDO O PRÓPRIO REPOSITÓRIO DA ENTIDADE. Quando era o serviço de preenchimento, não estava funcionando o update.
			preenchimentoRepository.save(preenchimentoEntity);
		}

		if (SituacaoPreenchimento.NAO_PREENCH != participante.getAvaliacao().getSituacaoPreenchimento()) {
			AvaliacaoTO avaliacao = avaliacaoService.getById(participante.getAvaliacao().getId());

			List<ParticipanteTO> listParticipantes = obterListaParticipantesPorAvaliacao(avaliacao);

			if (listParticipantes.size() == 1) {
				if (participante.getLimiteAv1() == null)
					participante.getAvaliacao().setSituacaoPreenchimento(SituacaoPreenchimento.NAO_PREENCH);
				else if (participante.getLimiteAv1() != null && participante.getPreenchidaAV1() != null && participante.getPreenchidaAV1())
					participante.getAvaliacao().setSituacaoPreenchimento(SituacaoPreenchimento.PARCIALMENTE_PREENCH);
			} else if (listParticipantes.size() > 1) {

				boolean nenhumaAV2Preenchida = true;

				for (ParticipanteTO participanteTO : listParticipantes) {
					if (participanteTO.getPreenchidaAV2() != null && participanteTO.getPreenchidaAV2()) {
						nenhumaAV2Preenchida = false;
						break;
					}
				}

				if (nenhumaAV2Preenchida && participante.getLimiteAv1() == null && participante.getLimiteAv4() == null)
					participante.getAvaliacao().setSituacaoPreenchimento(SituacaoPreenchimento.NAO_PREENCH);
				else
					participante.getAvaliacao().setSituacaoPreenchimento(SituacaoPreenchimento.PARCIALMENTE_PREENCH);
			}

			participante.setAvaliacao(avaliacao);
		}

		return update(participante, participante.getId());
	}

	@Override
	public List<AvaliacaoTO> listarAvaliacoesPorAvaliado(Long avaliadoId) {
		QFuncionario qFuncionario = QFuncionario.funcionario;
		QEmpresa qEmpresa = QEmpresa.empresa;
		QAvaliacao qAvaliacao = QAvaliacao.avaliacao;
		QCampanha qCampanha = QCampanha.campanha;
		List<Participante> participantes = select(
			Projections.fields(Participante.class,
				qParticipante.id,
				Projections.fields(Avaliacao.class,
					qAvaliacao.id,
					qAvaliacao.descricaoAbreviada,
					qAvaliacao.limiteAv1,
					qAvaliacao.limiteAv2,
					qAvaliacao.limiteAv3,
					qAvaliacao.situacao,
					Projections.fields(Campanha.class, qCampanha.id, qCampanha.descricao).as("campanha")
				).as("avaliacao")
			))
			.distinct()
			.join(qParticipante.avaliado, qFuncionario)
			.join(qParticipante.avaliacao, qAvaliacao)
			.join(qAvaliacao.campanha, qCampanha)
			.join(qFuncionario.empresa, qEmpresa)
			.where(
				qFuncionario.id.eq(avaliadoId),
				//FIXME Verificar todos os usos do getLoggedFuncionario() - Esses filtros por usuário devem ser implementados no ServiceFilter e não em cada query
				// Remover onde está redundante e implementar corretamente no ServiceFilter quando necessário
				qEmpresa.id.eq(getLoggedUser().getEmpresa().getId()))
			.fetch();

		Set<Avaliacao> avaliacoes = new TreeSet<Avaliacao>();
		for (Participante participante : participantes) {
			avaliacoes.add(participante.getAvaliacao());
		}
		return mapDefault(avaliacoes);
	}

	//TODO: Ver se é necessário todos estes campos
	@Override
	public List<ParticipanteTO> listarAvaliacoesComPDIPorAvaliado(Long avaliadoId) {
		UserTO loggedUser = getLoggedUser();
		QFuncionario qFuncionario = QFuncionario.funcionario;
		QEmpresa qEmpresa = QEmpresa.empresa;
		QParticipante qParticipante = QParticipante.participante;
		QAvaliacao qAvaliacao = QAvaliacao.avaliacao;
		QCampanha qCampanha = QCampanha.campanha;
		List<Participante> participantes = select(
			Projections.fields(Participante.class,
				qParticipante.id,
				qParticipante.pdi,
				Projections.fields(Avaliacao.class,
					qAvaliacao.id,
					qAvaliacao.descricaoAbreviada,
					Projections.fields(Campanha.class,
						qCampanha.id,
						qCampanha.descricao
					).as("campanha")
				).as("avaliacao")
			)
		)
			.join(qParticipante.avaliado, qFuncionario)
			//.join(qParticipante.avaliador, qFuncionario)
			.join(qParticipante.avaliacao, qAvaliacao)
			.join(qAvaliacao.campanha, qCampanha)
			.join(qFuncionario.empresa, qEmpresa)
			.where(
				qParticipante.avaliado.id.eq(avaliadoId),
				//qParticipante.avaliador.id.eq(loggedUser.getId()),
				qEmpresa.id.eq(getLoggedUser().getEmpresa().getId()),
				qAvaliacao.situacao.eq(SituacaoAvaliacao.LIBERADA)
			)
			.fetch();

		return map(participantes);
	}

	@Override
	public List<ParticipanteTO> obterParticipantesPorAvaliadoEAvaliacao(Long avaliadoId, Long avaliacaoId) {
		return map(avaliacaoParticipanteRepository.findByAvaliadoIdAndAvaliacaoId(avaliadoId, avaliacaoId));
	}


	@Override
	public List<ParticipanteTO> obterParticipanteProjections(Long avaliadoId, Long avaliadorId, Long revidorId, Long avaliacaoId) {
		JPAQuery<Participante> partQuery = select();

		partQuery.where(qParticipante.avaliado.id.eq(avaliadoId)
			.and(dslHelper.eq(qParticipante.avaliador.id, avaliadorId))
			.and(dslHelper.eq(qParticipante.revisor.id, revidorId))
			.and(dslHelper.eq(qParticipante.avaliacao.id, avaliacaoId))
		);
		partQuery.select(Projections.fields(
			Participante.class,
			qParticipante.id,
			qParticipante.perfilAgente,
			Projections.fields(
				Funcionario.class,
				qParticipante.avaliado.id,
				qParticipante.avaliado.nome
			).as(qParticipante.avaliado),
			Projections.fields(
				Funcionario.class,
				qParticipante.avaliador.id,
				qParticipante.avaliador.nome
			).as(qParticipante.avaliador)
		));
		return map(partQuery.fetch());
	}

	private boolean isAvaliacaoParticipanteValida(ParticipanteTO participante) {
		boolean isValido = true;

		List<ParticipanteTO> lista = null;

		if (participante.getRevisor() != null && participante.getRevisor().getId() != null) {
			lista = obterParticipanteProjections(participante.getAvaliado().getId(), participante.getAvaliador().getId(), participante.getRevisor().getId(), participante.getAvaliacao().getId());
		} else if (participante.getAvaliador() != null && participante.getAvaliador().getId() != null) {
			lista = obterParticipanteProjections(participante.getAvaliado().getId(), participante.getAvaliador().getId(), null, participante.getAvaliacao().getId());
		}
		if (lista != null && !lista.isEmpty()) {
			if (lista.size() > 1)
				isValido = false;
		}

		return isValido;
	}

	private void prepare(AvaliacaoSimpleTO avaliacao, ParticipanteTO participante) {
		avaliacao.setPreencheAv2(true);

		if (TipoAvaliacaoPerfil.GESTOR.equals(participante.getPerfilAgente())) {
			if ((avaliacao.getPreencheAv3() && avaliacao.getLimiteAv3() != null)) {
				if (participante.getLimiteAv3() == null) {
					participante.setLimiteAv3(avaliacao.getLimiteAv3());
				}
				if (participante.getLimiteAv1() == null) {
					participante.setLimiteAv1(avaliacao.getLimiteAv1());
				}
			}

			if ((avaliacao.getPreencheAv2() && avaliacao.getLimiteAv2() != null)) {
				if (participante.getLimiteAv2() == null) {
					participante.setLimiteAv2(avaliacao.getLimiteAv2());
				}
			}
		} else if (TipoAvaliacaoPerfil.AUTOAVALIACAO.equals(participante.getPerfilAgente())) {
			if (participante.getLimiteAv1() == null) {
				participante.setLimiteAv1(avaliacao.getLimiteAv1());
			}
		} else {
			participante.setLimiteAv4(null);
			participante.setLimiteAv3(null);
			participante.setLimiteAv2(avaliacao.getLimiteAv2());
			participante.setLimiteAv1(null);
		}

		if (participante.getConsensoLiberado() == null) {
			participante.setConsensoLiberado(false);
		}

		if (participante.getRevisaoIniciada() == null) {
			participante.setRevisaoIniciada(false);
		}

		participante.setRevisado(Boolean.TRUE);
	}


	private void validate(AvaliacaoSimpleTO avaliacao, ParticipanteTO participante) {

		TipoAvaliacaoPerfil perfilGestor = TipoAvaliacaoPerfil.GESTOR;
		if (avaliacao.getPreencheAv1() != null &&
			avaliacao.getPreencheAv1() == true &&
			avaliacao.getLimiteAv1() != null &&
			participante.getLimiteAv1() == null &&
			participante.getPerfilAgente().equals(perfilGestor)) {
			throw new ValidationException("avaliacao-participante", "empty-date-autoavaliacao");
		}

		if (avaliacao.getLimiteAv2() != null && participante.getLimiteAv2() == null) {
			throw new ValidationException("avaliacao-participante", "empty-date-avaliacao");
		}

		if (avaliacao.getPreencheAv3() != null &&
			avaliacao.getPreencheAv3() == true &&
			avaliacao.getLimiteAv3() != null &&
			participante.getLimiteAv3() == null &&
			participante.getPerfilAgente().equals(perfilGestor)) {
			throw new ValidationException("avaliacao-participante", "empty-date-consenso");
		}

		if (avaliacao.getPreencheAv4() != null &&
			avaliacao.getPreencheAv4() == true &&
			avaliacao.getLimiteAv4() != null &&
			participante.getLimiteAv4() == null &&
			participante.getPerfilAgente().equals(perfilGestor)) {
			throw new ValidationException("avaliacao-participante", "empty-date-revisao");
		}

		if (participante.getLimiteAv1() == null && participante.getLimiteAv2() == null && participante.getLimiteAv3() == null) {
			throw new ServiceException("A data de avaliação é um campo de preenchimento obrigatório.");
		}

		if (participante.getLimiteAv1() != null && participante.getPreenchidaAV3() != null && participante.getPreenchidaAV3() && participante.getLimiteAv3() == null) {
			throw new ServiceException("A data do consenso é um campo de preenchimento obrigatório.");
		}

		if (!isAvaliacaoParticipanteValida(participante)) {
			throw new ValidationException("avaliacao-participante", "validation-duplicate", participante.getAvaliado().getNome());
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void removerParticipanteAgLiberacao(Long id) {
		List<Participante> listaParticipantes = avaliacaoParticipanteRepository.findByAvaliacaoId(id);

		listaParticipantes.forEach((participante) -> {
			avaliacaoParticipanteRepository.delete(participante);
		});
	}

	@Override
	public Integer qtdAvaliadosPorAvaliacao(Long idAvaliacao) {
		List<Object[]> registros = avaliacaoParticipanteRepository.obterAvaliadosPorAvaliacao(idAvaliacao);
		if (registros != null && !registros.isEmpty())
			return registros.size();
		else
			return 0;
	}

	@Override
	public List<ParticipanteTO> obterAvaliadosPorAvaliacao(Long idAvaliacao) {
		List<ParticipanteTO> participanteTOList = new ArrayList<>();

		List<Object[]> registros = avaliacaoParticipanteRepository.obterAvaliadosPorAvaliacao(idAvaliacao);

		for (Object[] colunas : registros) {
			ParticipanteTO part = new ParticipanteTO();
			part.setId(Long.parseLong(colunas[0].toString()));

			AvaliacaoTO avaliacaoTO = new AvaliacaoTO();
			avaliacaoTO.setId(Long.parseLong(colunas[1].toString()));
			part.setAvaliacao(avaliacaoTO);

			FuncionarioSimpleTO avaliadoTO = new FuncionarioSimpleTO();
			avaliadoTO.setId(Long.parseLong(colunas[2].toString()));
			part.setAvaliado(avaliadoTO);

			FuncionarioSimpleTO avaliadorTO = new FuncionarioSimpleTO();
			avaliadorTO.setId(Long.parseLong(colunas[3].toString()));
			part.setAvaliador(avaliadorTO);

			part.setCriador(colunas[10].toString());
			part.setDataCriacao((Date) colunas[11]);
			part.setAtualizador(colunas[12].toString());
			part.setDataAtualizacao((Date) colunas[13]);
			part.setLimiteAv1((Date) colunas[14]);
			part.setLimiteAv2((Date) colunas[15]);
			part.setLimiteAv3((Date) colunas[16]);
			part.setPreenchidaAV1((Boolean) colunas[19]);
			part.setPreenchidaAV2((Boolean) colunas[20]);
			part.setPreenchidaAV3((Boolean) colunas[21]);
			part.setDataConclusaoAv1((Date) colunas[25]);
			part.setDataConclusaoAv2((Date) colunas[26]);
			part.setDataConclusaoAv3((Date) colunas[27]);
			part.setDataInicioAv1((Date) colunas[29]);
			part.setDataInicioAv2((Date) colunas[30]);
			part.setDataInicioAv3((Date) colunas[31]);
			part.setPerfilAgente(colunas[33] != null ? TipoAvaliacaoPerfil.valueOf(Integer.parseInt(colunas[33].toString())) : null);
			part.setRevisado((Boolean) colunas[34]);

			participanteTOList.add(part);
		}

		return participanteTOList;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private AvaliacaoCompetenciaTO saveOrReadAvaliacaoCompetencia(Avaliacao avaliacao, DetalhamentoCargoCompetenciaTO dp) {
		AvaliacaoCompetenciaTO avaliacaoCompetenciaTO = null;

		avaliacaoCompetenciaTO = avaliacaoCompetenciaService.obterAvaliacaoCompetenciaProjections(avaliacao.getId(), dp.getCargoCompetencia().getCompetencia().getId());

		if (avaliacaoCompetenciaTO == null) {
			AvaliacaoCompetencia avalCompetencia = new AvaliacaoCompetencia();
			avalCompetencia.setAvaliacao(avaliacao);
			avalCompetencia.setIdCompetencia(dp.getCargoCompetencia().getCompetencia().getId());
			avalCompetencia.setIdTipoCompetencia(dp.getCargoCompetencia().getCompetencia().getTipo().getId());
			avalCompetencia.setDescricaoTipoCompetencia(dp.getCargoCompetencia().getCompetencia().getTipo().getNome());
			avalCompetencia.setDescricaoAbreviada(dp.getCargoCompetencia().getCompetencia().getDescricaoAbreviada());
			avalCompetencia.setDirecionamentoGestor(dp.getCargoCompetencia().getCompetencia().getDirecionamentoGestor());
			avalCompetencia.setDirecionamentoNaoGestor(dp.getCargoCompetencia().getCompetencia().getDirecionamentoNaoGestor());
			avalCompetencia.setAtualizadorOriginal(dp.getCargoCompetencia().getCompetencia().getModifiedBy() == null ? dp.getCargoCompetencia().getCompetencia().getCreatedBy() : dp.getCargoCompetencia().getCompetencia().getModifiedBy());
			avalCompetencia.setDataAtualizacaoOriginal(dp.getCargoCompetencia().getCompetencia().getModificationDate() == null ? dp.getCargoCompetencia().getCompetencia().getCreationDate() : dp.getCargoCompetencia().getCompetencia().getModificationDate());
			avalCompetencia.setCreatedBy(getLoggedUser().getNome());
			avalCompetencia.setCreationDate(new Date());
			avalCompetencia = avaliacaoCompetenciaRepository.save(avalCompetencia);

			avaliacaoCompetenciaTO = mapper.map(avalCompetencia, AvaliacaoCompetenciaTO.class);
			logger.info(String.format("Salvou AvaliacaoCompetenciaTO com o id %s ...", avaliacaoCompetenciaTO.getId()));
		}

		return avaliacaoCompetenciaTO;
	}

	private void saveOrReadAvalDimComDet(AvaliacaoTO avaliacaoTO, DetalhamentoCargoCompetenciaTO dp) {
		AvalDimComDetTO avalDimComDetTO = null;

		avalDimComDetTO = avalDimComDetService.findAvalDimCompDetByDetalhamentoAndAvaliacao(avaliacaoTO.getId(), dp.getCompetenciaDetalhamento().getId());

		if (avalDimComDetTO == null) {

			DimensaoCompetenciaDetalhamentoTO dimCompDetTO = dimensaoCompetenciaDetalhamentoService.obterDimensaoDetalhamentoPorCompDet(dp.getCompetenciaDetalhamento().getId());

			AvalDimComDet avalDimCompDet = new AvalDimComDet();
			avalDimCompDet.setIdCompDet(dp.getCompetenciaDetalhamento().getId());
			avalDimCompDet.setDimensaoDescricao(dimCompDetTO.getDimensao().getDescricao());
			avalDimCompDet.setDimensaoNome(dimCompDetTO.getDimensao().getNome());
			avalDimCompDet.setIdDimensao(dimCompDetTO.getDimensao().getId());
			avalDimCompDet.setIdAvaliacao(avaliacaoTO.getId());
			avalDimCompDet = avalDimComDetRepository.save(avalDimCompDet);

			avalDimComDetTO = mapper.map(avalDimCompDet, AvalDimComDetTO.class);
			logger.info(String.format("Salvou AvalDimComDetTO com o id %s ...", avalDimComDetTO.getId()));
		}

	}

	private AvaliacaoCompDetTO saveOrReadAvaliacaoCompDet(AvaliacaoCompetenciaTO avaliacaoCompetenciaTO, DetalhamentoCargoCompetenciaTO dp) {
		AvaliacaoCompDetTO avaliacaoCompDetTO = null;

		avaliacaoCompDetTO = avaliacaoCompDetService.obterAvaliacaoCompDetProjections(avaliacaoCompetenciaTO.getId(), dp.getCompetenciaDetalhamento().getId(), null);

		if (avaliacaoCompDetTO == null) {
			AvaliacaoCompetencia avalComp = new AvaliacaoCompetencia();
			avalComp.setId(avaliacaoCompetenciaTO.getId());

			AvaliacaoCompDet avalCompDet = new AvaliacaoCompDet();
			avalCompDet.setAvaliacaoCompetencia(avalComp);
			avalCompDet.setIdCompetenciaDetalhamento(dp.getCompetenciaDetalhamento().getId());
			avalCompDet.setIdModeloGraduacao(dp.getCompetenciaDetalhamento().getModeloGraduacao().getId());
			avalCompDet.setOrdem(dp.getCompetenciaDetalhamento().getOrdem());
			avalCompDet.setDescricao(dp.getCompetenciaDetalhamento().getDescricao());
			avalCompDet.setAtualizadorOriginal(dp.getCompetenciaDetalhamento().getModifiedBy() == null ? dp.getCompetenciaDetalhamento().getCreatedBy() : dp.getCompetenciaDetalhamento().getModifiedBy());
			avalCompDet.setDataAtualizacaoOriginal(dp.getCompetenciaDetalhamento().getModificationDate() == null ? dp.getCompetenciaDetalhamento().getCreationDate() : dp.getCompetenciaDetalhamento().getModificationDate());
			avalCompDet.setCreatedBy(getLoggedUser().getNome());
			avalCompDet.setCreationDate(new Date());
			avalCompDet = avaliacaoCompDetRepository.save(avalCompDet);

			avaliacaoCompDetTO = mapper.map(avalCompDet, AvaliacaoCompDetTO.class);
			logger.info(String.format("Salvou AvaliacaoCompDetTO com o id %s ...", avaliacaoCompDetTO.getId()));
		}

		return avaliacaoCompDetTO;
	}

	private void saveOrReadAvaliacaoCompDetHint(AvaliacaoCompDetTO avaliacaoCompDet, CompDetalhamentoHintTO hint) {
		AvaliacaoCompDetHintTO avalCompDetHintTO = avaliacaoCompDetHintService.obterAvaliacaoCompDetHintProjections(avaliacaoCompDet.getId(), hint.getId());

		if (avalCompDetHintTO == null) {
			AvaliacaoCompDet avalCompDet = new AvaliacaoCompDet();
			avalCompDet.setId(avaliacaoCompDet.getId());

			AvaliacaoCompDetHint avalCompDetHint = new AvaliacaoCompDetHint();
			avalCompDetHint.setAvaliacaoCompDet(avalCompDet);
			avalCompDetHint.setIdHint(hint.getId());
			avalCompDetHint.setHint(hint.getHint());
			avalCompDetHint.setIdGraduacao(hint.getGraduacao().getId());
			avalCompDetHint.setComentarioObrigatorio(hint.getComentarioObrigatorio());
			avalCompDetHint.setAtualizadorOriginal(getLoggedUser().getNome());
			avalCompDetHint.setDataAtualizacaoOriginal(new Date());
			avalCompDetHint.setCreatedBy(getLoggedUser().getNome());
			avalCompDetHint.setCreationDate(new Date());
			avaliacaoCompDetHintRepository.save(avalCompDetHint);
			logger.info(String.format("Salvou AvaliacaoCompDetHint com o id %s ...", avalCompDetHint.getId()));
		}
	}

	private void saveAvaliacaoDetGab(AvaliacaoCompDetTO avaliacaoCompDet, DetalhamentoCargoCompetenciaTO dp) {
		CargoTipoCompetencia cargoTipoCompetencia = cargoTipoCompetenciaRepository.findByCargoIdAndTipoCompetenciaId(dp.getCargoCompetencia().getCargo().getId(), dp.getCargoCompetencia().getCompetencia().getTipo().getId());

		AvaliacaoCompDet avalCompDet = new AvaliacaoCompDet();
		avalCompDet.setId(avaliacaoCompDet.getId());

		AvaliacaoDetCargoComp avalDetGab = new AvaliacaoDetCargoComp();
		avalDetGab.setAvaliacaoCompDet(avalCompDet);
		avalDetGab.setIdDetCargoComp(dp.getId());
		avalDetGab.setIdCargoCompetencia(dp.getCargoCompetencia().getId());
		avalDetGab.setIdCompetenciaDetalhamento(dp.getCompetenciaDetalhamento().getId());
		avalDetGab.setPesoDetCargoComp(dp.getPeso());
		avalDetGab.setPesoCargoCompetencia(dp.getCargoCompetencia().getPeso());
		avalDetGab.setPesoCargoTipoCompetencia(cargoTipoCompetencia != null ? cargoTipoCompetencia.getPeso() : null);
		avalDetGab.setAplicavelCargo(dp.getAplicavelAoCargo());
		avalDetGab.setAtualizadorOriginal(dp.getModifiedBy() == null ? dp.getCreatedBy() : dp.getModifiedBy());
		avalDetGab.setDataAtualizacaoOriginal(dp.getModificationDate() == null ? dp.getCreationDate() : dp.getModificationDate());
		avalDetGab.setCreatedBy(getLoggedUser().getNome());
		avalDetGab.setCreationDate(new Date());
		avaliacaoDetCargoCompRepository.save(avalDetGab);
		logger.info(String.format("Salvou AvaliacaoDetCargoComp com o id %s ...", avalDetGab.getId()));
	}

	private Long saveModeloGraduacaoComp(Long idModeloGraduacao, Long idAvaliacao) {
		ModeloGraduacaoCompTO modeloGraduacaoCompTO = null;

		if (idModeloGraduacao != null) {
			modeloGraduacaoCompTO = modeloGraduacaoCompService.obterModeloGraduacaoCompProjections(idModeloGraduacao, idAvaliacao);
		}

		if (modeloGraduacaoCompTO == null) {
			ModeloGraduacaoTO modeloGraduacao = modeloGraduacaoService.obterModeloGraduacaoProjections(idModeloGraduacao);

			ModeloGraduacaoComp modeloGraduacaoComp = new ModeloGraduacaoComp();
			modeloGraduacaoComp.setIdModeloGraduacao(modeloGraduacao.getId());
			modeloGraduacaoComp.setDescricaoModelo(modeloGraduacao.getDescricaoModelo());
			modeloGraduacaoComp.setNaoGraduar(modeloGraduacao.getNaoGraduar());

			modeloGraduacaoComp.setIdDimensao(modeloGraduacao.getDimensao() == null ? null : modeloGraduacao.getDimensao().getId());
			modeloGraduacaoComp.setIdEmpresa(modeloGraduacao.getEmpresa().getId());

			modeloGraduacaoComp.setIdAvaliacao(idAvaliacao);
			modeloGraduacaoComp.setAtualizadorOriginal(modeloGraduacao.getModifiedBy() == null ? modeloGraduacao.getCreatedBy() : modeloGraduacao.getModifiedBy());
			modeloGraduacaoComp.setDataAtualizacaoOriginal(modeloGraduacao.getModificationDate() == null ? modeloGraduacao.getCreationDate() : modeloGraduacao.getModificationDate());
			modeloGraduacaoComp.setCreatedBy(getLoggedUser().getNome());
			modeloGraduacaoComp.setCreationDate(new Date());
			modeloGraduacaoComp = modeloGraduacaoCompRepository.save(modeloGraduacaoComp);

			logger.info(String.format("Salvou ModeloGraduacaoComp com o id %s ...", modeloGraduacaoComp.getId()));

			List<GraduacaoTO> graducoes = graduacaoService.obterGraduacoesProjections(idModeloGraduacao);

			for (GraduacaoTO grad : graducoes) {
				saveGraduacaoCompetencia(modeloGraduacaoComp, grad);
			}

			return modeloGraduacaoComp.getId();
		}

		return modeloGraduacaoCompTO.getId();
	}

	private void saveOrReadModeloGraduacaoAvalComp(AvaliacaoCompetenciaTO avaliacaoCompetencia, Long idModeloGraduacao, AvaliacaoCompDetTO avaliacaoCompDet, Long idAvaliacao) {
		ModeloGraduacaoAvalCompTO modeloGraduacaoAvalCompTO = null;

		if (idModeloGraduacao != null && avaliacaoCompetencia != null) {
			modeloGraduacaoAvalCompTO = modeloGraduacaoAvalCompService.obterModeloGraduacaoAvalCompProjections(avaliacaoCompetencia.getId(), idModeloGraduacao, null);
		} else if (idModeloGraduacao != null && avaliacaoCompDet != null) {
			modeloGraduacaoAvalCompTO = modeloGraduacaoAvalCompService.obterModeloGraduacaoAvalCompProjections(null, idModeloGraduacao, avaliacaoCompDet.getId());
		}

		if (modeloGraduacaoAvalCompTO == null) {
			Long modeloGraduacaoId = saveModeloGraduacaoComp(idModeloGraduacao, idAvaliacao);

			ModeloGraduacaoComp modGrad = new ModeloGraduacaoComp();
			modGrad.setId(modeloGraduacaoId);

			ModeloGraduacaoAvalComp modeloGraduacaoAvalComp = new ModeloGraduacaoAvalComp();

			if (avaliacaoCompetencia != null) {
				AvaliacaoCompetencia avalComp = new AvaliacaoCompetencia();
				avalComp.setId(avaliacaoCompetencia.getId());
				modeloGraduacaoAvalComp.setAvaliacaoCompetencia(avalComp);
			} else if (idModeloGraduacao != null && avaliacaoCompDet != null) {
				AvaliacaoCompDet avalCompDet = new AvaliacaoCompDet();
				avalCompDet.setId(avaliacaoCompDet.getId());
				modeloGraduacaoAvalComp.setAvaliacaoCompDet(avalCompDet);
			}

			modeloGraduacaoAvalComp.setModeloGraduacaoComp(modGrad);
			modeloGraduacaoAvalCompRepository.save(modeloGraduacaoAvalComp);
			logger.info(String.format("Salvou ModeloGraduacaoAvalComp com o id %s ...", modeloGraduacaoAvalComp.getId()));
		}
	}


	private void saveGraduacaoCompetencia(ModeloGraduacaoComp modeloGraduacaoComp, GraduacaoTO graduacao) {
		GraduacaoCompetenciaTO graduacaoCompetenciaTO = null;

		if (graduacao.getId() != null) {
			graduacaoCompetenciaTO = graduacaoCompetenciaService.obterGraduacaoCompetenciaProjections(modeloGraduacaoComp.getId(), graduacao.getId());
		}

		if (graduacaoCompetenciaTO == null) {
			GraduacaoCompetencia graduacaoComp = new GraduacaoCompetencia();
			graduacaoComp.setModeloGraduacao(modeloGraduacaoComp);
			graduacaoComp.setIdGraduacao(graduacao.getId());
			graduacaoComp.setOrdem(graduacao.getOrdem());
			graduacaoComp.setLabel(graduacao.getLabel());
			graduacaoComp.setHint(graduacao.getHint());
			graduacaoComp.setPonto(graduacao.getPonto());
			String cor = graduacao.getCor() != null ? graduacao.getCor() : null;
			logger.info(String.format("Cor da GraduacaoCompetencia s ...", cor));
			graduacaoComp.setCor(cor);
			graduacaoComp.setAtualizadorOriginal(graduacao.getModifiedBy() == null ? graduacao.getCreatedBy() : graduacao.getModifiedBy());
			graduacaoComp.setDataAtualizacaoOriginal(graduacao.getModificationDate() == null ? graduacao.getCreationDate() : graduacao.getModificationDate());
			graduacaoComp.setCreatedBy(getLoggedUser().getNome());
			graduacaoComp.setCreationDate(new Date());
			graduacaoCompetenciaRepository.save(graduacaoComp);
			logger.info(String.format("Salvou GraduacaoCompetencia com o id %s ...", graduacaoComp.getId()));
		}
	}

	private DetalhamentoParticipanteTO getDetalhamentoParticipante(Object[] objetos) {
		DetalhamentoParticipanteTO dp = new DetalhamentoParticipanteTO();

		dp.setIdCompetencia(Long.parseLong(objetos[1].toString()));

		dp.setDescricaoAbreviada(objetos[2].toString());

		dp.setIdTipoCompetencia(Long.parseLong(objetos[3].toString()));

		dp.setNomeCurto(objetos[4].toString());

		dp.setIdDetalhamento(Long.parseLong(objetos[5].toString()));

		dp.setDescricaoDetalhamento(objetos[6].toString());

		dp.setIdAvaliacao(Long.parseLong(objetos[7].toString()));

		dp.setDescricaoAbreviada(objetos[8].toString());

		dp.setIdPerfilAgente(Long.parseLong(objetos[9].toString()));

		dp.setIdCargo(Long.parseLong(objetos[10].toString()));

		dp.setNomeCargo(objetos[11].toString());

		dp.setIdDetCargoComp(Long.parseLong(objetos[12].toString()));

		return dp;
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void salvarPerguntas(ParticipanteTO participante, List<DetalhamentoCargoCompetenciaTO> listaPerguntas, Set<CompetenciaDetalhamentoTO> detalhamentosSalvos) {
		long start = System.currentTimeMillis();
		logger.info(String.format("Iniciada a persitência das perguntas ..."));
		Avaliacao avaliacaoEntity = mapper.map(participante.getAvaliacao(), Avaliacao.class);

		Participante participanteEntity = new Participante();
		participanteEntity.setId(participante.getId());

		for (DetalhamentoCargoCompetenciaTO dp : listaPerguntas) {

			AvaliacaoCompetenciaTO avaliacaoCompetencia = saveOrReadAvaliacaoCompetencia(avaliacaoEntity, dp);
			AvaliacaoCompDetTO avaliacaoCompDet = saveOrReadAvaliacaoCompDet(avaliacaoCompetencia, dp);
			if (participante.getAvaliacao().getCampanha().getDimensao() != null && participante.getAvaliacao().getCampanha().getDimensao()) {
				saveOrReadAvalDimComDet(participante.getAvaliacao(), dp);
			}

			AvaliacaoCompDet avalCompDet = new AvaliacaoCompDet();
			avalCompDet.setId(avaliacaoCompDet.getId());

			Object[] resultPreenchimento = preenchimentoRepository.obterPreenchimento(participante.getId(), avaliacaoCompDet.getId());
			if (resultPreenchimento == null || resultPreenchimento.length <= 0) {
				Preenchimento preenchimento = new Preenchimento();
				preenchimento.setAvaliacaoCompDet(avalCompDet);
				preenchimento.setParticipante(participanteEntity);
				preenchimento.setTipoClient(TipoClient.WEB);
				preenchimento.setCreatedBy(getLoggedUser().getMatricula());
				preenchimento.setCreationDate(new Date());
				preenchimentoRepository.save(preenchimento);
				logger.info(String.format("Preenchimento salvo com o id %s para o participante %s ...", preenchimento.getId(), participante.getId()));
			}

			saveAvaliacaoDetGab(avaliacaoCompDet, dp);
			List<CompDetalhamentoHintTO> listaHints = compDetalhamentoHintService.obterCompDetalhamentoHintProjections(avaliacaoCompDet.getIdCompetenciaDetalhamento());

			listaHints.forEach(hint -> {
				Graduacao graduacao = graduacaoRepository.findOne(hint.getGraduacao().getId());
				if (graduacao.getModeloGraduacao().getId().equals(avaliacaoCompDet.getIdModeloGraduacao()))
					saveOrReadAvaliacaoCompDetHint(avaliacaoCompDet, hint);
			});

			if (dp.getCompetenciaDetalhamento() != null && dp.getCompetenciaDetalhamento().getModeloGraduacao() != null) {
				saveOrReadModeloGraduacaoAvalComp(null, dp.getCompetenciaDetalhamento().getModeloGraduacao().getId(), avaliacaoCompDet, avaliacaoEntity.getId());
			} else if (dp.getCargoCompetencia() != null && dp.getCargoCompetencia().getCompetencia() != null && dp.getCargoCompetencia().getCompetencia().getModeloGraduacao() != null && dp.getCargoCompetencia().getCompetencia().getModeloGraduacao().getId() != null) {
				saveOrReadModeloGraduacaoAvalComp(avaliacaoCompetencia, dp.getCargoCompetencia().getCompetencia().getModeloGraduacao().getId(), null, avaliacaoEntity.getId());
			}

			detalhamentosSalvos.add(dp.getCompetenciaDetalhamento());
		}

		logger.info(String.format("A lista detalhamentosSalvos está com %s itens ...", detalhamentosSalvos.size()));

		long delay = System.currentTimeMillis() - start;
		long segundos = (delay / 1000) % 60;
		long minutos = (delay / 60000) % 60;
		long horas = delay / 3600000;// 3600000 = 60 * 60 * 10000) % 60;
		logger.info(String.format("Finalizada a persitência das perguntas, o método salvarPerguntas executou em %02d:%02d:%02d ...", horas, minutos, segundos));
	}

	@Override
	public void removerTodasPerguntasDoParticipante(ParticipanteTO participante) {
		List<AvaliacaoCompetencia> competencias = new ArrayList<>();
		List<AvaliacaoCompDet> detalhamentos = new ArrayList<>();
		List<Preenchimento> preenchimentos = preenchimentoRepository.findByParticipanteId(participante.getId());
		Set<Long> modelosGradComp = new HashSet<>();

		for (Preenchimento preenchimento : preenchimentos) {
			List<AvalPesoCargoComp> listAvalPesoCargoComp = avalPesoCargoCompRepository.findByIdPreenchimento(preenchimento.getId());

			for (AvalPesoCargoComp avalPCComp : listAvalPesoCargoComp) {
				avalPesoCargoCompRepository.delete(avalPCComp);
			}

			detalhamentos.add(preenchimento.getAvaliacaoCompDet());
			preenchimentoRepository.delete(preenchimento);
		}

		for (AvaliacaoCompDet avaliacaoCompDet : detalhamentos) {
			List<Preenchimento> lista = preenchimentoRepository.findByAvaliacaoCompDetId(avaliacaoCompDet.getId());
			if (lista == null || lista.isEmpty()) {
				competencias.add(avaliacaoCompDet.getAvaliacaoCompetencia());

				List<AvaliacaoDetCargoComp> gabaritos = avaliacaoDetCargoCompRepository.findByAvaliacaoCompDetId(avaliacaoCompDet.getId());
				for (AvaliacaoDetCargoComp avaliacaoDetCargoComp : gabaritos) {
					avaliacaoDetCargoCompRepository.delete(avaliacaoDetCargoComp);
				}

				List<AvaliacaoCompDetHint> hints = avaliacaoCompDetHintRepository.findByAvaliacaoCompDetId(avaliacaoCompDet.getId());
				for (AvaliacaoCompDetHint avaliacaoCompDetHint : hints) {
					avaliacaoCompDetHintRepository.delete(avaliacaoCompDetHint);
				}

				ModeloGraduacaoAvalComp modeloGraduacaoAvalComp = modeloGraduacaoAvalCompRepository.findByAvaliacaoCompDetId(avaliacaoCompDet.getId());
				if (modeloGraduacaoAvalComp != null) {
					modelosGradComp.add(modeloGraduacaoAvalComp.getModeloGraduacaoComp().getId());
					modeloGraduacaoAvalCompRepository.delete(modeloGraduacaoAvalComp);
				}

				avaliacaoCompDetRepository.delete(avaliacaoCompDet);
			}
		}

		for (AvaliacaoCompetencia avaliacaoCompetencia : competencias) {
			List<AvaliacaoCompDet> lista = avaliacaoCompDetRepository.findByAvaliacaoCompetenciaId(avaliacaoCompetencia.getId());

			if (lista == null || lista.isEmpty()) {
				ModeloGraduacaoAvalComp modeloGraduacaoAvalComp = modeloGraduacaoAvalCompRepository.findByAvaliacaoCompetenciaId(avaliacaoCompetencia.getId());
				if (modeloGraduacaoAvalComp != null) {
					modelosGradComp.add(modeloGraduacaoAvalComp.getModeloGraduacaoComp().getId());
					modeloGraduacaoAvalCompRepository.delete(modeloGraduacaoAvalComp);
				}

				avaliacaoCompetenciaRepository.delete(avaliacaoCompetencia);
			}
		}

		for (Long idModGradComp : modelosGradComp) {
			List<ModeloGraduacaoAvalComp> listModGradAvalComp = modeloGraduacaoAvalCompRepository.findByModeloGraduacaoCompId(idModGradComp);

			if (listModGradAvalComp == null || listModGradAvalComp.isEmpty()) {
				List<GraduacaoCompetencia> graduacoes = graduacaoCompetenciaRepository.findByModeloGraduacaoId(idModGradComp);

				for (GraduacaoCompetencia graduacaoCompetencia : graduacoes) {
					graduacaoCompetenciaRepository.delete(graduacaoCompetencia);
				}
				modeloGraduacaoCompRepository.delete(idModGradComp);
			}

		}
	}

	@Override
	public boolean temAvaliadores(Long avaliacaoId, Long avaliadoId) {
		JPAQuery<Participante> query = select()
			.where(
				qParticipante.avaliacao.id.eq(avaliacaoId),
				qParticipante.avaliado.id.eq(avaliadoId),
				qParticipante.perfilAgente.eq(TipoAvaliacaoPerfil.GESTOR)
			);

		return query.fetchCount() > 0;
	}

	//TODO: Passar isso para o serviceFilter e ver se nao tera side-effects
	@Override
	public List<ParticipanteTO> obterAvaliacaoParticipantePDI() {
		UserTO user = getLoggedUser();
		BooleanBuilder builder = new BooleanBuilder();

		switch (user.getTipoPerfil()) {
			case ADMINISTRADOR:
			case GESTOR_RH:
				builder.and(qEmpresa.id.eq(user.getEmpresa().getId()));
				break;
			case CONSULTOR_RH: //TODO Ver todas as empresas da qual ele é gestor
				builder.and(qEmpresa.id.eq(user.getEmpresa().getId()));
				break;
			case GESTOR_AREA: //TODO: Ele seria avaliado ou avaliador? Ver isso!
				builder.and(qParticipante.avaliado.departamento.id.eq(user.getDepartamento().getId())
					.or(qParticipante.avaliador.departamento.id.eq(user.getDepartamento().getId())));
				break;
			default:
				builder.and(qParticipante.avaliado.id.eq(user.getId())
					.or(qParticipante.avaliador.id.eq(user.getId())));
				break;
		}

		JPAQuery<Participante> query = select(Projections.fields(Participante.class,
			qParticipante.id,
			Projections.fields(Avaliacao.class,
				qAvaliacao.id,
				qAvaliacao.descricao,
				qAvaliacao.descricaoAbreviada,
				Projections.fields(Campanha.class,
					qCampanha.id,
					qCampanha.descricao)
					.as("campanha")
			).as("avaliacao")
		)).join(qParticipante.avaliacao, qAvaliacao)
			.join(qAvaliacao.campanha, qCampanha)
			.join(qCampanha.empresa, qEmpresa)
			.distinct()
			.where(builder);
		List<ParticipanteTO> listaPart = map(query.fetch());

		return listaPart;
	}
}
