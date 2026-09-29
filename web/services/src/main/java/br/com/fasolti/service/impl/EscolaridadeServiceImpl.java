package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Escolaridade;
import br.com.fasolti.avd.domain.QEscolaridade;
import br.com.fasolti.avd.transfer.ContratanteFasoltiTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.repository.EscolaridadeRepository;
import br.com.fasolti.avd.service.EscolaridadeService;
import br.com.fasolti.avd.transfer.EscolaridadeTO;
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
public class EscolaridadeServiceImpl
	extends BaseAvdService<Escolaridade, EscolaridadeTO, EscolaridadeTO>
	implements EscolaridadeService {

	@Autowired
	private EscolaridadeRepository escolaridadeRepository;

	private final QEscolaridade qEscolaridade = QEscolaridade.escolaridade;

	@Override
	protected ServiceFilter<Escolaridade> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		mandatoryFilter.or(qEscolaridade.contratante.id.eq(contratante.getId()));

		return ServiceFilter.create(qEscolaridade, mandatoryFilter);
	}

	@Autowired
	public EscolaridadeServiceImpl(EscolaridadeRepository repository) {
		super(repository);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<EscolaridadeTO> listAllActiveForLoggedUser() {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		return map(escolaridadeRepository.findByContratanteIdAndSituacao(contratante.getId(), Situacao.ATIVA));
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Escolaridade> query) {
		query.select(Projections.fields(
			Escolaridade.class,
			qEscolaridade.id,
			qEscolaridade.nome,
			qEscolaridade.preencheOutrosCursos,
			qEscolaridade.preencheTipoCurso,
			qEscolaridade.situacao
		));
	}

	private void validar(EscolaridadeTO escolaridade) {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		EscolaridadeTO escolaridadeExistente = findByNomeAndContratante(escolaridade.getNome(), contratante.getId());
		if (escolaridadeExistente != null && !escolaridadeExistente.getId().equals(escolaridade.getId())) {
			//FIXME: Passar a entidade para internacionalização
			throw new ValidationException("validation.duplicate", "escolaridade", "field.nome", escolaridade.getNome());
		}
	}

	@Override
	protected void doBeforeSave(EscolaridadeTO object) throws ServiceException {
		validar(object);
		//FIXME Valores default devem ser atribuidos no setDefaultValues() - Varrer todos os serviços para corrigir isso
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		object.setContratante(contratante);
	}

	@Override
	protected void doBeforeUpdate(EscolaridadeTO object) throws ServiceException {
		validar(object);
	}

	@Override
	protected EscolaridadeTO doAfterCreate(EscolaridadeTO object) throws ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return object;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public EscolaridadeTO findByNomeAndContratante(String nome, Long idContratante) {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		return map(escolaridadeRepository.findByContratanteIdAndNomeIgnoreCase(contratante.getId(), nome));
	}
}
