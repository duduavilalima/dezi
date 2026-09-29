package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.AvaliacaoParticipanteService;
import br.com.fasolti.avd.service.AvaliadorService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.mobile.FuncionarioFotoTO;
import br.com.fasolti.avd.transfer.mobile.FuncionarioMobileTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("avaliador")
public class AvaliadorResource extends BaseAvdResource<FuncionarioTO, FuncionarioTO> {

	@Autowired
	private Mapper mapper;

	@Autowired
	private AvaliadorService avaliadorService;

	@Autowired
	private AvaliacaoParticipanteService participanteService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	public AvaliadorResource(AvaliadorService avaliadorService) {
		super(avaliadorService);
		this.avaliadorService = avaliadorService;
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/obterfotosfuncionariologado")
	public final List<FuncionarioFotoTO> obterFotosFuncionarioLogado(@RequestBody FuncionarioMobileTO avaliador) {
		return avaliadorService.fotosAvaliacoesAtivas(avaliador.getId());
	}

	@RequestMapping(method = RequestMethod.GET, value = {"/{avaliadoId:[0-9]+}/avaliador/{avaliacaoId:[0-9]+}"})
	public FuncionarioSimpleTO subordinados(@PathVariable Long avaliadoId, @PathVariable Long avaliacaoId) {
		FuncionarioSimpleTO avaliador = null;

		boolean temAvaliadores = participanteService.temAvaliadores(avaliacaoId, avaliadoId);

		//TODO Qual é a lógica desse método? Está retornando nulo se não tem avaliadores
		if (!temAvaliadores) {
			FuncionarioTO funcionario = funcionarioService.load(avaliadoId);
			FuncionarioSimpleTO avaliado = mapper.map(funcionario, FuncionarioSimpleTO.class);

			avaliador = avaliadorService.findAvaliador(avaliado);
		}

		return avaliador;
	}
}
