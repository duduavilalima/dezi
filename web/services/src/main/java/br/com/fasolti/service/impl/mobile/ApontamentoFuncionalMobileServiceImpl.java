package br.com.fasolti.service.impl.mobile;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.ApontamentoFuncionalRepository;
import br.com.fasolti.avd.service.FotoService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.service.TipoCompetenciaService;
import br.com.fasolti.avd.service.mobile.ApontamentoFuncionalMobileService;
import br.com.fasolti.avd.transfer.FotoTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.transfer.mobile.*;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.service.util.BaseDeziService;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ApontamentoFuncionalMobileServiceImpl
	extends BaseDeziService<ApontamentoFuncional, ApontamentoFuncionalMobileTO>
	implements ApontamentoFuncionalMobileService {

	private final QApontamentoFuncional qApontamentoFuncional = QApontamentoFuncional.apontamentoFuncional;
	private final QFuncionario qFuncionario  = QFuncionario.funcionario;
	private final QDepartamento qDepartamento  = QDepartamento.departamento;
	private final QCargo qCargo  = QCargo.cargo;
	private final QCargoCompetencia qCargoCompetencia  = QCargoCompetencia.cargoCompetencia;
	private final QCompetencia qCompetencia = QCompetencia.competencia;
	private final QTipoCompetencia qTipoCompetencia = QTipoCompetencia.tipoCompetencia;

	private final QBean<TipoCompetencia> pTipoCompetencia =
		Projections.fields(TipoCompetencia.class,
			qCargoCompetencia.competencia.tipo.id,
			qCargoCompetencia.competencia.tipo.nome,
			qCargoCompetencia.competencia.tipo.nomeCurto
		);

	private final QBean<Funcionario> pAvaliado =
		Projections.fields(Funcionario.class,
			qFuncionario.id,
			qFuncionario.nome,
			Projections.fields(Cargo.class,
				qFuncionario.cargo.id,
				qFuncionario.cargo.nome
			).as("cargo")
		);

	private final QBean<Competencia> pCompetencia =
		Projections.fields(Competencia.class,
			qCompetencia.id,
			qCompetencia.descricaoAbreviada,
			Projections.fields(TipoCompetencia.class,
				qCompetencia.tipo.id
			).as("tipo"),
			Projections.fields(Empresa.class,
				qCompetencia.empresa.id
			).as("empresa")
		);

	// TODO Usar (o futuro) FotoMobileResource para obter as fotos e NÃO preencher o fotoBase64
	@Autowired
	private FotoService fotoService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	public ApontamentoFuncionalMobileServiceImpl(ApontamentoFuncionalRepository repository) {
		super(repository);
	}

	public List<FuncionarioMobileTO> listFuncionariosAvaliadosByLoggedUser() {
		UserTO avaliador = getLoggedUser();
		FuncionarioTO funcionarioTO = funcionarioService.findById(avaliador.getId());

		List<FuncionarioTO> avaliados = funcionarioService.obterSubordinados(funcionarioTO);
		List<FuncionarioMobileTO> avaliadosTO = mapper.map(avaliados, FuncionarioMobileTO.class).stream()
			.map(this::setAdditionalDataForFuncionario)
			.collect(Collectors.toList());

		avaliadosTO.sort(Comparator.comparing(FuncionarioMobileTO::getNome));

		return avaliadosTO;
	}

	@Override
	public List<TipoCompetenciaMobileTO> listTiposCompetenciaByLoggedUserAndFuncionarioId(Long avaliadoId) {

		FuncionarioTO funcionario = funcionarioService.findById(avaliadoId);
		Long avaliadoCargoId = funcionario.getCargo().getId();

		// FIXME Usar o serviço de Tipos de competência (ou seria o de CargoCompetencia?)
		JPAQuery<TipoCompetencia> query = new JPAQuery<>(entityManager)
			.select(pTipoCompetencia)
			.distinct()
			.from(qCargoCompetencia)
			.innerJoin(qCargoCompetencia.cargo, qCargo)
			.innerJoin(qCargoCompetencia.competencia, qCompetencia)
			.innerJoin(qCompetencia.tipo, qTipoCompetencia)
			.where(qCargoCompetencia.cargo.id.eq(avaliadoCargoId)
				.and(qTipoCompetencia.situacao.eq(Situacao.ATIVA)))
			.orderBy(qCargoCompetencia.competencia.tipo.nomeCurto.asc());

		List<TipoCompetencia> tiposCompetencia = query.fetch();
		List<TipoCompetenciaMobileTO> tiposCompetenciaTO =
			mapper.map(tiposCompetencia, TipoCompetenciaMobileTO.class);

		return tiposCompetenciaTO;
	}

	@Override
	public List<CompetenciaMobileTO> listCompetenciaByTipoCompetenciaId(Long tipoCompetenciaId) {
		UserTO avaliador = getLoggedUser();

		JPAQuery<Competencia> query = new JPAQuery<>(entityManager)
			.select(pCompetencia)
			.distinct()
			.from(qCompetencia)
			.where(qCompetencia.tipo.id.eq(tipoCompetenciaId)
				.and(qCompetencia.empresa.id.eq(avaliador.getEmpresa().getId())))
			.orderBy(qCompetencia.descricaoAbreviada.asc());

		List<Competencia> competencias = query.fetch();
		List<CompetenciaMobileTO> competenciasTO = mapper.map(competencias, CompetenciaMobileTO.class);

		return competenciasTO;
	}

	private FeedForwardMobileTO setAdditionalData(FeedForwardMobileTO ffRecebido) {
		if (ffRecebido.getAvaliador() != null) setAdditionalDataForFuncionario(ffRecebido.getAvaliador());
		if (ffRecebido.getAvaliado() != null) setAdditionalDataForFuncionario(ffRecebido.getAvaliado());
		return ffRecebido;
	}

	private FuncionarioMobileTO setAdditionalDataForFuncionario(FuncionarioMobileTO funcionarioTO) {
		FotoTO foto = fotoService.obterFotoPerfilFuncionario(funcionarioTO.getId());
		if (foto != null) {
			funcionarioTO.setFotoBase64(foto.getBase64());
		} else {
			funcionarioTO.setFotoBase64("");
		}
		return funcionarioTO;
	}
}
