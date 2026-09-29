package br.com.fasolti.avd.rest.mobile;

import br.com.fasolti.avd.service.mobile.ApontamentoMobileService;
import br.com.fasolti.avd.transfer.mobile.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("mobile/apontamento")
public class ApontamentoMobileResource {

	@Autowired
	private ApontamentoMobileService apontamentoMobileService;

	@ResponseBody
	@RequestMapping(path = "all/avaliado", method = RequestMethod.GET)
	public List<FuncionarioMobileTO> listFuncionariosAvaliados() throws Exception {
		return apontamentoMobileService.listFuncionariosAvaliadosByLoggedUser();
	}

	@ResponseBody
	@RequestMapping(path = "all/tipoCompetencia", method = RequestMethod.GET)
	public List<TipoCompetenciaMobileTO> listTiposCompetenciaByAvaliadoId(
		@RequestParam("avaliadoId") Long avaliadoId
	) throws Exception {
		return apontamentoMobileService.listTiposCompetenciaByLoggedUserAndFuncionarioId(
			Objects.requireNonNull(avaliadoId, "Faltando ID do funcionário avaliado.")
		);
	}

	@ResponseBody
	@RequestMapping(path = "all", method = RequestMethod.GET)
	public List<ApontamentoMobileTO> list(
		@RequestParam("avaliadoId") Long avaliadoId,
		@RequestParam("tipoCompetenciaId") Long tipoCompetenciaId
	) throws Exception {
		return apontamentoMobileService.listByLoggedUserAndFuncionarioIdAndTipoCompetenciaId(
			Objects.requireNonNull(avaliadoId, "Faltando ID do funcionário avaliado."),
			Objects.requireNonNull(tipoCompetenciaId, "Faltando ID do tipo da competência.")
		);
	}

	@ResponseBody
	@RequestMapping(path = "competencia/all", method = RequestMethod.GET)
	public List<CompetenciaMobileTO> listCompetenciaByTipoCompetenciaId(
		@RequestParam("tipoCompetenciaId") Long tipoCompetenciaId
	) throws Exception {
		return apontamentoMobileService.listCompetenciaByTipoCompetenciaId(tipoCompetenciaId);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public ApontamentoMobileTO create(@RequestBody ApontamentoMobileTO apontamento) throws Exception {
		return apontamentoMobileService.create(apontamento);
	}
}
