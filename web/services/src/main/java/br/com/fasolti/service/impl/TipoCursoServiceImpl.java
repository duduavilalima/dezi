package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.QTipoCurso;
import br.com.fasolti.avd.domain.TipoCurso;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.repository.TipoCursoRepository;
import br.com.fasolti.avd.service.TipoCursoService;
import br.com.fasolti.avd.transfer.TipoCursoTO;
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
public class TipoCursoServiceImpl
	extends BaseAvdService<TipoCurso, TipoCursoTO, TipoCursoTO>
	implements TipoCursoService {

	@Autowired
	private TipoCursoRepository tipoCursoRepository;

	private final QTipoCurso qTipoCurso = QTipoCurso.tipoCurso;

	@Autowired
	public TipoCursoServiceImpl(TipoCursoRepository repository) {
		super(repository);
	}

	@Override
	protected ServiceFilter<TipoCurso> getServiceFilter() {
		Long contratanteId = getLoggedUser().getEmpresa().getContratante().getId();
		return ServiceFilter.create(qTipoCurso, qTipoCurso.contratante.id.eq(contratanteId));
	}

	@Override
	protected void buildSearchQuery(JPAQuery<TipoCurso> query) {
		query.select(Projections.fields(
			TipoCurso.class,
			qTipoCurso.id,
			qTipoCurso.nome,
			qTipoCurso.situacao,
			qTipoCurso.preencheEspecializacao,
			qTipoCurso.preencheGraduacao
		));
	}

	@Override
	protected void doBeforeSave(TipoCursoTO object) throws ServiceException {
		validar(object);
		// Sempre salva com a empresa do usuário logado.
		object.setContratante(getLoggedUser().getEmpresa().getContratante());
	}

	@Override
	protected void doBeforeUpdate(TipoCursoTO object) throws ServiceException {
		validar(object);
	}

	private void validar(TipoCursoTO tipoCurso) {
		TipoCursoTO tipoCursoExistente = findByNomeAndContratante(tipoCurso.getNome(), getLoggedUser().getEmpresa().getContratante().getId());
		if (tipoCursoExistente != null && !tipoCursoExistente.getId().equals(tipoCurso.getId())) {
			//FIXME: Passar a entidade para internacionalização
			throw new ValidationException("validation.duplicate", "tipo de curso", "field.nome", tipoCurso.getNome());
		}
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<TipoCursoTO> findByContratanteUsuarioLogadoSituacaoAtivo() {
		return map(tipoCursoRepository.findByContratanteIdAndSituacao(getLoggedUser().getEmpresa().getContratante().getId(), Situacao.ATIVA));
	}

	@Override
	protected TipoCursoTO doAfterCreate(TipoCursoTO object) throws br.com.fasolti.commons.lang.ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return object;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public TipoCursoTO findByNomeAndContratante(String nome, Long idContratante) {
		return map(tipoCursoRepository.findByContratanteIdAndNomeIgnoreCase(getLoggedUser().getEmpresa().getContratante().getId(), nome));
	}
}
