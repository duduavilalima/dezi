package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.UnidadeService;
import br.com.fasolti.avd.transfer.UnidadeTO;
import br.com.fasolti.avd.transfer.UnidadeTreeTO;
import br.com.fasolti.avd.types.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("unidade")
public class UnidadeResource extends BaseAvdResource<UnidadeTO, UnidadeTO> {

	@Autowired
	private UnidadeService unidadeService;

	@Autowired
	public UnidadeResource(UnidadeService service) {
		super(service);
	}

	@Override
	protected Enum valueOfStatus(String status) {
		return Situacao.valueOf(status);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/filhos")
	public List<UnidadeTO> obterFilhosDoTipoDeCompetenciaRaiz(@PathVariable("id") Long id) throws Exception {
		return unidadeService.obterSubunidadesDeUnidade(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/arvore")
	public List<UnidadeTreeTO> obterFilhosDoTipoDeCompetenciaRaiz() throws Exception {
		return unidadeService.listarArvoreDeHierarquiaDeUnidade();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/listar")
	public List<UnidadeTO> obterunidadesAtivas() throws Exception {
		return unidadeService.listarUnidadesAtivas();
	}
}
