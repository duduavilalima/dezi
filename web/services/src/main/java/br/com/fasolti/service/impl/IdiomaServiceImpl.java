package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.transfer.ContratanteFasoltiTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.repository.IdiomaRepository;
import br.com.fasolti.avd.service.IdiomaService;
import br.com.fasolti.avd.transfer.IdiomaTO;
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

import java.util.List;

@Service
public class IdiomaServiceImpl
	extends BaseAvdService<Idioma, IdiomaTO, IdiomaTO>
	implements IdiomaService {

	@Autowired
	private IdiomaRepository idiomaRepository;

	private final QIdioma qIdioma = QIdioma.idioma;

	@Autowired
	public IdiomaServiceImpl(IdiomaRepository repository) {
		super(repository);
	}

	@Override
	protected ServiceFilter<Idioma> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		mandatoryFilter.or(qIdioma.contratante.id.eq(contratante.getId()));

		return ServiceFilter.create(qIdioma, mandatoryFilter);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Idioma> query) {
		query.select(Projections.fields(
			Idioma.class,
			qIdioma.id,
			qIdioma.nome,
			qIdioma.situacao)
		);
	}

	private void validar(IdiomaTO idioma) {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		IdiomaTO cursoEspecializacaoExistente = findByNomeAndContratante(idioma.getNome(), contratante.getId());
		if (cursoEspecializacaoExistente != null && !cursoEspecializacaoExistente.getId().equals(idioma.getId())) {
			//FIXME: Passar a entidade para internacionalização
			throw new ValidationException("validation.duplicate", "curso de graduação", "field.nome", idioma.getNome());
		}
	}

	@Override
	protected void doBeforeSave(IdiomaTO object) throws ServiceException {
		validar(object);
		// Sempre salva com a empresa do usuário logado.
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		object.setContratante(contratante);
	}

	@Override
	protected void doBeforeUpdate(IdiomaTO object) throws ServiceException {
		//FIXME Checar todos os doBeforeUpdate, eles estão forçando a empresa ou contratante e não pode alterar isso no update (talvez validar e lançar exceção se for diferente)
		validar(object);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<IdiomaTO> findIdiomasByContratanteAndSituacao() {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		return map(idiomaRepository.findByContratanteIdAndSituacao(contratante.getId(), Situacao.ATIVA));
	}

	@Override
	protected IdiomaTO doAfterCreate(IdiomaTO object) throws br.com.fasolti.commons.lang.ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return object;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public IdiomaTO findByNomeAndContratante(String nome, Long idContratante) {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		return map(idiomaRepository.findByContratanteIdAndNomeIgnoreCase(contratante.getId(), nome));
	}
}
