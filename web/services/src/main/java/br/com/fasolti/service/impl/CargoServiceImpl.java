package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Cargo;
import br.com.fasolti.avd.domain.CargoTipoCompetencia;
import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.domain.QCargo;
import br.com.fasolti.avd.repository.CargoCompetenciaRepository;
import br.com.fasolti.avd.repository.CargoRepository;
import br.com.fasolti.avd.repository.FuncionarioRepository;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.simple.CargoSimpleTO;
import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.avd.types.TipoPendencia;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseDeziService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CargoServiceImpl extends BaseDeziService<Cargo, CargoTO> implements CargoService {

	@Autowired
	private CargoRepository cargoRepository;

	@Autowired
	private CargoCompetenciaRepository cargoCompetenciaRepository;

	@Autowired
	private CargoCompetenciaService cargoCompetenciaService;

	@Autowired
	private DetalhamentoCargoCompetenciaService detalhamentoCargoCompetenciaService;

	@Autowired
	private CompetenciaDetalhamentoService competenciaDetalhamentoService;

	@Autowired
	private CargoTipoCompetenciaService cargoTipoCompetenciaService;

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private DimensaoCompetenciaDetalhamentoService dimensaoCompetenciaDetalhamentoService;

	@Autowired
	private AvaliacaoDimensaoService avaliacaoDimensaoService;

	private final QCargo qCargo = QCargo.cargo;

	@Override
	protected ServiceFilter<Cargo> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		UserTO user = getLoggedUser();
		switch (user.getTipoPerfil()) {
			case ADMINISTRADOR:
			case GESTOR_RH:
				mandatoryFilter.and(qCargo.empresa.id.eq(user.getEmpresa().getId()));
				break;
			case CONSULTOR_RH: //TODO Ver todas as empresas da qual ele é gestor
				mandatoryFilter.and(qCargo.empresa.id.eq(user.getEmpresa().getId()));
				break;
			case GESTOR_AREA:
				mandatoryFilter.and(qCargo.departamento.id.eq(user.getDepartamento().getId()));
				break;
			default:
				mandatoryFilter.and(qCargo.id.eq(user.getCargo().getId()));
				break;
		}

		return ServiceFilter.create(qCargo, mandatoryFilter);
	}

	@Autowired
	public CargoServiceImpl(CargoRepository repository) {
		super(repository);
	}

	@Override
	protected CargoTO doAfterCreate(CargoTO object) throws ServiceException {
		object.setSituacao(Situacao.INATIVA);
		return object;
	}

	@Override
	protected void doBeforeSave(CargoTO object) throws ServiceException {
		object.setEmpresa(mapper.map(getLoggedUser().getEmpresa(), EmpresaSimpleTO.class));
		if (object.getCargoPai() != null && object.getCargoPai().getId() == null) {
			object.setCargoPai(null);
		}
		validar(object);
	}

	@Override
	protected void doBeforeUpdate(Cargo entity, CargoTO object) throws ServiceException {
		//Ao inativar um cargo, inativar também seus respectivos tipos de competência e competências
		if (entity.getSituacao().equals(Situacao.ATIVA) && object.getSituacao().equals(Situacao.INATIVA)) {
			List<CargoTipoCompetenciaTO> tipoCompetenciaTOList = cargoTipoCompetenciaService.findAllCargoTipoCompetenciaByCargo(object.getId());
			tipoCompetenciaTOList.forEach(cargoTipoCompetenciaTO -> cargoTipoCompetenciaService.inativarCargoTipoCompetenciaByCargo(object.getId(), cargoTipoCompetenciaTO.getTipoCompetencia().getId()));
		}

		if (object.getCargoPai() != null && object.getCargoPai().getId() == null) {
			object.setCargoPai(null);
		}
		validar(object);
	}

	private void validar(CargoTO cargo) {
		CargoTO cargoExistente = findByEmpresaAndIdCargoClienteIgnoreCase(getLoggedUser().getEmpresa().getId(), cargo.getIdCargoCliente().trim());
		if (cargoExistente != null && cargoExistente.getIdCargoCliente().trim().toLowerCase().equals(cargo.getIdCargoCliente().trim().toLowerCase()) && !cargoExistente.getId().equals(cargo.getId())) {
			//FIXME: Passar a entidade para internacionalização
			throw new ValidationException("validation.duplicate", "cargo", "field.idCargoCliente", cargo.getIdCargoCliente());
		}
		List<CargoTO> listcargosExitente = findByEmpresaIdAndNomeIgnoreCase(getLoggedUser().getEmpresa().getId(), cargo.getNome().trim());
		if (!listcargosExitente.isEmpty()) {
			if (listcargosExitente.get(0).getNome().trim().toLowerCase().equals(cargo.getNome().trim().toLowerCase()) && !listcargosExitente.get(0).getId().equals(cargo.getId())) {
				//FIXME: Passar a entidade para internacionalização
				throw new ValidationException("validation.duplicate", "cargo", "field.nome", cargo.getNome());
			}
		}
	}

	@Override
	public List<CargoTO> findAllCargosByEmpresaAndAtivos(EmpresaTO empresa) {
		return map(cargoRepository.findByEmpresaIdAndSituacao(empresa.getId(), Situacao.ATIVA));
	}

	@Override
	public List<CargoTO> findAllCargosByUserEmpresaAndAtivos() {
		return map(cargoRepository.findByEmpresaIdAndSituacao(getLoggedUser().getEmpresa().getId(), Situacao.ATIVA));
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public CargoTO findByEmpresaAndIdCargoClienteIgnoreCase(Long empresaId, String idCargoCliente) {
		return map(cargoRepository.findByEmpresaIdAndIdCargoClienteIgnoreCase(empresaId, idCargoCliente));
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<CargoTO> findByEmpresaIdAndNomeIgnoreCase(Long empresaId, String nome) {
		return map(cargoRepository.findByEmpresaIdAndNomeIgnoreCase(empresaId, nome));
	}

	@Override
	public List<PendenciaTO> validarParaLiberacao(CargoSimpleTO cargo, List<AvaliacaoTipoCompetenciaTO> idsTiposCompetencia, Map<Long, List<DetalhamentoCargoCompetenciaTO>> detalhamentosPorCargo, AvaliacaoTO avaliacao) {
		long start = System.currentTimeMillis();
		logger.info(String.format("Iniciando a validação do cargo %s ...", cargo.getNome()));

		List<PendenciaTO> pendencias = new ArrayList<>();

		if (!Situacao.ATIVA.equals(cargo.getSituacao())) {
			PendenciaTO pendencia = new PendenciaTO("cargo-inactive", "cargo inativo", TipoPendencia.CARGO, cargo, new Date());
			pendencias.add(pendencia);
		}

		int qtdTipos = 0;
		List<CargoTipoCompetenciaTO> tiposCompetencia = cargoTipoCompetenciaService.findAllCargoTipoCompetenciaByCargo(cargo.getId());

		if (tiposCompetencia != null && !tiposCompetencia.isEmpty()) {
			boolean tiposAtivos = false;

			for (CargoTipoCompetenciaTO tipo : tiposCompetencia) {

				if (Situacao.ATIVA.equals(tipo.getSituacao())) {
					tiposAtivos = true;
				}

				for (AvaliacaoTipoCompetenciaTO avalTipoComp : idsTiposCompetencia) {
					if (avalTipoComp.getTipoCompetencia().getId().equals(tipo.getTipoCompetencia().getId()))
						qtdTipos++;
				}
			}


			if (qtdTipos == 0) {
				PendenciaTO pendencia = new PendenciaTO("cargo-not-found-tipos-competencia-avaliacao", "não possui os tipos de competências associadas ao cargo", TipoPendencia.CARGO, cargo, new Date());
				pendencias.add(pendencia);
			}

			if (!tiposAtivos) {
				PendenciaTO pendencia = new PendenciaTO("cargo-not-found-tipos-competencia-ative", "não possui tipos de competência ativos", TipoPendencia.CARGO, cargo, new Date());
				pendencias.add(pendencia);
			}

		} else {
			PendenciaTO pendencia = new PendenciaTO("cargo-not-found-tipos-competencia", "não possui tipos de competência", TipoPendencia.CARGO, cargo, new Date());
			pendencias.add(pendencia);
		}


		List<CargoCompetenciaTO> listaCargoCompAtivas = new ArrayList<>();//lista de CargoCompetencia ativas.
		List<CompetenciaDetalhamentoTO> listaCompDet = new ArrayList<>();//lista de CompetenciaDetalhamento de um (1) CargoCompetencia ativas.
		List<CompetenciaDetalhamentoTO> listaCompDetAtivas = new ArrayList<>();//lista de CompetenciaDetalhamento ativas.
		List<DetalhamentoCargoCompetenciaTO> listaDetCargoCompAplicaveis = new ArrayList<>();//lista de DetalhamentoCargoCompetencia aplicáveis ao cargo.

		List<CargoCompetenciaTO> listaCargoComp = cargoCompetenciaService.findAllCargoCompetenciaByCargo(cargo.getId());
		if (listaCargoComp == null || listaCargoComp.isEmpty()) {
			PendenciaTO pendencia = new PendenciaTO("cargo-not-found-competencia", "não possui competências associadas ao cargo", TipoPendencia.CARGO, cargo, new Date());
			pendencias.add(pendencia);
		}


		for (CargoCompetenciaTO cargoCompetencia : listaCargoComp) {
			List<CompetenciaDetalhamentoTO> resutadosDetalhamentos = null;

			if (cargoCompetencia.getSituacao().equals(Situacao.ATIVA) && cargoCompetencia.getCompetencia().getSituacao().equals(Situacao.ATIVA)) {
				listaCargoCompAtivas.add(cargoCompetencia);

				resutadosDetalhamentos = competenciaDetalhamentoService.listByCompetencia(cargoCompetencia.getCompetencia().getId());
				if (resutadosDetalhamentos != null && !resutadosDetalhamentos.isEmpty()) {
					listaCompDet.addAll(resutadosDetalhamentos);
				}
			}
		}

		if (listaCargoCompAtivas.isEmpty()) {
			PendenciaTO pendencia = new PendenciaTO("cargo-not-found-competencia-ative", "não possui competências ativas ao cargo", TipoPendencia.CARGO, cargo, new Date());
			pendencias.add(pendencia);
		}

		if (listaCompDet.isEmpty()) {
			PendenciaTO pendencia = new PendenciaTO("cargo-not-found-detalhamento", "não possui detalhamentos, para as competências ativas ao cargo", TipoPendencia.CARGO, cargo, new Date());
			pendencias.add(pendencia);
		}

		int numPesquisados = 0;
		int numEncontrados = 0;


		for (CargoCompetenciaTO cargoComp : listaCargoCompAtivas) {
			for (CompetenciaDetalhamentoTO compDet : listaCompDet) {
				if (cargoComp.getCompetencia().getId().equals(compDet.getCompetencia().getId())) {
					if (compDet.getSituacao().equals(Situacao.ATIVA)) {
						listaCompDetAtivas.add(compDet);

						DetalhamentoCargoCompetenciaTO detCargoComp = detalhamentoCargoCompetenciaService.findDetalhamentoCCByCargoCompetenciaAndCompetenciaDetalhamento(cargoComp.getId(), compDet.getId());
						numPesquisados++;
						if (detCargoComp != null && detCargoComp.getAplicavelAoCargo() == true) {
							listaDetCargoCompAplicaveis.add(detCargoComp);
							numEncontrados++;
						}
					}


				}
			}
		}

		if (listaCompDetAtivas.isEmpty()) {
			PendenciaTO pendencia = new PendenciaTO("cargo-not-found-detalhamento-ative", "não possui detalhamentos ativos, para as competências ativas ao cargo", TipoPendencia.CARGO, cargo, new Date());
			pendencias.add(pendencia);
		}

		if (listaDetCargoCompAplicaveis.isEmpty()) {
			PendenciaTO pendencia = new PendenciaTO("cargo-not-found-gabarito", "não possui detalhamentos aplicáveis ao cargo", TipoPendencia.CARGO, cargo, new Date());
			pendencias.add(pendencia);
		} else {
			if (avaliacao.getCampanha().getDimensao() != null && avaliacao.getCampanha().getDimensao()) {
				List<AvaliacaoDimensaoTO> listaAvaliacaoDimensao = avaliacaoDimensaoService.obterDimensaoPorAvaliacao(avaliacao);
				List<DetalhamentoCargoCompetenciaTO> listaDetDim = new ArrayList<>();

				for (AvaliacaoDimensaoTO avaliacaoDimensaoTO : listaAvaliacaoDimensao) {
					List<DimensaoCompetenciaDetalhamentoTO> listaDetalhamentosDimensao = dimensaoCompetenciaDetalhamentoService.obterDetalhamentosPorDimensao(avaliacaoDimensaoTO.getDimensao());

					for (DimensaoCompetenciaDetalhamentoTO dimCompDetTO : listaDetalhamentosDimensao) {

						for (DetalhamentoCargoCompetenciaTO dccTO : listaDetCargoCompAplicaveis) {
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
					PendenciaTO pendencia = new PendenciaTO("cargo-not-found-gabarito-dimensao", "não possui detalhamentos aplicáveis ao cargo - dimensão", TipoPendencia.CARGO, cargo, new Date());
					pendencias.add(pendencia);
				} else {
					listaDetCargoCompAplicaveis.clear();
					listaDetCargoCompAplicaveis.addAll(listaDetDim);
				}
			}

		}

		detalhamentosPorCargo.put(cargo.getId(), listaDetCargoCompAplicaveis);

		long delay = System.currentTimeMillis() - start;
		logger.info(String.format("Foram encontrado %s detalhamentos aplicáveis ao cargo de [%d] pesquisados...", numEncontrados, numPesquisados));
		logger.info(String.format("O método validarParaLiberacao executou em %s ", delay));
		return pendencias;
	}


	@Override
	public Map<TipoAvaliacaoPerfil, List<DetalhamentoCargoCompetenciaTO>> agruparPerguntasPorPerfilAgente(List<DetalhamentoCargoCompetenciaTO> listaDetalhamentos, CargoSimpleTO cargo, AvaliacaoTO avaliacao, List<AvaliacaoTipoCompetenciaTO> listaTiposDoAvaliadores) {
		logger.info(String.format("Iniciando o agrupamento das perguntas do cargo %s ...", cargo.getNome()));

		ArrayList<DetalhamentoCargoCompetenciaTO> perguntasAutoavaliacao = new ArrayList<>();
		ArrayList<DetalhamentoCargoCompetenciaTO> perguntasGestor = new ArrayList<>();
		ArrayList<DetalhamentoCargoCompetenciaTO> perguntasSubordinado = new ArrayList<>();
		ArrayList<DetalhamentoCargoCompetenciaTO> perguntasPares = new ArrayList<>();


		for (DetalhamentoCargoCompetenciaTO det : listaDetalhamentos) {

			for (AvaliacaoTipoCompetenciaTO tipoCompetencia : listaTiposDoAvaliadores) {

				if (det.getCargoCompetencia().getCompetencia().getTipo().getId().equals(tipoCompetencia.getTipoCompetencia().getId())) {
					if (tipoCompetencia.getPerfilAgente().equals(TipoAvaliacaoPerfil.AUTOAVALIACAO)) {
						perguntasAutoavaliacao.add(det);
					} else if (tipoCompetencia.getPerfilAgente().equals(TipoAvaliacaoPerfil.GESTOR)) {
						perguntasGestor.add(det);
					} else if (tipoCompetencia.getPerfilAgente().equals(TipoAvaliacaoPerfil.SUBORDINADO)) {
						perguntasSubordinado.add(det);
					} else if (tipoCompetencia.getPerfilAgente().equals(TipoAvaliacaoPerfil.PARES)) {
						perguntasPares.add(det);
					}
				}
			}
		}

		Map<TipoAvaliacaoPerfil, List<DetalhamentoCargoCompetenciaTO>> mapaPerguntasPorPerfil = new HashMap<>();
		mapaPerguntasPorPerfil.put(TipoAvaliacaoPerfil.AUTOAVALIACAO, perguntasAutoavaliacao);
		mapaPerguntasPorPerfil.put(TipoAvaliacaoPerfil.GESTOR, perguntasGestor);
		mapaPerguntasPorPerfil.put(TipoAvaliacaoPerfil.SUBORDINADO, perguntasSubordinado);
		mapaPerguntasPorPerfil.put(TipoAvaliacaoPerfil.PARES, perguntasPares);

		logger.info(String.format("Finalizando o agrupamento das perguntas do cargo %s ...", cargo.getNome()));
		return mapaPerguntasPorPerfil;
	}


	@Override
	public List<FuncionarioTO> obterFuncionariosAtivosDoCargo(Long idCargo) {
		List<Funcionario> listaDefuncionariosAtivosDoCargo = funcionarioRepository.findByCargoIdAndSituacao(idCargo, Situacao.ATIVA);

		List<FuncionarioTO> listaDeTOs = new ArrayList<>();

		listaDefuncionariosAtivosDoCargo.forEach(func -> {
			listaDeTOs.add(mapDefault(func));
		});

		return listaDeTOs;
	}

	@Override
	public List<CargoTreeTO> listarArvoreDeHierarquiaDeCargos(Long idCargo) {
		// Trazer todos os Pais
		List<Cargo> listaPais = select()
			.where(new BooleanBuilder().and(qCargo.cargoPai.isNull()).and(qCargo.situacao.eq(Situacao.ATIVA)).and(qCargo.empresa.id.eq(getLoggedUser().getEmpresa().getId())))
			.orderBy(qCargo.nome.asc())
			.fetch();

		List<CargoTreeTO> listaPaisTreeTO = new ArrayList<>();

		Collections.sort(listaPais, new Comparator<Cargo>() {
			@Override
			public int compare(Cargo o1, Cargo o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		});

		// Transformando a lista em TO
		for (Cargo pai : listaPais) {
			//FIXME: Obter apenas cargo Pai.
			if (pai.getCargoPai() == null) {
				listaPaisTreeTO.add(mapper.map(pai, CargoTreeTO.class));
			}
		}

		// Para cada pai, trazer filhos
		for (CargoTreeTO cargoTreeTOPai : listaPaisTreeTO) {
			List<Cargo> listaFilhos = select(Projections.fields(Cargo.class, qCargo.id, qCargo.nome, qCargo.situacao, qCargo.cargoPai))
				.where(new BooleanBuilder().and(qCargo.cargoPai.id.eq(cargoTreeTOPai.getId())).and(qCargo.situacao.eq(Situacao.ATIVA)))
				.orderBy(qCargo.nome.asc())
				.fetch();

			List<CargoTreeTO> listaFilhosTreeTO = new ArrayList<>();

			Collections.sort(listaFilhos, new Comparator<Cargo>() {
				@Override
				public int compare(Cargo o1, Cargo o2) {
					return o1.getNome().compareTo(o2.getNome());
				}
			});

			for (Cargo pai : listaFilhos) {
				listaFilhosTreeTO.add(mapper.map(pai, CargoTreeTO.class));
			}

			cargoTreeTOPai.setChildren(listaFilhosTreeTO);

			obterFilhosCargoPai(cargoTreeTOPai);
		}

		return listaPaisTreeTO;
	}

	@Override
	public List<CargoTO> obterCargosFilhos(Long idCargo) {
		List<Cargo> filhos = select()
			.where(new BooleanBuilder().and(qCargo.cargoPai.id.eq(idCargo)).and(qCargo.situacao.eq(Situacao.ATIVA))).fetch();
		return map(filhos);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public CargoTO copiarCargo(Long idCargo) {
		CargoTO cargo = map(cargoRepository.findOne(idCargo));
		CargoTO cargoClone = new CargoTO();
		Cargo newCargo = new Cargo();
		CargoTO newCargoTo = new CargoTO();
		try {
			int posicao = cargo.getNome().indexOf(" - Cópia");
			String nomeCargo = posicao > 0 ? cargo.getNome().substring(0,posicao): cargo.getNome();
			cargoClone = cargo.clone();
			cargoClone.setNome(verificarNomeCargo(nomeCargo));
			cargoClone.setId(null);
			cargoClone.setCreatedBy(getLoggedUser().getUsername());
			cargoClone.setCreationDate(new Date());
			cargoClone.setAtualizador(null);
			cargoClone.setDataAtualizacao(null);
			cargoClone.setIdCargoCliente(cargo.getId().toString().concat("-").concat(numCopiasCargo(nomeCargo).toString()));
			Cargo cargoCopiado = map(cargoClone);
			newCargoTo = map(cargoRepository.save(cargoCopiado));
			copiarCargoTipoCompetencia(cargo.getId(),newCargoTo);
			copiarCargoCompetencia(cargo.getId(),newCargoTo);

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return newCargoTo;
	}

	public String verificarNomeCargo(String nome) {
		Long countName = numCopiasCargo(nome);
		if(countName > 1L) {
			return nome.concat(" - Cópia0").concat(countName.toString()); //FIXME: melhora contagem começando com zero
		}else {
			return nome.concat(" - Cópia");
		}
	}

	public Long numCopiasCargo(String nome) {
		return select().where(qCargo.nome.like(nome.concat("%")).and(qCargo.empresa.id.eq(getLoggedUser().getEmpresa().getId()))).fetchCount();
	}

	public void copiarCargoTipoCompetencia(Long idCargo, CargoTO newCargo) {
		List<CargoTipoCompetenciaTO> listCtcTO = cargoTipoCompetenciaService.findAllCargoTipoCompetenciaByCargo(idCargo);
		listCtcTO.forEach(ctcTO -> {
			try {
				CargoTipoCompetenciaTO ctcClone = ctcTO.clone();
				ctcClone.setId(null);
				ctcClone.setCargo(newCargo);
				cargoTipoCompetenciaService.save(ctcClone);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		});
	}

	public void copiarCargoCompetencia(Long idCargo, CargoTO newCargo) {
		List<CargoCompetenciaTO> listCcTO = cargoCompetenciaService.findAllCargoCompetenciaByCargo(idCargo);
		listCcTO.forEach(ccTO -> {
			try {
				CargoCompetenciaTO ccClone = ccTO.clone();
				ccClone.setId(null);
				ccClone.setCargo(newCargo);
				CargoCompetenciaTO newCcTO = cargoCompetenciaService.save(ccClone);
				copiarDetalhamentoCargoCompetencia(ccTO,newCcTO);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		});
	}

	public void copiarDetalhamentoCargoCompetencia(CargoCompetenciaTO cargoCompetenciaTO, CargoCompetenciaTO newCargoCompetenciaTO) {
		List<DetalhamentoCargoCompetenciaTO> listDetTO = detalhamentoCargoCompetenciaService.findAllDetalhamentosCCByCargoCompetencia(cargoCompetenciaTO.getId());
		listDetTO.forEach(detTO -> {
			try {
				DetalhamentoCargoCompetenciaTO detClone = detTO.clone();
				detClone.setId(null);
				detClone.setCargoCompetencia(newCargoCompetenciaTO);
				detalhamentoCargoCompetenciaService.save(detClone);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public List<CargoTO> obterCargosDescendentes(CargoTO cargo) {
		List<CargoTO> descendentes = new ArrayList<>();

		List<CargoTO> resultado = obterCargosFilhos(cargo.getId());

		for (CargoTO cargoTO : resultado) {
			List<CargoTO> lista = obterCargosDescendentes(cargoTO);
			if (lista != null && !lista.isEmpty())
				descendentes.addAll(lista);

			descendentes.add(cargoTO);
		}

		return descendentes;
	}

	private void obterFilhosCargoPai(CargoTreeTO cargoTreeTOPai) {
		if (cargoTreeTOPai.getChildren().isEmpty()) {
			return;
		}

		List<CargoTreeTO> listaFilhosCargoTree = new ArrayList<>();

		for (CargoTreeTO cargoTOFilho : cargoTreeTOPai.getChildren()) {
			List<Cargo> listaFilhos = select()
				.where(new BooleanBuilder().and(qCargo.cargoPai.id.eq(cargoTOFilho.getId())).and(qCargo.situacao.eq(Situacao.ATIVA))).fetch();

			List<CargoTreeTO> listaFilhosTreeTO = new ArrayList<>();

			Collections.sort(listaFilhos, new Comparator<Cargo>() {
				@Override
				public int compare(Cargo o1, Cargo o2) {
					return o1.getNome().compareTo(o2.getNome());
				}
			});

			for (Cargo filho : listaFilhos) {
				listaFilhosTreeTO.add(mapper.map(filho, CargoTreeTO.class));
			}

			cargoTOFilho.setChildren(listaFilhosTreeTO);

			obterFilhosCargoPai(cargoTOFilho);
		}
	}

}
