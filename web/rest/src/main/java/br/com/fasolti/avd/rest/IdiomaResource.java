package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.IdiomaService;
import br.com.fasolti.avd.transfer.IdiomaTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("idioma")
public class IdiomaResource
	extends BaseAvdResource<IdiomaTO, IdiomaTO> {

	@Autowired
	private IdiomaService idiomaService;

	@Autowired
	public IdiomaResource(IdiomaService service) {
		super(service);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "idiomasbycontratanteandativas")
	public final List<IdiomaTO> idiomasPorContratanteAndAtivas() throws Exception {
		return idiomaService.findIdiomasByContratanteAndSituacao();
	}

}
