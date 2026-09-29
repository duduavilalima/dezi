package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.PreenchimentoRepository;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.mobile.*;
import br.com.fasolti.avd.types.*;
import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseDeziService;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class PreenchimentoMobileServiceImpl extends BaseDeziService<Preenchimento, PreenchimentoMobileTO> implements PreenchimentoMobileService {

	private static final QPreenchimento qPreenchimento = QPreenchimento.preenchimento;
	private static final QParticipante qParticipante = QParticipante.participante;
	private static final QAvaliacaoCompDet qAvaliacaoCompDet = QAvaliacaoCompDet.avaliacaoCompDet;
	private static final QAvaliacaoCompetencia qAvaliacaoCompetencia = QAvaliacaoCompetencia.avaliacaoCompetencia;
	private static final QAvaliacao qAvaliacao = QAvaliacao.avaliacao;
	private static final QGraduacaoCompetencia qGraduacaoCompetencia = QGraduacaoCompetencia.graduacaoCompetencia;
	private static final QFuncionario qFuncionario = QFuncionario.funcionario;


	@Autowired
	private PreenchimentoRepository preenchimentoRepository;

	@Autowired
	private ModeloGraduacaoCompService modeloGraduacaoCompService;

	@Autowired
	private GraduacaoCompetenciaService graduacaoCompetenciaService;

	@Autowired
	private GraduacaoService graduacaoService;

	@Autowired
	private FotoService fotoService;

	@Autowired
	private AvaliacaoCompDetHintService avaliacaoCompDetHintService;

	@Autowired
	private CompDetalhamentoHintService compDetalhamentoHintService;

	@Autowired
	public PreenchimentoMobileServiceImpl(PreenchimentoRepository preenchimentoRepository) {
		super(preenchimentoRepository);
	}

	@Override
	protected ServiceFilter<Preenchimento> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless

		// Vide AvaliacaoCompDetResource#listByAvaliacaoCompetencia se for necessário equiparar o Mobile ao Web.
		// Aparentemente, alterações aqui devem ser replicadas em ParticipanteMobileServiceImpl#getServiceFilter também.

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
		return ServiceFilter.create(qPreenchimento, filter);
	}


	private static final QBean<Preenchimento> TIPO_COMPETENCIA_PROJECTION = Projections.fields(
		Preenchimento.class,
		qPreenchimento.id,
		Projections.fields(
			Graduacao.class,
			qPreenchimento.graduacaoAv1.id
		).as("graduacaoAv1"),
		Projections.fields(
			Graduacao.class,
			qPreenchimento.graduacaoAv2.id
		).as("graduacaoAv2"),
		Projections.fields(
			AvaliacaoCompDet.class,
			Projections.fields(
				AvaliacaoCompetencia.class,
				qAvaliacaoCompetencia.id,
				qAvaliacaoCompetencia.idTipoCompetencia,
				qAvaliacaoCompetencia.descricaoTipoCompetencia,
//				qAvaliacaoCompetencia.direcionamentoGestor,
				qAvaliacaoCompetencia.descricaoAbreviada,
				Projections.fields(
					Avaliacao.class,
					qAvaliacao.id,
					qAvaliacao.limiteAv1,
					qAvaliacao.limiteAv2,
					qAvaliacao.limiteAv3,
					qAvaliacao.descricaoAbreviada,
					qAvaliacao.preencheAv1,
					qAvaliacao.preencheAv2,
					qAvaliacao.preencheAv3
				).as("avaliacao")
			).as("avaliacaoCompetencia")
		).as("avaliacaoCompDet"),
		Projections.fields(
			Participante.class,
			qParticipante.id,
			qParticipante.perfilAgente,
			Projections.fields(
				Funcionario.class,
				qParticipante.avaliador.id
			).as("avaliador"),
			Projections.fields(
				Funcionario.class,
				qParticipante.avaliado.id
			).as("avaliado")
		).as("participante")
	);

	private static final QBean<Preenchimento> COMPETENCIA_DETALHAMENTO_PROJECTION = Projections.fields(
		Preenchimento.class,
		qPreenchimento.id,
		qPreenchimento.observacaoAv1,
		qPreenchimento.observacaoAv2,
		Projections.fields(
			Graduacao.class,
			qPreenchimento.graduacaoAv1.id
		).as("graduacaoAv1"),
		Projections.fields(
			Graduacao.class,
			qPreenchimento.graduacaoAv2.id
		).as("graduacaoAv2"),
		qPreenchimento.graduacaoCompAv1Id,
		qPreenchimento.graduacaoCompAv2Id,
		Projections.fields(
			AvaliacaoCompDet.class,
			qAvaliacaoCompDet.id,
			qAvaliacaoCompDet.descricao,
			qAvaliacaoCompDet.idModeloGraduacao,
			qAvaliacaoCompDet.idCompetenciaDetalhamento,
			qAvaliacaoCompDet.ordem,
			Projections.fields(
				AvaliacaoCompetencia.class,
				qAvaliacaoCompetencia.id,
				qAvaliacaoCompetencia.descricaoAbreviada,
				qAvaliacaoCompetencia.direcionamentoGestor,
				Projections.fields(
					Avaliacao.class,
					qAvaliacao.id,
					qAvaliacao.preencheAv1,
					qAvaliacao.preencheAv2
				).as("avaliacao")
			).as("avaliacaoCompetencia")
		).as("avaliacaoCompDet"),
		Projections.fields(
			Participante.class,
			qParticipante.id,
			qParticipante.consensoLiberado,
			qParticipante.preenchidaAV1,
			qParticipante.preenchidaAV2,
			qParticipante.perfilAgente,
			Projections.fields(
				Funcionario.class,
				qParticipante.avaliador.id,
				qParticipante.avaliador.nome
			).as("avaliador"),
			Projections.fields(
				Funcionario.class,
				qParticipante.avaliado.id,
				qParticipante.avaliado.nome
			).as("avaliado")
		).as("participante")
	);

	private JPAQuery<Preenchimento> avaliacaoParticipanteQuery() {
		JPAQuery<Preenchimento> query = select(TIPO_COMPETENCIA_PROJECTION)
			.join(qPreenchimento.participante, qParticipante)
			.join(qPreenchimento.avaliacaoCompDet, qAvaliacaoCompDet)
			.join(qParticipante.avaliacao, qAvaliacao)
			.join(qAvaliacaoCompDet.avaliacaoCompetencia, qAvaliacaoCompetencia);
		return query;
	}

	private JPAQuery<Preenchimento> competenciaDetalhamentoQuery() {
		JPAQuery<Preenchimento> query = select(COMPETENCIA_DETALHAMENTO_PROJECTION)
			.join(qPreenchimento.participante, qParticipante)
			.join(qPreenchimento.avaliacaoCompDet, qAvaliacaoCompDet)
			.join(qParticipante.avaliacao, qAvaliacao)
			.join(qParticipante.avaliado, qFuncionario)
			.join(qParticipante.avaliador, qFuncionario)
			.join(qAvaliacaoCompDet.avaliacaoCompetencia, qAvaliacaoCompetencia);
		return query;
	}


	/*Como as queries são cacheadas,
	foi utilizada a mesma query do tipo de competencia*/
	private JPAQuery<Preenchimento> percentualCompetenciaQuery() {
		JPAQuery<Preenchimento> query = select(TIPO_COMPETENCIA_PROJECTION)
			.join(qPreenchimento.participante, qParticipante)
			.join(qPreenchimento.avaliacaoCompDet, qAvaliacaoCompDet)
			.join(qAvaliacaoCompDet.avaliacaoCompetencia, qAvaliacaoCompetencia)
			.join(qParticipante.avaliacao, qAvaliacao);
		return query;
	}

	/**
	 * Deveria ser feito em AvaliacaoMobileService,
	 * porém com a modelagem atual, é inviável buscar
	 * todas as informações necessárias a partir da avaliação,
	 * por conta disso, foi feito no PreenchimentoMobileService
	 *
	 * @param avaliacaoId
	 * @return List<AvaliacaoTipoCompetenciaMobileTO>
	 */
	@Override
	public AvaliacaoMobileTO listAvTipoCompetenciaPorAvaliacao(Long avaliacaoId) {
		JPAQuery<Preenchimento> query = avaliacaoParticipanteQuery()
			.where(
				qAvaliacao.id.eq(avaliacaoId),
				qAvaliacao.situacao.eq(SituacaoAvaliacao.LIBERADA)
			);

		return tipoCompetenciaMobileMapper(query.fetch());
	}

	private AvaliacaoMobileTO tipoCompetenciaMobileMapper(List<Preenchimento> preenchimentoList) {
		UserTO funcionarioLogado = getLoggedUser();
		AvaliacaoMobileTO avaliacaoMobileTO = new AvaliacaoMobileTO();
		if (preenchimentoList != null && preenchimentoList.size() > 0) {
			avaliacaoMobileTO.setDescricaoAbreviada(preenchimentoList.get(0).getAvaliacaoCompDet().getAvaliacaoCompetencia().getAvaliacao().getDescricaoAbreviada());
			avaliacaoMobileTO.setId(preenchimentoList.get(0).getAvaliacaoCompDet().getAvaliacaoCompetencia().getAvaliacao().getId());

		}

		List<AvaliacaoTipoCompetenciaMobileTO> tipoCompetenciaMobileTOList = new ArrayList<>();
		preenchimentoList.stream().forEach(preenchimento -> {
			AvaliacaoCompetencia avCompetencia = preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia();

			//Obtém a avaliaçãoTipoComp na lista
			Optional<AvaliacaoTipoCompetenciaMobileTO> optAvTipoCompMobileTO = tipoCompetenciaMobileTOList
				.stream()
				.filter(avTipoCompMobile -> avTipoCompMobile.getId().equals(avCompetencia.getIdTipoCompetencia()))
				.findAny();

			//Se a lista não contiver o objeto, cria-o
			if (!optAvTipoCompMobileTO.isPresent()) {

				AvaliacaoTipoCompetenciaMobileTO avtipoCompetenciaMobileTO = new AvaliacaoTipoCompetenciaMobileTO();
				createAvTipoCompMobile(avtipoCompetenciaMobileTO, avCompetencia);
				//E também cria a competencia
				createCompetenciaMobile(preenchimento, avtipoCompetenciaMobileTO, funcionarioLogado);

				tipoCompetenciaMobileTOList.add(avtipoCompetenciaMobileTO);
			} else {

				//Obtém a competência na lista de competências de avaliacaoTipoComp
				Optional<CompetenciaMobileTO> optCompetenciaMobile = optAvTipoCompMobileTO.get().getListaCompetencias()
					.stream()
					.filter(compMobile -> compMobile.getId().equals(avCompetencia.getId()))
					.findAny();

				//Caso não tenha essa competência na lista do tipo, cria
				if (!optCompetenciaMobile.isPresent()) {
					createCompetenciaMobile(preenchimento, optAvTipoCompMobileTO.get(), funcionarioLogado);
				}
				optAvTipoCompMobileTO.get().getListaCompetencias().sort(Comparator.comparing(CompetenciaMobileTO::getDescricaoAbreviada));
			}
		});
		calculaPorcentagemTotal(tipoCompetenciaMobileTOList);
		tipoCompetenciaMobileTOList.sort(Comparator.comparing(AvaliacaoTipoCompetenciaMobileTO::getDescricaoTipoCompetencia));
		avaliacaoMobileTO.setAvaliacaoTipoCompetenciaList(tipoCompetenciaMobileTOList);
		return avaliacaoMobileTO;
	}

	private void createAvTipoCompMobile(AvaliacaoTipoCompetenciaMobileTO avTipoCompMobile, AvaliacaoCompetencia avaliacaoCompetencia) {
		Date dataLimiteAvaliacao;
		Avaliacao avaliacao = avaliacaoCompetencia.getAvaliacao();

		if (!avaliacao.getPreencheAv2()) {
			dataLimiteAvaliacao = avaliacao.getLimiteAv1();

		} else {
			dataLimiteAvaliacao = avaliacao.getLimiteAv2();
		}

		if (dataLimiteAvaliacao == null) {
			throw new IllegalStateException("Avaliação não possui data-limite.");
		}

		Integer diasRestantes = getDiasRestantes(new Date(), dataLimiteAvaliacao);

		avTipoCompMobile.setId(avaliacaoCompetencia.getIdTipoCompetencia());
		avTipoCompMobile.setDescricaoTipoCompetencia(avaliacaoCompetencia.getDescricaoTipoCompetencia());
		avTipoCompMobile.setDiasRestantes(diasRestantes);
		avTipoCompMobile.setListaCompetencias(new ArrayList<>());
	}

	private Integer getDiasRestantes(Date dataAntes, Date dataDepois) {
		long diff = dataDepois.getTime() - dataAntes.getTime();
		return Math.toIntExact(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
	}

	private void createCompetenciaMobile(Preenchimento preenchimento, AvaliacaoTipoCompetenciaMobileTO avalTipoCompMob, UserTO funcionarioLogado) {
		AvaliacaoCompetencia avCompetencia = preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia();

		CompetenciaMobileTO competenciaMobile = new CompetenciaMobileTO();

		competenciaMobile.setId(avCompetencia.getId());
		competenciaMobile.setDirecionamentoGestor(avCompetencia.getDirecionamentoGestor());
		competenciaMobile.setDescricaoAbreviada(avCompetencia.getDescricaoAbreviada());

		calculaPerguntasERespostas(competenciaMobile,
			avCompetencia.getId(),
			funcionarioLogado);

		avalTipoCompMob.getListaCompetencias().add(competenciaMobile);
	}

	private void calculaPerguntasERespostas(CompetenciaMobileTO competenciaMobile, Long avCompId, UserTO funcionarioLogado) {

		List<Preenchimento> preenchimentos = percentualCompetenciaQuery()
			.where(
				qAvaliacaoCompetencia.id.eq(avCompId)
			).fetch();

		Long qtdRespostas = preenchimentos.stream().filter(p -> verificaGraduacao(p, funcionarioLogado)).count();
		Integer qtdTotalPerguntas = preenchimentos.size();
		Double percentual = (double) Math.round((qtdRespostas * 100) / (double) qtdTotalPerguntas);

		competenciaMobile.setQtdCompletosPorPreenchimento((double) qtdRespostas);
		competenciaMobile.setQtdTotalDePreenchimentos(qtdTotalPerguntas);
		if (qtdTotalPerguntas > 0 && qtdRespostas > 0) {
			competenciaMobile.setPorcentagemPreenchimento(percentual);
		} else {
			competenciaMobile.setPorcentagemPreenchimento(0D);
		}

	}

	private void calculaPorcentagemTotal(List<AvaliacaoTipoCompetenciaMobileTO> avaliacaoTipoCompetenciaMobileTOList) {
		calculaPercentualPorCompetencia(avaliacaoTipoCompetenciaMobileTOList);
		calculaPercentualPorPreenchimento(avaliacaoTipoCompetenciaMobileTOList);
	}

	private void calculaPercentualPorCompetencia(List<AvaliacaoTipoCompetenciaMobileTO> avaliacaoTipoCompetenciaMobileTOList) {
		avaliacaoTipoCompetenciaMobileTOList.forEach(avTipoComp -> {
			Double porcentagemTipoComp = avTipoComp.getListaCompetencias()
				.stream()
				.map(CompetenciaMobileTO::getPorcentagemPreenchimento)
				.filter(preenchimento -> preenchimento.equals(100D))
				.reduce(0D, (a, b) -> (a + b));
			Double percentual = (double) Math.round(porcentagemTipoComp / (double) avTipoComp.getListaCompetencias().size());

			avTipoComp.setPorcentagemPreenchimentoPorCompetencia(percentual);
			avTipoComp.setQtdCompletosPorCompetencia(Math.floor(porcentagemTipoComp / 100));
		});
	}

	private void calculaPercentualPorPreenchimento(List<AvaliacaoTipoCompetenciaMobileTO> avaliacaoTipoCompetenciaMobileTOList) {
		avaliacaoTipoCompetenciaMobileTOList.forEach(avTipoComp -> {

			Double qtdCompletosGeralPorPreench = avTipoComp.getListaCompetencias()
				.stream()
				.map(CompetenciaMobileTO::getQtdCompletosPorPreenchimento)
				.reduce(0D, (a, b) -> (a + b));

			Integer qtdTotalDePreench = avTipoComp.getListaCompetencias()
				.stream()
				.map(CompetenciaMobileTO::getQtdTotalDePreenchimentos)
				.reduce(0, (a, b) -> (a + b));

			Double percentual = (double) Math.round((qtdCompletosGeralPorPreench * 100) / (double) qtdTotalDePreench);

			avTipoComp.setPorcentagemPreenchimentoPorPreenchimento(percentual);
			avTipoComp.setQtdCompletosPorPreenchimento(qtdCompletosGeralPorPreench);
			avTipoComp.setQtdTotalPorPreenchimento(qtdTotalDePreench);
		});
	}

	@Override
	public CompetenciaMobileTO listPreenchimentoPorCompetencia(Long competenciaId) {
		JPAQuery<Preenchimento> query = competenciaDetalhamentoQuery().where(
			qAvaliacao.situacao.eq(SituacaoAvaliacao.LIBERADA),
			qAvaliacaoCompetencia.id.eq(competenciaId)
		);
		return competenciaMobileMapper(query.fetch());
	}

	private CompetenciaMobileTO competenciaMobileMapper(List<Preenchimento> preenchimentoList) {
		CompetenciaMobileTO competenciaMobileTO = new CompetenciaMobileTO();
		/*Caso exista preenchimentos, traz os dados pertinentes da competencia para exibi-los no Mobile*/
		if (preenchimentoList != null && preenchimentoList.size() > 0) {
			updateCompetenciaMobile(competenciaMobileTO, preenchimentoList.get(0));
		}

		List<DetalhamentoMobileTO> detalhamentoMobileTOList = new ArrayList<DetalhamentoMobileTO>();

		/*Percorre a lista de preenchimentos,
		 * reestruturando-a em uma lista de DetalhamentoMobile
		 * e, dentro dela, seus respectivos preenchimentos*/
		preenchimentoList.stream().forEach(preenchimento -> {
			AvaliacaoCompDet avaliacaoCompDet = preenchimento.getAvaliacaoCompDet();

			/*Cria-se o preenchimentoMobile inicialmente*/
			PreenchimentoMobileTO preenchimentoMobileTO = new PreenchimentoMobileTO();
			createPreenchimentoMobile(preenchimentoMobileTO, preenchimento);

			//Verifica se na lista de detalhamentos já existe o objeto
			Optional<DetalhamentoMobileTO> optDetalhamentoMobileTO = detalhamentoMobileTOList
				.stream()
				.filter(detalhamentoMobile -> detalhamentoMobile.getId().equals(avaliacaoCompDet.getId()))
				.findAny();

			/*Se não existir,
			 * cria-o e insere o preenchimentoMobile criado previamente;vim
			 * Caso contrario,
			 * apenas insere o preenchimentoMobile;
			 */
			if (!optDetalhamentoMobileTO.isPresent()) {
				DetalhamentoMobileTO detalhamentoMobile = new DetalhamentoMobileTO();

				createDetalhamentoMobile(detalhamentoMobile, avaliacaoCompDet);
				updatePreenchimentoList(detalhamentoMobile, avaliacaoCompDet, preenchimentoMobileTO);

				detalhamentoMobileTOList.add(detalhamentoMobile);
			} else {
				updatePreenchimentoList(optDetalhamentoMobileTO.get(), avaliacaoCompDet, preenchimentoMobileTO);
			}
		});
		detalhamentoMobileTOList.forEach(this::sortDetalhamentos);
		competenciaMobileTO.setListaDetalhamentos(detalhamentoMobileTOList);
		competenciaMobileTO.getListaDetalhamentos().sort(Comparator.comparing(DetalhamentoMobileTO::getOrdem));
		return competenciaMobileTO;
	}

	private void updateCompetenciaMobile(CompetenciaMobileTO competenciaMobileTO, Preenchimento preenchimento) {
		competenciaMobileTO.setDescricaoAbreviada(preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia().getDescricaoAbreviada());
		competenciaMobileTO.setDescricaoAbreviada(preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia().getDescricaoAbreviada());
		competenciaMobileTO.setDirecionamentoGestor(preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia().getDirecionamentoGestor());
		competenciaMobileTO.setId(preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia().getId());
	}

	private void createDetalhamentoMobile(DetalhamentoMobileTO detalhamentoMobile, AvaliacaoCompDet avaliacaoCompDet) {
		detalhamentoMobile.setId(avaliacaoCompDet.getId());
		detalhamentoMobile.setOrdem(avaliacaoCompDet.getOrdem());
		detalhamentoMobile.setDescricao(avaliacaoCompDet.getDescricao());

		detalhamentoMobile.setListaGraduacao(new ArrayList<>());
		detalhamentoMobile.setListaPreenchimentos(new ArrayList<>());
	}

	private void updatePreenchimentoList(DetalhamentoMobileTO detalhamentoMobile, AvaliacaoCompDet avaliacaoCompDet, PreenchimentoMobileTO preenchimentoMobileTO) {

		ModeloGraduacaoCompTO modeloGraduacaoComp = modeloGraduacaoCompService.obterModeloGraduacaoCompProjections(avaliacaoCompDet.getIdModeloGraduacao(), avaliacaoCompDet.getAvaliacaoCompetencia().getAvaliacao().getId());

		listGraduacaoMobileByModeloGraduacao(detalhamentoMobile, modeloGraduacaoComp, avaliacaoCompDet);
		detalhamentoMobile.getListaPreenchimentos().add(preenchimentoMobileTO);

	}


	private void listGraduacaoMobileByModeloGraduacao(DetalhamentoMobileTO detalhamentoMobileTO, ModeloGraduacaoCompTO modeloGraduacaoCompTO, AvaliacaoCompDet avaliacaoCompDet) {
		List<AvaliacaoCompDetHintTO> listaHints = avaliacaoCompDetHintService.listByAvaliacaoCompDetId(avaliacaoCompDet.getId());

		listaHints.forEach(hint -> {
			GraduacaoCompetenciaTO graduacaoTO = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacao(modeloGraduacaoCompTO.getId(), hint.getIdGraduacao());
			graduacaoTO.setCor(GraduacaoCorEnum.valueOf((graduacaoTO.getOrdem()==null || graduacaoTO.getOrdem() > 10)?11:graduacaoTO.getOrdem()).getNome());
			hint.setGraduacao(graduacaoTO);
		});

		detalhamentoMobileTO.setListaGraduacao(graduacaoListMapper(listaHints));
	}

	private List<GraduacaoMobileTO> graduacaoListMapper(List<AvaliacaoCompDetHintTO> avaliacaoCompDetHintTOList) {
		List<GraduacaoMobileTO> graduacaoMobileTOList = new ArrayList<>();
		avaliacaoCompDetHintTOList.forEach(avaliacaoCompDet -> {
			GraduacaoMobileTO graduacaoMobileTO = new GraduacaoMobileTO();
			graduacaoMobileTO.setComentarioObrigatorio(avaliacaoCompDet.getComentarioObrigatorio());
			graduacaoMobileTO.setCor(avaliacaoCompDet.getGraduacao().getCor());
			graduacaoMobileTO.setHint(avaliacaoCompDet.getGraduacao().getHint());
			graduacaoMobileTO.setId(avaliacaoCompDet.getGraduacao().getIdGraduacao());
			graduacaoMobileTO.setLabel(avaliacaoCompDet.getGraduacao().getLabel());
			graduacaoMobileTO.setPonto(avaliacaoCompDet.getGraduacao().getPonto());
			graduacaoMobileTO.setOrdem(avaliacaoCompDet.getGraduacao().getOrdem());

			graduacaoMobileTOList.add(graduacaoMobileTO);
		});

		return graduacaoMobileTOList;
	}

	private void createPreenchimentoMobile(PreenchimentoMobileTO preenchimentoMobileTO, Preenchimento preenchimento) {
		UserTO funcionario = getLoggedUser();

		preenchimentoMobileTO.setId(preenchimento.getId());
		//converteParticipante(preenchimentoMobileTO, preenchimento.getParticipante());

		if (preenchimento.getParticipante() != null
			&& preenchimento.getParticipante().getAvaliado() != null
			&& preenchimento.getParticipante().getAvaliador() != null) {
			preenchimentoMobileTO.setParticipante(mapper.map(preenchimento.getParticipante(), ParticipanteMobileTO.class));

			FotoTO foto = fotoService.obterFotoPerfilFuncionario(preenchimento.getParticipante().getAvaliado().getId());
			if (foto != null) {
				preenchimentoMobileTO.getParticipante().setFotoAvaliado(foto.getBase64());
			}
			/*Caso o usuario logado seja o avaliado (Av1)*/
			if (preenchimento.getParticipante().getAvaliado().getId().equals(funcionario.getId())
				&& preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia().getAvaliacao().getPreencheAv1() == true
				&& Arrays.asList(TipoAvaliacaoPerfil.GESTOR, TipoAvaliacaoPerfil.AUTOAVALIACAO).contains(preenchimento.getParticipante().getPerfilAgente())) {

				preenchimentoMobileTO.setTipoAv(TipoAvMobile.AV1);
				preenchimentoMobileTO.setGraduacaoCompAv1Id(preenchimento.getGraduacaoCompAv1Id());
				//Caso tenha preenchido Av1
				if (exists(preenchimento.getGraduacaoAv1())) {
					/*Usado o service para obter a graduação pois caso coloque os campos da graduacao no projections,
					não está trazendo o preenchimento por conta do JOIN com graduacao*/
					GraduacaoTO graduacaoTO = graduacaoService.load(preenchimento.getGraduacaoAv1().getId());

					graduacaoTO.setCor(GraduacaoCorEnum.valueOf((graduacaoTO.getOrdem()==null || graduacaoTO.getOrdem()>10)?11:graduacaoTO.getOrdem()).getNome());
					preenchimentoMobileTO.setGraduacao(mapper.map(graduacaoTO, GraduacaoMobileTO.class));
					preenchimentoMobileTO.setObservacao(preenchimento.getObservacaoAv1());
				}

				/*Caso o usuario logado seja o avaliador (Av2)*/
			} else if (
				preenchimento.getParticipante().getAvaliador().getId().equals(funcionario.getId())
					&& preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia().getAvaliacao().getPreencheAv2() == true) {

				preenchimentoMobileTO.setTipoAv(TipoAvMobile.AV2);
				preenchimentoMobileTO.setGraduacaoCompAv2Id(preenchimento.getGraduacaoCompAv2Id());
				//Caso tenha preenchido Av2
				if (exists(preenchimento.getGraduacaoAv2())) {
					/*Usado o service para obter a graduação pois caso coloque os campos da graduacao no projections,
					não está trazendo o preenchimento por conta do JOIN com graduacao*/
					GraduacaoTO graduacaoTO = graduacaoService.load(preenchimento.getGraduacaoAv2().getId());
					graduacaoTO.setCor(GraduacaoCorEnum.valueOf((graduacaoTO.getOrdem()==null || graduacaoTO.getOrdem()>10)?11:graduacaoTO.getOrdem()).getNome());
					preenchimentoMobileTO.setGraduacao(mapper.map(graduacaoTO, GraduacaoMobileTO.class));
					preenchimentoMobileTO.setObservacao(preenchimento.getObservacaoAv2());
				}
			}
		}
	}

	private void sortDetalhamentos(DetalhamentoMobileTO detalhamentoMobileTO) {
		detalhamentoMobileTO.getListaGraduacao().sort(Comparator.comparing(GraduacaoMobileTO::getPonto));
		detalhamentoMobileTO.getListaPreenchimentos()
			.sort(
				Comparator.comparing(
					PreenchimentoMobileTO::getParticipante, (part1, part2) -> {
						if (part1 != null && part2 != null) {
							return part1.getNomeAvaliado().compareTo(part2.getNomeAvaliado()) > 0 ? 1 : -1;
						} else {
							return 0;
						}
					})
			);
	}

	private boolean verificaGraduacao(Preenchimento p, UserTO funcionarioLogado) {
		if (p.getParticipante().getAvaliado().getId().equals(funcionarioLogado.getId())
			&& p.getAvaliacaoCompDet().getAvaliacaoCompetencia().getAvaliacao().getPreencheAv1() == true
			&& Arrays.asList(TipoAvaliacaoPerfil.GESTOR, TipoAvaliacaoPerfil.AUTOAVALIACAO).contains(p.getParticipante().getPerfilAgente())
			&& exists(p.getGraduacaoAv1())) {
			return true;
		} else if (
			p.getParticipante().getAvaliador().getId().equals(funcionarioLogado.getId())
				&& p.getAvaliacaoCompDet().getAvaliacaoCompetencia().getAvaliacao().getPreencheAv2() == true
				&& exists(p.getGraduacaoAv2())) {
			return true;
		} else {
			return false;
		}
	}

	private boolean exists(TransferObject objeto) {
		return objeto != null && objeto.getId() != null;
	}

	private boolean exists(BaseEntity objeto) {
		return objeto != null && objeto.getId() != null;
	}
}
