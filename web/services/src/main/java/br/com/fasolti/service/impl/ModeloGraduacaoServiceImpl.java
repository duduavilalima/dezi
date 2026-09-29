package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.DimensaoRepository;
import br.com.fasolti.avd.repository.ModeloGraduacaoRepository;
import br.com.fasolti.avd.service.GraduacaoService;
import br.com.fasolti.avd.service.ModeloGraduacaoService;
import br.com.fasolti.avd.transfer.GraduacaoTO;
import br.com.fasolti.avd.transfer.ModeloGraduacaoTO;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ModeloGraduacaoServiceImpl
	extends BaseAvdService<ModeloGraduacao, ModeloGraduacaoTO, ModeloGraduacaoTO>
	implements ModeloGraduacaoService {

	@Autowired
	private ModeloGraduacaoRepository modeloGraduacaoRepository;

	private final QModeloGraduacao qModeloGraduacao = QModeloGraduacao.modeloGraduacao;
	private final QDimensao qDimensao = QDimensao.dimensao;
	private final QEmpresa qEmpresa = QEmpresa.empresa;

	@Autowired
	private GraduacaoService graduacaoService;

	@Autowired
	private DimensaoRepository dimensaoRepository;

	@Autowired
	public ModeloGraduacaoServiceImpl(ModeloGraduacaoRepository modeloGraduacaoRepository) {
		super(modeloGraduacaoRepository);
	}
	@Override
	protected void buildSearchQuery(JPAQuery<ModeloGraduacao> query) {
		query.select(Projections.fields(
			ModeloGraduacao.class,
			qModeloGraduacao.id,
			qModeloGraduacao.descricaoModelo,
			qModeloGraduacao.qtdGraduacoes,
			qModeloGraduacao.situacao
		));
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, ModeloGraduacaoTO modeloGraduacaoTO) {
		if (modeloGraduacaoTO.getSituacao() != null) {
			builder.and(qModeloGraduacao.situacao.eq(modeloGraduacaoTO.getSituacao()));
		}
	}

	@Override
	protected ServiceFilter<ModeloGraduacao> getServiceFilter() {
		BooleanBuilder mandatoryFilter = new BooleanBuilder();
		mandatoryFilter.or(qModeloGraduacao.empresa.id.eq(getLoggedUser().getEmpresa().getId()));

		return ServiceFilter.create(qModeloGraduacao, mandatoryFilter);
	}

	@Override
	protected void doBeforeSave(ModeloGraduacaoTO object) throws ServiceException {
		validar(object);
		if(object.getNaoGraduar() != null && object.getNaoGraduar()){
			object.setQtdGraduacoes(1);
			object.setSituacao(Situacao.ATIVA);
		}
	}

	@Override
	protected void doAfterSave(ModeloGraduacao entity, ModeloGraduacaoTO object) throws ServiceException {
		if(object.getNaoGraduar() != null && object.getNaoGraduar()){
			GraduacaoTO graduacaoTO = graduacaoService.create();
			graduacaoTO.setLabel("Não posso avaliar");
			graduacaoTO.setHint("Não posso avaliar");
			//Ao criar uma graduação, como ela está inativa, a ordem é nula.
			graduacaoTO.setOrdem(null);
			graduacaoTO.setPonto(0);
			graduacaoTO.setSituacao(Situacao.INATIVA);
			graduacaoTO.setModeloGraduacao(object);
			graduacaoService.save(graduacaoTO);
		}
	}

	private void validar(ModeloGraduacaoTO modeloGraduacao) {
		ModeloGraduacaoTO modeloGraduacaoExistente = findByEmpresaAndDescricaoModelo(null, modeloGraduacao.getDescricaoModelo());
		if (modeloGraduacaoExistente != null && !modeloGraduacaoExistente.getId().equals(modeloGraduacao.getId())) {
			//FIXME: Passar a entidade para internacionalização
			throw new ValidationException("validation.duplicate", "escala de graduação", "field.descricaoModelo", modeloGraduacao.getDescricaoModelo());
		}
	}

	@Override
	protected void setDefaultValues(ModeloGraduacaoTO object) throws br.com.fasolti.commons.lang.ServiceException {
		object.setQtdGraduacoes(0);
		object.setEmpresa(getLoggedUser().getEmpresa());
		object.setDimensao(mapDefault(dimensaoRepository.findAll().iterator().next()));
		object.setSituacao(Situacao.INATIVA);
		object.setNaoGraduar(Boolean.TRUE);
	}

	@Override
	protected void doBeforeUpdate(ModeloGraduacaoTO object) throws ServiceException {
		if(object.getSituacao().equals(Situacao.ATIVA)) {
			object.setSituacao(Situacao.ATIVA);
		}
		validar(object);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ModeloGraduacaoTO findByEmpresaAndDescricaoModelo(Long idEmpresa, String descricaoModelo) {
		return map(modeloGraduacaoRepository.findByEmpresaIdAndDescricaoModeloIgnoreCase(getLoggedUser().getEmpresa().getId(), descricaoModelo.trim().toLowerCase()));
	}

	@Override
	public ModeloGraduacaoTO fullObject(ModeloGraduacaoTO modeloGraduacao) {
		modeloGraduacao.setGraduacoes(graduacaoService.findAllGraduacoesByModelo(modeloGraduacao));
		return modeloGraduacao;
	}

	@Override
	public ModeloGraduacaoTO obterModeloGraduacaoProjections(Long idModeloGraduacao) {
		return map(
			select(
				Projections.fields(
					ModeloGraduacao.class,
					qModeloGraduacao.id,
					qModeloGraduacao.descricaoModelo,
					qModeloGraduacao.naoGraduar,
					qModeloGraduacao.criador,
					qModeloGraduacao.dataCriacao,
					qModeloGraduacao.atualizador,
					qModeloGraduacao.dataAtualizacao,
					Projections.fields(Empresa.class, qEmpresa.id).as("empresa"),
					Projections.fields(Dimensao.class, qDimensao.id).as("dimensao")
				)
			)
			.join(qModeloGraduacao.empresa, qEmpresa)
			.join(qModeloGraduacao.dimensao, qDimensao)
			.where(qModeloGraduacao.id.eq(idModeloGraduacao))
			.fetchOne());
	}
}
