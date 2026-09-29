package br.com.fasolti;

import br.com.fasolti.avd.service.DadoPreenchimentoService;
import br.com.fasolti.avd.transfer.DadoPreenchimentoTO;
import br.com.fasolti.avd.transfer.DadoPreenchimentoTotaisTO;
import br.com.fasolti.commons.rest.BaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dados")
public class DadoPreenchimentoResource extends BaseResource<DadoPreenchimentoTO> {
	private final DadoPreenchimentoService dadoPreenchimentoService;

	@Autowired
	public DadoPreenchimentoResource(DadoPreenchimentoService service) {
		super(service);

		this.dadoPreenchimentoService = service;
	}

	@RequestMapping(method = RequestMethod.GET, path = "/campanha/{campanhaId}/avaliacao/{avaliacaoId}/total")
	public DadoPreenchimentoTotaisTO totalizar(
		@PathVariable final Long campanhaId, @PathVariable final Long avaliacaoId,
		@RequestParam(required = false) Long departamentoId, @RequestParam(required = false) Long responsavelId
	) {
		return dadoPreenchimentoService.totalizar(campanhaId, avaliacaoId, departamentoId, responsavelId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/campanha/{campanhaId}/avaliacao/{avaliacaoId}/responsaveis")
	public List<DadoPreenchimentoTO> listarResposanveis(
		@PathVariable final Long campanhaId, @PathVariable final Long avaliacaoId,
		@RequestParam(required = false) Long departamentoId
	) {
		return dadoPreenchimentoService.listarResponsaveis(campanhaId, avaliacaoId, departamentoId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/campanha/{campanhaId}/avaliacao/{avaliacaoId}/pendencias")
	public List<DadoPreenchimentoTO> listarPendencias(
		@PathVariable final Long campanhaId, @PathVariable final Long avaliacaoId,
		@RequestParam(required = false) Long departamentoId
	) {
		return dadoPreenchimentoService.listarPendencias(campanhaId, avaliacaoId, departamentoId);
	}

}
