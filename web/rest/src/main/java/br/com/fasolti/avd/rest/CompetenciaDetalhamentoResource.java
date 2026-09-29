package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.CompetenciaDetalhamentoService;
import br.com.fasolti.avd.transfer.CompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.CompetenciaTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("competencia/detalhamento") //TODO Melhorar esse path, talvez integrar no CompetenciaResource (não parece fazer sentido pesquisar isso fora da competencia)
public class CompetenciaDetalhamentoResource
	extends BaseAvdResource<CompetenciaDetalhamentoTO, CompetenciaDetalhamentoTO> {

	@Autowired
	private CompetenciaDetalhamentoService competenciaDetalhamentoService;

	@Autowired
	public CompetenciaDetalhamentoResource(CompetenciaDetalhamentoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/detalhamentosporcompetencia/{id:[0-9]+}")
	public final List<CompetenciaDetalhamentoTO> detalhamentosPorCompetencia(@PathVariable("id") Long id) {
		return competenciaDetalhamentoService.listByCompetencia(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/toView/{id:[0-9]+}")
	public CompetenciaDetalhamentoTO buscarCompetenciaDetalhamento(@PathVariable("id") Long id) {
		return competenciaDetalhamentoService.obterCompetenciaDetalhamentoProjections(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/participante")
	public final List<CompetenciaDetalhamentoTO> detalhamentosPorParticipanete(@RequestBody ParticipanteTO participante) {
		return competenciaDetalhamentoService.listByParticipante(participante);
	}

}
