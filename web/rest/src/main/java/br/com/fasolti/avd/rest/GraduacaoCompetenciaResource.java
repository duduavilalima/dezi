package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.AvaliacaoCompetenciaService;
import br.com.fasolti.avd.service.AvdService;
import br.com.fasolti.avd.service.GraduacaoCompetenciaService;
import br.com.fasolti.avd.service.ModeloGraduacaoCompService;
import br.com.fasolti.avd.transfer.AvaliacaoCompetenciaTO;
import br.com.fasolti.avd.transfer.GraduacaoCompetenciaTO;
import br.com.fasolti.avd.transfer.ModeloGraduacaoCompTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("graduacaocompetencia")  //TODO talvez integrar no outro resource (não parece fazer sentido pesquisar separadamente)
public class GraduacaoCompetenciaResource
	extends BaseAvdResource<GraduacaoCompetenciaTO, GraduacaoCompetenciaTO> {

	protected static final String AVALIACAO_ID = "/{avaliacaoId:[0-9]+}";
	protected static final String COMPETENCIA_ID = "/{competenciaId:[0-9]+}";
	protected static final String AVALIACAO_COMPETENCIA_ID = "/{avaliacaoCompetenciaId:[0-9]+}";

	@Autowired
	private GraduacaoCompetenciaService graduacaoCompetenciaService;

	@Autowired
	private AvaliacaoCompetenciaService avaliacaoCompetenciaService;

	@Autowired
	private ModeloGraduacaoCompService modeloGraduacaoCompService;


	@Autowired
	public GraduacaoCompetenciaResource(AvdService<GraduacaoCompetenciaTO, GraduacaoCompetenciaTO> service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/avaliacao/"+AVALIACAO_ID+"/competencia/"+COMPETENCIA_ID +"/graduacoes")
	public List<GraduacaoCompetenciaTO> listByAvaliacaoAndCompetencia(@PathVariable("avaliacaoId") Long avaliacaoId,
																						  @PathVariable("competenciaId") Long competenciaId) {

		AvaliacaoCompetenciaTO avaliacaoCompetenciaTO = avaliacaoCompetenciaService.getByAvaliacaoIdAndIdCompetencia(avaliacaoId, competenciaId);
		ModeloGraduacaoCompTO modeloGraduacaoCompTO = modeloGraduacaoCompService.getByAvaliacaoCompetenciaId(avaliacaoCompetenciaTO.getId());
		return graduacaoCompetenciaService.listByModeloGraduacaoId(modeloGraduacaoCompTO.getId());
	}

	@RequestMapping(method = RequestMethod.GET, path = "/avaliacaocompetencia/"+AVALIACAO_COMPETENCIA_ID +"/graduacoes")
	public List<GraduacaoCompetenciaTO> listByAvaliacaoCompetencia(@PathVariable("avaliacaoCompetenciaId") Long avaliacaoCompetenciaId) {

		AvaliacaoCompetenciaTO avaliacaoCompetenciaTO = avaliacaoCompetenciaService.getById(avaliacaoCompetenciaId);
		ModeloGraduacaoCompTO modeloGraduacaoCompTO = modeloGraduacaoCompService.getByAvaliacaoCompetenciaId(avaliacaoCompetenciaTO.getId());
		return graduacaoCompetenciaService.listByModeloGraduacaoId(modeloGraduacaoCompTO.getId());
	}
}
