package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.FotoService;
import br.com.fasolti.avd.transfer.FotoTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("foto")
public class FotoResource extends BaseAvdResource<FotoTO, FotoTO> {

	@Autowired
	private FotoService fotoService;

	@Autowired
	public FotoResource(FotoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/avatar")
	public FotoTO obterAvatarFuncionario(@PathVariable Long id) throws Exception {
		return fotoService.obterAvatarFuncionario(id);
	}
}
