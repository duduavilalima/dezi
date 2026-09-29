package br.com.fasolti.avd.rest.mobile;

import br.com.fasolti.avd.service.mobile.FeedForwardRecebidoMobileService;
import br.com.fasolti.avd.transfer.mobile.FeedForwardRecebidoMobileTO;
import br.com.fasolti.avd.transfer.mobile.TipoCompetenciaMobileTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mobile/ffrecebido")
public class FeedForwardRecebidoMobileResource {

	@Autowired
	private FeedForwardRecebidoMobileService feedForwardsRecebidosService;

	@ResponseBody
	@RequestMapping(path = "all/tipocompetencia", method = RequestMethod.GET)
	public List<TipoCompetenciaMobileTO> listTipoCompetencia() throws Exception {
		 return feedForwardsRecebidosService.listTipoCompetenciaByLoggedFuncionario();
	}

	@ResponseBody
	@RequestMapping(path = "all", method = RequestMethod.GET)
	public List<FeedForwardRecebidoMobileTO> list(
		@RequestParam("tipoCompetenciaId") Long tipoCompetenciaId
	) throws Exception {
		return feedForwardsRecebidosService.listByTipoCompetenciaIdAndLoggedFuncionario(tipoCompetenciaId);
	}

}
