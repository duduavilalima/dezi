package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.EscolaridadeService;
import br.com.fasolti.avd.transfer.EscolaridadeTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("escolaridade")
public class EscolaridadeResource extends BaseAvdResource<EscolaridadeTO, EscolaridadeTO> {

	@Autowired
	private EscolaridadeService escolaridadeService;

	@Autowired
	public EscolaridadeResource(EscolaridadeService service) {
		super(service);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "porcontratanteusuariologado")
	public final List<EscolaridadeTO> porContratanteUsuarioLogado() throws Exception {
		return escolaridadeService.listAllActiveForLoggedUser();
	}
}
