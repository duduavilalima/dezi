package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Dimensao;
import br.com.fasolti.avd.domain.QDimensao;
import br.com.fasolti.avd.repository.DimensaoCompetenciaDetalhamentoRepository;
import br.com.fasolti.avd.repository.DimensaoRepository;
import br.com.fasolti.avd.service.DimensaoCompetenciaDetalhamentoService;
import br.com.fasolti.avd.service.DimensaoService;
import br.com.fasolti.avd.service.TipoCompetenciaService;
import br.com.fasolti.avd.transfer.DimensaoCompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.DimensaoTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DimensaoServiceImpl
	extends BaseAvdService<Dimensao, DimensaoTO, DimensaoTO>
	implements DimensaoService {

	@Autowired
	private DimensaoRepository dimensaoRepository;

	@Autowired
	private TipoCompetenciaService tipoCompetenciaService;

	@Autowired
	private DimensaoCompetenciaDetalhamentoService dimensaoCompDetService;

	private DimensaoCompetenciaDetalhamentoRepository dimensaoCompDetRepository;

	@Autowired
	private Mapper mapper;

	private final QDimensao qDimensao = QDimensao.dimensao;

	@Autowired
	public DimensaoServiceImpl(DimensaoRepository repository) {
		super(repository);
	}

	@Override
	protected ServiceFilter<Dimensao> getServiceFilter() {
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		mandatoryFilter.or(qDimensao.empresa.id.eq(getLoggedUser().getEmpresa().getId()));

		return ServiceFilter.create(qDimensao, mandatoryFilter);
	}

	@Override
	protected void doBeforeSave(DimensaoTO object) throws ServiceException {
		Dimensao dimensao = obterDimensaoPorNomeEEmpresa(object.getNome());
		if (dimensao != null)
			throw new ValidationException("validation.duplicate", "dimensão", "dimensao", object.getNome());

		object.setEmpresa(getLoggedUser().getEmpresa());
	}

	@Override
	protected void doAfterSave(Dimensao entity, DimensaoTO object) throws ServiceException {
		for (DimensaoCompetenciaDetalhamentoTO dcdTO : object.getDetalhamentos()) {
			dcdTO.setDimensao(object);
			dimensaoCompDetService.save(dcdTO);
		}
	}

	@Override
	protected void doBeforeUpdate(DimensaoTO object) throws ServiceException {
		object.setEmpresa(getLoggedUser().getEmpresa());
	}

	@Override
	protected void doAfterUpdate(Dimensao entity, DimensaoTO object) throws ServiceException {
		if (object.getDetalhamentos() != null && !object.getDetalhamentos().isEmpty()) {
			List<DimensaoCompetenciaDetalhamentoTO> listaAntigos = dimensaoCompDetService.obterDetalhamentosPorDimensao(object);

			for (DimensaoCompetenciaDetalhamentoTO dcdAntigoTO : listaAntigos) {
				dcdAntigoTO.setDimensao(object);
				dimensaoCompDetService.delete(dcdAntigoTO);
			}

			for (DimensaoCompetenciaDetalhamentoTO dcdTO : object.getDetalhamentos()) {
				dcdTO.setDimensao(object);
				dimensaoCompDetService.save(dcdTO);
			}
		}

	}

	private Dimensao obterDimensaoPorNomeEEmpresa(String nome) {
		Dimensao resultado = dimensaoRepository.findByNomeAndEmpresaId(nome, getLoggedUser().getEmpresa().getId());
		return resultado;
	}

	@Override
	public List<DimensaoTO> obterDimensaoPorSituacao(Situacao situacao) {
		return map(dimensaoRepository.findBySituacaoAndEmpresaId(Situacao.ATIVA, getLoggedUser().getEmpresa().getId()));
	}
}
