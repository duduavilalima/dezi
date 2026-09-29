package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.CargoCompetenciaRepository;
import br.com.fasolti.avd.repository.DepartamentoRepository;
import br.com.fasolti.avd.repository.FuncionarioRepository;
import br.com.fasolti.avd.service.DepartamentoService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.simple.CargoSimpleTO;
import br.com.fasolti.avd.transfer.simple.CompetenciaBaseTO;
import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DepartamentoServiceImpl
	extends BaseAvdService<Departamento, DepartamentoTO, DepartamentoTO>
	implements DepartamentoService {

	@Autowired
	private DepartamentoRepository departamentoRepository;

	private final QDepartamento qDepartamento = QDepartamento.departamento;
	private final QFuncionario qFuncionario = QFuncionario.funcionario;
	private final QCargo qCargo = QCargo.cargo;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private CargoCompetenciaRepository cargoCompetenciaRepository;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	public DepartamentoServiceImpl(DepartamentoRepository repository) {
		super(repository);
	}

	@Override
	protected ServiceFilter<Departamento> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		UserTO user = getLoggedUser();
		switch (user.getTipoPerfil()) {
			case ADMINISTRADOR:
			case GESTOR_RH:
				mandatoryFilter.and(qDepartamento.empresa.id.eq(user.getEmpresa().getId()));
				break;
			case CONSULTOR_RH: //TODO Ver todas as empresas da qual ele é gestor
				mandatoryFilter.and(qDepartamento.empresa.id.eq(user.getEmpresa().getId()));
				break;
			case GESTOR_AREA:
				mandatoryFilter.and(qDepartamento.id.eq(user.getDepartamento().getId()));
				break;
			default:
				mandatoryFilter.and(qDepartamento.id.eq(user.getDepartamento().getId()));
				break;
		}

		return ServiceFilter.create(qDepartamento, mandatoryFilter);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Departamento> query) {
		query.select(Projections.fields(
			Departamento.class,
			qDepartamento.id,
			qDepartamento.nome,
			qDepartamento.idDepartamentoCliente,
			qDepartamento.situacao
		));
	}

	@Override
	protected void doBeforeSave(DepartamentoTO object) throws ServiceException {
		object.setEmpresa(mapper.map(getLoggedUser().getEmpresa(), EmpresaSimpleTO.class));
		validar(object);
	}

	private void validar(DepartamentoTO departamentoTO) {
		EmpresaSimpleTO empresa = getLoggedUser().getEmpresa();
		DepartamentoTO departamentoExistente = findByEmpresaAndIdDepartamentoCliente(empresa.getId(), departamentoTO.getIdDepartamentoCliente());
		if (departamentoExistente != null && !departamentoExistente.getId().equals(departamentoTO.getId())) {
			throw new ValidationException("validation.idDepartamentoCliente", "idDepartamentoCliente", "field.idDepartamentoCliente", departamentoTO.getIdDepartamentoCliente());
		}
		Departamento departamento = departamentoRepository.findByEmpresaIdAndNome(empresa.getId(), departamentoTO.getNome());
		if (departamento != null && !departamento.getId().equals(departamentoTO.getId())) {
			throw new ValidationException("validation.duplicate", "setor", "field.nome", departamentoTO.getIdDepartamentoCliente());
		}

	}

	@Override
	protected void doBeforeUpdate(DepartamentoTO object) throws ServiceException {
		validar(object);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<DepartamentoTO> findAllDepartamentosEmpresaLogada() {
		return map(
			select(
				Projections.fields(
					Departamento.class,
					qDepartamento.id,
					qDepartamento.nome
				)
			)
			.where(qDepartamento.empresa.id.eq(getLoggedUser().getEmpresa().getId()).and(qDepartamento.situacao.eq(Situacao.ATIVA)))
			.orderBy(qDepartamento.nome.asc())
			.fetch());
	}

	@Override
	protected DepartamentoTO doAfterCreate(DepartamentoTO object) throws br.com.fasolti.commons.lang.ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return object;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public DepartamentoTO findByEmpresaAndIdDepartamentoCliente(Long idEmpresa, String idDepartamentoCliente) {
		return map(departamentoRepository.findByEmpresaIdAndIdDepartamentoClienteIgnoreCase(getLoggedUser().getEmpresa().getId(), idDepartamentoCliente));
	}

	@Override
	public List<FuncionarioTO> obterFuncionariosAtivosDoDepartamento(Long idDepartamento) {
        return funcionarioRepository.findByDepartamentoIdAndSituacaoAndEmpresaId(idDepartamento, Situacao.ATIVA, getLoggedUser().getEmpresa().getId())
			.stream()
			.map(func -> {
				FuncionarioTO funTO = mapDefault(func);
				return funTO;
			})
			.collect(Collectors.toList());
	}

	@Override
	public List<CargoListaCompetenciaTO> obterCargoCompetenciaPorDepartamento(Long idDepartamento) {
		List<CargoListaCompetenciaTO> listaCompetenciaTO = new ArrayList<CargoListaCompetenciaTO>();

		List<FuncionarioTO> listaDeFuncionarios = funcionarioService.findByDepartamentoIdProjections(idDepartamento);

		Set<CargoSimpleTO> listaDeCargos = new TreeSet<CargoSimpleTO>();

		for(FuncionarioTO funcionario : listaDeFuncionarios) {
			listaDeCargos.add(funcionario.getCargo());
		}

		//Não há repetição de cargos, já que existe um set definido anteriormente, logo não é necessário um if para filtrar
		for(CargoSimpleTO cargo : listaDeCargos) {
			CargoTO cargoTO = mapper.map(cargo, CargoTO.class);

			Set<CompetenciaBaseTO> listaCompetencias = new TreeSet<CompetenciaBaseTO>();

			for(CargoCompetencia cargoCompetencia : cargoCompetenciaRepository.findByCargoId(cargo.getId())) {
				listaCompetencias.add(mapper.map(cargoCompetencia.getCompetencia(), CompetenciaBaseTO.class));
			}

			listaCompetenciaTO.add(new CargoListaCompetenciaTO(cargoTO, listaCompetencias));
		}

		return listaCompetenciaTO;
	}

	@Override
	public List<DepartamentoTreeTO> listarArvoreDeHierarquiaDeDepartamentos() {
		// Trazer todos os Pais
		List<Departamento> listaPais = select()
			.where(new BooleanBuilder().and(qDepartamento.departamentoPai.isNull()).and(qDepartamento.situacao.eq(Situacao.ATIVA)).and(qDepartamento.empresa.id.eq(getLoggedUser().getEmpresa().getId())))
			.orderBy(qDepartamento.nome.asc())
			.fetch();

		List<DepartamentoTreeTO> listaPaisTreeTO = new ArrayList<>();

		Collections.sort(listaPais, new Comparator<Departamento>() {
			@Override
			public int compare(Departamento o1, Departamento o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		});

		// Transformando a lista em TO
		for (Departamento pai : listaPais) {
			if (pai.getDepartamentoPai() == null) {
				listaPaisTreeTO.add(mapper.map(pai, DepartamentoTreeTO.class));
			}
		}

		// Para cada pai, trazer filhos
		for (DepartamentoTreeTO treeTOPai : listaPaisTreeTO) {
			List<Departamento> listaFilhos = select(Projections.fields(Departamento.class, qDepartamento.id, qDepartamento.nome, qDepartamento.situacao, qDepartamento.departamentoPai))
				.where(new BooleanBuilder().and(qDepartamento.departamentoPai.id.eq(treeTOPai.getId())).and(qDepartamento.situacao.eq(Situacao.ATIVA)))
				.orderBy(qDepartamento.nome.asc())
				.fetch();

			List<DepartamentoTreeTO> listaFilhosTreeTO = new ArrayList<>();

			Collections.sort(listaFilhos, new Comparator<Departamento>() {
				@Override
				public int compare(Departamento o1, Departamento o2) {
					return o1.getNome().compareTo(o2.getNome());
				}
			});

			for (Departamento pai : listaFilhos) {
				listaFilhosTreeTO.add(mapper.map(pai, DepartamentoTreeTO.class));
			}

			treeTOPai.setChildren(listaFilhosTreeTO);

			obterFilhosDepartamentoPai(treeTOPai);
		}

		return listaPaisTreeTO;
	}

	@Override
	public List<DepartamentoTO> obterDepartamentosFilhos(Long idDepartamento) {
		List<Departamento> filhos = departamentoRepository.findByDepartamentoPaiIdAndSituacao(idDepartamento, Situacao.ATIVA);
		/*select()
			.where(new BooleanBuilder().and(qDepartamento.departamentoPai.id.eq(idDepartamento)).and(qDepartamento.situacao.eq(Situacao.ATIVA))).fetch();*/
		return map(filhos);
	}

	@Override
	public List<DepartamentoTO> obterDepartamentosDescendentes(DepartamentoTO departamento) {
		List<DepartamentoTO> descendentes = new ArrayList<>();

		List<DepartamentoTO> resultado = obterDepartamentosFilhos(departamento.getId());

		for (DepartamentoTO departamentoTO : resultado) {
			List<DepartamentoTO> lista = obterDepartamentosDescendentes(departamentoTO);
			if (lista != null && !lista.isEmpty())
				descendentes.addAll(lista);

			descendentes.add(departamentoTO);
		}

		return descendentes;
	}

	@Override
	public List<DepartamentoTO> obterDepartamentosAscendentes(DepartamentoTO departamento) {
		List<DepartamentoTO> ascendentes = new ArrayList<>();
		Departamento depto = select().where(new BooleanBuilder().and(qDepartamento.id.eq(departamento.getId()))
			.and(qDepartamento.situacao.eq(Situacao.ATIVA))).fetchOne();

		if (depto != null && depto.getDepartamentoPai() != null) {
			ascendentes.add(map(depto));
			DepartamentoTO paiTO = map(depto.getDepartamentoPai());
			ascendentes.add(paiTO);
			List<DepartamentoTO> resultado = obterDepartamentosAscendentes(paiTO);
			ascendentes.addAll(resultado);
		}

		return ascendentes;
	}

	@Override
	public List<DepartamentoTO> obterDepartamentosUsuarioLogado() {
		UserTO user = getLoggedUser();
		if (TipoPerfil.GESTOR_AREA.equals(user.getTipoPerfil())) {
			List<DepartamentoTO> departamentos = new ArrayList<>();
			DepartamentoTO departamentoTO = map(departamentoRepository.findOne(user.getDepartamento().getId()));
			departamentos.add(departamentoTO);
			departamentos.addAll(this.obterDepartamentosDescendentes(departamentoTO));
			return departamentos;
		}
		return this.listAll();
	}

	private void obterFilhosDepartamentoPai(DepartamentoTreeTO departamentoTreeTOPai) {
		if (departamentoTreeTOPai.getChildren().isEmpty()) {
			return;
		}

		List<DepartamentoTreeTO> listaFilhosCargoTree = new ArrayList<>();

		for (DepartamentoTreeTO cargoTOFilho : departamentoTreeTOPai.getChildren()) {
			List<Departamento> listaFilhos = select()
				.where(new BooleanBuilder().and(qDepartamento.departamentoPai.id.eq(cargoTOFilho.getId())).and(qDepartamento.situacao.eq(Situacao.ATIVA))).fetch();

			List<DepartamentoTreeTO> listaFilhosTreeTO = new ArrayList<>();

			Collections.sort(listaFilhos, new Comparator<Departamento>() {
				@Override
				public int compare(Departamento o1, Departamento o2) {
					return o1.getNome().compareTo(o2.getNome());
				}
			});

			for (Departamento filho : listaFilhos) {
				listaFilhosTreeTO.add(mapper.map(filho, DepartamentoTreeTO.class));
			}

			cargoTOFilho.setChildren(listaFilhosTreeTO);

			obterFilhosDepartamentoPai(cargoTOFilho);
		}
	}
}
