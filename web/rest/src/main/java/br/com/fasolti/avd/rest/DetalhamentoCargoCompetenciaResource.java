package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.DetalhamentoCargoCompetenciaService;
import br.com.fasolti.avd.transfer.DetalhamentoCargoCompetenciaTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("cargoCompetencia/detalhamento")
//TODO Melhorar esse path, talvez integrar no outro resource (não parece fazer sentido pesquisar separadamente)
public class DetalhamentoCargoCompetenciaResource
	extends BaseAvdResource<DetalhamentoCargoCompetenciaTO, DetalhamentoCargoCompetenciaTO> {

	@Autowired
	private DetalhamentoCargoCompetenciaService detalhamentoCargoCompetenciaService;

	@Autowired
	public DetalhamentoCargoCompetenciaResource(DetalhamentoCargoCompetenciaService detalhamentoCargoCompetenciaService) {
		super(detalhamentoCargoCompetenciaService);
	}

	/**
	 * Método usado ao editar um cargoCompetencia já pre existente
	 * Sem alterar a combo de competências no frontend
	 * @param competenciaId
	 * @param cargoCompetenciaId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/detalhamentosPorCargoCompetencia/{competenciaId:[0-9]+}/{cargoCompetenciaId:[0-9]+}")
	public final List<DetalhamentoCargoCompetenciaTO> detalhamentosPorCargoCompetencia(@PathVariable("competenciaId") Long competenciaId, @PathVariable("cargoCompetenciaId") Long cargoCompetenciaId) throws Exception {
		return detalhamentoCargoCompetenciaService.findOrCreateDetalhamentosCC(competenciaId, cargoCompetenciaId);
	}

	/**
	 * Método usado ao alterar a competencia na combo de competências
	 * no frontend. Ou seja, o cargoCompetencia permanece com o mesmo id,
	 * alterando apenas o id da competência
	 * @param competenciaId
	 * @param cargoCompetenciaId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.GET, value = {"/detalhamentosPorCompetencia/{competenciaId:[0-9]+}/{cargoCompetenciaId:[0-9]+}", "/detalhamentosPorCompetencia/{competenciaId:[0-9]+}"})
	public final List<DetalhamentoCargoCompetenciaTO> detalhamentosPorCompetencia(@PathVariable("competenciaId") Long competenciaId, @PathVariable("cargoCompetenciaId") Optional<Long> cargoCompetenciaId) throws Exception {
		return detalhamentoCargoCompetenciaService.findOrCreateDetalhamentosCCByCompetencia(competenciaId, cargoCompetenciaId);
	}

}
