package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.rest.util.LogRequired;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.MensageriaTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.search.AvaliacaoSearchTO;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.commons.lang.ValidationException;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("avaliacao")
public class AvaliacaoResource extends BaseAvdResource<AvaliacaoTO, AvaliacaoSearchTO> {

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	private PainelControleService painelControleService;

	@Autowired
	private AvaliacaoParticipanteService avaliacaoParticipanteService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private Mapper mapper;

	@Autowired
	private MensageriaService mensageriaService;

	@Autowired
	public AvaliacaoResource(AvaliacaoService avaliacaoService) {
		super(avaliacaoService);
	}

	@Override
	protected Enum valueOfStatus(String status) {
		return SituacaoAvaliacao.valueOf(status);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/copiar")
	@LogRequired(entity = "Avaliacao")
	public AvaliacaoTO copiar(@RequestBody AvaliacaoTO avaliacaoTO) throws Exception {
		return avaliacaoService.copiar(avaliacaoTO);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{id:[0-9]+}/mensagens")
	@LogRequired(entity = "Avaliacao")
	public ResponseEntity enviarMensagem(@RequestBody List<MensageriaTO> listMensagens) {
		try {
			return ResponseEntity.ok(mensageriaService.saveAndSendMensagensAvaliacao(listMensagens));
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getErrorInfo());
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/liberar")
	@LogRequired(entity = "Avaliacao")
	public ResponseEntity liberar(@RequestBody AvaliacaoTO avaliacaoTO) throws Exception {
		try {
			avaliacaoService.liberar(avaliacaoTO);
			return ResponseEntity.ok(avaliacaoService.load(avaliacaoTO.getId()));
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getErrorInfo());
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/salvarparticipantes")
	@LogRequired(entity = "Avaliacao")
	public AvaliacaoTO salvaParticipantes(@RequestBody AvaliacaoTO avaliacaoTO) throws Exception {
		avaliacaoService.salvarParticipantes(avaliacaoTO);
		AvaliacaoTO avaliacaoTOAtualizada = avaliacaoService.load(avaliacaoTO.getId());
		return avaliacaoTOAtualizada;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/salvaravaliadores")
	@LogRequired(entity = "Avaliacao")
	public AvaliacaoTO salvarAvaliadores(@RequestBody AvaliacaoTO avaliacaoTO) throws Exception {
		if (avaliacaoTO.getStatus().equals(SituacaoAvaliacao.LIBERADA)
			&& avaliacaoTO.getAutoavaliacao().equals(true)
			&& avaliacaoTO.getGestores().equals(false)
			&& avaliacaoTO.getPares().equals(false)
			&& avaliacaoTO.getSubordinados().equals(false)
		) {
			avaliacaoService.liberar(avaliacaoTO);
			AvaliacaoTO avaliacaoTOAtualizada = avaliacaoService.load(avaliacaoTO.getId());
			return avaliacaoTOAtualizada;
		}
		if (avaliacaoTO.getStatus().equals(SituacaoAvaliacao.LIBERADA))
			avaliacaoService.salvarAvaliadoresLiberacao(avaliacaoTO);
		else
			avaliacaoService.salvarAvaliadores(avaliacaoTO);
		AvaliacaoTO avaliacaoTOAtualizada = avaliacaoService.load(avaliacaoTO.getId());
		return avaliacaoTOAtualizada;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/removeravaliado")
	@LogRequired(entity = "Avaliacao")
	public AvaliacaoTO removerAvaliado(@RequestBody AvaliacaoTO avaliacaoTO) throws Exception {
		avaliacaoService.removerAvaliado(avaliacaoTO.getParticipantes(), avaliacaoTO);
		AvaliacaoTO avaliacaoTOAtualizada = avaliacaoService.getById(avaliacaoTO.getId());
		return avaliacaoTOAtualizada;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/removertodosparticipantes")
	@LogRequired(entity = "Avaliacao")
	public AvaliacaoTO removerTodosParticipantes(@RequestBody AvaliacaoTO avaliacaoTO) throws Exception {
		avaliacaoService.removerTodosParticipantes(avaliacaoTO);
		AvaliacaoTO avaliacaoTOAtualizada = avaliacaoService.getById(avaliacaoTO.getId());
		return avaliacaoTOAtualizada;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/removeragliberacao")
	public void removeragliberacao(@PathVariable Long id) throws Exception {
		avaliacaoService.removeragliberacao(id);
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public List<ParticipanteTO> fileUpload(@RequestParam(value = "file") MultipartFile file) throws IOException {
		return avaliacaoService.importarPares(file.getInputStream());
	}

	@RequestMapping(value = "/salvarpares", method = RequestMethod.POST)
	@LogRequired(entity = "Participante")
	public AvaliacaoTO salvarPares(@RequestBody List<ParticipanteTO> pares) throws Exception {
		avaliacaoService.salvarPares(pares);
		AvaliacaoTO avaliacaoTOAtualizada = avaliacaoService.load(pares.get(0).getAvaliacao().getId());
		return avaliacaoTOAtualizada;
	}

	@RequestMapping(value = "/pdi", method = RequestMethod.GET)
	public List<AvaliacaoTO> obterAvaliacoesPDI() {
		return avaliacaoService.obterAvaliacoesPDI();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/{id:[0-9]+}/ajustarpesos")
	@LogRequired(entity = "Avaliacao")
	public ResponseEntity<AvaliacaoTO> ajustarAvalPesoCargoComp() {
		avaliacaoService.ajustarAvalPesoCargoComp();
		return ResponseEntity.ok(new AvaliacaoTO());
	}

}
