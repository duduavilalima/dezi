package br.com.fasolti.avd.rest.mobile;

import br.com.fasolti.avd.service.mobile.FeedForwardMobileService;
import br.com.fasolti.avd.transfer.mobile.CompetenciaMobileTO;
import br.com.fasolti.avd.transfer.mobile.FeedForwardMobileTO;
import br.com.fasolti.avd.transfer.mobile.FuncionarioMobileTO;
import br.com.fasolti.avd.transfer.mobile.TipoCompetenciaMobileTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("mobile/ff")
public class FeedForwardMobileResource {

	@Autowired
	private FeedForwardMobileService feedForwardService;

	@ResponseBody
	@RequestMapping(path = "all/avaliado", method = RequestMethod.GET)
	public List<FuncionarioMobileTO> listFuncionariosAvaliados() throws Exception {
		return feedForwardService.listFuncionariosAvaliadosByLoggedUser();
	}

	@ResponseBody
	@RequestMapping(path = "all/tipoCompetencia", method = RequestMethod.GET)
	public List<TipoCompetenciaMobileTO> listTiposCompetenciaByAvaliadoId(
		@RequestParam("avaliadoId") Long avaliadoId
	) throws Exception {
		return feedForwardService.listTiposCompetenciaByLoggedUserAndFuncionarioId(
			Objects.requireNonNull(avaliadoId, "Faltando ID do funcionário avaliado.")
		);
	}

	@ResponseBody
	@RequestMapping(path = "all", method = RequestMethod.GET)
	public List<FeedForwardMobileTO> list(
		@RequestParam("avaliadoId") Long avaliadoId,
		@RequestParam("tipoCompetenciaId") Long tipoCompetenciaId
	) throws Exception {
		return feedForwardService.listByLoggedUserAndFuncionarioIdAndTipoCompetenciaId(
			Objects.requireNonNull(avaliadoId, "Faltando ID do funcionário avaliado."),
			Objects.requireNonNull(tipoCompetenciaId, "Faltando ID do tipo da competência.")
		);
	}

	@ResponseBody
	@RequestMapping(path = "competencia/all", method = RequestMethod.GET)
	public List<CompetenciaMobileTO> listCompetenciaByTipoCompetenciaId(
		@RequestParam("tipoCompetenciaId") Long tipoCompetenciaId
	) throws Exception {
		return feedForwardService.listCompetenciaByTipoCompetenciaId(tipoCompetenciaId);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public FeedForwardMobileTO create(@RequestBody FeedForwardMobileTO feedForward) throws Exception {
		return feedForwardService.create(feedForward);
	}
}
