package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.*;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.campanhasrapidas.HomeOfficeBrasilTO;
import br.com.fasolti.avd.transfer.mobile.AvaliacaoMobileTO;
import br.com.fasolti.avd.transfer.mobile.AvaliacaoSyncMobileTO;
import br.com.fasolti.avd.transfer.search.AvaliacaoSearchTO;
import br.com.fasolti.avd.transfer.simple.CargoSimpleTO;
import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.types.*;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.service.util.BaseAvdService;
import br.com.fasolti.service.util.CalculadorDatas;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.apache.commons.collections4.ListUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.concurrent.DelegatingSecurityContextRunnable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AvaliacaoServiceImpl
	extends BaseAvdService<Avaliacao, AvaliacaoTO, AvaliacaoSearchTO>
	implements AvaliacaoService {

	private static final QAvaliacao qAvaliacao = QAvaliacao.avaliacao;
	private static final QCampanha qCampanha = QCampanha.campanha;

	@Value("${homeoffice.empresaId}")
	private Long empresaId;

	@Value("${homeoffice.deptoId}")
	private Long deptoId;

	@Value("${homeoffice.cargoId}")
	private Long cargoId;

	@Value("${homeoffice.unidadeId}")
	private Long unidadeId;

	@Value("${homeoffice.avaliacaoId}")
	private Long avaliacaoId;

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private AvaliacaoParticipanteRepository avaliacaoParticipanteRepository;

	@Autowired
	private MensageriaService mensageriaService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private DepartamentoService departamentoService;

	@Autowired
	private AvaliacaoParticipanteService avaliacaoParticipanteService;

	@Autowired
	private CargoCompetenciaService cargoCompetenciaService;

	@Autowired
	private DetalhamentoCargoCompetenciaService detalhamentoCargoCompetenciaService;

	@Autowired
	private AvaliacaoTipoCompetenciaRepository avaliacaoTipoCompetenciaRepository;

	@Autowired
	private AvaliacaoTipoCompetenciaService avaliacaoTipoCompetenciaService;

	@Autowired
	private TipoCompetenciaRepository tipoCompetenciaRepository;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private UnidadeService unidadeService;

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	private Mapper mapper;

	@Autowired
	private AvaliadorService avaliadorService;

	@Autowired
	private PendenciaService pendenciaService;

	@Autowired
	private PendenciaRepository pendenciaRepository;

	@Autowired
	private AvaliacaoDimensaoService avaliacaoDimensaoService;

	@Autowired
	private DimensaoService dimensaoService;

	@Autowired
	private DimensaoCompetenciaDetalhamentoService dimensaoCompetenciaDetalhamentoService;

	@Autowired
	private PreenchimentoRepository preenchimentoRepository;

	@Autowired
	private AvaliacaoCompetenciaRepository avaliacaoCompetenciaRepository;

	@Autowired
	private AvaliacaoCompDetRepository avaliacaoCompDetRepository;

	@Autowired
	private CargoTipoCompetenciaRepository cargoTipoCompetenciaRepository;

	@Autowired
	private AvaliacaoDetCargoCompRepository avaliacaoDetCargoCompRepository;

	@Autowired
	private ModeloGraduacaoCompRepository modeloGraduacaoCompRepository;

	@Autowired
	private ModeloGraduacaoAvalCompRepository modeloGraduacaoAvalCompRepository;

	@Autowired
	private AvaliacaoCompDetHintRepository avaliacaoCompDetHintRepository;

	@Autowired
	private AvaliacaoCompDetHintService avaliacaoCompDetHintService;

	@Autowired
	private CompDetalhamentoHintService compDetalhamentoHintService;

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
	private GraduacaoCompetenciaRepository graduacaoCompetenciaRepository;

	@Autowired
	private CampanhaRepository campanhaRepository;

	@Autowired
	private AvalPesoCargoCompRepository avalPesoCargoCompRepository;

	@Autowired
	private CargoCompetenciaRepository cargoCompetenciaRepository;


	@Autowired
	public AvaliacaoServiceImpl(AvaliacaoRepository avaliacaoRepository) {
		super(avaliacaoRepository);
	}

	@Override
	protected void setDefaultValues(AvaliacaoTO object) throws ServiceException {
		object.setEmpresa(getLoggedUser().getEmpresa());
		object.setStatus(SituacaoAvaliacao.AG_LIBERACAO); // TODO Implementar comportamento do TransactionalObject
		object.setSituacaoPreenchimento(SituacaoPreenchimento.NAO_PREENCH);
	}

	@Override
	protected void doAfterSearch(List<Avaliacao> entities, List<AvaliacaoSearchTO> result) throws ServiceException {
		result.forEach(avaliacaoSearch -> {
				avaliacaoSearch.setNumAvaliacoes(avaliacaoParticipanteService.obterTotalAvaliacoesPorAvaliacao(avaliacaoSearch));
				List<ParticipanteTO> avaliados = avaliacaoParticipanteService.obterAvaliadosPorAvaliacao(avaliacaoSearch.getId());
				if (avaliados != null && !avaliados.isEmpty())
					avaliacaoSearch.setNumParticipantes(Long.valueOf(avaliados.size()));
				else
					avaliacaoSearch.setNumParticipantes(0L);
			}
		);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, AvaliacaoSearchTO exampleObj) {
		if (exampleObj.getCampanha() != null && exampleObj.getCampanha().getId() != null) {
			builder.and(qAvaliacao.campanha.id.eq(exampleObj.getCampanha().getId()));
		}

		if (exampleObj.getListaCampanha() != null && !exampleObj.getListaCampanha().isEmpty()) {
			builder.and(qAvaliacao.campanha.id.in(exampleObj.getListaCampanha()));
		}
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Avaliacao> query) {
		query.select(Projections.fields(Avaliacao.class,
			qAvaliacao.id,
			qAvaliacao.descricao,
			qAvaliacao.descricaoAbreviada,
			qAvaliacao.situacao,
			Projections.fields(Campanha.class,
				qCampanha.id,
				qCampanha.descricao,
				qCampanha.situacao).as("campanha"),
			qAvaliacao.preencheAv1,
			qAvaliacao.preencheAv2,
			qAvaliacao.preencheAv3,
			qAvaliacao.limiteAv1,
			qAvaliacao.limiteAv2,
			qAvaliacao.limiteAv3,
			qAvaliacao.gestores,
			qAvaliacao.subordinados,
			qAvaliacao.pares,
			qAvaliacao.autoavaliacao
		));
	}

	@Override
	protected void doBeforeTransition(AvaliacaoTO avaliacao, Enum status) throws ServiceException {
		// TODO Validar se pode transicionar

		switch ((SituacaoAvaliacao) status) {
			case LIBERADA:
				avaliacao.setSituacaoPreenchimento(SituacaoPreenchimento.NAO_PREENCH);
				break;
		}
	}

	@Override
	protected void doAfterTransition(AvaliacaoTO avaliacao, Enum status) throws ServiceException {
		switch ((SituacaoAvaliacao) status) {
			case LIBERADA:
				//liberarAvaliacao(avaliacao);
				break;
			case CANCELADA:
				// TODO Verificar as ações que devem ser feitas após o cancelamento
				// FIXME: Provisótio, pois a chamada $transition não tá funcionando.
				Avaliacao avaliacaoEntity = avaliacaoRepository.findOne(avaliacao.getId());
				avaliacaoEntity.setStatus(SituacaoAvaliacao.CANCELADA);
				avaliacaoRepository.save(avaliacaoEntity);
				break;
			case CONCLUIDA:
				Avaliacao avaliacaoEnt = avaliacaoRepository.findOne(avaliacao.getId());
				avaliacaoEnt.setStatus(SituacaoAvaliacao.CONCLUIDA);
				avaliacaoRepository.save(avaliacaoEnt);
				break;
		}
	}

	@Override
	protected void doBeforeSave(AvaliacaoTO object) throws ServiceException {
		validar(object);
	}

	@Override
	protected void doBeforeSave(Avaliacao entity, AvaliacaoTO to) throws ServiceException {
		super.doBeforeSave(entity, to);

		/*
		 * A tela de preenchimento precisa desses campos preenchidos para funcionar corretamente. Por isso
		 * a necessidade de prencher esses campos.
		 */
		entity.setPreencheAv1(entity.getPreencheAv1() == null ? Boolean.FALSE : entity.getPreencheAv1());
		entity.setPreencheAv2(entity.getPreencheAv2() == null ? Boolean.FALSE : entity.getPreencheAv2());
		entity.setPreencheAv3(entity.getPreencheAv3() == null ? Boolean.FALSE : entity.getPreencheAv3());
		entity.setPreencheAv4(entity.getPreencheAv4() == null ? Boolean.FALSE : entity.getPreencheAv4());

		entity.setAutoavaliacao(entity.getAutoavaliacao() == null ? Boolean.FALSE : entity.getAutoavaliacao());
		entity.setGestores(entity.getGestores() == null ? Boolean.FALSE : entity.getGestores());
		entity.setSubordinados(entity.getSubordinados() == null ? Boolean.FALSE : entity.getSubordinados());
		entity.setPares(entity.getPares() == null ? Boolean.FALSE : entity.getPares());
	}

	@Override
	protected void doAfterSave(Avaliacao entity, AvaliacaoTO object) throws ServiceException {
		AvaliacaoTO originalTO = new AvaliacaoTO();
		originalTO.setId(entity.getId());

		//Neste momento estamos gravando os tipos de competências e os agentes que a avaliação deve ter.
		criarTipoCompetencia(entity, object);

		AvaliacaoTO avaliacao = map(entity);
		if (object.getParticipantes() != null && !object.getParticipantes().isEmpty()) {
			List<Long> listAvaliados = new ArrayList<>();
			List<ParticipanteTO> list = new ArrayList<>();

			object.getParticipantes().forEach(participante -> {
				if (!listAvaliados.contains(participante.getAvaliado().getId())) {
					list.add(participante);
				}
			});
			object.setParticipantes(list);
			try {
				salvarParticipantes(object);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		if (object.getListaDimensao() != null && !object.getListaDimensao().isEmpty()) {
			object.getListaDimensao().forEach(avaliacaoDimensao -> {
				avaliacaoDimensao.setAvaliacao(object);
				avaliacaoDimensaoService.save(avaliacaoDimensao);
			});
		}

	}

	private void criarTipoCompetencia(Avaliacao entity, AvaliacaoTO object) {
		object.getAgentes().forEach(tipoCompetencia -> {
			if (tipoCompetencia.getId() == null) {
				AvaliacaoTipoCompetencia avaliacaoTipoCompetencia = new AvaliacaoTipoCompetencia();
				avaliacaoTipoCompetencia.setAvaliacao(entity);
				avaliacaoTipoCompetencia.setTipoCompetencia(tipoCompetenciaRepository.findOne(tipoCompetencia.getTipoCompetencia().getId()));
				avaliacaoTipoCompetencia.setPerfilAgente(TipoAvaliacaoPerfil.valueOf(tipoCompetencia.getPerfilAgente().getCode()));
				avaliacaoTipoCompetencia.setPeso(tipoCompetencia.getPeso());
				avaliacaoTipoCompetenciaRepository.save(avaliacaoTipoCompetencia);
			}
		});
	}

	@Override
	protected void doBeforeUpdate(AvaliacaoTO object) throws ServiceException {
		validar(object);
	}

	@Override
	protected void doBeforeUpdate(Avaliacao entity, AvaliacaoTO object) throws ServiceException {
		//entity.setPreencheAv2(true);
		//entity.setGestores(true);
		entity.setSubordinados(entity.getSubordinados() == null ? Boolean.FALSE : entity.getSubordinados());
		entity.setPares(entity.getPares() == null ? Boolean.FALSE : entity.getPares());

		// Não permite atualizar uma avaliação cancelada
		if (SituacaoAvaliacao.CANCELADA.equals(entity.getStatus())) {
			throw new ValidationException("validation.canceled", "avaliacao", "field.situacao", object.getStatus().name());
		}
		//FIXME: Isso é feito para não mostrar a avaliação inativa nos preenchimentos!
		if (!entity.getStatus().equals(SituacaoAvaliacao.INATIVA) && object.getSituacao().equals(SituacaoAvaliacao.INATIVA)) {
			Calendar dataInativacao = Calendar.getInstance();
			dataInativacao.setTimeInMillis(System.currentTimeMillis());
			dataInativacao.set(Calendar.DAY_OF_MONTH, dataInativacao.get(Calendar.DAY_OF_MONTH) - 1);
			object.setLimiteAv1(dataInativacao.getTime());
			object.setLimiteAv2(dataInativacao.getTime());
			object.setLimiteAv3(dataInativacao.getTime());
		}
	}

	@Override
	protected void doAfterUpdate(Avaliacao entity, AvaliacaoTO object) throws ServiceException {

		if (ajustarTipoCompetenciasDaAvaliacao(entity, object)) {
			removeParticipantesQueNaoPossuiTipoCompetenciaSelecionadoNaAvaliacao(entity, object);
		}

		Optional.ofNullable(avaliacaoParticipanteService.obterListaParticipantesPorAvaliacao(object))
			.ifPresent(x -> {
					x.forEach(participante -> {
						if (TipoAvaliacaoPerfil.GESTOR.equals(participante.getPerfilAgente())) {
							if ((object.getPreencheAv3() && object.getLimiteAv3() != null)) {
								participante.setLimiteAv3(object.getLimiteAv3());
							}
							if ((object.getPreencheAv1() && object.getLimiteAv1() != null)) {
								participante.setLimiteAv1(object.getLimiteAv1());
							}
							if ((object.getPreencheAv2() && object.getLimiteAv2() != null)) {
								participante.setLimiteAv2(object.getLimiteAv2());
							}
						} else if (TipoAvaliacaoPerfil.AUTOAVALIACAO.equals(participante.getPerfilAgente())) {
							if ((object.getPreencheAv1() && object.getLimiteAv1() != null)) {
								participante.setLimiteAv1(object.getLimiteAv1());
							}
						} else {
							participante.setLimiteAv4(null);
							participante.setLimiteAv3(null);
							participante.setLimiteAv2(object.getLimiteAv2());
							participante.setLimiteAv1(null);
						}

						avaliacaoParticipanteRepository.updateDataLimite(participante.getLimiteAv1(), participante.getLimiteAv2(),
							participante.getLimiteAv3(), participante.getId());
					});
				}
			);

		ajustarDimensoesDaAvaliacao(entity, object);
	}

	@Override
	protected void doAfterLoad(AvaliacaoTO object) throws ServiceException {
		super.doAfterLoad(object);

		Set<TipoCompetenciaTO> tiposDeCompetencia = new HashSet<>();
		List<AvaliacaoTipoCompetenciaTO> listAgentes = new ArrayList<>();

		Optional.ofNullable(avaliacaoTipoCompetenciaRepository.findByAvaliacaoId(object.getId()))
			.ifPresent(listOp -> {
				listOp.forEach(avaliacaoTipoCompetencia -> {
					AvaliacaoTipoCompetenciaTO agente = mapDefault(avaliacaoTipoCompetencia);
					listAgentes.add(agente);
					tiposDeCompetencia.add(mapDefault(avaliacaoTipoCompetencia.getTipoCompetencia()));
				});
			});

		object.setPendencias(pendenciaService.findByAvaliacaoId(object.getId()));
		object.setParticipantes(avaliacaoParticipanteService.obterListaParticipantesPorAvaliacao(object));
		object.setAgentes(listAgentes);
		object.setTiposCompetencia(tiposDeCompetencia.stream().sorted((a, b) -> b.getNomeCurto().compareTo(a.getNomeCurto())).collect(Collectors.toList()));
		object.setListaDimensao(avaliacaoDimensaoService.obterDimensaoPorAvaliacao(object));
	}

	private void validar(AvaliacaoTO object) {
		// Busca avaliações repetidas
		List<Avaliacao> avaliacaoList = select(Projections.fields(
			Avaliacao.class,
			qAvaliacao.id,
			qAvaliacao.descricaoAbreviada,
			qAvaliacao.descricao,
			Projections.fields(Campanha.class,
				qCampanha.id,
				qCampanha.descricao).as("campanha")
		)).where(qAvaliacao.descricaoAbreviada.equalsIgnoreCase(object.getDescricaoAbreviada().trim()).and(qCampanha.id.eq(object.getCampanha().getId())))
			.join(qAvaliacao.campanha, qCampanha).fetch();

		if (!avaliacaoList.isEmpty()) {
			AvaliacaoTO avaliacaoExistente = map(avaliacaoList.get(0));
			if (object.getId() == null || !avaliacaoExistente.equals(object)) {
				//FIXME: Passar a entidade para internacionalização
				throw new ValidationException("validation.duplicate", "avaliação", "field.descricao", object.getDescricaoAbreviada());
			}
		}

		if (object.getId() == null) {
			if (object.getAgentes() == null || object.getAgentes().isEmpty()) {
				//FIXME: Passar a entidade para internacionalização
				throw new ValidationException("validation.empty", "tipos de competências", "field.tipos_competencias");
			}
		}
	}

	@Override
	public List<AvaliacaoTO> listByCampanha(Long id) {
		List<AvaliacaoTO> list = map(select(Projections.fields(
			Avaliacao.class,
			qAvaliacao.id,
			qAvaliacao.descricaoAbreviada,
			qAvaliacao.limiteAv1,
			qAvaliacao.limiteAv2,
			qAvaliacao.limiteAv3,
			qAvaliacao.situacao,
			qAvaliacao.emLiberacao,
			qAvaliacao.avHomeOffice,
			qAvaliacao.situacaoPreenchimento,
			qAvaliacao.autoavaliacao,
			qAvaliacao.gestores)).where(new BooleanBuilder().and(qAvaliacao.campanha.id.eq(id))).fetch());

		list.forEach(avaliacao -> {
			calcularPreenchimentoTotalDaAvaliacao(avaliacao);
			avaliacao.setNumAvaliacoes(avaliacaoParticipanteService.obterTotalAvaliacoesPorAvaliacao(avaliacao));
			avaliacao.setNumParticipantes(Long.valueOf(avaliacaoParticipanteService.qtdAvaliadosPorAvaliacao(avaliacao.getId())));
		});

		return list;
	}

	@Override
	public List<AvaliacaoTO> listarAvaliacoesLiberadasDaCampanha(Long id) {
		return select(Projections.fields(
			Avaliacao.class,
			qAvaliacao.id,
			qAvaliacao.descricaoAbreviada,
			qAvaliacao.limiteAv1,
			qAvaliacao.limiteAv2,
			qAvaliacao.limiteAv3,
			qAvaliacao.emLiberacao,
			qAvaliacao.avHomeOffice,
			qAvaliacao.situacao,
			qAvaliacao.situacaoPreenchimento))
			.where(new BooleanBuilder().and(qAvaliacao.campanha.id.eq(id).and(qAvaliacao.situacao.eq(SituacaoAvaliacao.LIBERADA)))).fetch()
			.stream()
			.map(avaliacao -> {
				AvaliacaoTO avaTO = map(avaliacao);
				calcularPreenchimentoTotalDaAvaliacao(avaTO);
				avaTO.setNumAvaliacoes(avaliacaoParticipanteService.obterTotalAvaliacoesPorAvaliacao(avaTO));
				List<ParticipanteTO> avaliados = avaliacaoParticipanteService.obterAvaliadosPorAvaliacao(avaliacao.getId());
				if (avaliados != null && !avaliados.isEmpty())
					avaTO.setNumParticipantes(Long.valueOf(avaliados.size()));
				else
					avaTO.setNumParticipantes(0L);
				return avaTO;
			})
			.collect(Collectors.toList());
	}

	@Override
	public List<AvaliacaoTO> listByCampanha(CampanhaTO campanha) {
		return listByCampanha(campanha.getId());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public AvaliacaoTO copiar(AvaliacaoTO avaliacaoTO) {
		// TODO Verificar a possibilidade de incluir na biblioteca
		logger.info(String.format("Inicinado cópia da Avaliação %s [%d]...", avaliacaoTO.getDescricaoAbreviada(), avaliacaoTO.getId()));

		Avaliacao avaliacao = avaliacaoRepository.findOne(avaliacaoTO.getId());

		List<AvaliacaoTipoCompetencia> listTiposCompetencia = avaliacaoTipoCompetenciaRepository.findByAvaliacaoId(avaliacao.getId());

		avaliacao.setSituacaoPreenchimento(SituacaoPreenchimento.NAO_PREENCH);

		AvaliacaoTO copia = map(avaliacao);

		// FIXME: Garantindo que os dados estão corretos, algum erro do DOZER?!?!?!
		copia.setId(null);
		copia.setDescricaoAbreviada(avaliacaoTO.getDescricaoAbreviada());
		List<ParticipanteTO> participantes = new ArrayList<ParticipanteTO>();
		copia.setParticipantes(participantes);

		/*
		 * Neste momento precisamos criar uma lista de tipos de competência
		 * para passar ao TO. Isso é necessário para ser validado ao salvar uma
		 * avaliação nova.
		 */
		List<AvaliacaoTipoCompetenciaTO> listaDeAgentes = new ArrayList<AvaliacaoTipoCompetenciaTO>();

		listTiposCompetencia.forEach(avaliacaoTipoCompetencia -> {
			AvaliacaoTipoCompetenciaTO tipoCompetencia = mapDefault(avaliacaoTipoCompetencia);
			tipoCompetencia.setId(null);
			tipoCompetencia.getAvaliacao().setId(null);
			listaDeAgentes.add(tipoCompetencia);
		});

		copia.setAgentes(listaDeAgentes);
		copia = save(copia);

		// Obtendo a lista de particioantes da avaliação original.
		List<ParticipanteTO> listaDeParticipantes = avaliacaoParticipanteService.obterListaParticipantesPorAvaliacao(avaliacaoTO);

		/*
		 * Guardaremos aqui na nova lista de participantes os que têm competências associadas ao cargo dos mesmos tipos
		 * selecionado para a avaliação.
		 */

		for (ParticipanteTO participanteTO : listaDeParticipantes) {
			// Validando se o cargo do participante tem os mesmo tipos de competência da avaliação.
			if (temTipoCompetenciaNoCargo(participanteTO, listTiposCompetencia)) {
				ParticipanteTO novo = new ParticipanteTO();
				novo.setLimiteAv1(copia.getLimiteAv1());
				novo.setLimiteAv2(copia.getLimiteAv2());
				novo.setLimiteAv3(copia.getLimiteAv3());
				novo.setLimiteAv4(copia.getLimiteAv4());
				novo.setAvaliacao(copia);
				novo.setAvaliado(participanteTO.getAvaliado());
				novo.setAvaliador(participanteTO.getAvaliador());
				novo.setRevisor(participanteTO.getRevisor());
				novo.setDataCriacao(participanteTO.getDataCriacao());
				novo.setCriador(participanteTO.getCriador());
				novo.setConsensoLiberado(Boolean.FALSE);
				novo.setRevisaoLiberada(Boolean.FALSE);

				novo.setPerfilAgente(participanteTO.getPerfilAgente());
				avaliacaoParticipanteService.save(novo);
			}
		}

		return copia;
	}

	/**
	 * Validar ser as competências associadas ao cargo do participante tem os mesmos tipo
	 * de competência da avaliação.
	 *
	 * @param participante         O participante a ser validado.
	 * @param listTiposCompetencia A lista de tipos de competências da avaliação.
	 * @return <code>TRUE</code> caso tenham os tipos no cargo do participante. <code>FALSE</code> caso contrário.
	 */
	private Boolean temTipoCompetenciaNoCargo(ParticipanteTO participante, List<AvaliacaoTipoCompetencia> listTiposCompetencia) {
		List<CargoCompetenciaTO> list = cargoCompetenciaService.findAllCargoCompetenciaByCargoAndAtivos(participante.getAvaliado().getCargo().getId());

		Boolean temCompetenciasPorTipo = Boolean.FALSE;

		list.forEach(x -> {

		});

		for (CargoCompetenciaTO cargoCompetenciaTO : list) {
			List<AvaliacaoTipoCompetencia> avaliacaoTipoCompetencias = listTiposCompetencia.stream()
				.filter(tipo -> cargoCompetenciaTO.getCompetencia().getTipo().getId().equals(tipo.getTipoCompetencia().getId())).collect(Collectors.toList());
			if (!avaliacaoTipoCompetencias.isEmpty()) {
				temCompetenciasPorTipo = Boolean.TRUE;
			}
		}

		return temCompetenciasPorTipo;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void liberar(AvaliacaoTO avaliacao) {
		avaliacao.setEmLiberacao(true);
		avaliacao.setSituacao(SituacaoAvaliacao.AG_LIBERACAO);
		Avaliacao avaliacaoEntity = avaliacaoRepository.findOne(avaliacao.getId());
		avaliacaoEntity.setStatus(SituacaoAvaliacao.AG_LIBERACAO);
		avaliacaoEntity.setEmLiberacao(true);
		avaliacaoRepository.save(avaliacaoEntity);

		Campanha campanha = campanhaRepository.findOne(avaliacaoEntity.getCampanha().getId());
		campanha.setAvEmLiberacao(true);
		campanhaRepository.save(campanha);

		if (!TipoPerfil.ADMINISTRADOR.equals(getLoggedUser().getTipoPerfil())) {
			mensageriaService.mensagemLiberarAvaliacao(avaliacao);
		}

		logger.info("Antes do início da Thread na rotina de liberação");
		if (avaliacao.getAvHomeOffice() != null && avaliacao.getAvHomeOffice()) {
			Runnable originalRunnable = new Runnable() {
				public void run() {
					try {
						liberarHomeOffice(avaliacao);
					} catch (Exception e) {
						mensageriaService.mensagemLiberarAvaliacaoErro(avaliacao, e);
						e.printStackTrace();
						logger.error(e.getMessage(), e);
					}
				}
			};

			SecurityContext context = SecurityContextHolder.getContext();
			DelegatingSecurityContextRunnable wrappedRunnable =
				new DelegatingSecurityContextRunnable(originalRunnable, context);

			new Thread(wrappedRunnable).start();
		} else {
			Runnable originalRunnable = new Runnable() {
				public void run() {
					try {
						liberarDefault(avaliacao);
					} catch (Exception e) {
						mensageriaService.mensagemLiberarAvaliacaoErro(avaliacao, e);
						e.printStackTrace();
						logger.error(e.getMessage(), e);
					}
				}
			};

			SecurityContext context = SecurityContextHolder.getContext();
			DelegatingSecurityContextRunnable wrappedRunnable =
				new DelegatingSecurityContextRunnable(originalRunnable, context);

			new Thread(wrappedRunnable).start();
		}
		logger.info("Thread da rotina de liberação iniciado");
	}


	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private void liberarDefault(AvaliacaoTO avaliacao) {
		long start = System.currentTimeMillis();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat horaFormatter = new SimpleDateFormat("HH:mm");

		Date dataInicio = new Date();
		System.out.println("Iniciando a liberação no dia " + dateFormatter.format(dataInicio) + " na hora " + horaFormatter.format(dataInicio) + " ...");

		List<DetalhamentoCargoCompetenciaTO> listaDetCargoComp = new ArrayList<>();//lista de DetalhamentoCargoCompetencia aplicaveis ao cargo.

		logger.info(String.format("Pesquisando os participantes da avaliação ..."));
		List<ParticipanteTO> participantes = avaliacaoParticipanteService.obterListaParticipantesPorAvaliacao(avaliacao);

		if (participantes == null || participantes.isEmpty()) {
			throw new ValidationException("participantes", "empty-participantes", "field.participantes");
		}

		List<PendenciaTO> pendencias = pendenciaService.findByAvaliacaoId(avaliacao.getId());

		if (pendencias != null && !pendencias.isEmpty()) {
			throw new ValidationException("avaliacao-participante", "error-pendencias");
		}

		logger.info(String.format("Ordenando os participantes da avaliação por cargo ..."));
		Collections.sort(participantes, new Comparator<ParticipanteTO>() {
			@Override
			public int compare(ParticipanteTO o1, ParticipanteTO o2) {
				int result = o1.getAvaliado().getCargo().getNome().compareTo(o2.getAvaliado().getCargo().getNome());
				return result;
			}
		});

		logger.info(String.format("Pesquisando os tipos de competências e os agentes da avaliação ..."));
		List<AvaliacaoTipoCompetenciaTO> listaTiposCompetencia = avaliacaoTipoCompetenciaService.obterAvaliacaoTipoCompetenciaProjections(avaliacao.getId());

		int contador = 0;
		Long idCargoAnterior = null;
		Set<CompetenciaDetalhamentoTO> detalhamentosSalvos = new HashSet<>();//lista de perfis executados por cargo
		Map<TipoAvaliacaoPerfil, List<DetalhamentoCargoCompetenciaTO>> mapaPerguntasPorPerfilAgente = new HashMap<>();

		logger.info(String.format("A lista de participantes contém [%d] itens ...", participantes.size()));

		for (int i = 0; i < participantes.size(); i++) {
			ParticipanteTO participante = participantes.get(i);
			contador = i + 1;
			logger.info(String.format("Iniciando item %s de [%d]...", contador, participantes.size()));
			logger.info(String.format("Participante = { id: %s} , Avaliado = {nome: %s, cargo: %s} , Avaliador = {nome: %s, perfil: %s}  ...", participante.getId()
				, participante.getAvaliado().getNome(), participante.getAvaliado().getCargo().getNome(), participante.getAvaliador().getNome(), participante.getPerfilAgente().getNome()));

			if (idCargoAnterior == null || !idCargoAnterior.equals(participante.getAvaliado().getCargo().getId())) {
				listaDetCargoComp.clear();
				detalhamentosSalvos.clear();
				mapaPerguntasPorPerfilAgente.clear();

				logger.info(String.format("Pesquisando as perguntas ..."));
				listaDetCargoComp = detalhamentoCargoCompetenciaService.findDetalhamentosAplicaveisAoCargo(participante.getAvaliado().getCargo().getId(), Situacao.ATIVA);

				if (listaDetCargoComp.isEmpty()) {
					logger.info(String.format("não há detalhamentos aplicáveis ao cargo %s ...", participante.getAvaliado().getCargo()));
					throw new ServiceException("Erro ao liberação avaliação: não há detalhamentos aplicáveis ao cargo " + participante.getAvaliado().getCargo());
				}

				mapaPerguntasPorPerfilAgente = cargoService.agruparPerguntasPorPerfilAgente(listaDetCargoComp, participante.getAvaliado().getCargo(), avaliacao, listaTiposCompetencia);

				idCargoAnterior = participante.getAvaliado().getCargo().getId();
			}

			logger.info(String.format("Perguntas do perfil %s ...", participante.getPerfilAgente().getNome()));
			List<DetalhamentoCargoCompetenciaTO> perguntas = mapaPerguntasPorPerfilAgente.get(participante.getPerfilAgente());

			if (perguntas.isEmpty() && avaliacao.getAutoavaliacao()) {
				logger.info(String.format("não há perguntas para avaliadores com perfil o %s para o cargo de %s ...", participante.getPerfilAgente().getNome(), participante.getAvaliado().getCargo()));
				throw new ServiceException("Erro ao liberação avaliação: não há perguntas para avaliadores com perfil o " + participante.getPerfilAgente().getNome()
					+ " para o cargo de " + participante.getAvaliado().getCargo());
			} else {
				if (avaliacao.getCampanha() != null && avaliacao.getCampanha().getDimensao() != null && avaliacao.getCampanha().getDimensao()) {
					List<AvaliacaoDimensaoTO> listaAvaliacaoDimensao = avaliacaoDimensaoService.obterDimensaoPorAvaliacao(avaliacao);
					List<DetalhamentoCargoCompetenciaTO> listaDetDim = new ArrayList<>();

					for (AvaliacaoDimensaoTO avaliacaoDimensaoTO : listaAvaliacaoDimensao) {
						List<DimensaoCompetenciaDetalhamentoTO> listaDetalhamentosDimensao = dimensaoCompetenciaDetalhamentoService.obterDetalhamentosPorDimensao(avaliacaoDimensaoTO.getDimensao());

						for (DimensaoCompetenciaDetalhamentoTO dimCompDetTO : listaDetalhamentosDimensao) {

							for (DetalhamentoCargoCompetenciaTO dccTO : perguntas) {
								if (dccTO.getCompetenciaDetalhamento().getId().equals(dimCompDetTO.getCompetenciaDetalhamento().getId())) {
									try {
										listaDetDim.add(dccTO.clone());
										break;
									} catch (CloneNotSupportedException cnse) {
										throw new ValidationException("avaliacao-participante", "error-clone-detalhamento");
									}
								}
							}
						}
					}

					if (listaDetDim.isEmpty()) {
						PendenciaTO pendencia = new PendenciaTO("cargo-not-found-gabarito-dimensao", "não possui detalhamentos aplicáveis ao cargo - dimensão", TipoPendencia.CARGO, participante.getAvaliado().getCargo(), new Date());
						pendencias.add(pendencia);
					} else {
						perguntas.clear();
						perguntas.addAll(listaDetDim);
					}
				}
			}

			participante.getAvaliacao().setSituacao(SituacaoAvaliacao.LIBERADA);
			participante.getAvaliacao().setStatus(SituacaoAvaliacao.LIBERADA);
			//FIXME: codigo duplicado, existente no 'liberarParticipantes'
			if (participante.getIdCargoAvaliado() == null)
				participante.setIdCargoAvaliado(participante.getAvaliado().getCargo().getId());

			if (participante.getNomeCargoAvaliado() == null)
				participante.setNomeCargoAvaliado(participante.getAvaliado().getCargo().getNome());

			if (participante.getIdDeptoAvaliado() == null)
				participante.setIdDeptoAvaliado(participante.getAvaliado().getDepartamento().getId());

			if (participante.getNomeDeptoAvaliado() == null)
				participante.setNomeDeptoAvaliado(participante.getAvaliado().getDepartamento().getNome());
			avaliacaoParticipanteService.saveOrUpdate(participante);
			avaliacaoParticipanteService.salvarPerguntas(participante, perguntas, detalhamentosSalvos);

			logger.info(String.format("Finalizando item %s de [%d]...", contador, participantes.size()));
			logger.info(String.format("---------------------------------------"));
		}

		// FIXME: $transition não tá mudando os status.
		avaliacao.setSituacao(SituacaoAvaliacao.LIBERADA);
		avaliacao.setEmLiberacao(false);

		Avaliacao avaliacaoEntity = avaliacaoRepository.findOne(avaliacao.getId());
		avaliacaoEntity.setStatus(SituacaoAvaliacao.LIBERADA);
		avaliacaoEntity.setEmLiberacao(false);
		avaliacaoRepository.save(avaliacaoEntity);

		Campanha campanha = campanhaRepository.findOne(avaliacaoEntity.getCampanha().getId());
		campanha.setAvEmLiberacao(false);
		campanhaRepository.save(campanha);

		mensageriaService.mensagemLiberarAvaliacaoConcluido(avaliacao);

		Date dataFim = new Date();
		System.out.println("Finalizando a liberação no dia " + dateFormatter.format(dataFim) + " na hora " + horaFormatter.format(dataFim) + " ...");

		long delay = System.currentTimeMillis() - start;
		long segundos = (delay / 1000) % 60;
		long minutos = (delay / 60000) % 60;
		long horas = delay / 3600000;// 3600000 = 60 * 60 * 1000
		logger.info(String.format("O método liberarAvaliacao executou em %03d:%02d:%02d ", horas, minutos, segundos));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<ParticipanteTO> importarPares(InputStream file) {
		List<ParticipanteTO> participantes = new ArrayList<>();

//		importacaoService.importarPlanilhasCadastrosFuncionais(file);
//		importacaoService.importarPlanilhasCadastrosCompetencias(file);
//		importacaoService.importarPlanilhasAssociacoes(file);

		try {

			Workbook workbook = new XSSFWorkbook(file);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();

			int linha = 0;
			while (iterator.hasNext()) {

				linha++;
				Row currentRow = iterator.next();
				Iterator<Cell> cellIterator = currentRow.iterator();

				int coluna = 0;
				FuncionarioSimpleTO avaliador = null;

				while (cellIterator.hasNext()) {
					coluna++;
					Cell currentCell = cellIterator.next();

					if (!currentCell.getStringCellValue().equalsIgnoreCase("Avaliador") && !currentCell.getStringCellValue().contentEquals("Avaliado Par")) {
						if (coluna == 1) {
							avaliador = buscarFuncionario(currentCell.getStringCellValue());
						} else if (avaliador != null) {
							FuncionarioSimpleTO avaliado = buscarFuncionario(currentCell.getStringCellValue());

							if (avaliado != null) {
								ParticipanteTO participante = new ParticipanteTO();
								participante.setAvaliador(avaliador);
								participante.setAvaliado(avaliado);
								participante.setPerfilAgente(TipoAvaliacaoPerfil.PARES);
								participantes.add(participante);
							}
						}
					}
				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return participantes;
	}

	private String removerEspacos(String strOrigem) {
		strOrigem = strOrigem.replaceAll("\\s+", " ").trim();
		return strOrigem;
	}

	private FuncionarioSimpleTO buscarFuncionario(String nome) {
		FuncionarioSimpleTO funcionarioSimpleTO = null;

		FuncionarioTO funcionarioTO = funcionarioService.findByNome(removerEspacos(nome));
		if (funcionarioTO != null)
			funcionarioSimpleTO = mapper.map(funcionarioTO, FuncionarioSimpleTO.class);

		return funcionarioSimpleTO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public List<ParticipanteTO> salvarPares(List<ParticipanteTO> pares) {
		List<PendenciaTO> pendencias = new ArrayList<>();

		if (pares == null || pares.isEmpty()) {
			throw new ValidationException("validation.empty", "planilha_pares");
		}

		return validarEpersistirParticipantes(pares.get(0).getAvaliacao(), pendencias, pares);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void salvarAvaliadores(AvaliacaoTO avaliacao) throws CloneNotSupportedException {
		//O front-end está passando para este método, na propriedade avaliacao.participantes(getParticipantes),
		//somente a lista dos participante que possuem avaliadores e não a lista completa dos colaboradores
		//da avaliação
		if (avaliacao.getParticipantes() == null || avaliacao.getParticipantes().isEmpty()) {
			throw new ValidationException("validation.empty", "participantes", "field.participantes");
		}

		List<ParticipanteTO> participantes = avaliacaoParticipanteService.obterListaParticipantesPorAvaliacao(avaliacao);
		Set<ParticipanteTO> avaliadoresExclusao = new HashSet<>();

		/*
		 * Na inclusão de novos avaliadores em uma avaliação já liberada, o frontend não está passando os dados completos de
		 * participantes, onde o preenchimento da avaliação já foi iniciado. São informações que não constam na tela, como o campo
		 * dataInicioAv1. O for abaixo recupera essas informações do banco para que não sejam sobrescritas pelo objeto incompleto.
		 *
		 * Nesta mesma iteração é montada a lista dos avaliadores que foram removidos na tela de seleção de avaliadores e que
		 * serão excluídos do banco
		 */
		Map<ParticipanteTO, ParticipanteTO> mapParticipante = new HashMap<>();
		for (ParticipanteTO participante : participantes) { //Lista de participantes gravada no banco
			for (ParticipanteTO participanteTO : avaliacao.getParticipantes()) { //Lista de participantes da tela de edição de avaliadores
				if (!participante.getAvaliado().equals(participante.getAvaliador()) && !avaliacao.getParticipantes().contains(participante)) {
					avaliadoresExclusao.add(participante);
					break;
				}
				else if (participanteTO.getId() != null && participanteTO.getId().equals(participante.getId())) {
					participante.setPerfilAgente(participanteTO.getPerfilAgente());
					mapParticipante.put(participanteTO, participante);
					break;
				}
			}
		}

		for (ParticipanteTO participanteTO : mapParticipante.keySet()) {
			avaliacao.getParticipantes().remove(participanteTO);
			avaliacao.getParticipantes().add(mapParticipante.get(participanteTO));
		}
		//Excluir do banco os avaliadores que foram removidos na tela de seleção de avaliadores
		participantes.forEach(participanteTO -> {
			if (!participanteTO.getAvaliado().equals(participanteTO.getAvaliador()) && !avaliacao.getParticipantes().contains(participanteTO))
				avaliadoresExclusao.add(participanteTO);
		});

		// Verifica se a avaliação possui o tipo "Auto Avaliação" e sem o tipo "Gestores".
		// Caso afirmativo, criar todas as autoavaliações que estiverem faltando
		if (avaliacao.getAutoavaliacao() && !avaliacao.getGestores()) {
			//Guarda os ids de todos os avaliados.
			//Um avaliado pode aparecer várias vezes na lista de participantes, pois pode ser avaliado por
			//mais de um avaliador. A utilização do Set impede que um id seja incluído em duplicidade
			Set<Long> idsAutoavaliacao = participantes.stream().map(ParticipanteTO::getAvaliado).map(FuncionarioSimpleTO::getId).collect(Collectors.toSet());

			participantes.forEach(participanteTO -> {
				//Se já existe uma autoavaliação no banco, o id é removido da lista
				if (participanteTO.getAvaliado().equals(participanteTO.getAvaliador()))
					idsAutoavaliacao.remove(participanteTO.getAvaliado().getId());
			});

			//Cria todas as autoavaliações que faltam
			idsAutoavaliacao.forEach(idAvaliado -> {
				FuncionarioTO avaliado = funcionarioService.findById(idAvaliado);
				ParticipanteTO participanteTO = new ParticipanteTO();
				participanteTO.setAvaliado(buscarFuncionario(avaliado.getNome()));
				participanteTO.setAvaliador(buscarFuncionario(avaliado.getNome()));
				participanteTO.setPerfilAgente(TipoAvaliacaoPerfil.AUTOAVALIACAO);
				participanteTO.setAvaliacao(avaliacao);
				avaliacao.getParticipantes().add(participanteTO);
			});
		}

		for (ParticipanteTO participante : avaliadoresExclusao) {
			preenchimentoRepository.excluirPrenchimentoPorParticipante(participante.getId());
			avaliacaoParticipanteRepository.excluirParticipante(participante.getId());
//			avaliacaoParticipanteService.delete(participante);
		}

		validarEpersistirParticipantes(avaliacao, new ArrayList<>(), avaliacao.getParticipantes());

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void salvarAvaliadoresLiberacao(AvaliacaoTO avaliacao) throws CloneNotSupportedException {
		List<ParticipanteTO> participantesTodos = new ArrayList<>();
		List<ParticipanteTO> participantesNovos = new ArrayList<>();
		List<PendenciaTO> pendencias = new ArrayList<>();

		//chave: código do cargo, valor:lista com detalhamentos
		Map<Long, List<DetalhamentoCargoCompetenciaTO>> detalhamentosPorCargo = new HashMap<>();

		if (avaliacao.getParticipantes() == null || avaliacao.getParticipantes().isEmpty()) {
			throw new ValidationException("validation.empty", "participantes", "field.participantes");
		}

		this.salvarAvaliadores(avaliacao);
		List<ParticipanteTO> participantes =  participantes = avaliacaoParticipanteService.obterListaParticipantesPorAvaliacao(avaliacao);

		for (ParticipanteTO participante : participantes) {
			List<ParticipanteTO> avaliadores = new ArrayList<>();
			List<ParticipanteTO> resultado = avaliacaoParticipanteService.obterParticipantesPorAvaliadoEAvaliacao(participante.getAvaliado().getId(), avaliacao.getId());

			if (resultado == null || resultado.isEmpty()) {
				//Se a lista de resultado estiver vazia, isto significa que foi selecionado um novo funcionário para ser avaliado.
				//Esse novo funcionário(avaliado) é adicionado na lista "participantes" junto com os seus subordinados(se a avaliação possuir subordinados).

				avaliadores = criarAvaliadores(participante, avaliacao);
				if (avaliadores != null && !avaliadores.isEmpty()) {
					participantesNovos.addAll(avaliadores);
					participantesTodos.addAll(avaliadores);
				} else {
					PendenciaTO pendencia = new PendenciaTO("not-found-avaliador", "não possui avaliadores", TipoPendencia.COLABORADORES, participante.getAvaliado(), new Date());
					pendencias.add(pendencia);
				}
			} else {
				participantesTodos.add(participante);

				boolean avaliadorNovo = false;
				for (ParticipanteTO avaliador : resultado) {
					if (participante.getAvaliador() != null && avaliador.getAvaliador().getId().equals(participante.getAvaliador().getId())) {
						avaliadorNovo = true;
						break;
					}
				}

				if (!avaliadorNovo) {
					participantesNovos.add(participante);
					participantesTodos.add(participante);
				} else {
					List<Preenchimento> listaPreenchimentos = preenchimentoRepository.findByParticipanteId(participante.getId());
					if (listaPreenchimentos == null || listaPreenchimentos.isEmpty()) {
						participantesNovos.add(participante);
					}
				}

			}
		}


		/** Encontra os participantes que foram removidos - inicio **/
		List<ParticipanteTO> oldParticipantes = avaliacaoParticipanteService.obterListaParticipantesPorAvaliacao(avaliacao);
		List<ParticipanteTO> removidos = ListUtils.subtract(oldParticipantes, participantesTodos);
		for (ParticipanteTO participante : removidos) {
			avaliacaoParticipanteService.delete(participante);
		}
		/** Encontra os participantes que foram removidos - fim **/

		List<AvaliacaoTipoCompetenciaTO> idsTiposCompetencia = avaliacaoTipoCompetenciaService.findIdsTiposCompetenciaByAvaliacaoId(avaliacao.getId());

		for (ParticipanteTO participante : participantesNovos) {

			if (!SituacaoFuncionario.EM_ATIVIDADE.equals(participante.getAvaliado().getSituacaoFuncional())) {
				PendenciaTO pendencia = new PendenciaTO("avaliado-situacao-funcional", "o avaliado está com a situação funcional diferente de em atividade", TipoPendencia.COLABORADORES, participante.getAvaliado(), participante.getAvaliador(), new Date());
				pendencias.add(pendencia);
			}

			List<PendenciaTO> resultados = null;
			if (!detalhamentosPorCargo.containsKey(participante.getAvaliado().getCargo().getId())) {
				resultados = cargoService.validarParaLiberacao(participante.getAvaliado().getCargo(), idsTiposCompetencia, detalhamentosPorCargo, avaliacao);
			}


			if (resultados != null && !resultados.isEmpty()) {
				pendencias.addAll(resultados);
			} else if (contemPendencia(participante.getAvaliado().getId(), participante.getAvaliado().getCargo().getId(), pendencias) == null) {
				removerPendencias(participante.getAvaliado().getId(), participante.getAvaliado().getCargo().getId(), avaliacao);
				saveOrUpdateParticipante(participante, avaliacao);
			}

		}

		if (pendencias != null && !pendencias.isEmpty()) {
			removerPendencias(pendencias, avaliacao);
			salvarPendencias(pendencias, avaliacao);
		}

		AvaliacaoTO avaliacaoTOTemp = load(avaliacao.getId());

		if (!avaliacaoTOTemp.getStatus().equals(SituacaoAvaliacao.LIBERADA) && participantesTodos != null && !participantesTodos.isEmpty()) {
			liberarParticipantes(participantesTodos, avaliacao);
		} else if (avaliacaoTOTemp.getStatus().equals(SituacaoAvaliacao.LIBERADA) && participantesNovos != null && !participantesNovos.isEmpty()) {
			liberarParticipantes(participantesNovos, avaliacao);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void salvarParticipantes(AvaliacaoTO avaliacao) throws CloneNotSupportedException {

		List<PendenciaTO> pendencias = new ArrayList<>();
		List<ParticipanteTO> participantes = new ArrayList<>();

		//chave: código do cargo, valor:lista com detalhamentos
		Map<Long, List<DetalhamentoCargoCompetenciaTO>> detalhamentosPorCargo = new HashMap<>();

		if (avaliacao.getParticipantes() == null || avaliacao.getParticipantes().isEmpty()) {
			throw new ValidationException("validation.empty", "participantes", "field.participantes");
		}

		for (ParticipanteTO participante : avaliacao.getParticipantes()) {
			List<ParticipanteTO> avaliadores = new ArrayList<>();
			List<ParticipanteTO> resultado = avaliacaoParticipanteService.obterParticipantesPorAvaliadoEAvaliacao(participante.getAvaliado().getId(), avaliacao.getId());

			if (resultado == null || resultado.isEmpty()) {
				//Se a lista de resultado estiver vazia, isto significa que foi selecionado um novo funcionário para ser avaliado.
				//Esse novo funcionário(avaliado) é adicionado na lista "participantes" junto com os seus subordinados(se a avaliação possuir subordinados).

				avaliadores = criarAvaliadores(participante, avaliacao);
				if (avaliadores != null && !avaliadores.isEmpty()) {
					participantes.addAll(avaliadores);
				} else {
					PendenciaTO pendencia = new PendenciaTO("not-found-avaliador", "não possui avaliadores", TipoPendencia.COLABORADORES, participante.getAvaliado(), new Date());
					pendencias.add(pendencia);
				}
			} else {
				//Se a lista de resultado estiver com pelo menos um item, isto significa que é um funcionário que já participa da avaliação.
				//Então é só manter os avaliadores que já foram selecionados para ele e atualizar os dados.

				avaliadores = atualiazarAvalidores(resultado, participante, avaliacao);
				participantes.addAll(avaliadores);
			}
		}

		/** Encontra os participantes que foram removidos**/
		removerParticipantesAusentes(avaliacao, participantes);
		validarEpersistirParticipantes(avaliacao, pendencias, participantes);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public FuncionarioSimpleTO homeOfficeBrasilSalvarParticipante(FuncionarioTO funcTO) throws CloneNotSupportedException {

		FuncionarioSimpleTO funcSimpTO = mapper.map(funcTO, FuncionarioSimpleTO.class);
		ParticipanteTO avaliado = homeOfficeBrasilCriarAvaliadoComFuncionario(funcSimpTO);
		AvaliacaoTO avaliacao = this.load(avaliacaoId); //ID da avaliacao do COVID
		List<ParticipanteTO> participantes = criarAvaliadores(avaliado, avaliacao); //No caso, esse metodo nao cria 'apenas' os avaliadores, mas o participante
		participantes.forEach(participante -> saveOrUpdateParticipante(participante, avaliacao));
		liberarParticipantes(participantes, avaliacao);
		return funcSimpTO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public FuncionarioTO homeOfficeBrasilCriarFuncionario(HomeOfficeBrasilTO objectTO) {
		/*FuncionarioTO persistedFunc = funcionarioService.findByEmailFuncionalAndEmpresaIdAndSituacao(objectTO.getEmailFuncional(), empresaId, Situacao.ATIVA);
		if(persistedFunc != null && persistedFunc.getId() != null) {
			throw new ValidationException("validation.duplicate_email", "cadastro", "field_email");
		}*/

		FuncionarioTO funcionarioTO = mapper.map(objectTO, FuncionarioTO.class);
		funcionarioTO.setMatricula(objectTO.getEmailFuncional());
		funcionarioTO.setSituacao(Situacao.ATIVA);
		funcionarioTO.setSituacaoFuncional(SituacaoFuncionario.EM_ATIVIDADE);
		funcionarioTO.setPrimeiroAcesso(true);
		funcionarioTO.setPontoFocalRh(false);
		funcionarioTO.setTipoPerfil(TipoPerfil.FUNCIONARIO);
		funcionarioTO.setDataCargo(new Date());
		funcionarioTO.setAdmissao(new Date());
		funcionarioTO.setPreencheFormacaoEIdiomas(false);
		homeOfficeBrasilCriarDependenciasFuncionario(funcionarioTO);

		FuncionarioTO funcTO = funcionarioService.save(funcionarioTO);
		homeOfficeBrasilCriarEndereco(funcTO.getId(), objectTO.getMunicipio().getId());
		return funcTO;
	}

	private void homeOfficeBrasilCriarDependenciasFuncionario(FuncionarioTO funcionarioTO) {
		EmpresaSimpleTO empTO = new EmpresaSimpleTO();
		empTO.setId(empresaId);
		DepartamentoTO deptoTO = departamentoService.load(deptoId);
		CargoSimpleTO cargoTO = mapper.map(cargoService.load(cargoId), CargoSimpleTO.class);
		UnidadeTO unidadeTO = unidadeService.load(unidadeId);

		funcionarioTO.setUnidade(unidadeTO);
		funcionarioTO.setDepartamento(deptoTO);
		funcionarioTO.setCargo(cargoTO);
		funcionarioTO.setEmpresa(empTO);
	}

	private void homeOfficeBrasilCriarEndereco(Long idFuncionario, Long idMunicipio) {
		Endereco endereco = new Endereco();
		endereco.setIdFuncionario(idFuncionario);
		endereco.setIdMunicipio(idMunicipio);
		enderecoRepository.save(endereco);
	}

	private ParticipanteTO homeOfficeBrasilCriarAvaliadoComFuncionario(FuncionarioSimpleTO funcionarioSimpleTO) {
		ParticipanteTO participanteTO = new ParticipanteTO();
		participanteTO.setAvaliado(funcionarioSimpleTO);
		return participanteTO;
	}

	private void removerParticipantesAusentes(AvaliacaoTO avaliacao, List<ParticipanteTO> participantes) {
		List<ParticipanteTO> oldParticipantes = avaliacaoParticipanteService.obterListaParticipantesPorAvaliacao(avaliacao);
		List<ParticipanteTO> removidos = ListUtils.subtract(oldParticipantes, participantes);
		for (ParticipanteTO participante : removidos) {
			avaliacaoParticipanteService.delete(participante);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private List<ParticipanteTO> validarEpersistirParticipantes(AvaliacaoTO avaliacao, List<PendenciaTO> pendencias, List<ParticipanteTO> participantes) {
		List<ParticipanteTO> participantesPersistidos = new ArrayList<>();

		//chave: código do cargo, valor:lista com detalhamentos
		Map<Long, List<DetalhamentoCargoCompetenciaTO>> detalhamentosPorCargo = new HashMap<>();

		List<AvaliacaoTipoCompetenciaTO> idsTiposCompetencia = avaliacaoTipoCompetenciaService.findIdsTiposCompetenciaByAvaliacaoId(avaliacao.getId());

		for (ParticipanteTO participante : participantes) {

			if (!SituacaoFuncionario.EM_ATIVIDADE.equals(participante.getAvaliado().getSituacaoFuncional())) {
				PendenciaTO pendencia = new PendenciaTO("avaliado-situacao-funcional", "o avaliado está com a situação funcional diferente de em atividade", TipoPendencia.COLABORADORES, participante.getAvaliado(), participante.getAvaliador(), new Date());
				pendencias.add(pendencia);
			}

			List<PendenciaTO> resultados = null;
			if (!detalhamentosPorCargo.containsKey(participante.getAvaliado().getCargo().getId())) {
				resultados = cargoService.validarParaLiberacao(participante.getAvaliado().getCargo(), idsTiposCompetencia, detalhamentosPorCargo, avaliacao);
			}

			if (resultados != null && !resultados.isEmpty()) {
				pendencias.addAll(resultados);
				if (participante.getId() != null)
					avaliacaoParticipanteService.delete(participante);

			} else if (contemPendencia(participante.getAvaliado().getId(), participante.getAvaliado().getCargo().getId(), pendencias) == null) {
				removerPendencias(participante.getAvaliado().getId(), participante.getAvaliado().getCargo().getId(), avaliacao);
				if (participante.getIdCargoAvaliado() == null || participante.getIdDeptoAvaliado() == null) {
					FuncionarioTO funcionarioTO = funcionarioService.findById(participante.getAvaliado().getId());
					participante.setIdCargoAvaliado(funcionarioTO.getCargo().getId());
					participante.setNomeCargoAvaliado(funcionarioTO.getCargo().getNome());
					participante.setIdDeptoAvaliado(funcionarioTO.getDepartamento().getId());
					participante.setNomeDeptoAvaliado(funcionarioTO.getDepartamento().getNome());
				}
				ParticipanteTO participanteTO = saveOrUpdateParticipante(participante, avaliacao);
				participantesPersistidos.add(participanteTO);
			}

		}

		if (pendencias != null && !pendencias.isEmpty()) {
			removerPendencias(pendencias, avaliacao);
			salvarPendencias(pendencias, avaliacao);
		}

		return participantesPersistidos;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	private void liberarParticipantes(List<ParticipanteTO> participantes, AvaliacaoTO avaliacao) {
		long start = System.currentTimeMillis();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat horaFormatter = new SimpleDateFormat("HH:mm");

		Date dataInicio = new Date();
		System.out.println("Iniciando a liberação dos participantes no dia " + dateFormatter.format(dataInicio) + " na hora " + horaFormatter.format(dataInicio) + " ...");

		if (participantes == null || participantes.isEmpty()) {
			throw new ValidationException("validation.empty", "participantes", "field.participantes");
		}

		List<DetalhamentoCargoCompetenciaTO> listaDetCargoComp = new ArrayList<>();//lista de DetalhamentoCargoCompetencia aplicaveis ao cargo.

		logger.info(String.format("Ordenando os participantes da avaliação por cargo ..."));
		Collections.sort(participantes, new Comparator<ParticipanteTO>() {
			@Override
			public int compare(ParticipanteTO o1, ParticipanteTO o2) {
				int result = o1.getAvaliado().getCargo().getNome().compareTo(o2.getAvaliado().getCargo().getNome());
				return result;
			}
		});

		logger.info(String.format("Pesquisando os tipos de competências e os agentes da avaliação ..."));
		List<AvaliacaoTipoCompetenciaTO> listaTiposCompetencia = avaliacaoTipoCompetenciaService.obterAvaliacaoTipoCompetenciaProjections(avaliacao.getId());

		int contador = 0;
		Long idCargoAnterior = null;
		Set<CompetenciaDetalhamentoTO> detalhamentosSalvos = new HashSet<>();//lista de perfis executados por cargo
		Map<TipoAvaliacaoPerfil, List<DetalhamentoCargoCompetenciaTO>> mapaPerguntasPorPerfilAgente = new HashMap<>();

		logger.info(String.format("A lista de participantes contém [%d] itens ...", participantes.size()));

		List<PendenciaTO> pendencias = new ArrayList<>();

		for (int i = 0; i < participantes.size(); i++) {
			ParticipanteTO participante = participantes.get(i);
			contador = i + 1;
			logger.info(String.format("Iniciando item %s de [%d]...", contador, participantes.size()));
			logger.info(String.format("Participante = { id: %s} , Avaliado = {nome: %s, cargo: %s} , Avaliador = {nome: %s, perfil: %s}  ...", participante.getId()
				, participante.getAvaliado().getNome(), participante.getAvaliado().getCargo().getNome(), participante.getAvaliador().getNome(), participante.getPerfilAgente().getNome()));

			boolean possuiPendencia = false;

			if (idCargoAnterior == null || !idCargoAnterior.equals(participante.getAvaliado().getCargo().getId())) {
				listaDetCargoComp.clear();
				detalhamentosSalvos.clear();
				mapaPerguntasPorPerfilAgente.clear();

				logger.info(String.format("Pesquisando as perguntas ..."));
				listaDetCargoComp = detalhamentoCargoCompetenciaService.findDetalhamentosAplicaveisAoCargo(participante.getAvaliado().getCargo().getId(), Situacao.ATIVA);

				if (listaDetCargoComp.isEmpty()) {
					logger.info(String.format("não há detalhamentos aplicáveis ao cargo %s ...", participante.getAvaliado().getCargo()));
					PendenciaTO pendencia = new PendenciaTO("cargo-not-found-gabarito", "não possui detalhamentos aplicáveis ao cargo", TipoPendencia.CARGO, participante.getAvaliado().getCargo(), new Date());
					pendencias.add(pendencia);
					possuiPendencia = true;
				}

				mapaPerguntasPorPerfilAgente = cargoService.agruparPerguntasPorPerfilAgente(listaDetCargoComp, participante.getAvaliado().getCargo(), avaliacao, listaTiposCompetencia);

				idCargoAnterior = participante.getAvaliado().getCargo().getId();
			}

			logger.info(String.format("Perguntas do perfil %s ...", participante.getPerfilAgente().getNome()));
			List<DetalhamentoCargoCompetenciaTO> perguntas = mapaPerguntasPorPerfilAgente.get(participante.getPerfilAgente());

			if (perguntas.isEmpty()) {
				logger.info(String.format("não há perguntas para avaliadores com perfil o %s para o cargo de %s ...", participante.getPerfilAgente().getNome(), participante.getAvaliado().getCargo()));
				PendenciaTO pendencia = new PendenciaTO("cargo-not-found-gabarito", "não há perguntas para avaliadores com perfil" + participante.getPerfilAgente().getNome(), TipoPendencia.CARGO, participante.getAvaliado().getCargo(), new Date());
				pendencias.add(pendencia);
				possuiPendencia = true;
			} else {
				possuiPendencia = validarDimensao(avaliacao, pendencias, participante, possuiPendencia, perguntas);
			}

			if (possuiPendencia == false) {
				if (participante.getIdCargoAvaliado() == null)
					participante.setIdCargoAvaliado(participante.getAvaliado().getCargo().getId());

				if (participante.getNomeCargoAvaliado() == null)
					participante.setNomeCargoAvaliado(participante.getAvaliado().getCargo().getNome());

				if (participante.getIdDeptoAvaliado() == null)
					participante.setIdDeptoAvaliado(participante.getAvaliado().getDepartamento().getId());

				if (participante.getNomeDeptoAvaliado() == null)
					participante.setNomeDeptoAvaliado(participante.getAvaliado().getDepartamento().getNome());

				avaliacaoParticipanteService.salvarPerguntas(participante, perguntas, detalhamentosSalvos);
				avaliacaoParticipanteService.update(participante, participante.getId());
			} else {
				salvarPendencias(pendencias, avaliacao);
				pendencias.clear();
			}

			logger.info(String.format("Finalizando item %s de [%d]...", contador, participantes.size()));
			logger.info(String.format("---------------------------------------"));
		}

		avaliacao.setStatus(SituacaoAvaliacao.LIBERADA);
		avaliacaoRepository.updateStatus(SituacaoAvaliacao.LIBERADA, avaliacao.getId());

		Date dataFim = new Date();
		System.out.println("Finalizando a liberação no dia " + dateFormatter.format(dataFim) + " na hora " + horaFormatter.format(dataFim) + " ...");

		long delay = System.currentTimeMillis() - start;
		long segundos = (delay / 1000) % 60;
		long minutos = (delay / 60000) % 60;
		long horas = delay / 3600000;// 3600000 = 60 * 60 * 1000
		logger.info(String.format("O método liberarAvaliacao executou em %03d:%02d:%02d ", horas, minutos, segundos));
	}

	private boolean validarDimensao(AvaliacaoTO avaliacao, List<PendenciaTO> pendencias, ParticipanteTO participante, boolean possuiPendencia, List<DetalhamentoCargoCompetenciaTO> perguntas) {
		if (avaliacao.getCampanha().getDimensao() != null && avaliacao.getCampanha().getDimensao()) {
			List<AvaliacaoDimensaoTO> listaAvaliacaoDimensao = avaliacaoDimensaoService.obterDimensaoPorAvaliacao(avaliacao);
			List<DetalhamentoCargoCompetenciaTO> listaDetDim = new ArrayList<>();

			for (AvaliacaoDimensaoTO avaliacaoDimensaoTO : listaAvaliacaoDimensao) {
				List<DimensaoCompetenciaDetalhamentoTO> listaDetalhamentosDimensao = dimensaoCompetenciaDetalhamentoService.obterDetalhamentosPorDimensao(avaliacaoDimensaoTO.getDimensao());

				for (DimensaoCompetenciaDetalhamentoTO dimCompDetTO : listaDetalhamentosDimensao) {

					for (DetalhamentoCargoCompetenciaTO dccTO : perguntas) {
						if (dccTO.getCompetenciaDetalhamento().getId().equals(dimCompDetTO.getCompetenciaDetalhamento().getId())) {
							try {
								listaDetDim.add(dccTO.clone());
								break;
							} catch (CloneNotSupportedException cnse) {
								throw new ValidationException("avaliacao-participante", "error-clone-detalhamento");
							}
						}
					}
				}
			}

			if (listaDetDim.isEmpty()) {
				PendenciaTO pendencia = new PendenciaTO("cargo-not-found-gabarito-dimensao", "não possui detalhamentos aplicáveis ao cargo - dimensão", TipoPendencia.CARGO, participante.getAvaliado().getCargo(), new Date());
				pendencias.add(pendencia);
				possuiPendencia = true;
			} else {
				perguntas.clear();
				perguntas.addAll(listaDetDim);
			}
		}
		return possuiPendencia;
	}


	private void removerPendencias(Long idAvaliado, Long idCargo, AvaliacaoTO avaliacao) {
		List<Pendencia> pendenciasResolvidas = pendenciaRepository.findByAvaliacaoIdAndCargoIdOrAvaliadoId(avaliacao.getId(), idCargo, idAvaliado);

		if (pendenciasResolvidas != null && !pendenciasResolvidas.isEmpty()) {
			pendenciaRepository.delete(pendenciasResolvidas);
		}
	}

	private void removerPendencias(List<PendenciaTO> pendencias, AvaliacaoTO avaliacao) {
		if (avaliacao.getPendencias() != null && !avaliacao.getPendencias().isEmpty()) {
			for (PendenciaTO pendencia : avaliacao.getPendencias()) {
				pendencia.setAvaliacao(avaliacao);

				if (!contemPendencia(pendencia, pendencias)) {
					pendenciaService.delete(pendencia);
				}
			}
		}
	}

	private void salvarPendencias(List<PendenciaTO> pendencias, AvaliacaoTO avaliacao) {
		if (pendencias != null && !pendencias.isEmpty()) {
			for (PendenciaTO pendencia : pendencias) {
				pendencia.setAvaliacao(avaliacao);

				if (!contemPendencia(pendencia, avaliacao.getPendencias())) {
					pendenciaService.save(pendencia);
				}
			}
		}
	}

	private boolean contemPendencia(PendenciaTO pendenciaNova, List<PendenciaTO> pendenciasAntigas) {
		boolean contem = false;

		if (pendenciasAntigas != null && !pendenciasAntigas.isEmpty()) {
			for (PendenciaTO pendenciaAntiga : pendenciasAntigas) {
				if (pendenciaNova.getTipoPendencia().equals(pendenciaAntiga.getTipoPendencia()) && pendenciaNova.getCodigoErro().equals(pendenciaAntiga.getCodigoErro())) {
					if (TipoPendencia.COLABORADORES.equals(pendenciaNova.getTipoPendencia()) && pendenciaNova.getAvaliado().getId().equals(pendenciaAntiga.getAvaliado().getId())) {
						contem = true;
					} else if (TipoPendencia.CARGO.equals(pendenciaNova.getTipoPendencia()) && pendenciaNova.getCargo().getId().equals(pendenciaAntiga.getCargo().getId())) {
						contem = true;
					}
				}
			}
		}

		return contem;
	}

	private PendenciaTO contemPendencia(Long idAvaliado, Long idCargo, List<PendenciaTO> pendencias) {
		PendenciaTO resultado = null;

		if (pendencias != null && !pendencias.isEmpty()) {
			for (PendenciaTO pendenciaTO : pendencias) {
				if (TipoPendencia.COLABORADORES.equals(pendenciaTO.getTipoPendencia()) && idAvaliado.equals(pendenciaTO.getAvaliado().getId())) {
					resultado = pendenciaTO;
				} else if (TipoPendencia.CARGO.equals(pendenciaTO.getTipoPendencia()) && idCargo.equals(pendenciaTO.getCargo().getId())) {
					resultado = pendenciaTO;
				}
			}
		}

		return resultado;
	}


	private List<ParticipanteTO> criarAvaliadores(ParticipanteTO avaliado, AvaliacaoTO avaliacao) {
		List<ParticipanteTO> avaliadores = new ArrayList<>();

		try {
			FuncionarioTO funcTO = funcionarioService.findById(avaliado.getAvaliado().getId());
			avaliado.setLimiteAv1(avaliacao.getLimiteAv1());
			avaliado.setLimiteAv2(avaliacao.getLimiteAv2());
			avaliado.setLimiteAv3(avaliacao.getLimiteAv3());
			if (avaliacao.getGestores()) {
				avaliado.setAvaliado(mapper.map(funcTO, FuncionarioSimpleTO.class));
				FuncionarioSimpleTO avaliador = avaliadorService.findAvaliador(avaliado.getAvaliado());

				if (avaliador != null && avaliador.getId() != null && avaliador.getSituacaoFuncional() == SituacaoFuncionario.EM_ATIVIDADE) {
					avaliado.setRevisado(true);
					avaliado.setAvaliador(avaliador);
					avaliado.setPerfilAgente(TipoAvaliacaoPerfil.GESTOR);
					avaliadores.add(avaliado);
				}
			}

			//obtem os subordinados do avaliado
			if (avaliacao.getSubordinados()) {
				List<ParticipanteTO> subordinados = obterSubordinadosPorAvaliado(avaliado.getAvaliado(), avaliacao);

				if (subordinados != null && !subordinados.isEmpty())
					avaliadores.addAll(subordinados);
			}

			if (avaliacao.getGestores() == false && avaliacao.getSubordinados() == false) {
				avaliado.setAvaliador(avaliado.getAvaliado().clone());
				avaliado.setRevisado(true);

				if (avaliacao.getAutoavaliacao() == true)
					avaliado.setPerfilAgente(TipoAvaliacaoPerfil.AUTOAVALIACAO);
				else
					avaliado.setPerfilAgente(TipoAvaliacaoPerfil.PARES);

				avaliadores.add(avaliado.clone());
			}
		} catch (CloneNotSupportedException e) {
			throw new ValidationException("avaliacao-participante", "error-clone-participante");
		}

		return avaliadores;
	}


	private List<ParticipanteTO> atualiazarAvalidores(List<ParticipanteTO> avaliadores, ParticipanteTO avaliado, AvaliacaoTO avaliacao) {
		//Se a avaliação exige o perfil de subordinado então é verificado se tem algum avaliador com perfil de subordinado, caso não tenha será adicionado.
		List<ParticipanteTO> subordinados = new ArrayList<>();
		if (avaliacao.getSubordinados() == true) {
			subordinados = obterSubordinadosPorAvaliado(avaliado.getAvaliado(), avaliacao);
			if (subordinados != null && !subordinados.isEmpty()) {
				for (ParticipanteTO subordinado : subordinados) {
					boolean achou = false; //utilizado para verificar se um subordinado já faz parte da lista de avaliadores.
					for (ParticipanteTO avaliador : avaliadores) {
						if (avaliador.getAvaliador().getId().equals(subordinado.getAvaliador().getId())
								&& avaliador.getAvaliado().getId().equals(subordinado.getAvaliado().getId()))
							achou = true;
					}
					if (!achou)
						avaliadores.add(subordinado);
				}
			}
		}

		if (avaliacao.getGestores() == true) {
			boolean criarAvaliador = true;

			List<Participante> avaliadoresPerfilGestor = avaliacaoParticipanteRepository.findByAvaliadorPorAvaliadoAndAvaliacaoAndTipo(avaliado.getAvaliado().getId(), avaliacao.getId(), TipoAvaliacaoPerfil.GESTOR);

			if (avaliadoresPerfilGestor != null && !avaliadoresPerfilGestor.isEmpty() || (avaliado.getPerfilAgente().equals(TipoAvaliacaoPerfil.GESTOR) && !subordinados.isEmpty())) {
				criarAvaliador = false;
			}


			if (criarAvaliador == true) {
				FuncionarioSimpleTO avaliador = avaliadorService.findAvaliador(avaliado.getAvaliado());
				//Apenas pode existir um par de avaliado/avaliador por avaliação no sistema!
				List<ParticipanteTO> avaliadoresExistentes = avaliacaoParticipanteService.obterParticipanteProjections(avaliado.getAvaliado().getId(), avaliador.getId(), null, avaliacao.getId());
				if (!avaliadoresExistentes.isEmpty()) {
					throw new ValidationException("validation.already-exists", "participantes", avaliadoresExistentes.get(0).getAvaliado().getNome(), avaliador.getNome(), avaliadoresExistentes.get(0).getPerfilAgente());
				}
				if (avaliador != null && avaliador.getId() != null && avaliador.getSituacaoFuncional() == SituacaoFuncionario.EM_ATIVIDADE) {
					ParticipanteTO participante = new ParticipanteTO();
					participante.setAvaliacao(avaliacao);
					participante.setAvaliado(avaliado.getAvaliado());
					participante.setAvaliador(avaliador);
					participante.setPerfilAgente(TipoAvaliacaoPerfil.GESTOR);
					participante.setLimiteAv2(avaliacao.getLimiteAv2());

					if (avaliacao.getLimiteAv1() != null)
						participante.setLimiteAv1(avaliacao.getLimiteAv1());

					if (avaliacao.getLimiteAv3() != null)
						participante.setLimiteAv3(avaliacao.getLimiteAv3());

					participante.setRevisado(true);
					avaliadores.add(participante);
				}
			}
		}

		return avaliadores;
	}

	private void saveOrUpdateParticipantes(List<ParticipanteTO> participantes, AvaliacaoTO avaliacao) {

		// Atualiza ou grava os participantes recebidos
		for (ParticipanteTO participante : participantes) {

			if (SituacaoAvaliacao.LIBERADA.equals(avaliacao.getStatus()) && !participante.getRevisado()) {
				throw new ValidationException("avaliacao-participante", "error-liberacao-revisados");
			}

			participante.setAvaliacao(avaliacao);

			if (participante.getConsensoIniciado() == null) {
				participante.setConsensoIniciado(Boolean.FALSE);
			}

			if (participante.getConsensoLiberado() == null) {
				participante.setConsensoLiberado(Boolean.FALSE);
			}

			// Segundo a tela, ao alterar a data limite da avaliação, as datas dos participantes também devem ser modificadas.
			participante.setLimiteAv1(avaliacao.getLimiteAv1());
			participante.setLimiteAv2(avaliacao.getLimiteAv2());
			participante.setLimiteAv3(avaliacao.getLimiteAv3());
			participante.setLimiteAv4(avaliacao.getLimiteAv4());
			avaliacaoParticipanteService.saveOrUpdate(participante);
		}
	}

	private ParticipanteTO saveOrUpdateParticipante(ParticipanteTO participante, AvaliacaoTO avaliacao) {
		participante.setAvaliacao(avaliacao);

		if (participante.getConsensoIniciado() == null) {
			participante.setConsensoIniciado(Boolean.FALSE);
		}

		if (participante.getConsensoLiberado() == null) {
			participante.setConsensoLiberado(Boolean.FALSE);
		}

		// Segundo a tela, ao alterar a data limite da avaliação, as datas dos participantes também devem ser modificadas.
		participante.setLimiteAv1(avaliacao.getLimiteAv1());
		participante.setLimiteAv2(avaliacao.getLimiteAv2());
		participante.setLimiteAv3(avaliacao.getLimiteAv3());
		participante.setLimiteAv4(avaliacao.getLimiteAv4());
		return avaliacaoParticipanteService.saveOrUpdate(participante);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void removerAvaliado(List<ParticipanteTO> listaParticipantes, AvaliacaoTO avaliacao) {
		// Só processa se o frontend enviou a lista de participantes, caso contrário é uma tela que não os altera
		if (listaParticipantes != null && !listaParticipantes.isEmpty()) {
			FuncionarioSimpleTO avaliado = listaParticipantes.get(0).getAvaliado();

			List<ParticipanteTO> participantes = new ArrayList<>();
			List<ParticipanteTO> oldParticipantes = avaliacaoParticipanteService.obterParticipantesPorAvaliadoEAvaliacao(avaliado.getId(), avaliacao.getId());

			// Encontra os participantes que foram removidos
			List<ParticipanteTO> removidos = ListUtils.subtract(oldParticipantes, participantes);
			for (ParticipanteTO participante : removidos) {
				avaliacaoParticipanteService.delete(participante);
			}

		}
	}

	private void atualizarParticipantesPorAvaliado(AvaliacaoTO avaliacao, ParticipanteTO avaliado, List<ParticipanteTO> listaAvaliadores) {
		//se o usuário marcar um avaliado com revisado na "aba avaliadores", todos os
		//avaliadores de avaliado devem ser marcados com revisados também.
		//verifica se tem algum avaliador com perfil de subordinado

		if (listaAvaliadores != null && !listaAvaliadores.isEmpty()) {

			TipoAvaliacaoPerfil tipoSubordinado = TipoAvaliacaoPerfil.SUBORDINADO;


			boolean possuiSubordinado = false;
			for (ParticipanteTO avaliador : listaAvaliadores) {
				if (avaliado != null && avaliado.getRevisado() != null)
					avaliador.setRevisado(avaliado.getRevisado());
				else if (SituacaoAvaliacao.LIBERADA.equals(avaliacao.getStatus()))
					avaliador.setRevisado(true);
				else if (avaliador.getRevisado() == null)
					avaliador.setRevisado(false);


				if (avaliador.getPerfilAgente().equals(tipoSubordinado))
					possuiSubordinado = true;

			}

			// TODO Pensar na remoção de subordinados. Esse fluxo pode ocorrer quando o usuário desmarcar a opção subordinados da avaliação.


			//Se não tiver nenhum avaliador com perfil subordinado e a avaliação exige esse perfil então será adicionado.
			if (possuiSubordinado == false && avaliacao.getSubordinados() == true) {
				List<ParticipanteTO> subordinados = obterSubordinadosPorAvaliado(avaliado.getAvaliado(), avaliacao);

				if (subordinados != null && !subordinados.isEmpty())
					listaAvaliadores.addAll(subordinados);
			}

		}
	}

	private List<ParticipanteTO> criarParticipantesPorAvaliado(AvaliacaoTO avaliacao, ParticipanteTO avaliado) {
		List<ParticipanteTO> lista = new ArrayList<>();

		try {

			if (avaliado.getAvaliador() != null && avaliado.getAvaliador().getId() != null) {
				avaliado.setRevisado(true);
				lista.add(avaliado.clone());
			}

			//obtem os subordinados do avaliado
			if (avaliacao.getSubordinados()) {
				List<ParticipanteTO> subordinados = obterSubordinadosPorAvaliado(avaliado.getAvaliado(), avaliacao);

				if (subordinados != null && !subordinados.isEmpty())
					lista.addAll(subordinados);
			}

		} catch (CloneNotSupportedException e) {
			throw new ValidationException("avaliacao-participante", "error-clone-participante");
		}

		return lista;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void removerTodosParticipantes(AvaliacaoTO avaliacaoTO) {
		List<ParticipanteTO> listaParticipantes = avaliacaoParticipanteService.obterListaParticipantesPorAvaliacao(avaliacaoTO);

		for (ParticipanteTO participanteTO : listaParticipantes) {
			avaliacaoParticipanteService.delete(participanteTO);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void removeragliberacao(Long id) {

		Avaliacao avaliacao = avaliacaoRepository.findOne(id);

		if (SituacaoAvaliacao.AG_LIBERACAO.equals(avaliacao.getStatus())) {

			avaliacaoParticipanteService.removerParticipanteAgLiberacao(id);
			mensageriaService.removerMensageria(id);

			//TODO: Criar serviço
			List<AvaliacaoTipoCompetencia> avaliacaoTipoCompetencias = avaliacaoTipoCompetenciaRepository.findByAvaliacaoId(id);

			for (AvaliacaoTipoCompetencia avaliacaoTipoCompetencia : avaliacaoTipoCompetencias) {
				avaliacaoTipoCompetenciaRepository.delete(avaliacaoTipoCompetencia);
			}

			List<Pendencia> pendencias = pendenciaRepository.findByAvaliacaoId(id);

			for (Pendencia pendencia : pendencias) {
				pendenciaRepository.delete(pendencia);
			}

			avaliacaoRepository.delete(id);
		} else {
			throw new ValidationException("avaliacao", "error-status-remover-avaliacao", avaliacao.getDescricaoAbreviada());
		}
	}

	@Override
	public Long count(CampanhaTO campanhaTO) {
		return avaliacaoRepository.count(QAvaliacao.avaliacao.campanha.id.eq(campanhaTO.getId()));
	}

	@Override
	public List<ParticipanteTO> obterSubordinadosPorAvaliado(FuncionarioSimpleTO avaliado, AvaliacaoTO avaliacao) {
		List<ParticipanteTO> subordinados = new ArrayList<>();

		FuncionarioTO avaliadoTO = funcionarioService.findById(avaliado.getId());

		List<FuncionarioTO> listaFuncionarios = funcionarioService.obterSubordinados(avaliadoTO);

		for (FuncionarioTO func : listaFuncionarios) {
			if (func.getSituacaoFuncional() == SituacaoFuncionario.EM_ATIVIDADE) {
				ParticipanteTO participante = new ParticipanteTO();
				participante.setAvaliacao(avaliacao);
				participante.setAvaliado(avaliado);
				participante.setAvaliador(mapSimple(func));
				participante.setPerfilAgente(TipoAvaliacaoPerfil.SUBORDINADO);
				participante.setLimiteAv2(avaliacao.getLimiteAv2());
				participante.setRevisado(true);

				subordinados.add(participante);
			}
		}

		return subordinados;
	}

	/*@Override
	public ParticipanteTO criarParticipante(FuncionarioSimpleTO avaliado, AvaliacaoTO avaliacao) {

		FuncionarioTO avaliadoTO = funcionarioService.findById(avaliado.getId());

		List<FuncionarioTO> listaFuncionarios = funcionarioService.obterSubordinados(avaliadoTO);

		for (FuncionarioTO func : listaFuncionarios) {
			if (func.getSituacaoFuncional() == SituacaoFuncionario.EM_ATIVIDADE) {
				ParticipanteTO participante = new ParticipanteTO();
				participante.setAvaliacao(avaliacao);
				participante.setAvaliado(avaliado);
				participante.setAvaliador(mapSimple(func));
				participante.setPerfilAgente(TipoAvaliacaoPerfil.SUBORDINADO);
				participante.setLimiteAv2(avaliacao.getLimiteAv2());
				participante.setRevisado(true);

				subordinados.add(participante);
			}
		}

		return subordinados;
	}*/

	/**
	 * Ajustar, remover  ou adicionar, tipos de competências de uma avalição.
	 *
	 * @param entity A entidade original.
	 * @param to     O TO vindo do front-end com os tipos de competências atualizados,
	 */
	private boolean ajustarTipoCompetenciasDaAvaliacao(Avaliacao entity, AvaliacaoTO to) {
		int contador = 0;

		// Lista que guardará os id dos tipos que serão removidos.
		Set<Long> listaParaDeletar = new TreeSet<Long>();

		/*
		 * Neste momento iremos verificar se o tipo está na lista do TO. Caso contrário,
		 * iremos remover o tipo não usado na avaliação.
		 */
		for (AvaliacaoTipoCompetencia avaliacaoTipoCompetencia : avaliacaoTipoCompetenciaRepository.findByAvaliacaoId(entity.getId())) {

			boolean remover = Boolean.TRUE;

			for (AvaliacaoTipoCompetenciaTO tipoCompetenciaTO : to.getAgentes()) {
				if (avaliacaoTipoCompetencia.getId().equals(tipoCompetenciaTO.getId())) {
					remover = Boolean.FALSE;
					contador++;
					// Estando na lista de agentes, não remover da base de dados.
					break;
				}
				if (remover) {
					listaParaDeletar.add(avaliacaoTipoCompetencia.getId());
				}
			}

		}

		listaParaDeletar.forEach(id -> {
			avaliacaoTipoCompetenciaRepository.delete(id);
		});

		to.getAgentes().forEach(agente -> {
			AvaliacaoTipoCompetencia avaliacaoTipoCompetencia = new AvaliacaoTipoCompetencia();
			avaliacaoTipoCompetencia.setId(agente.getId());
			avaliacaoTipoCompetencia.setAvaliacao(entity);
			avaliacaoTipoCompetencia.setTipoCompetencia(tipoCompetenciaRepository.findOne(agente.getTipoCompetencia().getId()));
			avaliacaoTipoCompetencia.setPerfilAgente(TipoAvaliacaoPerfil.valueOf(agente.getPerfilAgente().getCode()));
			avaliacaoTipoCompetencia.setPeso(agente.getPeso());

			avaliacaoTipoCompetenciaRepository.save(avaliacaoTipoCompetencia);
		});

		return !(contador == to.getAgentes().size());
	}


	/**
	 * Ajustar, remover ou adicionar, dimensões de uma avalição.
	 *
	 * @param entity A entidade original.
	 * @param to     O TO vindo do front-end com as dimensões atualizadas,
	 */
	private void ajustarDimensoesDaAvaliacao(Avaliacao entity, AvaliacaoTO to) {

		// Lista que guardará os id dos tipos que serão removidos.
		Set<AvaliacaoDimensaoTO> listaParaDeletar = new TreeSet<AvaliacaoDimensaoTO>();

		/*
		 * Neste momento iremos verificar se o tipo está na lista do TO. Caso contrário,
		 * iremos remover o tipo não usado na avaliação.
		 */
		for (AvaliacaoDimensaoTO avaliacaoDimensaoTO : avaliacaoDimensaoService.obterDimensaoPorAvaliacao(to)) {

			boolean remover = Boolean.TRUE;

			for (AvaliacaoDimensaoTO dimensaoTO : to.getListaDimensao()) {
				if (avaliacaoDimensaoTO.getId().equals(dimensaoTO.getId())) {
					remover = Boolean.FALSE;
					// Estando na lista de dimensão, não remover da base de dados.
					break;
				}
			}
			if (remover) {
				listaParaDeletar.add(avaliacaoDimensaoTO);
			}

		}

		listaParaDeletar.forEach(id -> {
			avaliacaoDimensaoService.delete(id);
		});

		to.getListaDimensao().forEach(avDimensao -> {
			AvaliacaoDimensaoTO avaliacaoDimensao = new AvaliacaoDimensaoTO();
			avaliacaoDimensao.setId(avDimensao.getId());
			avaliacaoDimensao.setAvaliacao(to);
			avaliacaoDimensao.setDimensao(dimensaoService.load(avDimensao.getDimensao().getId()));

			if (avDimensao.getId() == null) {
				avaliacaoDimensaoService.save(avaliacaoDimensao);
			} else {
				avaliacaoDimensaoService.update(avaliacaoDimensao, avDimensao.getId());
			}
		});

	}

	public void removeParticipantesQueNaoPossuiTipoCompetenciaSelecionadoNaAvaliacao(Avaliacao entity, AvaliacaoTO object) {
		Set<Long> listaRemover = new TreeSet<Long>();

		List<Participante> participantes = avaliacaoParticipanteRepository.findByAvaliacaoId(entity.getId());

		List<PendenciaTO> pendencias = new ArrayList<>();
		//chave: código do cargo, valor:lista com detalhamentos
		Map<Long, List<DetalhamentoCargoCompetenciaTO>> detalhamentosPorCargo = new HashMap<>();

		List<AvaliacaoTipoCompetenciaTO> idsTiposCompetencia = avaliacaoTipoCompetenciaService.findIdsTiposCompetenciaByAvaliacaoId(object.getId());

		for (Participante part : participantes) {
			ParticipanteTO participante = mapper.map(part, ParticipanteTO.class);

			if (!SituacaoFuncionario.EM_ATIVIDADE.equals(participante.getAvaliado().getSituacaoFuncional())) {
				PendenciaTO pendencia = new PendenciaTO("avaliado-situacao-funcional", "o avaliado está com a situação funcional diferente de em atividade", TipoPendencia.COLABORADORES, participante.getAvaliado(), participante.getAvaliador(), new Date());
				pendencias.add(pendencia);
			}

			if (!detalhamentosPorCargo.containsKey(participante.getAvaliado().getCargo().getId())) {
				pendencias = cargoService.validarParaLiberacao(participante.getAvaliado().getCargo(), idsTiposCompetencia, detalhamentosPorCargo, object);
			}

			if (pendencias != null && !pendencias.isEmpty()) {
				if (participante.getId() != null) {
					listaRemover.add(part.getId());
					pendencias.clear();
				}
			}
		}

		listaRemover.forEach(id -> {
			avaliacaoParticipanteRepository.delete(id);
		});
	}

	public void calcularPreenchimentoTotalDaAvaliacao(AvaliacaoTO avaliacao) {

		Long totalpreenchimento = 0L;

		Long totalav1 = 0L, totalav2 = 0L, totalav3 = 0L, preenchidaAv1 = 0L, preenchidaAv2 = 0L, preenchidaAv3 = 0L;

		totalav1 = totalav1 + preenchimentoService.count(//QPreenchimento.preenchimento.participante.id.eq(participante.getId())
			QPreenchimento.preenchimento.participante.limiteAv1.isNotNull()
				.and(QPreenchimento.preenchimento.participante.avaliacao.id.eq(avaliacao.getId())));

		totalav2 = totalav2 + preenchimentoService.count(//QPreenchimento.preenchimento.participante.id.eq(participante.getId())
			QPreenchimento.preenchimento.participante.limiteAv2.isNotNull()
				.and(QPreenchimento.preenchimento.participante.avaliacao.id.eq(avaliacao.getId())));

		totalav3 = totalav3 + preenchimentoService.count( //QPreenchimento.preenchimento.participante.id.eq(participante.getId())
			QPreenchimento.preenchimento.participante.limiteAv3.isNotNull()
				.and(QPreenchimento.preenchimento.participante.avaliacao.id.eq(avaliacao.getId())));

		preenchidaAv1 = avaliacaoParticipanteRepository.countPreenchimentoAv1ByAvaliacao(avaliacao.getId());
		preenchidaAv2 = avaliacaoParticipanteRepository.countPreenchimentoAv2ByAvaliacao(avaliacao.getId());
		preenchidaAv3 = avaliacaoParticipanteRepository.countPreenchimentoAv3ByAvaliacao(avaliacao.getId());

		if (totalav1 != 0 || totalav2 != 0 || totalav3 != 0)
			totalpreenchimento = (preenchidaAv1 + preenchidaAv2 + preenchidaAv3) * 100 / (totalav1 + totalav2 + totalav3);

		avaliacao.setPercentualTotalPreenchimento(totalpreenchimento);
	}

	@Override
	public AvaliacaoSyncMobileTO obterAvaliacoesDoAvaliadorMobile(AvaliacaoSyncMobileTO avaliacao) {

		Long idFuncionarioLogado = getLoggedUser().getId();
		if (idFuncionarioLogado.equals(avaliacao.getIdAvaliador())) {

			List<AvaliacaoMobileTO> listaAvaliacao = new ArrayList<>();
			if (avaliacao.getDataUltimaSync() == null) {
				avaliacao.setDataUltimaSync(CalculadorDatas.converteStringParaDate("1970-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss"));
			}
			List<Object[]> listaAvaliacoesQuery = avaliacaoRepository.obterAvaliacoesMobile(
				avaliacao.getIdAvaliador(),
				avaliacao.getDataUltimaSync(),
				getLoggedUser().getEmpresa().getId());

			for (Object[] registro : listaAvaliacoesQuery) {
				AvaliacaoMobileTO avaliacaoMobileTO = new AvaliacaoMobileTO();
				avaliacaoMobileTO.setId(Long.parseLong(registro[0].toString()));
				avaliacaoMobileTO.setDescricaoAbreviada(registro[1].toString());

				listaAvaliacao.add(avaliacaoMobileTO);
			}
			avaliacao.setListaAvaliacoes(listaAvaliacao);
			avaliacao.setDataFimSync(new Date());
			return avaliacao;
		}
		return null;
	}

	@Override
	public List<AvaliacaoTO> obterAvaliacoesPDI() {
		List<ParticipanteTO> participanteTOList = avaliacaoParticipanteService.obterAvaliacaoParticipantePDI();
		List<AvaliacaoTO> avaliacaoTOList = participanteTOList
			.stream()
			.map(participanteTO -> participanteTO.getAvaliacao())
			.distinct()
			.collect(Collectors.toList());
		return avaliacaoTOList;
	}

	/*
	Este método é temporário até que os ajustes de desempenho e chamada assincrona da liberação sejam resolvidos
	Por questões de desempenho há trechos do código abaixo que não foram separados em chamadas a métodos,
	como por exemplo a parte que cuida da persistência da AvaliacaoCompetencia.
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	private void liberarHomeOffice(AvaliacaoTO avaliacao) {
		long start = System.currentTimeMillis();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat horaFormatter = new SimpleDateFormat("HH:mm");

		Avaliacao avaliacaoEntity = avaliacaoRepository.findOne(avaliacao.getId());

		List<PendenciaTO> pendencias = new ArrayList<>();

		Date dataInicio = new Date();
		logger.info("Iniciando a liberação no dia " + dateFormatter.format(dataInicio) + " na hora " + horaFormatter.format(dataInicio) + " ...");

		logger.info(String.format("Pesquisando os participantes da avaliação ..."));
		List<ParticipanteTO> participantes = avaliacaoParticipanteService.obterListaParticipantesPorAvaliacao(avaliacao);

		if (participantes == null || participantes.isEmpty()) {
			throw new ValidationException("participantes", "empty-participantes", "field.participantes");
		}

		logger.info(String.format("Pesquisando os tipos de competências e os agentes da avaliação ..."));
		List<AvaliacaoTipoCompetenciaTO> listaTiposCompetencia = avaliacaoTipoCompetenciaService.obterAvaliacaoTipoCompetenciaProjections(avaliacao.getId());

		//Para a avaliação de HomeOffice haverá somente um cargo, logo posso usar o primeiro participante como
		//referência e não será necessário separar por cargo
		ParticipanteTO participante = participantes.get(0);

		logger.info(String.format("Pesquisando as perguntas ..."));
		List<DetalhamentoCargoCompetenciaTO> listaDetCargoComp = detalhamentoCargoCompetenciaService.findDetalhamentosAplicaveisAoCargo(participante.getAvaliado().getCargo().getId(), Situacao.ATIVA);
		if (listaDetCargoComp.isEmpty()) {
			logger.info(String.format("não há perguntas para avaliadores com perfil o %s para o cargo de %s ...", participante.getPerfilAgente().getNome(), participante.getAvaliado().getCargo()));
			PendenciaTO pendencia = new PendenciaTO("cargo-not-found-gabarito", "não há perguntas para avaliadores com perfil" + participante.getPerfilAgente().getNome(), TipoPendencia.CARGO, participante.getAvaliado().getCargo(), new Date());
			pendencias.add(pendencia);
			salvarPendencias(pendencias, avaliacao);
		}

		if (pendencias.isEmpty()) { //não há pendências, então a liberação pode prosseguir
			Long idCargoAvaliado = participante.getAvaliado().getCargo().getId();
			String nomeCargoAvaliado = participante.getAvaliado().getCargo().getNome();
			Long idDepartamento = participante.getAvaliado().getDepartamento().getId();
			String nomeDepartamentoAvaliado = participante.getAvaliado().getDepartamento().getNome();

			//Salvando avaliacao_competencia e avaliacao_comp_det
			Set<CompetenciaTO> competencias = new HashSet<>();
			Map<CompetenciaTO, List<CompetenciaDetalhamentoTO>> mapCompetenciaDetalhamento = new HashMap();
			Set<AvaliacaoCompDet> setAvalCompDet = new HashSet<>();

			//Este map será utilizado para a inserção na tabela avaliacao_det_cargo_comp
			//Não está separado por cargo, pois a avaliação HomeOffice possui apenas um cargo
			Map<String, DetalhamentoCargoCompetenciaTO> mapDetCargoComp = new HashMap<>();

			//Separando as competências e o detalhamento das competências, para iserir avaliacao_competencia e avaliacao_comp_det
			for (DetalhamentoCargoCompetenciaTO dp : listaDetCargoComp) {
				competencias.add(dp.getCargoCompetencia().getCompetencia());
				if (!mapCompetenciaDetalhamento.containsKey(dp.getCargoCompetencia().getCompetencia()))
					mapCompetenciaDetalhamento.put(dp.getCargoCompetencia().getCompetencia(), new ArrayList<>());

				mapCompetenciaDetalhamento.get(dp.getCargoCompetencia().getCompetencia()).add(dp.getCompetenciaDetalhamento());

				String chave = dp.getCargoCompetencia().getCompetencia().getId() + "-" + dp.getCompetenciaDetalhamento().getId();
				mapDetCargoComp.put(chave, dp);
			}

			for (CompetenciaTO competenciaTO : competencias) {
				Long agora = System.currentTimeMillis();
				AvaliacaoCompetencia avalCompetencia = new AvaliacaoCompetencia();
				avalCompetencia.setAvaliacao(avaliacaoEntity);
				avalCompetencia.setIdCompetencia(competenciaTO.getId());
				avalCompetencia.setIdTipoCompetencia(competenciaTO.getTipo().getId());
				avalCompetencia.setDescricaoTipoCompetencia(competenciaTO.getTipo().getNome());
				avalCompetencia.setDescricaoAbreviada(competenciaTO.getDescricaoAbreviada());
				avalCompetencia.setDirecionamentoGestor(competenciaTO.getDirecionamentoGestor());
				avalCompetencia.setDirecionamentoNaoGestor(competenciaTO.getDirecionamentoNaoGestor());
				avalCompetencia.setAtualizadorOriginal(competenciaTO.getModifiedBy() == null ? competenciaTO.getCreatedBy() : competenciaTO.getModifiedBy());
				avalCompetencia.setDataAtualizacaoOriginal(competenciaTO.getModificationDate() == null ? competenciaTO.getCreationDate() : competenciaTO.getModificationDate());
				avalCompetencia.setCreatedBy(getLoggedUser().getNome());
				avalCompetencia.setCreationDate(new Date(System.currentTimeMillis()));
				avalCompetencia = avaliacaoCompetenciaRepository.save(avalCompetencia);

				for (CompetenciaDetalhamentoTO competenciaDetalhamentoTO : mapCompetenciaDetalhamento.get(competenciaTO)) {
					//Persistir AvaliacaoCompetencia
					AvaliacaoCompetencia avalComp = new AvaliacaoCompetencia();
					avalComp.setId(avalCompetencia.getId());

					AvaliacaoCompDet avalCompDet = new AvaliacaoCompDet();
					avalCompDet.setAvaliacaoCompetencia(avalComp);
					avalCompDet.setIdCompetenciaDetalhamento(competenciaDetalhamentoTO.getId());
					avalCompDet.setIdModeloGraduacao(competenciaDetalhamentoTO.getModeloGraduacao().getId());
					avalCompDet.setOrdem(competenciaDetalhamentoTO.getOrdem());
					avalCompDet.setDescricao(competenciaDetalhamentoTO.getDescricao());
					avalCompDet.setAtualizadorOriginal(competenciaDetalhamentoTO.getModifiedBy() == null ? competenciaDetalhamentoTO.getCreatedBy() : competenciaDetalhamentoTO.getModifiedBy());
					avalCompDet.setDataAtualizacaoOriginal(competenciaDetalhamentoTO.getModificationDate() == null ? competenciaDetalhamentoTO.getCreationDate() : competenciaDetalhamentoTO.getModificationDate());
					avalCompDet.setCreatedBy(getLoggedUser().getNome());
					avalCompDet.setCreationDate(new Date());
					avalCompDet = avaliacaoCompDetRepository.save(avalCompDet);

					setAvalCompDet.add(avalCompDet);

					String chave = competenciaTO.getId() + "-" + competenciaDetalhamentoTO.getId();
					DetalhamentoCargoCompetenciaTO dp = mapDetCargoComp.get(chave);
					CargoTipoCompetencia cargoTipoCompetencia = cargoTipoCompetenciaRepository.findByCargoIdAndTipoCompetenciaId(dp.getCargoCompetencia().getCargo().getId(), dp.getCargoCompetencia().getCompetencia().getTipo().getId());

					//Persistir AvalicaoDetCargoComp
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

					List<CompDetalhamentoHintTO> listaHints = compDetalhamentoHintService.obterCompDetalhamentoHintProjections(avalCompDet.getIdCompetenciaDetalhamento());

					for (CompDetalhamentoHintTO hint : listaHints) {
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
					}

					ModeloGraduacaoComp modeloGraduacaoComp = modeloGraduacaoCompRepository.findByIdModeloGraduacao(dp.getCompetenciaDetalhamento().getModeloGraduacao().getId());
					Long modeloGraduacaoCompId = null;
					if (modeloGraduacaoComp == null)
						modeloGraduacaoCompId = saveModeloGraduacaoComp(dp.getCompetenciaDetalhamento().getModeloGraduacao().getId(), avaliacaoEntity.getId());
					else
						modeloGraduacaoCompId = modeloGraduacaoComp.getId();


					ModeloGraduacaoAvalCompTO modeloGraduacaoAvalCompTO =
						modeloGraduacaoAvalCompService.obterModeloGraduacaoAvalCompProjections(avalComp.getId(),
							dp.getCompetenciaDetalhamento().getModeloGraduacao().getId(), null);

					if (modeloGraduacaoAvalCompTO == null) {
						ModeloGraduacaoComp modGrad = new ModeloGraduacaoComp();
						modGrad.setId(modeloGraduacaoCompId);
						ModeloGraduacaoAvalComp modeloGraduacaoAvalComp = new ModeloGraduacaoAvalComp();
						modeloGraduacaoAvalComp.setAvaliacaoCompetencia(avalCompetencia);
						modeloGraduacaoAvalComp.setModeloGraduacaoComp(modGrad);
						modeloGraduacaoAvalCompRepository.save(modeloGraduacaoAvalComp);
					}

					logger.info(String.format("Salvou Avaliação Competencia : [%d] ", System.currentTimeMillis() - agora));
				}
			}

			for (ParticipanteTO participanteTO : participantes) {
				Long agora = System.currentTimeMillis();
				for (AvaliacaoCompDet avalCompDet : setAvalCompDet) {
					Preenchimento preenchimento = new Preenchimento();
					Participante participanteEntity = new Participante();
					participanteEntity.setId(participanteTO.getId());
					preenchimento.setAvaliacaoCompDet(avalCompDet);
					preenchimento.setParticipante(participanteEntity);
					preenchimento.setTipoClient(TipoClient.WEB);
					preenchimento.setCreatedBy(getLoggedUser().getMatricula());
					preenchimento.setCreationDate(new Date());
					preenchimentoRepository.save(preenchimento);
				}
				logger.info(String.format("Salvou AvaliacaoParticipante : [%d] ", System.currentTimeMillis() - agora));
			}
		}

		avaliacao.setSituacao(SituacaoAvaliacao.LIBERADA);
		avaliacaoEntity.setStatus(SituacaoAvaliacao.LIBERADA);
		avaliacaoEntity.setEmLiberacao(false);
		avaliacaoRepository.save(avaliacaoEntity);

		Campanha campanha = campanhaRepository.findOne(avaliacaoEntity.getCampanha().getId());
		campanha.setAvEmLiberacao(false);
		campanhaRepository.save(campanha);

		mensageriaService.mensagemLiberarAvaliacaoConcluido(avaliacao);

		long delay = System.currentTimeMillis() - start;
		long segundos = (delay / 1000) % 60;
		long minutos = (delay / 60000) % 60;
		long horas = delay / 3600000;// 3600000 = 60 * 60 * 1000
		logger.info(String.format("O método liberarAvaliacao executou em %03d:%02d:%02d ", horas, minutos, segundos));
	}

	private Long saveModeloGraduacaoComp(Long idModeloGraduacao, Long idAvaliacao) {
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

		List<GraduacaoTO> graducoes = graduacaoService.obterGraduacoesProjections(idModeloGraduacao);

		for (GraduacaoTO grad : graducoes) {
			saveGraduacaoCompetencia(modeloGraduacaoComp, grad);
		}
		return modeloGraduacaoComp.getId();
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
			graduacaoComp.setCor(GraduacaoCorEnum.valueOf(graduacao.getOrdem()).getNome());
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

	@Override
	@Transactional
	public void ajustarAvalPesoCargoComp() {
		String msg = "%s o processo de ajuste de pesos das avaliações: %s";
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		System.out.println(String.format(msg, "Iniciando", sdf.format(new Date(System.currentTimeMillis()))));

		JPAQuery<Avaliacao> query = select(
			Projections.fields(
				Avaliacao.class,
				qAvaliacao.id)
			);

		List<Avaliacao> avaliacoes = query.fetch();

		avaliacoes.forEach(avaliacao -> {
			List<Participante> participantes = avaliacaoParticipanteRepository.findByAvaliacaoId(avaliacao.getId());

			participantes.forEach(participante -> {
				ajustarAvalPesoCargoComp(participante);
			});

		});

		System.out.println(String.format(msg, "Finalizando", sdf.format(new Date(System.currentTimeMillis()))));
	}

	private void ajustarAvalPesoCargoComp(Participante participante) {
		QPreenchimento qPreenchimento = QPreenchimento.preenchimento;

		List<PreenchimentoTO> preenchimentos = preenchimentoService.listByParticipanteSemAvalPesoCargoComp(participante.getId());

		if (preenchimentos != null && !preenchimentos.isEmpty()) {
			Map<Competencia, CargoCompetencia> mapCargoCompetencias = cargoCompetenciaRepository.findByCargoId(participante.getAvaliado().getCargo().getId())
					.stream().collect(Collectors.toMap(CargoCompetencia::getCompetencia, cargoCompetencia -> cargoCompetencia));

			Map<TipoCompetencia, CargoTipoCompetencia> mapTipoCargoCompetencias = cargoTipoCompetenciaRepository.findByCargoId(participante.getAvaliado().getCargo().getId())
					.stream().collect(Collectors.toMap(CargoTipoCompetencia::getTipoCompetencia, cargoTipoCompetencia -> cargoTipoCompetencia));

			List<CargoTipoCompetencia> cargoTipoCompetencias = cargoTipoCompetenciaRepository.findByCargoId(participante.getIdCargoAvaliado());

			if (preenchimentos != null && !preenchimentos.isEmpty()) {
				preenchimentos.forEach(preenchimentoTO -> {
					Competencia competencia = new Competencia();
					competencia.setId(preenchimentoTO.getAvaliacaoCompDet().getAvaliacaoCompetencia().getIdCompetencia());

					TipoCompetencia tipoCompetencia = new TipoCompetencia();
					tipoCompetencia.setId(preenchimentoTO.getAvaliacaoCompDet().getAvaliacaoCompetencia().getIdTipoCompetencia());

					AvalPesoCargoComp avalPesoCargoComp = new AvalPesoCargoComp();
					avalPesoCargoComp.setIdAvalCompetencia(preenchimentoTO.getAvaliacaoCompDet().getAvaliacaoCompetencia().getId());
					avalPesoCargoComp.setIdAvaliacao(participante.getAvaliacao().getId());
					avalPesoCargoComp.setIdCargo(participante.getAvaliado().getCargo().getId());
					avalPesoCargoComp.setIdPreenchimento(preenchimentoTO.getId());

					if (mapCargoCompetencias.keySet().contains(competencia))
						avalPesoCargoComp.setPesoCargoCompetencia(mapCargoCompetencias.get(competencia).getPeso());
					else {
						avalPesoCargoComp.setPesoCargoCompetencia(1L);
					}

					if (mapTipoCargoCompetencias.keySet().contains(tipoCompetencia))
						avalPesoCargoComp.setPesoCargoTipoCompetencia(mapTipoCargoCompetencias.get(tipoCompetencia).getPeso());
					else {
						avalPesoCargoComp.setPesoCargoTipoCompetencia(1L);
					}

					avalPesoCargoCompRepository.save(avalPesoCargoComp);
				});
			}
		}

	}
}


