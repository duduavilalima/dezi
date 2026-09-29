package br.com.fasolti.service.impl.mobile;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.ApontamentoFuncionalRepository;
import br.com.fasolti.avd.service.mobile.ApontamentoFuncionalMobileService;
import br.com.fasolti.avd.service.mobile.ApontamentoMobileService;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.transfer.mobile.*;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseDeziService;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ApontamentoMobileServiceImpl
extends BaseDeziService<ApontamentoFuncional, ApontamentoMobileTO>
implements ApontamentoMobileService {

	private final QApontamentoFuncional qApontamentoFuncional = QApontamentoFuncional.apontamentoFuncional;

	// TIP: EDITE AQUI as projeções dos dados retornados na API.
	// Para editar as projeções de Tipos de competência, competências e avaliados, veja ApontamentoFuncionalMobileServiceImpl.
	// Note que alterações nas projeções destes recursos afetarão todas as telas relacionadas a apontamento funcional!
	// Se for necessário adicionar ou customizar algum valor, sobreescreva, nesta mesma classe:
	// - listCompetenciaByTipoCompetenciaId;
	// - listFuncionariosAvaliadosByLoggedUser; ou
	// - listTiposCompetenciaByLoggedUserAndFuncionarioId.

	private final QBean<ApontamentoFuncional> pApontamento =
		Projections.fields(ApontamentoFuncional.class,
			qApontamentoFuncional.id,
			qApontamentoFuncional.dataApontamento,
			qApontamentoFuncional.graduacaoVisual,
			qApontamentoFuncional.registroCritico,
			Projections.fields(Competencia.class,
				qApontamentoFuncional.competencia.id,
				qApontamentoFuncional.competencia.descricaoAbreviada,
				Projections.fields(TipoCompetencia.class,
					qApontamentoFuncional.competencia.tipo.id
				).as("tipo")
			).as("competencia")
		);

	// END TIP

	@Autowired
	private ApontamentoFuncionalMobileService apontamentoFuncionalMobileService;

	@Autowired
	public ApontamentoMobileServiceImpl(ApontamentoFuncionalRepository repository) {
		super(repository);
	}

	@Override
	public List<CompetenciaMobileTO> listCompetenciaByTipoCompetenciaId(Long tipoCompetenciaId) {
		return apontamentoFuncionalMobileService.listCompetenciaByTipoCompetenciaId(tipoCompetenciaId);
	}

	@Override
	public List<FuncionarioMobileTO> listFuncionariosAvaliadosByLoggedUser() {
		return apontamentoFuncionalMobileService.listFuncionariosAvaliadosByLoggedUser();
	}

	@Override
	public List<TipoCompetenciaMobileTO> listTiposCompetenciaByLoggedUserAndFuncionarioId(Long avaliadoId) {
		return apontamentoFuncionalMobileService.listTiposCompetenciaByLoggedUserAndFuncionarioId(avaliadoId);
	}

	@Override
	public List<ApontamentoMobileTO> listByLoggedUserAndFuncionarioIdAndTipoCompetenciaId(Long funcionarioId, Long tipoCompetenciaId) {
		UserTO avaliador = getLoggedUser();

		JPAQuery<ApontamentoFuncional> query = select(pApontamento)
			.from(qApontamentoFuncional)
			.where(createDefaultPredicate(avaliador)
				.and(qApontamentoFuncional.avaliado.id.eq(funcionarioId))
				.and(qApontamentoFuncional.competencia.tipo.id.eq(tipoCompetenciaId)))
			.orderBy(qApontamentoFuncional.dataApontamento.desc());

		List<ApontamentoFuncional> apontamentos = query.fetch();
		List<ApontamentoMobileTO> apontamentosTO = mapper.map(apontamentos, ApontamentoMobileTO.class);

		return apontamentosTO;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public ApontamentoMobileTO create(ApontamentoMobileTO input) {
		UserTO avaliadorUser = getLoggedUser();
		Objects.requireNonNull(input);

		Long avaliadoId = Optional.ofNullable(input.getAvaliadoId())
			.or(() -> Optional.of(input.getAvaliado()).map(FuncionarioMobileTO::getId))
			.orElseThrow(() -> new IllegalArgumentException("Faltando ID do avaliado."));
		FuncionarioMobileTO avaliado = new FuncionarioMobileTO();
		avaliado.setId(avaliadoId);

		Long avaliadorId = avaliadorUser.getId();
		FuncionarioMobileTO avaliador = new FuncionarioMobileTO();
		avaliador.setId(avaliadorId);

		Long competenciaId = Optional.ofNullable(input.getCompetenciaId())
			.or(() -> Optional.of(input.getCompetencia()).map(CompetenciaMobileTO::getId))
			.orElseThrow(() -> new IllegalArgumentException("Faltando ID da competência."));
		CompetenciaMobileTO competencia = new CompetenciaMobileTO();
		competencia.setId(competenciaId);

		ApontamentoMobileTO apontamento = new ApontamentoMobileTO();
		apontamento.setAvaliado(avaliado);
		apontamento.setAvaliador(avaliador);
		apontamento.setCompetencia(competencia);

		apontamento.setGraduacaoVisual(input.getGraduacaoVisual());
		apontamento.setRegistroCritico(Objects.requireNonNull(input.getRegistroCritico(), "Faltando o texto do apontamento."));
		apontamento.setDataApontamento(new Date());

		return save(apontamento);
	}

	@Override
	protected ServiceFilter<ApontamentoFuncional> getServiceFilter() {
		UserTO avaliadorUser = getLoggedUser();
		return ServiceFilter.create(qApontamentoFuncional, createDefaultPredicate(avaliadorUser));
	}

	private BooleanExpression createDefaultPredicate(UserTO avaliador) {
		return qApontamentoFuncional.registroCritico.isNotNull()
			.and(qApontamentoFuncional.avaliador.id.eq(avaliador.getId()));
	}
}
