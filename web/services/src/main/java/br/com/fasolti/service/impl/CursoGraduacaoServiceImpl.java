package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.CursoGraduacao;
import br.com.fasolti.avd.domain.QCursoGraduacao;
import br.com.fasolti.avd.transfer.ContratanteFasoltiTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.repository.CursoGraduacaoRepository;
import br.com.fasolti.avd.service.CursoGraduacaoService;
import br.com.fasolti.avd.transfer.CursoGraduacaoTO;
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
public class CursoGraduacaoServiceImpl
	extends BaseAvdService<CursoGraduacao, CursoGraduacaoTO, CursoGraduacaoTO>
	implements CursoGraduacaoService {

	@Autowired
	private CursoGraduacaoRepository cursoGraduacaoRepository;

	private final QCursoGraduacao qCursoGraduacao = QCursoGraduacao.cursoGraduacao;

	@Autowired
	public CursoGraduacaoServiceImpl(CursoGraduacaoRepository repository) {
		super(repository);
	}

	@Override
	protected ServiceFilter<CursoGraduacao> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();

		mandatoryFilter.or(qCursoGraduacao.contratante.id.eq(contratante.getId()));

		return ServiceFilter.create(qCursoGraduacao, mandatoryFilter);
	}

	@Override
	protected void buildSearchQuery(JPAQuery<CursoGraduacao> query) {
		query.select(Projections.fields(
			CursoGraduacao.class,
			qCursoGraduacao.id,
			qCursoGraduacao.nome,
			qCursoGraduacao.preencheOutrosCursos,
			qCursoGraduacao.situacao
		));
	}

	private void validar(CursoGraduacaoTO cursoGraduacao) {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		CursoGraduacaoTO cursoGraduacaoExistente = findByNomeAndContratante(cursoGraduacao.getNome(), contratante.getId());
		if (cursoGraduacaoExistente != null && !cursoGraduacaoExistente.getId().equals(cursoGraduacao.getId())) {
			//FIXME: Passar a entidade para internacionalização
			throw new ValidationException("validation.duplicate", "curso de graduação", "field.nome", cursoGraduacao.getNome());
		}
	}

	@Override
	protected void doBeforeSave(CursoGraduacaoTO object) throws ServiceException {
		validar(object);
		// Sempre salva com a empresa do usuário logado.
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		object.setContratante(contratante);
	}

	@Override
	protected void doBeforeUpdate(CursoGraduacaoTO object) throws ServiceException {
		validar(object);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<CursoGraduacaoTO> listAtivos() {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		return map(cursoGraduacaoRepository.findByContratanteIdAndSituacao(contratante.getId(), Situacao.ATIVA));
	}

	@Override
	protected CursoGraduacaoTO doAfterCreate(CursoGraduacaoTO object) throws br.com.fasolti.commons.lang.ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return object;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public CursoGraduacaoTO findByNomeAndContratante(String nome, Long idContratante) {
		ContratanteFasoltiTO contratante = getLoggedUser().getEmpresa().getContratante();
		return map(cursoGraduacaoRepository.findByContratanteIdAndNomeIgnoreCase(contratante.getId(), nome));
	}
}
