package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.BiParametroUsuarioService;
import br.com.fasolti.avd.service.CampanhaService;
import br.com.fasolti.avd.transfer.BiParametroUsuarioTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("parametrousuario")
public class BiParametroUsuarioResource extends BaseAvdResource<BiParametroUsuarioTO, BiParametroUsuarioTO> {

	@Autowired
	BiParametroUsuarioService biParametroUsuarioService;

	@Autowired
	public BiParametroUsuarioResource(BiParametroUsuarioService biParametroUsuarioService) {
		super(biParametroUsuarioService);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/criarparametros/{campanhaId:[0-9]+}/{idFuncionario:[0-9]+}")
	public BiParametroUsuarioTO criarParametros(@PathVariable("campanhaId") Long campanhaId, @PathVariable("idFuncionario") Long idFuncionario) throws Exception {
		return biParametroUsuarioService.criarParametros(campanhaId, idFuncionario);
	}
}
