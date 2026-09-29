package br.com.fasolti.service.impl.mobile;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.ApontamentoFuncionalRepository;
import br.com.fasolti.avd.service.mobile.FeedForwardRecebidoMobileService;
import br.com.fasolti.avd.service.FotoService;
import br.com.fasolti.avd.transfer.FotoTO;
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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedForwardRecebidoMobileServiceImpl
		extends BaseDeziService<ApontamentoFuncional, FeedForwardRecebidoMobileTO>
		implements FeedForwardRecebidoMobileService {

	// TODO Usar (o futuro) FotoMobileResource para obter as fotos e NÃO preencher o fotoBase64
	@Autowired
	private FotoService fotoService;

	@Autowired
	public FeedForwardRecebidoMobileServiceImpl(ApontamentoFuncionalRepository repository) {
		super(repository);
	}

	private final QApontamentoFuncional qApontamentoFuncional = QApontamentoFuncional.apontamentoFuncional;
	private final QCompetencia qCompetencia = QCompetencia.competencia;
	private final QTipoCompetencia qTipoCompetencia = QTipoCompetencia.tipoCompetencia;

	private final QBean<TipoCompetencia> pTipoCompetencia =
		Projections.fields(TipoCompetencia.class,
			qTipoCompetencia.id,
			qTipoCompetencia.nome,
			qTipoCompetencia.nomeCurto
		);

	private final QBean<ApontamentoFuncional> pFeedForwardRecebido =
		Projections.fields(ApontamentoFuncional.class,
			qApontamentoFuncional.id,
			qApontamentoFuncional.dataApontamento,
			qApontamentoFuncional.graduacaoVisual,
			qApontamentoFuncional.feedforward,
			Projections.fields(Funcionario.class,
				qApontamentoFuncional.avaliador.id,
				qApontamentoFuncional.avaliador.nome
			).as("avaliador"),
			Projections.fields(Competencia.class,
				qApontamentoFuncional.competencia.id,
				qApontamentoFuncional.competencia.descricaoAbreviada
			).as("competencia")
		);

	@Override
	public List<TipoCompetenciaMobileTO> listTipoCompetenciaByLoggedFuncionario() {
		// TODO No futuro, obter ID do funcionário a partir do ID do usuário
		// (esperar a refatoração de Funcionário e criação da entidade Usuário)
		// (e.g. funcionarioService.findByUsuarioId(usuarioId))
		UserTO avaliado = getLoggedUser();

		JPAQuery<TipoCompetencia> query = select(pTipoCompetencia)
			.distinct()
			.from(qApontamentoFuncional)
			.innerJoin(qApontamentoFuncional.competencia, qCompetencia)
			.innerJoin(qCompetencia.tipo, qTipoCompetencia)
			.where(createDefaultPredicate(avaliado))
			.orderBy(qTipoCompetencia.nomeCurto.asc());

		List<TipoCompetencia> apontamentos = query.fetch();
		List<TipoCompetenciaMobileTO> tiposCompetenciaTO =
			mapper.map(apontamentos, TipoCompetenciaMobileTO.class);

		return tiposCompetenciaTO;
	}

	@Override
	public List<FeedForwardRecebidoMobileTO> listByTipoCompetenciaIdAndLoggedFuncionario(Long tipoCompetenciaId) {
		// TODO No futuro, obter ID do funcionário a partir do ID do usuário
		// (esperar a refatoração de Funcionário e criação da entidade Usuário)
		// (e.g. funcionarioService.findByUsuarioId(usuarioId))
		UserTO avaliado = getLoggedUser();

		BooleanExpression condition = createDefaultPredicate(avaliado)
			.and(qApontamentoFuncional.competencia.tipo.id.eq(tipoCompetenciaId));

		JPAQuery<ApontamentoFuncional> query = select(pFeedForwardRecebido)
			.from(qApontamentoFuncional)
			.where(condition)
			.orderBy(qApontamentoFuncional.dataApontamento.desc());

		List<ApontamentoFuncional> ffRecebidos = query.fetch();
		List<FeedForwardRecebidoMobileTO> ffRecebidosTO = map(ffRecebidos).stream()
			.map(this::setAdditionalData)
			.collect(Collectors.toList());

		return ffRecebidosTO;
	}

	@Override
	protected ServiceFilter<ApontamentoFuncional> getServiceFilter() {
		UserTO avaliadorUser = getLoggedUser();
		return ServiceFilter.create(qApontamentoFuncional, createDefaultPredicate(avaliadorUser));
	}

	private BooleanExpression createDefaultPredicate(UserTO avaliado) {
		return qApontamentoFuncional.feedforward.isNotNull()
			.and(qApontamentoFuncional.avaliado.id.eq(avaliado.getId()));
	}

	private FeedForwardRecebidoMobileTO setAdditionalData(FeedForwardRecebidoMobileTO ffRecebido) {
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
