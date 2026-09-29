package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.AvaliacaoCompetenciaService;
import br.com.fasolti.avd.transfer.AvaliacaoCompetenciaTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("avaliacaocompetencia")
public class AvaliacaoCompetenciaResource extends BaseAvdResource<AvaliacaoCompetenciaTO, AvaliacaoCompetenciaTO> {

	@Autowired
	private AvaliacaoCompetenciaService avaliacaoCompetenciaService;

	protected static final String AVALIACAO_ID = "/{avaliacaoId:[0-9]+}";
	protected static final String COMPETENCIA_ID = "/{competenciaId:[0-9]+}";

	@Autowired
	public AvaliacaoCompetenciaResource(AvaliacaoCompetenciaService avaliacaoCompetenciaService) {
		super(avaliacaoCompetenciaService);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/avaliacao/"+AVALIACAO_ID+"/competencia/"+COMPETENCIA_ID)
	public AvaliacaoCompetenciaTO getByAvaliacaoAndCompetencia(@PathVariable("avaliacaoId") Long avaliacaoId,
															   @PathVariable("competenciaId") Long competenciaId) {

		return avaliacaoCompetenciaService.getByAvaliacaoIdAndIdCompetencia(avaliacaoId, competenciaId);
	}

}
