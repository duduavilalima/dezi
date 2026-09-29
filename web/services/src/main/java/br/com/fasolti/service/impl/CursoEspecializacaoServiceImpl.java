package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.transfer.ContratanteFasoltiTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.repository.CursoEspecializacaoRepository;
import br.com.fasolti.avd.service.CursoEspecializacaoService;
import br.com.fasolti.avd.transfer.CursoEspecializacaoTO;
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
public class CursoEspecializacaoServiceImpl
	extends BaseAvdService<CursoEspecializacao, CursoEspecializacaoTO, CursoEspecializacaoTO>
	implements CursoEspecializacaoService {

	@Autowired
	private CursoEspecializacaoRepository cursoEspecializacaoRepository;

	private final QCursoEspecializacao qCursoEspecializacao = QCursoEspecializacao.cursoEspecializacao;

	@Autowired
	public CursoEspecializacaoServiceImpl(CursoEspecializacaoRepository repository) {
		super(repository);
	}

	@Override
	protected ServiceFilter<CursoEspecializacao> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		mandatoryFilter.or(qCursoEspecializacao.contratante.id.eq(contratante.getId()));

		return ServiceFilter.create(qCursoEspecializacao, mandatoryFilter);
	}

	private void validar(CursoEspecializacaoTO cursoEspecializacao) {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		CursoEspecializacaoTO cursoEspecializacaoExistente = findByNomeAndContratante(cursoEspecializacao.getNome(), contratante.getId());
		if (cursoEspecializacaoExistente != null && !cursoEspecializacaoExistente.getId().equals(cursoEspecializacao.getId())) {
			//FIXME: Passar a entidade para internacionalização
			throw new ValidationException("validation.duplicate", "curso de graduação", "field.nome", cursoEspecializacao.getNome());
		}
	}

	@Override
	protected void buildSearchQuery(JPAQuery<CursoEspecializacao> query) {
		query.select(Projections.fields(
			CursoEspecializacao.class,
			qCursoEspecializacao.id,
			qCursoEspecializacao.nome,
			qCursoEspecializacao.preencheOutrosCursos,
			qCursoEspecializacao.situacao
		));
	}

	@Override
	protected void doBeforeSave(CursoEspecializacaoTO object) throws ServiceException {
		validar(object);
		// Sempre salva com a empresa do usuário logado.
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		object.setContratante(contratante);
	}

	@Override
	protected void doBeforeUpdate(CursoEspecializacaoTO object) throws ServiceException {
		validar(object);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<CursoEspecializacaoTO> findByContratanteUsuarioLogadoAndSituacaoAtivo() {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		return map(cursoEspecializacaoRepository.findByContratanteIdAndSituacao(contratante.getId(), Situacao.ATIVA));
	}

	@Override
	protected CursoEspecializacaoTO doAfterCreate(CursoEspecializacaoTO object) throws br.com.fasolti.commons.lang.ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return object;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public CursoEspecializacaoTO findByNomeAndContratante(String nome, Long idContratante) {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		return map(cursoEspecializacaoRepository.findByContratanteIdAndNomeIgnoreCase(contratante.getId(), nome));
	}
}
