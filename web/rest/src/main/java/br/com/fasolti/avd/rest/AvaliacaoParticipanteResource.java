package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.rest.util.LogRequired;
import br.com.fasolti.avd.service.AvaliacaoParticipanteService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.FuncionarioMensagemTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.search.ParticipanteSearchTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("avaliacaoparticipante")
public class AvaliacaoParticipanteResource extends BaseAvdResource<ParticipanteTO, ParticipanteSearchTO>{

	protected static final String AVALIADO_ID = "/{avaliadoId:[0-9]+}";
	protected static final String AVALIACAO_ID = "/{avaliacaoId:[0-9]+}";
	protected static final String PARTICIPANTE_ID = "/{participanteId:[0-9]+}";

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private AvaliacaoParticipanteService avaliacaoParticipanteService;

	@Autowired
	public AvaliacaoParticipanteResource(AvaliacaoParticipanteService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/atualizaavaliadorandrevisor")
	@LogRequired(entity = "Participante")
	public ParticipanteTO atualizaAvaliadorAndRevisor(@RequestBody ParticipanteTO participante) throws Exception{
		return avaliacaoParticipanteService.atualizaAvaliadorAndRevisor(participante);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/liberaavaliacaoconsenso")
	@LogRequired(entity = "Participante")
	public ParticipanteTO liberaAvaliacaoConsenso(@RequestBody ParticipanteTO participante) throws Exception{
		return avaliacaoParticipanteService.liberaAvaliacaoConsenso(participante);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/listaravaliacoesporavaliado")
	public List<AvaliacaoTO> listarAvaliacoesPorAvaliado(@RequestBody Long avaliadoId) throws Exception{
		return avaliacaoParticipanteService.listarAvaliacoesPorAvaliado(avaliadoId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/avaliado/"+AVALIADO_ID+"/avaliacao/"+AVALIACAO_ID+"/participantes")
	public List<ParticipanteTO> participantesPorAvaliadorEAvaliacao(@PathVariable("avaliadoId") Long avaliadoId, @PathVariable("avaliacaoId") Long avaliacaoId) {
		return avaliacaoParticipanteService.obterParticipantesPorAvaliadoEAvaliacao(avaliadoId, avaliacaoId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/avaliacao/"+AVALIACAO_ID+"/participantes")
	public List<FuncionarioMensagemTO> participantesEPerifsPorAvaliacao(@PathVariable("avaliacaoId") Long avaliacaoId) {
		return funcionarioService.obterFuncionarioParticipantesDaAvaliacao(avaliacaoId);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/pdi/"+PARTICIPANTE_ID+"/participante")
	public List<ParticipanteTO> pdiDoParticipante(@PathVariable("participanteId") Long participanteId) {
		return avaliacaoParticipanteService.listarAvaliacoesComPDIPorAvaliado(participanteId);
	}

}
