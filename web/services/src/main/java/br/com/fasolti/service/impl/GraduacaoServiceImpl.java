package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Graduacao;
import br.com.fasolti.avd.domain.ModeloGraduacao;
import br.com.fasolti.avd.domain.QGraduacao;
import br.com.fasolti.avd.domain.QModeloGraduacao;
import br.com.fasolti.avd.repository.GraduacaoRepository;
import br.com.fasolti.avd.service.CompDetalhamentoHintService;
import br.com.fasolti.avd.service.GraduacaoService;
import br.com.fasolti.avd.service.ModeloGraduacaoService;
import br.com.fasolti.avd.transfer.CompDetalhamentoHintTO;
import br.com.fasolti.avd.transfer.GraduacaoTO;
import br.com.fasolti.avd.transfer.ModeloGraduacaoTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class GraduacaoServiceImpl
	extends BaseAvdService<Graduacao, GraduacaoTO, GraduacaoTO>
	implements GraduacaoService {

	public final QGraduacao qGraduacao = QGraduacao.graduacao;

	@Autowired
	private GraduacaoRepository graduacaoRepository;

	@Autowired
	private CompDetalhamentoHintService compDetalhamentoHintService;

	@Autowired
	private ModeloGraduacaoService modeloGraduacaoService;

	@Autowired
	public GraduacaoServiceImpl(GraduacaoRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, GraduacaoTO exampleObj) {
		builder.and(dslHelper.eq(qGraduacao.modeloGraduacao, exampleObj.getModeloGraduacao()));

		if (exampleObj.getSituacao() != null) {
			if (exampleObj.getSituacao() != null) {
				builder.and(qGraduacao.situacao.eq(exampleObj.getSituacao()));
			} else if (exampleObj.getSituacao() != null) {
				builder.and(qGraduacao.situacao.eq(exampleObj.getSituacao()));
			}
		}
	}

	@Override
	public List<GraduacaoTO> findAllGraduacoesAtivasByModelo(ModeloGraduacaoTO modelo) {
		List<GraduacaoTO> result = map(graduacaoRepository.findByModeloGraduacaoIdOrderByOrdemAsc(modelo.getId()));

		List<GraduacaoTO> graduacaoList = new ArrayList<>();

		for (GraduacaoTO graduacaoTO : result) {
			if (graduacaoTO.getSituacao().equals("ATIVA"))
				graduacaoList.add(graduacaoTO);
		}

		return graduacaoList;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<GraduacaoTO> findAllGraduacoesByModelo(ModeloGraduacaoTO modelo) {
		return map(select(
			Projections.fields(
				Graduacao.class,
				qGraduacao.id,
				qGraduacao.hint,
				qGraduacao.label,
				qGraduacao.ordem,
				qGraduacao.ponto,
				qGraduacao.situacao,
				Projections.fields(
					ModeloGraduacao.class,
					QModeloGraduacao.modeloGraduacao.id
				).as("modeloGraduacao")
			)
		)
			.join(qGraduacao.modeloGraduacao, QModeloGraduacao.modeloGraduacao)
			.where(qGraduacao.modeloGraduacao.id.eq(modelo.getId()))
			.orderBy(qGraduacao.ordem.asc())
			.fetch());
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<GraduacaoTO> findAllGraduacoesByModeloAndSituacao(ModeloGraduacaoTO modelo, Situacao situacao) {
		return map(select(
				Projections.fields(
					Graduacao.class,
					qGraduacao.id,
					qGraduacao.hint,
					qGraduacao.label,
					qGraduacao.ordem,
					qGraduacao.ponto,
					qGraduacao.situacao,
					Projections.fields(
						ModeloGraduacao.class,
						QModeloGraduacao.modeloGraduacao.id
					).as("modeloGraduacao")
				)
			)
			.join(qGraduacao.modeloGraduacao, QModeloGraduacao.modeloGraduacao)
			.where(qGraduacao.modeloGraduacao.id.eq(modelo.getId())
				.and(qGraduacao.situacao.eq(situacao)))
			.orderBy(qGraduacao.ordem.asc())
			.fetch());
	}

	private void validar(GraduacaoTO graduacao) {
		GraduacaoTO graduacaoExistente = findByModeloGraduacaoIdAndLabelIgnoreCase(graduacao.getModeloGraduacao().getId(), graduacao.getLabel());
		if (graduacaoExistente != null && !graduacaoExistente.getId().equals(graduacao.getId())) {
			//FIXME: Passar a entidade para internacionalização
			throw new ValidationException("validation.duplicate", "graduação", "field.label", graduacao.getLabel());
		}
	}

	@Override
	protected void doBeforeSave(GraduacaoTO object) throws ServiceException {
		validar(object);
	}

	@Override
	protected void doBeforeUpdate(Graduacao entity, GraduacaoTO object) throws ServiceException {
		validar(object);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List<GraduacaoTO> findAllGraduacoesByModeloAtivos(Long modeloGraduacaoId) {
		return map(graduacaoRepository.findByModeloGraduacaoIdAndSituacaoOrderByOrdemAsc(modeloGraduacaoId, Situacao.ATIVA));
	}

	@Override
	protected GraduacaoTO doAfterCreate(GraduacaoTO object) throws br.com.fasolti.commons.lang.ServiceException {
		object.setSituacao(Situacao.ATIVA);
		return object;
	}

	@Override
	protected void doAfterSave(Graduacao entity, GraduacaoTO object) throws ServiceException {
		ModeloGraduacaoTO modeloGraduacao = modeloGraduacaoService.getById(object.getModeloGraduacao().getId());
		modeloGraduacao.setSituacao(Situacao.ATIVA);
		modeloGraduacaoService.update(modeloGraduacao, modeloGraduacao.getId());

		updateGraduacoesOrdem(object);
	}

	@Override
	protected void doAfterUpdate(Graduacao entity, GraduacaoTO object) throws ServiceException {
//		updateGraduacoesOrdem(object);

		List<CompDetalhamentoHintTO> compDetalhamentoHints = compDetalhamentoHintService.listByGraduacao(object);
		compDetalhamentoHints.forEach(compDetalhamentoHintTO -> {
			compDetalhamentoHintTO.setHint(object.getHint());
			compDetalhamentoHintService.update(compDetalhamentoHintTO, compDetalhamentoHintTO.getId());
		});
	}

	@Override
	protected void doBeforeDelete(GraduacaoTO object) throws br.com.fasolti.commons.lang.ServiceException {
		List<CompDetalhamentoHintTO> compDetalhamentoHints = compDetalhamentoHintService.listByGraduacao(object);
		if (compDetalhamentoHints != null && !compDetalhamentoHints.isEmpty())
			throw new ServiceException("Esta graduação não pode ser removida pois está associada a algum hint de datalhamento de competência");
	}


	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public GraduacaoTO findByModeloGraduacaoIdAndLabelIgnoreCase(Long idModeloGraduacao, String label) {
		return map(graduacaoRepository.findByModeloGraduacaoIdAndLabelIgnoreCase(idModeloGraduacao, label.trim().toLowerCase()));
	}

	@Override
	public List<GraduacaoTO> obterGraduacoesProjections(Long modeloGraduacaoId) {
		JPAQuery<Graduacao> hintQuery = select();
		hintQuery.where(qGraduacao.modeloGraduacao.id.eq(modeloGraduacaoId));
		hintQuery.select(Projections.fields(
			Graduacao.class,
			qGraduacao.id,
			qGraduacao.hint,
			qGraduacao.label,
			qGraduacao.ordem,
			qGraduacao.ponto,
			qGraduacao.cor,
			qGraduacao.criador,
			qGraduacao.dataCriacao,
			qGraduacao.atualizador,
			qGraduacao.dataAtualizacao
		));

		return map(hintQuery.fetch());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public GraduacaoTO alterarSituacaoGraduacao(Long id, Situacao situacao) {
		Graduacao graduacao = graduacaoRepository.findOne(id);

		graduacao.setSituacao(situacao);
		graduacao.setModificationDate(new Date(System.currentTimeMillis()));
		graduacao.setModifiedBy(getLoggedUser().getId() + "%" + getLoggedUser().getMatricula());

		GraduacaoTO graduacaoTO = map(graduacao);

		saveOrUpdate(graduacaoTO);

		this.updateGraduacoesOrdem(graduacaoTO);

		return graduacaoTO;

	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	private void updateGraduacoesOrdem(GraduacaoTO graduacaoTO) {
		/*
		 *  Após a ativação/inativação/inclusão da graduação, podem ocorrer saltos ou repetição no campo ordem.
		 *  Se a graduação de ordem 3 for inativada, a ordem das graduações ativas ficará 1,2,4. O ajuste abaixo corrige estes problemas
		 *  de gap de valores na ordenção.
		 * 	Primeiro ajustamos o campo ordem das graduações ativas.
		 */

		List<Graduacao> listaCompleta = new ArrayList<>();
		List<Graduacao> graduacaoList = (graduacaoRepository.findByModeloGraduacaoIdAndSituacaoOrderByOrdemAsc(graduacaoTO.getModeloGraduacao().getId(), Situacao.ATIVA));

		AtomicInteger contadorAtivo = new AtomicInteger(1);
		AtomicInteger ultimaOrdemAtiva = new AtomicInteger(graduacaoList.size());
		graduacaoList.forEach(graduacao -> {
			if (graduacao.getLabel().equals("Não posso avaliar"))
				graduacao.setOrdem(ultimaOrdemAtiva.get());
			else
				graduacao.setOrdem(contadorAtivo.getAndIncrement());
		});

		listaCompleta.addAll(graduacaoList);

		/*
		 *	Em seguida ajustamos o campo ordem das graduações inativas.
		 *  Pelo mesmo motivo descrito acima.
		 *  A graduação com label "Não posso avaliar" deverá sempre ser a última
		 */

		AtomicInteger contadorInativo = new AtomicInteger(graduacaoList.size() + 1);
		graduacaoList = graduacaoRepository.findByModeloGraduacaoIdAndSituacaoOrderByPontoAsc(graduacaoTO.getModeloGraduacao().getId(), Situacao.INATIVA);

		AtomicInteger ultimaOrdemInativa = new AtomicInteger((contadorInativo.get() - 1) + graduacaoList.size());
		graduacaoList.forEach(graduacao -> {
			if (graduacao.getLabel().equals("Não posso avaliar"))
				graduacao.setOrdem(ultimaOrdemInativa.get());
			else
				graduacao.setOrdem(contadorInativo.getAndIncrement());
		});

		listaCompleta.addAll(graduacaoList);

		for (Graduacao graduacao : listaCompleta) {
			graduacaoRepository.save(graduacao);
		}
	}
}
