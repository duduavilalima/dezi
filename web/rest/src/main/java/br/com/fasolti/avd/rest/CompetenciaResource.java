package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.CompetenciaService;
import br.com.fasolti.avd.transfer.CompetenciaPorTipoCompTreeTO;
import br.com.fasolti.avd.transfer.CompetenciaTO;
import br.com.fasolti.avd.transfer.simple.AvaliacaoSimpleTO;
import br.com.fasolti.avd.types.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("competencia")
public class CompetenciaResource extends BaseAvdResource<CompetenciaTO, CompetenciaTO> {

	@Autowired
	private CompetenciaService competenciaService;

	@Autowired
	public CompetenciaResource(CompetenciaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/avaliacoesporcompetencia")
	public List<AvaliacaoSimpleTO> buscarAvaliacoesPorCompetencia(@RequestBody CompetenciaTO competencia) {
		return competenciaService.listAvaliacaoByCompetencia(competencia);
	}

	@Override
	protected Enum valueOfStatus(String status) {
		return Situacao.valueOf(status);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listar")
	public List<CompetenciaTO> buscarCompetencia() {
		return competenciaService.buscarCompetenciasAtivas();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/toView/{id:[0-9]+}")
	public CompetenciaTO buscarCompetencia(@PathVariable("id") Long id) {
		return competenciaService.obterCompetenciaProjections(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/tipo/{id:[0-9]+}")
	public List<CompetenciaTO> buscarCompetenciaPorTipoCompetencia(@PathVariable("id") Long id) {
		return competenciaService.buscarCompetenciasAtivasPorTipoCompetencia(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/arvore/tipocompetencia")
	public List<CompetenciaPorTipoCompTreeTO> obterArvoreDeCompetenciaPorTipoCompetencia() {
		return competenciaService.obterArvoreDeCompetenciaPorTipoCompetencia();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/arvore/{idCompetencia:[0-9]+}")
	public List<CompetenciaPorTipoCompTreeTO> obterArvoreTipoCompetenciaPorCompetencia(@PathVariable("idCompetencia") Long idCompetencia) {
		return competenciaService.obterArvoreDeTiposDeCompetenciaComCompetencias();
	}


}
