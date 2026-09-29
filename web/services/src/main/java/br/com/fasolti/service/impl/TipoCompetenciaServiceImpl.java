package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.QTipoCompetencia;
import br.com.fasolti.avd.domain.TipoCompetencia;
import br.com.fasolti.avd.repository.PreenchimentoRepository;
import br.com.fasolti.avd.repository.TipoCompetenciaRepository;
import br.com.fasolti.avd.service.CargoService;
import br.com.fasolti.avd.service.CargoTipoCompetenciaService;
import br.com.fasolti.avd.service.TipoCompetenciaService;
import br.com.fasolti.avd.transfer.ArvoreTipoCompetenciaTO;
import br.com.fasolti.avd.transfer.TipoCompetenciaTO;
import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TipoCompetenciaServiceImpl
	extends BaseAvdService<TipoCompetencia, TipoCompetenciaTO, TipoCompetenciaTO>
	implements TipoCompetenciaService {

	@Autowired
	private TipoCompetenciaRepository tipoCompetenciaRepository;

	private final QTipoCompetencia qTipoCompetencia = QTipoCompetencia.tipoCompetencia;

	@Autowired
	private PreenchimentoRepository avaliacaoParticipanteRepository;

	@Autowired
	private CargoTipoCompetenciaService cargoTipoCompetenciaService;

	@Autowired
	private CargoService cargoService;


	@Autowired
	public TipoCompetenciaServiceImpl(TipoCompetenciaRepository tipoCompetenciaRepository) {
		super(tipoCompetenciaRepository);
	}

	@Override
	protected void doAfterSearch(List<TipoCompetencia> entities, List<TipoCompetenciaTO> result) throws ServiceException {
		result.forEach(tipoCompetenciaTO -> {
				calcularNumeroDeFilhos(tipoCompetenciaTO);
				if (tipoCompetenciaTO.getIdPai() != null) {
					tipoCompetenciaTO.setPaiTO(getById(tipoCompetenciaTO.getIdPai()));
				}
			}
		);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<TipoCompetencia> query) {
		query.select(Projections.fields(
			TipoCompetencia.class,
			qTipoCompetencia.id,
			qTipoCompetencia.nome,
			qTipoCompetencia.descricao,
			qTipoCompetencia.idPai,
			qTipoCompetencia.situacao
		)).orderBy(qTipoCompetencia.nome.asc());
	}

	@Override
	protected ServiceFilter<TipoCompetencia> getServiceFilter() {
		return ServiceFilter.create(qTipoCompetencia, qTipoCompetencia.idEmpresa.eq(getLoggedUser().getEmpresa().getId()));
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, TipoCompetenciaTO exampleObj) {
		builder.and(qTipoCompetencia.idPai.isNull());
	}

	@Override
	protected void doBeforeSave(TipoCompetencia entity, TipoCompetenciaTO object) throws ServiceException {
		entity.setIdEmpresa(object.getEmpresa().getId());
		entity.setIdPai(object.getIdPai() == null ? null : object.getIdPai());
		entity.setNomeCurto(entity.getDescricao());
		object.setNomeCurto(entity.getDescricao());
		validar(object);
	}

	@Override
	protected void doBeforeUpdate(TipoCompetencia entity, TipoCompetenciaTO object) throws ServiceException {
		entity.setNomeCurto(entity.getDescricao());
		object.setNomeCurto(entity.getDescricao());
		validar(object);
	}

	@Override
	protected void doAfterUpdate(TipoCompetencia entity, TipoCompetenciaTO object) throws ServiceException {
		/* Ao inativar um TipoCompetencia, repassar para todos os cargos tipo competencia -> competencias -> detalhamentos */
		if(object.getSituacao().equals(Situacao.INATIVA)) {
			cargoTipoCompetenciaService.inativarCargoTipoCompetenciaByTipoCompetencia(entity.getId());
		}
	}

	@Override
	protected void setDefaultValues(TipoCompetenciaTO object) throws ServiceException {
		object.setSituacao(Situacao.ATIVA);
		object.setEmpresa(mapper.map(getLoggedUser().getEmpresa(), EmpresaSimpleTO.class));
	}

	@Override
	protected void doAfterLoad(TipoCompetenciaTO object) throws ServiceException {
		calcularNumeroDeFilhos(object);
		object.setEmpresa(mapper.map(getLoggedUser().getEmpresa(), EmpresaSimpleTO.class));
	}

	@Override
	public List<TipoCompetenciaTO> obterFilhosDoTipoDeComepetencia(TipoCompetenciaTO object) {
		return tipoCompetenciaRepository.findByIdPaiAndIdEmpresaOrderByNome(object.getId(), getLoggedUser().getEmpresa().getId())
			.stream()
			.map(tipoCompetencia -> {
				TipoCompetenciaTO tipoCompetenciaTO = map(tipoCompetencia);
				calcularNumeroDeFilhos(tipoCompetenciaTO);
				return tipoCompetenciaTO;
			}).collect(Collectors.toList());
	}

	public void calcularNumeroDeFilhos(TipoCompetenciaTO tipoCompetenciaTO) {
		tipoCompetenciaTO.setNumeroFilhos(obterFilhosDoTipoDeComepetencia(tipoCompetenciaTO).size());
	}

	@Override
	public List<TipoCompetenciaTO> obterTiposDeCompetenciaQueNaoTemPai() {
		return map(tipoCompetenciaRepository.findByIdPaiIsNullAndIdEmpresa(getLoggedUser().getEmpresa().getId()));
	}

	@Override
	public List<TipoCompetenciaTO> obterTipoCompetenciasPai() {
		return map(select()
			.where(qTipoCompetencia.idEmpresa.eq(getLoggedUser().getEmpresa().getId()).and(qTipoCompetencia.situacao.eq(Situacao.ATIVA).and(qTipoCompetencia.idPai.isNull())))
			.orderBy(qTipoCompetencia.nome.asc())
			.fetch());
	}

	@Override
	public List<TipoCompetenciaTO> obterTiposCompetenciasFilhosAtivos(Long idPai) {
		return map(select(Projections.fields(TipoCompetencia.class, qTipoCompetencia.id, qTipoCompetencia.nome, qTipoCompetencia.situacao, qTipoCompetencia.idPai))
			.where(new BooleanBuilder().and(qTipoCompetencia.idPai.eq(idPai)).and(qTipoCompetencia.situacao.eq(Situacao.ATIVA)))
			.orderBy(qTipoCompetencia.nome.asc())
			.fetch());
	}

	@Override
	public List<ArvoreTipoCompetenciaTO> obterArvoreDeTiposDeCompetencia() {
		List<ArvoreTipoCompetenciaTO> arvorePai = new ArrayList<>();
		List<TipoCompetenciaTO> tiposQueNaotemPai = obterTipoCompetenciasPai().stream()
			.sorted((a, b) -> b.getNome().compareTo(a.getNome()))
			.collect(Collectors.toList());
		tiposQueNaotemPai.forEach(pai -> arvorePai.add(mapper.map(pai, ArvoreTipoCompetenciaTO.class)));
		for (ArvoreTipoCompetenciaTO cargoTreeTOPai : arvorePai) {
			cargoTreeTOPai.setChildren(filhosToTree(cargoTreeTOPai.getId()));
			obterFilhosCargoPai(cargoTreeTOPai);
		}
		return arvorePai;
	}

	@Override
	public void obterFilhosCargoPai(ArvoreTipoCompetenciaTO tipoTreeTOPai) {
		if (tipoTreeTOPai.getChildren().isEmpty()) {
			return;
		}
		tipoTreeTOPai.getChildren().forEach(tipoTOFilho -> {
				tipoTOFilho.setChildren(filhosToTree(tipoTOFilho.getId()));
				obterFilhosCargoPai(tipoTOFilho);
			}
		);
	}

	private List<ArvoreTipoCompetenciaTO> filhosToTree(Long id) {
		List<TipoCompetenciaTO> listaFilhos = obterTiposCompetenciasFilhosAtivos(id)
			.stream()
			.sorted((a, b) -> b.getNome().compareTo(a.getNome()))
			.collect(Collectors.toList());

		List<ArvoreTipoCompetenciaTO> listaFilhosTreeTO = new ArrayList<>();

		listaFilhos.forEach(filho -> {
				listaFilhosTreeTO.add(mapper.map(filho, ArvoreTipoCompetenciaTO.class));
			}
		);
		return listaFilhosTreeTO;
	}

	@Override
	public List<ArvoreTipoCompetenciaTO> obterArvoreDeTiposDeCompetencia(Long id) {
		List<ArvoreTipoCompetenciaTO> arvore = new ArrayList<>();
		ArvoreTipoCompetenciaTO no = converterTipoEmArvore(getById(id));
		no.setChildren(new ArrayList<>());
		arvore.add(inserirSubniveis(no));
		return arvore;
	}

	@Override
	public List<TipoCompetenciaTO> listBySituacao(Situacao situacao) {
		return map(select(Projections.fields(TipoCompetencia.class,
			qTipoCompetencia.id,
			qTipoCompetencia.nome,
			qTipoCompetencia.situacao,
			qTipoCompetencia.idPai))
			.where(new BooleanBuilder().and(qTipoCompetencia.situacao.eq(Situacao.ATIVA)).and(qTipoCompetencia.idEmpresa.eq(getLoggedUser().getEmpresa().getId())))
			.orderBy(qTipoCompetencia.nome.asc()).fetch());
	}

	@Override
	public List<TipoCompetenciaTO> obterTiposDeCompetenciaAtivos() {
		return map(select(
			Projections.fields(
				TipoCompetencia.class,
				qTipoCompetencia.id,
				qTipoCompetencia.descricao,
				qTipoCompetencia.nome,
				qTipoCompetencia.nomeCurto,
				qTipoCompetencia.situacao,
				qTipoCompetencia.idEmpresa,
				qTipoCompetencia.idPai
			)
		)
			.where(qTipoCompetencia.situacao.eq(Situacao.ATIVA))
			.fetch());
	}

	private ArvoreTipoCompetenciaTO inserirSubniveis(ArvoreTipoCompetenciaTO no) {

		List<TipoCompetencia> listFilhos = tipoCompetenciaRepository.findByIdPaiAndIdEmpresaOrderByNome(no.getId(), getLoggedUser().getEmpresa().getId());

		listFilhos.forEach(
			tipoCompetencia -> {
				ArvoreTipoCompetenciaTO to = converterTipoEmArvore(map(tipoCompetencia));
				to.setChildren(new ArrayList<>());
				no.getChildren().add(inserirSubniveis(to));
			}
		);
		return no;
	}

	private ArvoreTipoCompetenciaTO converterTipoEmArvore(TipoCompetenciaTO tipoCompetencia) {
		ArvoreTipoCompetenciaTO arvoreTipoCompetenciaTO = new ArvoreTipoCompetenciaTO();
		arvoreTipoCompetenciaTO.setId(tipoCompetencia.getId());
		arvoreTipoCompetenciaTO.setNome(tipoCompetencia.getNome());
		arvoreTipoCompetenciaTO.setDescricao(tipoCompetencia.getDescricao());
		arvoreTipoCompetenciaTO.setIdPai(tipoCompetencia.getIdPai() != null ? tipoCompetencia.getIdPai() : null);
		arvoreTipoCompetenciaTO.setSituacao(tipoCompetencia.getSituacao());
		arvoreTipoCompetenciaTO.setChildren(new ArrayList<>());
		return arvoreTipoCompetenciaTO;
	}

	private void validar(TipoCompetenciaTO object) {
		// Busca tipo de competencia repetidos
		Iterable<TipoCompetencia> tipoCompetenciaList =
			select(Projections.fields(
				TipoCompetencia.class,
				qTipoCompetencia.id,
				qTipoCompetencia.nome,
				qTipoCompetencia.idEmpresa)).fetch();

		tipoCompetenciaList.forEach(tipoCompetencia -> {
				if ((object.getId() == null || !object.getId().equals(tipoCompetencia.getId()))
					&& validateNomeAndEmpresa(tipoCompetencia, object)) {
					throw new ValidationException("validation.duplicate", "tipo de competência", "field.tipodecompetencia", object.getNome());
				}
			}
		);
	}

	private boolean validateNomeAndEmpresa(TipoCompetencia tipoCompetencia, TipoCompetenciaTO object) {
		if (tipoCompetencia.getNome().equals(object.getNome()) && tipoCompetencia.getIdEmpresa().equals(getLoggedUser().getEmpresa().getId())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<TipoCompetenciaTO> obterTipoCompentenciaAscendentes(TipoCompetenciaTO tipoCompetenciaTO) {
		List<TipoCompetenciaTO> ascendentes = new ArrayList<>();
		TipoCompetencia tipoCompetencia = null;

		if (tipoCompetenciaTO != null && tipoCompetenciaTO.getId() != null)
			tipoCompetencia = select(
				Projections.fields(
					TipoCompetencia.class,
					qTipoCompetencia.id,
					qTipoCompetencia.nomeCurto,
					qTipoCompetencia.nome,
					qTipoCompetencia.descricao,
					qTipoCompetencia.idEmpresa,
					qTipoCompetencia.idPai
				)
			).where(new BooleanBuilder().and(qTipoCompetencia.id.eq(tipoCompetenciaTO.getId()))).fetchOne();

		if (tipoCompetencia != null) {
			if (tipoCompetencia.getIdPai() != null) {
				ascendentes.add(map(tipoCompetencia));
				TipoCompetenciaTO paiTO = load(tipoCompetencia.getIdPai());
				List<TipoCompetenciaTO> resultado = obterTipoCompentenciaAscendentes(paiTO);
				ascendentes.addAll(resultado);
			} else {
				TipoCompetenciaTO tc = load(tipoCompetencia.getId());
				ascendentes.add(tc);
			}
		}


		return ascendentes;
	}

	@Override
	public List<TipoCompetenciaTO> findByIdPaiAndIdEmpresaOrderByNome(Long idPai, Long idEmpresa) {
		return map(select(Projections.fields(TipoCompetencia.class,
			qTipoCompetencia.id,
			qTipoCompetencia.nome,
			qTipoCompetencia.situacao,
			qTipoCompetencia.idPai))
			.where(new BooleanBuilder().and(qTipoCompetencia.idPai.eq(idPai)).and(qTipoCompetencia.idEmpresa.eq(idEmpresa)))
			.orderBy(qTipoCompetencia.nome.asc()).fetch());
	}
}

