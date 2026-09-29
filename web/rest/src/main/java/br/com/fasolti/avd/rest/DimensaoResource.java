package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.DimensaoCompetenciaDetalhamentoService;
import br.com.fasolti.avd.service.DimensaoService;
import br.com.fasolti.avd.transfer.DimensaoCompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.DimensaoTO;
import br.com.fasolti.avd.types.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("dimensao")
public class DimensaoResource extends BaseAvdResource<DimensaoTO, DimensaoTO> {

	@Autowired
	private DimensaoCompetenciaDetalhamentoService dimensaoCompDetService;

	@Autowired
	private DimensaoService dimensaoService;

	@Autowired
	public DimensaoResource(DimensaoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/detalhamentos")
	public List<DimensaoCompetenciaDetalhamentoTO> obterDetalhamentos(@RequestBody DimensaoTO dimensaoTO) throws Exception {
		return dimensaoCompDetService.obterDetalhamentosPorDimensao(dimensaoTO);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/obterdimensaoativa")
	public List<DimensaoTO> obterDimensaoAtiva() throws Exception {
		return dimensaoService.obterDimensaoPorSituacao(Situacao.ATIVA);
	}

}
