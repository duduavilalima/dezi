package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.TipoCompetenciaService;
import br.com.fasolti.avd.transfer.ArvoreTipoCompetenciaTO;
import br.com.fasolti.avd.transfer.TipoCompetenciaTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("tipodecompetencias")
public class TipoCompetenciaResource extends BaseAvdResource<TipoCompetenciaTO, TipoCompetenciaTO> {

	@Autowired
	private TipoCompetenciaService tipoCompetenciaService;

	@Autowired
	public TipoCompetenciaResource(TipoCompetenciaService tipoCompetenciaService) {
		super(tipoCompetenciaService);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/obterfilhos")
	public List<TipoCompetenciaTO> obterFilhosDoTipoDeComepetencia(@RequestBody TipoCompetenciaTO tipoCompetenciaTO) throws Exception {
		return tipoCompetenciaService.obterFilhosDoTipoDeComepetencia(tipoCompetenciaTO);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/obterarvore")
	public List<ArvoreTipoCompetenciaTO> obterArvore() throws Exception {
		return tipoCompetenciaService.obterArvoreDeTiposDeCompetencia();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/obterarvore")
	public List<ArvoreTipoCompetenciaTO> obterFilhosDoTipoDeCompetenciaRaiz(@PathVariable("id") Long id) throws Exception {
		return tipoCompetenciaService.obterArvoreDeTiposDeCompetencia(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/tiposativos")
	public List<TipoCompetenciaTO> obterTiposDeCompetenciaAtivos() throws Exception {
		List<TipoCompetenciaTO> lista = tipoCompetenciaService.listBySituacao(Situacao.ATIVA);
		return lista;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/obtertipocompetenciapai")
	public List<TipoCompetenciaTO> obterTipoCompetenciaPai() throws Exception {
		return tipoCompetenciaService.obterTiposDeCompetenciaQueNaoTemPai();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/obterancestrais")
	public List<TipoCompetenciaTO> obterAncestrais(@RequestBody TipoCompetenciaTO tipoCompetenciaTO) throws Exception {
		return tipoCompetenciaService.obterTipoCompentenciaAscendentes(tipoCompetenciaTO);
	}
}
