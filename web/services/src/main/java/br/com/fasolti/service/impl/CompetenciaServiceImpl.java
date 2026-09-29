package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.*;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.simple.AvaliacaoSimpleTO;
import br.com.fasolti.avd.types.GraduacaoCorEnum;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.lang3.ObjectUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompetenciaServiceImpl	extends BaseAvdService<Competencia, CompetenciaTO, CompetenciaTO> implements CompetenciaService {

	private final QCompetencia qCompetencia = QCompetencia.competencia;

	private final QTipoCompetencia qTipoCompetencia = QTipoCompetencia.tipoCompetencia;

	private final QModeloGraduacao qModeloGraduacao = QModeloGraduacao.modeloGraduacao;

	@Autowired
	private CompetenciaDetalhamentoService competenciaDetalhamentoService;

    @Autowired
    private CompDetalhamentoHintService compDetalhamentoHintService;

	@Autowired
	private ModeloGraduacaoService modeloGraduacaoService;

	@Autowired
	private AvaliacaoCompetenciaService avaliacaoCompetenciaService;

	@Autowired
	private CargoCompetenciaService cargoCompetenciaService;

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private GraduacaoCompetenciaService graduacaoCompetenciaService;

	@Autowired
	private AvaliacaoCompDetHintService avaliacaoCompDetHintService;

	@Autowired
	private ModeloGraduacaoCompService modeloGraduacaoCompService;

	@Autowired
	private Mapper mapper;

	@Autowired
	private CompetenciaRepository competenciaRepository;

	@Autowired
	private  AvaliacaoRepository avaliacaoRepository;

	@Autowired
	private AvaliacaoParticipanteRepository avaliacaoParticipanteRepository;

	@Autowired
	private PreenchimentoRepository preenchimentoRepository;

	@Autowired
	private TipoCompetenciaService tipoCompetenciaService;

	@Autowired
	private CargoTipoCompetenciaService cargoTipoCompetenciaService;

	@Autowired
	private TipoCompetenciaRepository tipoCompetenciaRepository;

	@Autowired
	private GraduacaoCompetenciaRepository graduacaoCompetenciaRepository;

	@Autowired
	private ModeloGraduacaoRepository modeloGraduacaoRepository;

	@Autowired
	public CompetenciaServiceImpl(CompetenciaRepository competenciaRepository) {
		super(competenciaRepository);
	}

	@Override
	protected ServiceFilter<Competencia> getServiceFilter() {
		//FIXME Rever todos os ServiceFilter do sistema porque aparentemente foi tudo feito com copy/paste sem pensar nos casos específicos
		return ServiceFilter.create(qCompetencia, qCompetencia.empresa.id.eq(getLoggedUser().getEmpresa().getId()));
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Competencia> query) {
		query.select(Projections.fields(
			Competencia.class,
			qCompetencia.id,
			qCompetencia.situacao,
			qCompetencia.descricaoAbreviada,
			Projections.fields(
				TipoCompetencia.class,
				QTipoCompetencia.tipoCompetencia.id,
				QTipoCompetencia.tipoCompetencia.nome
			).as("tipo")
		)).join(qCompetencia.tipo, QTipoCompetencia.tipoCompetencia);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, CompetenciaTO exampleObj) {
		if (exampleObj.getSituacao() != null) {
			builder.and(qCompetencia.situacao.eq(exampleObj.getSituacao()));
		}
	}

	@Override
	protected void doBeforeSave(CompetenciaTO object) throws ServiceException {
		object.setEmpresa(getLoggedUser().getEmpresa());

		TipoCompetenciaTO tipoCompetenciaTO = tipoCompetenciaService.getById(object.getTipo().getId());
		object.setTipo(tipoCompetenciaTO);

		validar(object);
	}

	@Override
	protected void doBeforeSave(Competencia entity, CompetenciaTO object) throws br.com.fasolti.commons.lang.ServiceException {
		// FIXME Para que o código abaixo? O mapeamento deveria resolver isso
//		entity.setTipo(tipoRepository.findOne(object.getTipo().getId()));
//		entity.setModeloGraduacaoComp(modeloGraduacaoRepository.findOne(object.getModeloGraduacaoComp().getId()));
	}

	@Override
	protected void doBeforeUpdate(Competencia entity, CompetenciaTO object) throws br.com.fasolti.commons.lang.ServiceException {
		// FIXME Para que o código abaixo? O mapeamento deveria resolver isso
		object.setEmpresa(getLoggedUser().getEmpresa());
		if (object.getModeloGraduacao() == null || object.getModeloGraduacao().getId() == null) {
			object.setModeloGraduacao(null);
		} else {
			object.getModeloGraduacao().setEmpresa(getLoggedUser().getEmpresa());
		}
	}

	@Override
	protected void doBeforeUpdate(CompetenciaTO object) throws ServiceException {
		TipoCompetenciaTO tipoCompetenciaTO = tipoCompetenciaService.load(object.getTipo().getId());
		object.setTipo(tipoCompetenciaTO);
		validar(object);

		if(object.getSituacao().equals(Situacao.INATIVA)) {
			List<CargoCompetenciaTO> cargoCompetenciaTOList = cargoCompetenciaService.findCargoCompetenciaByCompetenciaAtivos(object.getId());
			cargoCompetenciaTOList.forEach(cargoCompetenciaTO -> {
				cargoCompetenciaTO.setSituacao(Situacao.INATIVA);
				cargoCompetenciaService.saveOrUpdate(cargoCompetenciaTO);
			});
		}
	}

	@Override
	protected CompetenciaTO doAfterCreate(CompetenciaTO object) throws ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return object;
	}

	@Override
	@Transactional(readOnly = true)
	public List<CompetenciaTO> findByEmpresaIdAndDescricaoAbreviadaIgnoreCase(Long idEmpresa, String descricaoAbreviada) {
		return map(competenciaRepository.findByEmpresaIdAndDescricaoAbreviadaIgnoreCase(idEmpresa, descricaoAbreviada.trim().toLowerCase()));
	}

	private void validar(CompetenciaTO competencia) {
		List<CompetenciaTO> competenciaExistente = findByEmpresaIdAndDescricaoAbreviadaIgnoreCase(getLoggedUser().getEmpresa().getId(), competencia.getDescricaoAbreviada());
		//if (competenciaExistente != null &&  !competenciaExistente.getId().equals(competencia.getId()) && competenciaExistente.getDescricaoAbreviada().equalsIgnoreCase(competencia.getDescricaoAbreviada())) {
		if (competenciaExistente != null && !competenciaExistente.isEmpty()) {
			if (!competenciaExistente.get(0).getId().equals(competencia.getId())) {
				//FIXME: Passar a entidade para internacionalização
				throw new ValidationException("validation.duplicate", "competência", "field.descricaoAbreviada", competencia.getDescricaoAbreviada());
			}
		}
	}

	@Override
	public List<CompetenciaPreenchimentoTO> listByParticipante(ParticipanteTO participante) {
		List<CompetenciaPreenchimentoTO> competencias = new ArrayList<>();

		List<PreenchimentoTO> preenchimentos = preenchimentoService.listByParticipante(participante);

		Map<Long, CompetenciaPreenchimentoTO> mapa = new HashMap<>();

		for(PreenchimentoTO preench : preenchimentos){

			if (preench.getGraduacaoAv3Id() == null) {
				if (preench.getGraduacaoAv1Id() != null && preench.getGraduacaoAv2Id() != null && preench.getGraduacaoAv1Id().equals(preench.getGraduacaoAv2Id())) {
					preench.setGraduacaoAv3Id(preench.getGraduacaoAv2Id());
				}
			}

			CompetenciaPreenchimentoTO competenciaPreenchimento;

			Long idCompetencia = preench.getAvaliacaoCompDet().getAvaliacaoCompetencia().getIdCompetencia();

			if(mapa.containsKey(idCompetencia)){
				competenciaPreenchimento = mapa.get(idCompetencia);
			} else {
				competenciaPreenchimento = new CompetenciaPreenchimentoTO();
				competenciaPreenchimento.setId(idCompetencia);
				competenciaPreenchimento.setDescricaoAbreviada(preench.getAvaliacaoCompDet().getAvaliacaoCompetencia().getDescricaoAbreviada());
				competenciaPreenchimento.setDirecionamentoGestor(preench.getAvaliacaoCompDet().getAvaliacaoCompetencia().getDirecionamentoGestor());
				competenciaPreenchimento.setDirecionamentoNaoGestor(preench.getAvaliacaoCompDet().getAvaliacaoCompetencia().getDirecionamentoNaoGestor());
				competenciaPreenchimento.setAcaoPDI(participante.getPdi());
				competenciaPreenchimento.setComentarioFinalAvaliado(participante.getComentarioFinalAvaliado());
				competenciaPreenchimento.setComentarioFinalAvaliador(participante.getComentarioFinalAvaliador());

				ModeloGraduacaoCompTO modeloGraduacaoComp = modeloGraduacaoCompService.getByAvaliacaoCompetenciaId(preench.getAvaliacaoCompDet().getAvaliacaoCompetencia().getId());

				if (modeloGraduacaoComp != null) {
					modeloGraduacaoComp = modeloGraduacaoCompService.fullObject(modeloGraduacaoComp);
					competenciaPreenchimento.setModeloGraduacao(modeloGraduacaoComp);
				}

				competenciaPreenchimento.setEmpresa(getLoggedUser().getEmpresa());
				competenciaPreenchimento.setDetalhamentos(new ArrayList<>());

				mapa.put(idCompetencia, competenciaPreenchimento);
			}

			AvaliacaoCompDetTO detalhamento = preench.getAvaliacaoCompDet().clone();
			List<AvaliacaoCompDetHintTO> listaHints = avaliacaoCompDetHintService.listByAvaliacaoCompDetId(preench.getAvaliacaoCompDet().getId());

			ModeloGraduacaoCompTO modeloGraduacaoComp = modeloGraduacaoCompService.obterModeloGraduacaoCompProjections(preench.getAvaliacaoCompDet().getIdModeloGraduacao(), preench.getParticipante().getAvaliacao().getId());

			//FIXME: Trocar esse set cor posteriormente!
			for (AvaliacaoCompDetHintTO hintTO : listaHints) {
				GraduacaoCompetenciaTO graduacaoTO = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacao(modeloGraduacaoComp.getId(), hintTO.getIdGraduacao());
				graduacaoTO.setCor(GraduacaoCorEnum.valueOf((graduacaoTO.getOrdem()==null || graduacaoTO.getOrdem() > 10)?11:graduacaoTO.getOrdem()).getNome());
				hintTO.setGraduacao(graduacaoTO);
			}

			Collections.sort(listaHints, new Comparator<AvaliacaoCompDetHintTO>() {
				@Override
				public int compare(AvaliacaoCompDetHintTO o1, AvaliacaoCompDetHintTO o2) {
					return ObjectUtils.compare(o1.getGraduacao().getOrdem(), o2.getGraduacao().getOrdem());
				}
			});

			detalhamento.setHints(listaHints);
			detalhamento.setPreenchimentos(new ArrayList<>());
			detalhamento.getPreenchimentos().add(preench);
			competenciaPreenchimento.getDetalhamentos().add(detalhamento);

		}

		for(Long idCompetencia : mapa.keySet()){

			CompetenciaPreenchimentoTO competenciaPreenchimento = mapa.get(idCompetencia);

			Collections.sort(competenciaPreenchimento.getDetalhamentos(), new Comparator<AvaliacaoCompDetTO>() {
				@Override
				public int compare(AvaliacaoCompDetTO o1, AvaliacaoCompDetTO o2) {
					int result = o1.getOrdem().compareTo(o2.getOrdem());
					return result;
				}
			});

			competencias.add(mapa.get(idCompetencia));
		}

		return competencias;
	}

	@Override
	public List<AvaliacaoSimpleTO> listAvaliacaoByCompetencia(CompetenciaTO competencia) {
		return avaliacaoCompetenciaService.listByIdCompetencia(competencia.getId())
			.stream()
			.map(avaliacaoComp -> mapper.map(avaliacaoComp.getAvaliacao(), AvaliacaoSimpleTO.class))
			.collect(Collectors.toList());
	}

	@Override
	public List<CompetenciaAvaliacao> listCompetenciaAvaliacaoByAvaliadorAndPeriodo(FuncionarioTO avaliador, Date periodo) {
		List<CompetenciaAvaliacao> listaCompetenciaAvaliacao = new ArrayList<>();

		List<Object[]> listaAvaliacoesQuery = avaliacaoRepository.obterAvaliacoesUsuarioLogado(avaliador.getId());

		for (Object[] registro : listaAvaliacoesQuery) {
			//A chave do mapa guarda o id da competência e valor guarda a lista de avaliados da competência.
			Map<Long, Set<Long>> mapaParticipantesCompetencias = new HashMap<>();


			Long idAvaliacao = Long.parseLong(registro[0].toString());

			List<Preenchimento> listaPreenchimento = preenchimentoRepository.findByAvaliadorAndLimiteAv2AndAvaliacao(avaliador.getId(), periodo, SituacaoAvaliacao.CONCLUIDA, idAvaliacao);


			for (Preenchimento preenchimento : listaPreenchimento) {

				CompetenciaAvaliacao search = new CompetenciaAvaliacao();
				search.setDescricaoCompetencia(preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia().getDescricaoAbreviada());
				search.setIdAvaliacao(idAvaliacao);
				search.setIdCompetencia(preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia().getIdCompetencia());


				if (!listaCompetenciaAvaliacao.contains(search)) {
					CompetenciaAvaliacao compAval = createCompetenciaAvaliacao(preenchimento.getParticipante(), idAvaliacao, preenchimento, avaliador.getId());

					listaCompetenciaAvaliacao.add(compAval);

					Set<Long> avaliados = new HashSet<>();
					avaliados.add(preenchimento.getParticipante().getAvaliado().getId());

					mapaParticipantesCompetencias.put(compAval.getIdCompetencia(), avaliados);
				} else {

					int indiceCompExistente = listaCompetenciaAvaliacao.indexOf(search);
					CompetenciaAvaliacao compAvalExistente = listaCompetenciaAvaliacao.get(indiceCompExistente);

					if (preenchimento.getParticipante() != null && preenchimento.getParticipante().getAvaliado() != null && preenchimento.getParticipante().getAvaliado().getId().equals(avaliador.getId())) {
						if (preenchimento.getGraduacaoAv1() != null)
							compAvalExistente.setQtdRespostas(compAvalExistente.getQtdRespostas() + 1);

						if (compAvalExistente.getTodosConsensosLiberados() != false && preenchimento.getParticipante().getConsensoLiberado() == false) {
							compAvalExistente.setTodosConsensosLiberados(preenchimento.getParticipante().getConsensoLiberado());
						}
					} else if (preenchimento.getParticipante() != null && preenchimento.getParticipante().getAvaliado() != null && !preenchimento.getParticipante().getAvaliado().getId().equals(avaliador.getId())) {
						if (preenchimento.getGraduacaoAv2() != null)
							compAvalExistente.setQtdRespostas(compAvalExistente.getQtdRespostas() + 1);

						if (compAvalExistente.getTodosConsensosLiberados() != false && preenchimento.getParticipante().getConsensoLiberado() == false) {
							compAvalExistente.setTodosConsensosLiberados(preenchimento.getParticipante().getConsensoLiberado());
						}
					}

					compAvalExistente.setQtdPerguntas(compAvalExistente.getQtdPerguntas() + 1);

					Set<Long> avaliados = mapaParticipantesCompetencias.get(compAvalExistente.getIdCompetencia());
					if (!avaliados.contains(preenchimento.getParticipante().getAvaliado().getId())) {
						compAvalExistente.setQtdAvaliados(compAvalExistente.getQtdAvaliados() + 1);
						avaliados.add(preenchimento.getParticipante().getAvaliado().getId());
					}

				}

			}
		}

		return listaCompetenciaAvaliacao;
	}

	@Override
	@Cacheable("competencia_projections")
	public CompetenciaTO obterCompetenciaProjections(Long id) {
		return map(
			select(
				Projections.fields(
					Competencia.class,
					qCompetencia.id,
					qCompetencia.descricaoAbreviada,
					qCompetencia.direcionamentoGestor,
					qCompetencia.direcionamentoNaoGestor,
					qCompetencia.atualizador,
					qCompetencia.dataAtualizacao,
					qCompetencia.criador,
					qCompetencia.dataCriacao,
					qCompetencia.situacao,
					Projections.fields(TipoCompetencia.class,
						qTipoCompetencia.id,
						qTipoCompetencia.descricao,
						qTipoCompetencia.nome
					).as("tipo"),
					Projections.fields(ModeloGraduacao.class,
						qModeloGraduacao.id
					).as("modeloGraduacao")
				)
			)
				.join(qCompetencia.tipo, qTipoCompetencia)
				.leftJoin(qCompetencia.modeloGraduacao, qModeloGraduacao)
				.where(qCompetencia.id.eq(id))
				.fetchOne()
		);
	}

	@Override
	public List<CompetenciaTO> buscarCompetenciasAtivas() {
		QTipoCompetencia qTipoCompetencia = QTipoCompetencia.tipoCompetencia;

		return map(select(Projections.fields(
			Competencia.class,
			qCompetencia.id,
			qCompetencia.descricaoAbreviada,
			qCompetencia.direcionamentoGestor,
			qCompetencia.direcionamentoNaoGestor,
			qCompetencia.situacao,
			Projections.fields(TipoCompetencia.class,
				qTipoCompetencia.id,
				qTipoCompetencia.nome,
				qTipoCompetencia.nomeCurto,
				qTipoCompetencia.situacao).as("tipo")
		)).join(qCompetencia.tipo, qTipoCompetencia).where(
			new BooleanBuilder().and(qCompetencia.situacao.eq(Situacao.ATIVA)).and(qCompetencia.empresa.id.eq(getLoggedUser().getEmpresa().getId()))
		).fetch());
	}

	@Override
	public List<CompetenciaTO> buscarCompetenciasAtivasPorTipoCompetencia(Long id) {
		return map(select(Projections.fields(
			Competencia.class,
			qCompetencia.id,
			qCompetencia.descricaoAbreviada,
			qCompetencia.direcionamentoGestor,
			qCompetencia.direcionamentoNaoGestor,
			qCompetencia.situacao,
			Projections.fields(TipoCompetencia.class,
				qTipoCompetencia.id,
				qTipoCompetencia.nome,
				qTipoCompetencia.nomeCurto,
				qTipoCompetencia.situacao).as("tipo")
		)).join(qCompetencia.tipo, qTipoCompetencia).where(
			new BooleanBuilder().and(qCompetencia.situacao.eq(Situacao.ATIVA)).and(qCompetencia.empresa.id.eq(getLoggedUser().getEmpresa().getId()).and(qCompetencia.tipo.id.eq(id)))
		).fetch());
	}

	private void updateQtdAvaliados(List<CompetenciaAvaliacao> competencias, Set<String> competenciasDoParticipante) {
		for (String nomeCompetencia : competenciasDoParticipante){
			CompetenciaAvaliacao comp = findCompetenciaAvaliacao(competencias, nomeCompetencia);

			if(comp != null)
				comp.setQtdAvaliados(comp.getQtdAvaliados() == null ? 1 : comp.getQtdAvaliados()+1);
		}
	}

	private CompetenciaAvaliacao createCompetenciaAvaliacao(Participante part, Long idAvaliacao, Preenchimento preench, Long idAvaliador) {

		CompetenciaAvaliacao compAval = new CompetenciaAvaliacao();

		compAval.setIdCompetencia(preench.getAvaliacaoCompDet().getAvaliacaoCompetencia().getIdCompetencia());
		compAval.setDescricaoCompetencia(preench.getAvaliacaoCompDet().getAvaliacaoCompetencia().getDescricaoAbreviada());

		String nomeTipoCompetencia = null;
		TipoCompetencia tipoCompetencia = tipoCompetenciaRepository.findOne(preench.getAvaliacaoCompDet().getAvaliacaoCompetencia().getIdTipoCompetencia());
		if (tipoCompetencia != null) {
			nomeTipoCompetencia = tipoCompetencia.getNome();
		} else {
			nomeTipoCompetencia = preench.getAvaliacaoCompDet().getAvaliacaoCompetencia().getDescricaoTipoCompetencia();
		}
		compAval.setTipoCompetencia(nomeTipoCompetencia);
		compAval.setIdAvaliacao(idAvaliacao);
		compAval.setDescricaoAvaliacao(part.getAvaliacao().getDescricaoAbreviada());
		compAval.setIdAvaliador(part.getAvaliador().getId());
		compAval.setQtdAvaliados(1);
		compAval.setTodosConsensosLiberados(part.getConsensoLiberado());
		if (part.getPerfilAgente() != null) {
			compAval.setIdPerfil(part.getPerfilAgente().getCode().longValue());
		}
		compAval.setQtdPerguntas(1);

		if (part.getAvaliado().getId().equals(idAvaliador) && preench.getGraduacaoAv1() != null) {
			compAval.setQtdRespostas(1);
		} else if (!part.getAvaliado().getId().equals(idAvaliador) && preench.getGraduacaoAv2() != null) {
			compAval.setQtdRespostas(1);
		} else {
			compAval.setQtdRespostas(0);
		}

		return compAval;
	}

	private CompetenciaAvaliacao findCompetenciaAvaliacao(List<CompetenciaAvaliacao> listaCompetencias, String nomeCompetencia) {
		CompetenciaAvaliacao search = new CompetenciaAvaliacao();
		search.setDescricaoCompetencia(nomeCompetencia);

		int index = listaCompetencias.indexOf(search);

		if(index > -1) {
			CompetenciaAvaliacao compAval = listaCompetencias.get(index);
			return compAval;
		}

		return null;
	}

	@Override
	public List<CompetenciaTO> obterCompetenciasPorTipoDeCompetencia(Long idTipoCompetencia) {
		List<Competencia> listaCompetencias = select(
			Projections.fields(Competencia.class, qCompetencia.id, qCompetencia.descricaoAbreviada, qCompetencia.situacao))
			.where(qCompetencia.tipo.id.eq(idTipoCompetencia))
			.orderBy(qCompetencia.descricaoAbreviada.asc()).fetch();
		return map(listaCompetencias);
	}

	@Override
	public List<CompetenciaPorTipoCompTreeTO> obterArvoreDeCompetenciaPorTipoCompetencia() {
		List<ArvoreTipoCompetenciaTO> listArvoreTipos = tipoCompetenciaService.obterArvoreDeTiposDeCompetencia();

		List<CompetenciaPorTipoCompTreeTO> listaArvore = new ArrayList<>();

		for (ArvoreTipoCompetenciaTO arvoreTipoCompetenciaTO : listArvoreTipos) {
			CompetenciaPorTipoCompTreeTO competenciaPorTipoCompTreeTO = new CompetenciaPorTipoCompTreeTO();
			competenciaPorTipoCompTreeTO.setNome(arvoreTipoCompetenciaTO.getNome());
			competenciaPorTipoCompTreeTO.setIdTipoCompetencia(arvoreTipoCompetenciaTO.getId());
			competenciaPorTipoCompTreeTO.setTipoCompetencia(Boolean.TRUE);
			competenciaPorTipoCompTreeTO.setChildren(new ArrayList<>());

			for (CompetenciaTO competencia : obterCompetenciasPorTipoDeCompetencia(arvoreTipoCompetenciaTO.getId())) {
				competenciaPorTipoCompTreeTO.getChildren().add(new CompetenciaPorTipoCompTreeTO(competencia.getDescricaoAbreviada(), competencia.getId()));
			}

			montarArvoreCompetenciaPorTipoCompTreeTO(arvoreTipoCompetenciaTO, competenciaPorTipoCompTreeTO);
			listaArvore.add(competenciaPorTipoCompTreeTO);
		}

		return listaArvore;
	}

	private void montarArvoreCompetenciaPorTipoCompTreeTO(ArvoreTipoCompetenciaTO arvoreTipoComTO, CompetenciaPorTipoCompTreeTO competenciaPorTipoTO) {
		if (arvoreTipoComTO.getChildren() == null) {
			return;
		}

		for (ArvoreTipoCompetenciaTO arvoreTipoCompetenciaTO : arvoreTipoComTO.getChildren()) {
			CompetenciaPorTipoCompTreeTO competenciaPorTipoCompTreeTO = new CompetenciaPorTipoCompTreeTO();
			competenciaPorTipoCompTreeTO.setNome(arvoreTipoCompetenciaTO.getNome());
			competenciaPorTipoCompTreeTO.setIdTipoCompetencia(arvoreTipoCompetenciaTO.getId());
			competenciaPorTipoCompTreeTO.setTipoCompetencia(Boolean.TRUE);
			competenciaPorTipoCompTreeTO.setChildren(new ArrayList<>());

			competenciaPorTipoTO.getChildren().add(competenciaPorTipoCompTreeTO);

			for (CompetenciaTO competencia : obterCompetenciasPorTipoDeCompetencia(arvoreTipoComTO.getId())) {
				competenciaPorTipoTO.getChildren().add(new CompetenciaPorTipoCompTreeTO(competencia.getDescricaoAbreviada(), competencia.getId()));
			}

			montarArvoreCompetenciaPorTipoCompTreeTO(arvoreTipoCompetenciaTO, competenciaPorTipoCompTreeTO);
		}
	}

	@Override
	public CompetenciaPorTipoCompTreeTO obterHierarquiaDeTiposDeCompetenciaDeUmaCompetencia(Long idCompetencia) {
		Competencia competencia = competenciaRepository.findOne(idCompetencia);

		CompetenciaPorTipoCompTreeTO competenciaPorTipoCompTreeTO = new CompetenciaPorTipoCompTreeTO();

		competenciaPorTipoCompTreeTO = montarHerarquiaDeTipoCompetenciaESuaCompetencia(competencia.getTipo(), competenciaPorTipoCompTreeTO);

		adicionarCompetenciaNaArvoreDeTiposCompetencias(competenciaPorTipoCompTreeTO, competencia);

		return competenciaPorTipoCompTreeTO;
	}

	private CompetenciaPorTipoCompTreeTO montarHerarquiaDeTipoCompetenciaESuaCompetencia(TipoCompetencia tipo, CompetenciaPorTipoCompTreeTO compTreeTO) {
		CompetenciaPorTipoCompTreeTO competenciaPorTipoCompTreeTO = new CompetenciaPorTipoCompTreeTO();

		if (tipo.getIdPai() != null) {
			CompetenciaPorTipoCompTreeTO pai = montarHerarquiaDeTipoCompetenciaESuaCompetencia(tipoCompetenciaRepository.findOne(tipo.getIdPai()), compTreeTO);
			competenciaPorTipoCompTreeTO.setNome(tipo.getNome());
			competenciaPorTipoCompTreeTO.setIdTipoCompetencia(tipo.getId());
			competenciaPorTipoCompTreeTO.setTipoCompetencia(Boolean.TRUE);
			competenciaPorTipoCompTreeTO.setChildren(new ArrayList<>());
			pai.getChildren().add(competenciaPorTipoCompTreeTO);
			return pai;
		} else {
			compTreeTO.setNome(tipo.getNome());
			compTreeTO.setIdTipoCompetencia(tipo.getId());
			compTreeTO.setTipoCompetencia(Boolean.TRUE);
			compTreeTO.setChildren(new ArrayList<>());
			return compTreeTO;
		}
	}

	private void adicionarCompetenciaNaArvoreDeTiposCompetencias(CompetenciaPorTipoCompTreeTO competenciaPorTipoCompTreeTO, Competencia competencia) {
		CompetenciaPorTipoCompTreeTO compTreeTO = new CompetenciaPorTipoCompTreeTO();
		if (competenciaPorTipoCompTreeTO.getChildren().isEmpty()) {
			compTreeTO.setNome(competencia.getDescricaoAbreviada());
			compTreeTO.setIdTipoCompetencia(competencia.getId());
			compTreeTO.setTipoCompetencia(Boolean.FALSE);
			compTreeTO.setChildren(new ArrayList<>());
			competenciaPorTipoCompTreeTO.getChildren().add(compTreeTO);
		} else {
			adicionarCompetenciaNaArvoreDeTiposCompetencias(competenciaPorTipoCompTreeTO.getChildren().get(0), competencia);
		}
	}

	@Override
	public List<CompetenciaPorTipoCompTreeTO> obterArvoreDeTiposDeCompetenciaComCompetencias() {
		List<ArvoreTipoCompetenciaTO> arvorePai = new ArrayList<>();
		List<TipoCompetenciaTO> tiposQueNaotemPai = tipoCompetenciaService.obterTipoCompetenciasPai();

		Collections.sort(tiposQueNaotemPai, new Comparator<TipoCompetenciaTO>() {
			@Override
			public int compare(TipoCompetenciaTO o1, TipoCompetenciaTO o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		});

		// Transformando a lista em TO
		for (TipoCompetenciaTO pai : tiposQueNaotemPai) {
			arvorePai.add(mapper.map(pai, ArvoreTipoCompetenciaTO.class));
		}

		// Para cada pai, trazer filhos
		for (ArvoreTipoCompetenciaTO cargoTreeTOPai : arvorePai) {
			List<TipoCompetenciaTO> listaFilhos = tipoCompetenciaService.obterTiposCompetenciasFilhosAtivos(cargoTreeTOPai.getId());

			List<ArvoreTipoCompetenciaTO> listaFilhosTreeTO = new ArrayList<>();

			Collections.sort(listaFilhos, new Comparator<TipoCompetenciaTO>() {
				@Override
				public int compare(TipoCompetenciaTO o1, TipoCompetenciaTO o2) {
					return o1.getNome().compareTo(o2.getNome());
				}
			});

			for (TipoCompetenciaTO pai : listaFilhos) {
				listaFilhosTreeTO.add(mapper.map(pai, ArvoreTipoCompetenciaTO.class));
			}

			cargoTreeTOPai.setChildren(listaFilhosTreeTO);

			tipoCompetenciaService.obterFilhosCargoPai(cargoTreeTOPai);
		}

		List<CompetenciaPorTipoCompTreeTO> listaCompleta = new ArrayList<CompetenciaPorTipoCompTreeTO>();

		for (ArvoreTipoCompetenciaTO arvoreTipoCompetenciaTO : arvorePai) {
			CompetenciaPorTipoCompTreeTO competenciaPorTipoCompTreeTO = new CompetenciaPorTipoCompTreeTO(arvoreTipoCompetenciaTO.getNome(), null, arvoreTipoCompetenciaTO.getId(), Boolean.TRUE);
			listaCompleta.add(competenciaPorTipoCompTreeTO);
			ajustarFilhosTipoCompetenciaECompetencia(competenciaPorTipoCompTreeTO, arvoreTipoCompetenciaTO.getChildren());
		}

		listaCompleta.sort(new Comparator<CompetenciaPorTipoCompTreeTO>() {
			@Override
			public int compare(CompetenciaPorTipoCompTreeTO o1, CompetenciaPorTipoCompTreeTO o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		});

		return listaCompleta;
	}

	private void ajustarFilhosTipoCompetenciaECompetencia(CompetenciaPorTipoCompTreeTO competenciaPorTipoCompTreeTOPai, List<ArvoreTipoCompetenciaTO> children) {
		competenciaPorTipoCompTreeTOPai.setChildren(new ArrayList<>());

		for (ArvoreTipoCompetenciaTO arvoreTipoCompetenciaTO : children) {
			CompetenciaPorTipoCompTreeTO competenciaPorTipoCompTreeTO = new CompetenciaPorTipoCompTreeTO(arvoreTipoCompetenciaTO.getNome(), null, arvoreTipoCompetenciaTO.getId(), Boolean.TRUE);
			competenciaPorTipoCompTreeTOPai.getChildren().add(competenciaPorTipoCompTreeTO);
			ajustarFilhosTipoCompetenciaECompetencia(competenciaPorTipoCompTreeTO, arvoreTipoCompetenciaTO.getChildren());
		}

		competenciaPorTipoCompTreeTOPai.getChildren().sort(new Comparator<CompetenciaPorTipoCompTreeTO>() {
			@Override
			public int compare(CompetenciaPorTipoCompTreeTO o1, CompetenciaPorTipoCompTreeTO o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		});

		for (CompetenciaTO competenciaTO : obterCompeTenciaPorTipoDeCompetencia(competenciaPorTipoCompTreeTOPai.getIdTipoCompetencia())) {
			competenciaPorTipoCompTreeTOPai.getChildren().add(new CompetenciaPorTipoCompTreeTO(competenciaTO.getDescricaoAbreviada(), competenciaTO.getId(), null, Boolean.FALSE));
		}
	}

	public List<CompetenciaTO> obterCompeTenciaPorTipoDeCompetencia(Long idTipoCompetencia) {
		return map(select().where(qCompetencia.tipo.id.eq(idTipoCompetencia).and(qCompetencia.empresa.id.eq(getLoggedUser().getEmpresa().getId()).and(qCompetencia.situacao.eq(Situacao.ATIVA)))).fetch());
	}

	@Override
	protected OrderSpecifier<?>[] defaultOrder() {
		OrderSpecifier[] orderSpecifier = {new OrderSpecifier(Order.ASC, QCompetencia.competencia.descricaoAbreviada)};
		return orderSpecifier;
	}
}

