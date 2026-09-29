package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.avd.types.TipoComp;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.avd.types.TipoSexo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tipo") //TODO Esse nome está genérico demais, mudar para tiposfixos
public class TipoResource {

	@RequestMapping(method = RequestMethod.GET, value = "tiposdesexo")
	public final TipoSexo[] tipoSexo() throws Exception {
		return TipoSexo.values();
	}

	@RequestMapping(method = RequestMethod.GET, value = "perfis")
	public final TipoPerfil[] tipoPerfis() throws Exception {
		return TipoPerfil.values();
	}

	@RequestMapping(method = RequestMethod.GET, value = "tiposcompetencias")
	public final TipoComp[] tiposCompetencias() throws Exception {
		return TipoComp.values();
	}

	@RequestMapping(method = RequestMethod.GET, value = "tiposagentes")
	public final TipoAvaliacaoPerfil[] tiposAgentes() throws Exception {
		return TipoAvaliacaoPerfil.values();
	}

}
