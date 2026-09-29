package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.AvaliacaoService;
import br.com.fasolti.avd.service.CampanhaService;
import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.CampanhaTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("campanha")
public class CampanhaResource extends BaseAvdResource<CampanhaTO, CampanhaTO> {

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	private CampanhaService campanhaService;

	@Autowired
	public CampanhaResource(CampanhaService campanhaService) {
		super(campanhaService);
	}

	@Override
	protected Enum valueOfStatus(String status) {
		return Situacao.valueOf(status);
	}

	@RequestMapping(method = RequestMethod.GET, value = ID + "/avaliacoes")
	public List<AvaliacaoTO> avaliacoes(@PathVariable("id") Long id) throws Exception {
		return avaliacaoService.listByCampanha(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = ID + "/avaliacoesliberadas")
	public List<AvaliacaoTO> avaliacoesLiberadas(@PathVariable("id") Long id) throws Exception {
		return avaliacaoService.listarAvaliacoesLiberadasDaCampanha(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = ID + "/campanhasporempresa")
	public List<CampanhaTO> listaCampanhas(@PathVariable("id") Long id) throws Exception {
		return campanhaService.findByEmpresa();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/campanhasativas")
	public List<CampanhaTO> listaCampanhasAtivas() throws Exception {
		return campanhaService.findByEmpresaActiveProjections();
	}

	@RequestMapping(method = RequestMethod.GET, value = ID + "/campanhasporempresaperfil")
	public List<CampanhaTO> listaCampanhasPorPerfil(@PathVariable("id") Long id) throws Exception {
		return campanhaService.findByEmpresaPerfil();
	}

}
