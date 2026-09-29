package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.AvaliacaoRepository;
import br.com.fasolti.avd.repository.CampanhaRepository;
import br.com.fasolti.avd.service.AvaliacaoService;
import br.com.fasolti.avd.service.CampanhaService;
import br.com.fasolti.avd.transfer.CampanhaTO;
import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class
CampanhaServiceImpl
	extends BaseAvdService<Campanha, CampanhaTO, CampanhaTO>
	implements CampanhaService {

	private final QCampanha qCampanha = QCampanha.campanha;

	@Autowired
	private CampanhaRepository campanhaRepository;

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	private Mapper mapper;

	@Autowired
	public CampanhaServiceImpl(CampanhaRepository campanhaRepository) {
		super(campanhaRepository);
	}

	@Override
	protected void doAfterLoad(CampanhaTO campanhaTO) throws ServiceException {
		campanhaTO.setTotalAvaliacoes(avaliacaoService.count(campanhaTO));
	}

	@Override
	protected void doAfterSearch(List<Campanha> entities, List<CampanhaTO> result) throws ServiceException {
		calcularTotalAvaliacoes(result);
	}

	@Override
	protected ServiceFilter<Campanha> getServiceFilter() {
		//FIXME Criar nas classes base o método canUpdate
		return ServiceFilter.create(qCampanha, qCampanha.empresa.id.eq(getLoggedUser().getEmpresa().getId()));
	}

	private void validar(CampanhaTO campanha) {
		CampanhaTO campanhaExistente = findByEmpresaAndDescricaoIgnoreCase(getLoggedUser().getEmpresa().getId(), campanha.getDescricao());
		if (campanhaExistente != null && !campanhaExistente.getId().equals(campanha.getId()) && campanhaExistente.getDescricao().equalsIgnoreCase(campanha.getDescricao())) {
			//FIXME: Passar a entidade para internacionalização
			throw new ValidationException("validation.duplicate", "campanha", "field.campanha", campanha.getDescricao());
		}
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Campanha> query) {
		query.select(
			Projections.fields(Campanha.class,
				qCampanha.id,
				qCampanha.descricao,
				qCampanha.dataCriacao,
				qCampanha.avEmLiberacao,
				qCampanha.situacao,
				Projections.fields(Empresa.class, QEmpresa.empresa.nome)
					.as("empresa"))
		).join(qCampanha.empresa, QEmpresa.empresa);
	}

	@Override
	protected void doBeforeTransition(CampanhaTO object, Enum status) throws ServiceException {
		// TODO Verificar as situações onde não pode inativar a campanha
	}

	@Override
	protected void doAfterTransition(CampanhaTO object, Enum status) throws ServiceException {
		// TODO Quando inativar uma campanha tem que inativar todas as avaliações dela
	}

	@Override
	protected void setDefaultValues(CampanhaTO object) throws ServiceException {
		object.setEmpresa(mapper.map(getLoggedUser().getEmpresa(), EmpresaSimpleTO.class));
		object.setStatus(Situacao.ATIVA); // TODO Criar comportamento padrão para o TransactionalObject
	}

	@Override
	protected void doBeforeSave(CampanhaTO object) throws ServiceException {
		validar(object);
	}

	@Override
	protected void doBeforeUpdate(CampanhaTO object) throws ServiceException {
		validar(object);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public CampanhaTO findByEmpresaAndDescricaoIgnoreCase(Long idEmpresa, String descricao) {
		return map(campanhaRepository.findByEmpresaIdAndDescricaoIgnoreCase(getLoggedUser().getEmpresa().getId(), descricao.trim().toLowerCase()));
	}

	@Override
	public List<CampanhaTO> findByEmpresa() {
		return map(campanhaRepository.findByEmpresaId(getLoggedUser().getEmpresa().getId()));
	}

	@Override
	public List<CampanhaTO> findByEmpresaPerfil() {
		if (TipoPerfil.ADMINISTRADOR.equals(getLoggedUser().getTipoPerfil())
				|| TipoPerfil.GESTOR_RH.equals(getLoggedUser().getTipoPerfil()))
			return map(campanhaRepository.findByEmpresaId(getLoggedUser().getEmpresa().getId()));

		List<Campanha> campanhas = campanhaRepository.findByEmpresaId(getLoggedUser().getEmpresa().getId());
		List<Campanha> campanhasAux = new ArrayList<>();

		campanhas.forEach((campanha) -> {
			List<Avaliacao> avaliacoes = avaliacaoRepository.findByCampanhaId(campanha.getId());

			if (Situacao.ATIVA.equals(campanha.getStatus())) {
				boolean autoavaliacao = true;
				for (Avaliacao avaliacao : avaliacoes) {
					if ((avaliacao.getGestores()
						|| avaliacao.getPares()
						|| avaliacao.getSubordinados())
						|| (avaliacao.getAutoavaliacao() != null && !avaliacao.getAutoavaliacao())
					) {
						autoavaliacao = false;
						break;
					}
				}
				if (!autoavaliacao)
					campanhasAux.add(campanha);
			}
		});

		return map(campanhasAux);
	}

	@Override
	public List<CampanhaTO> findByEmpresaActiveProjections() {
		List<CampanhaTO> resultList = map(
			select(
				Projections.fields(Campanha.class,
					qCampanha.id,
					qCampanha.descricao,
					qCampanha.avEmLiberacao,
					qCampanha.situacao,
						Projections.fields(Empresa.class, QEmpresa.empresa.nome)
						.as("empresa"))
			)
				.join(qCampanha.empresa, QEmpresa.empresa)
				.where(qCampanha.situacao.eq(Situacao.ATIVA))
				.fetch()
		);
		return calcularTotalAvaliacoes(resultList);
	}

	private List<CampanhaTO> calcularTotalAvaliacoes(List<CampanhaTO> campanhaTOList) {
		for (CampanhaTO campanhaSearchTO : campanhaTOList) {
			campanhaSearchTO.setTotalAvaliacoes(avaliacaoService.count(campanhaSearchTO));
		}
		return campanhaTOList;
	}

}
