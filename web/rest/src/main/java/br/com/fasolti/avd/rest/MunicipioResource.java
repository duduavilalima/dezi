package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.service.MunicipioService;
import br.com.fasolti.avd.transfer.MunicipioTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("municipio")
public class MunicipioResource {

	@Autowired
	private MunicipioService municipioService;

	public MunicipioResource() {
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, path = "/{uf}")
	public List<MunicipioTO> obterPorUf(@PathVariable("uf") String uf) {
		return  municipioService.byUf(uf);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, path = "/{uf}/{nome}")
	public List<MunicipioTO> containsNome(@PathVariable("uf") String uf, @PathVariable("nome") String nome) {
		return municipioService.containsNomeByUf(uf, nome);
	}

	/*@RequestMapping(method = RequestMethod.POST, path = "/nome")
	public List<MunicipioTO> containsNome(@RequestBody MunicipioTO municipioTO) {
		return municipioService.containsNome(municipioTO.getNome());
	}*/

}
