package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.CompDetalhamentoHintService;
import br.com.fasolti.avd.transfer.CompDetalhamentoHintTO;
import br.com.fasolti.avd.transfer.CompetenciaDetalhamentoTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Controller
@RequestMapping("competencia/detalhamentocalibrahint")
public class CompetenciaDetalhamentoCalibrahintResource
	extends BaseAvdResource<CompDetalhamentoHintTO, CompDetalhamentoHintTO> {

	@Autowired
	private CompDetalhamentoHintService compDetalhamentoHintService;

	@Autowired
	public CompetenciaDetalhamentoCalibrahintResource(CompDetalhamentoHintService service) {
		super(service);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/hintspordetalhamentos")
	public final List<CompDetalhamentoHintTO> hintsPorDetalhamentos(@RequestBody CompetenciaDetalhamentoTO competenciaDetalhamento) {
		return compDetalhamentoHintService.listByCompetenciaDetalhamento(competenciaDetalhamento);
	}

}
