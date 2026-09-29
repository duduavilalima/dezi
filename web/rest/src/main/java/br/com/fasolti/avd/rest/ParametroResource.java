package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.service.ParametroService;
import br.com.fasolti.avd.transfer.ParametroTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("parametro")
public class ParametroResource {

	protected static final String BUSCA = "/busca";
	protected static final String UPDATE = "/update";
	protected static final String BUSCA_TODOS = "/buscatodos";

	@Autowired
	private ParametroService parametroService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = BUSCA)
	public final ParametroTO buscaPorExemplo(@RequestBody ParametroTO exampleObj) {
		return parametroService.buscaPorChave(exampleObj.getChave());
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = BUSCA_TODOS)
	public final List<ParametroTO> buscaTodos() throws Exception {
		return parametroService.buscaTodos();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = UPDATE)
	public final ParametroTO atualizar(@RequestBody ParametroTO exampleObj) {
		return parametroService.save(exampleObj);
	}
}
