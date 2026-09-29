package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.AvaliacaoCompDetService;
import br.com.fasolti.avd.service.AvaliacaoCompetenciaService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.AvaliacaoCompDetTO;
import br.com.fasolti.avd.transfer.AvaliacaoCompetenciaTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("avaliacaocompdet")
public class AvaliacaoCompDetResource extends BaseAvdResource<AvaliacaoCompDetTO, AvaliacaoCompDetTO> {

	@Autowired
	private AvaliacaoCompDetService avaliacaoCompDetService;

	@Autowired
	private AvaliacaoCompetenciaService avaliacaoCompetenciaService;

	@Autowired
	private FuncionarioService funcionarioService;

	protected static final String AVALIADOR_ID = "/{avaliadorId:[0-9]+}";
	protected static final String AVALIACAO_COMPETENCIA_ID = "/{avaliacaoCompetenciaId:[0-9]+}";
	protected static final String PERFIL_ID = "/{perfilId:[0-9]+}";

	@Autowired
	public AvaliacaoCompDetResource(AvaliacaoCompDetService avaliacaoCompDetService) {
		super(avaliacaoCompDetService);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/avaliacaocompetencia/" + AVALIACAO_COMPETENCIA_ID + "/avaliador/" + AVALIADOR_ID + "/detalhamentos")
	public List<AvaliacaoCompDetTO> listByAvaliacaoCompetencia(@PathVariable("avaliacaoCompetenciaId") Long avaliacaoCompetenciaId, @PathVariable("avaliadorId") Long avaliadorId) {
		// Valida os IDs
		FuncionarioTO funcionario = funcionarioService.load(avaliadorId);
		AvaliacaoCompetenciaTO avaliacaoCompetencia = avaliacaoCompetenciaService.load(avaliacaoCompetenciaId);

		List<AvaliacaoCompDetTO> listaDetalhamentos = avaliacaoCompDetService.listByAvaliacaoCompetenciaAndAvaliadorWithPreenchimentoAndHint(avaliacaoCompetencia.getId(), funcionario.getId());

		Collections.sort(listaDetalhamentos, new Comparator<AvaliacaoCompDetTO>() {
			@Override
			public int compare(AvaliacaoCompDetTO o1, AvaliacaoCompDetTO o2) {
				int result = o1.getOrdem().compareTo(o2.getOrdem());

				return result;
			}
		});

		return listaDetalhamentos;
	}

}
