package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.QUnidade;
import br.com.fasolti.avd.domain.Unidade;
import br.com.fasolti.avd.repository.UnidadeRepository;
import br.com.fasolti.avd.service.UnidadeService;
import br.com.fasolti.avd.transfer.UnidadeTO;
import br.com.fasolti.avd.transfer.UnidadeTreeTO;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class UnidadeServiceImpl extends BaseAvdService<Unidade, UnidadeTO, UnidadeTO> implements UnidadeService {

	private static final QUnidade qUnidade = QUnidade.unidade;

	@Autowired
	private UnidadeRepository unidadeRepository;

	@Autowired
	public UnidadeServiceImpl(UnidadeRepository repository) {
		super(repository);
	}

	@Override
	protected ServiceFilter<Unidade> getServiceFilter() {
		// Todos os usuários tem acesso às unidades da própria empresa
		return ServiceFilter.create(qUnidade, qUnidade.empresa.id.eq(getLoggedUser().getEmpresa().getId()));
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Unidade> query) {
		query.select(Projections.fields(
			Unidade.class,
			qUnidade.id,
			qUnidade.nome,
			qUnidade.descricao,
			qUnidade.situacao
		));
	}

	@Override
	protected UnidadeTO doAfterCreate(UnidadeTO object) throws ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return super.doAfterCreate(object);
	}

	@Override
	protected void doBeforeSave(UnidadeTO object) throws ServiceException {
		object.setEmpresa(getLoggedUser().getEmpresa());
		validar(object);
	}

	@Override
	protected void doBeforeUpdate(UnidadeTO object) throws ServiceException {
		validar(object);
	}

	@Override
	@Transactional(readOnly = true)
	public List<UnidadeTO> findByEmpresaIdAndNomeIgnoreCase(Long idEmpresa, String nome) {
		return map(unidadeRepository.findByEmpresaIdAndNomeIgnoreCase(idEmpresa, nome.trim().toLowerCase()));
	}

	private void validar(UnidadeTO unidade) {
		List<UnidadeTO> unidadeExistente = findByEmpresaIdAndNomeIgnoreCase(getLoggedUser().getEmpresa().getId(), unidade.getNome());

		if (unidadeExistente != null && !unidadeExistente.isEmpty()) {
			if(!unidadeExistente.get(0).getId().equals(unidade.getId())) {
				//FIXME: Passar a entidade para internacionalização
				throw new ValidationException("validation.duplicate", "unidade", "field.nome", unidade.getNome());
			}
		}
	}

	@Override
	public List<UnidadeTO> obterSubunidadesDeUnidade(Long idUnidade) {
		List<Unidade> listFilhos = select()
			.where(
				qUnidade.unidadePai.id.eq(idUnidade),
				qUnidade.empresa.id.eq(getLoggedUser().getEmpresa().getId()))
			.orderBy(qUnidade.nome.asc())
			.fetch();
		return map(listFilhos);
	}

	@Override
	public List<UnidadeTreeTO> listarArvoreDeHierarquiaDeUnidade() {
		// Trazer todos os Pais
		List<Unidade> listaPais = select()
			.where(
				qUnidade.unidadePai.isNull(),
				qUnidade.situacao.eq(Situacao.ATIVA),
				qUnidade.empresa.id.eq(getLoggedUser().getEmpresa().getId()))
			.orderBy(qUnidade.nome.asc())
			.fetch();

		List<UnidadeTreeTO> listaPaisTreeTO = new ArrayList<>();

		Collections.sort(listaPais, new Comparator<Unidade>() {
			@Override
			public int compare(Unidade o1, Unidade o2) {
				return o1.getNome().compareTo(o2.getNome());
			}
		});

		// Transformando a lista em TO
		for (Unidade pai : listaPais) {
			//FIXME: Obter apenas Unidade Pai.
			if (pai.getUnidadePai() == null) {
				listaPaisTreeTO.add(mapper.map(pai, UnidadeTreeTO.class));
			}
		}

		// Para cada pai, trazer filhos
		for (UnidadeTreeTO unidadeTreeTOPai : listaPaisTreeTO) {
			List<Unidade> listaFilhos = select(Projections.fields(Unidade.class, qUnidade.id, qUnidade.nome, qUnidade.situacao, qUnidade.unidadePai))
				.where(new BooleanBuilder().and(qUnidade.unidadePai.id.eq(unidadeTreeTOPai.getId())).and(qUnidade.situacao.eq(Situacao.ATIVA)))
				.orderBy(qUnidade.nome.asc())
				.fetch();

			List<UnidadeTreeTO> listaFilhosTreeTO = new ArrayList<>();

			Collections.sort(listaFilhos, new Comparator<Unidade>() {
				@Override
				public int compare(Unidade o1, Unidade o2) {
					return o1.getNome().compareTo(o2.getNome());
				}
			});

			for (Unidade pai : listaFilhos) {
				listaFilhosTreeTO.add(mapper.map(pai, UnidadeTreeTO.class));
			}

			unidadeTreeTOPai.setChildren(listaFilhosTreeTO);

			obterFilhosUnidadePai(unidadeTreeTOPai);
		}

		return listaPaisTreeTO;
	}

	@Override
	public List<UnidadeTO> listarUnidadesAtivas() {
		List<Unidade> lista = select(Projections.fields(Unidade.class, qUnidade.id, qUnidade.nome, qUnidade.situacao))
			.where(
				qUnidade.empresa.id.eq(getLoggedUser().getEmpresa().getId()),
				qUnidade.situacao.eq(Situacao.ATIVA))
			.orderBy(qUnidade.nome.asc())
			.fetch();
		return map(lista);
	}

	private void obterFilhosUnidadePai(UnidadeTreeTO UnidadeTreeTOPai) {
		if (UnidadeTreeTOPai.getChildren().isEmpty()) {
			return;
		}

		List<UnidadeTreeTO> listaFilhosUnidadeTree = new ArrayList<>();

		for (UnidadeTreeTO unidadeTOFilho : UnidadeTreeTOPai.getChildren()) {
			List<Unidade> listaFilhos = select()
				.where(new BooleanBuilder().and(qUnidade.unidadePai.id.eq(unidadeTOFilho.getId())).and(qUnidade.situacao.eq(Situacao.ATIVA))).fetch();

			List<UnidadeTreeTO> listaFilhosTreeTO = new ArrayList<>();

			Collections.sort(listaFilhos, new Comparator<Unidade>() {
				@Override
				public int compare(Unidade o1, Unidade o2) {
					return o1.getNome().compareTo(o2.getNome());
				}
			});

			for (Unidade filho : listaFilhos) {
				listaFilhosTreeTO.add(mapper.map(filho, UnidadeTreeTO.class));
			}

			unidadeTOFilho.setChildren(listaFilhosTreeTO);

			obterFilhosUnidadePai(unidadeTOFilho);
		}
	}
}
