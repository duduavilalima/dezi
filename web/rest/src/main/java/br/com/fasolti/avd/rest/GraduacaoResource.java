package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.GraduacaoService;
import br.com.fasolti.avd.transfer.GraduacaoTO;
import br.com.fasolti.avd.types.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("graduacao")
public class GraduacaoResource extends BaseAvdResource<GraduacaoTO, GraduacaoTO> {

	@Autowired
	private GraduacaoService graduacaoService;

	@Autowired
	public GraduacaoResource(GraduacaoService service) {
		super(service);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/graduacoespormodelo")
	public final List<GraduacaoTO> graduacoesPorModelo(@RequestBody GraduacaoTO obj) throws Exception {
		return graduacaoService.findAllGraduacoesByModelo(obj.getModeloGraduacao());
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/graduacoesativaspormodelo")
	public final List<GraduacaoTO> graduacoesAtivasPorModelo(@RequestBody GraduacaoTO obj) throws Exception {
		return graduacaoService.findAllGraduacoesByModelo(obj.getModeloGraduacao());
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/inativar")
	public GraduacaoTO inativar(@PathVariable("id") Long id) throws Exception {
		return graduacaoService.alterarSituacaoGraduacao(id, Situacao.INATIVA);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/ativar")
	public GraduacaoTO ativar(@PathVariable("id") Long id) throws Exception {
		return graduacaoService.alterarSituacaoGraduacao(id, Situacao.ATIVA);
	}

}
