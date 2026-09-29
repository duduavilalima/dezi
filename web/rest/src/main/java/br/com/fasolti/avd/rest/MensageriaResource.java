package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.MensageriaService;
import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.MensageriaTO;
import br.com.fasolti.avd.transfer.search.MensageriaSearchTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("mensageria")
public class MensageriaResource extends BaseAvdResource<MensageriaTO, MensageriaSearchTO> {

	@Autowired
	private MensageriaService mensageriaService;

	@Autowired
	public MensageriaResource(MensageriaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getallmessagesbyavaliacao")
	public List<MensageriaTO> getAllMessagesByAvaliacao(@RequestBody AvaliacaoTO avaliacaoTO) {
		return mensageriaService.getAllMessagesByAvaliacao(avaliacaoTO);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/{userId:[0-9]+}/naolidas")
	public MensageriaSearchTO mensagensPorUsuarioNaoLidas(@PathVariable Long userId) {
		//FIXME Tem que fazer o ServiceFilter e passar a filtrar por default pelo usuário logado
		return mensageriaService.quantidadeMensagensNaoLidas(userId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/user/{userId:[0-9]+}/all")
	public Map<String, List<MensageriaTO>> mensagensPorFuncionario(@PathVariable Long userId) {
		//FIXME Tem que fazer o ServiceFilter e passar a filtrar por default pelo usuário logado
		return mensageriaService.mensagensPorUsuario(userId);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/destinatariosmensagem")
	public List<MensageriaTO> destinatariosMensagem(@RequestBody MensageriaTO mensageriaTO) {
		return mensageriaService.destinatariosMensagem(mensageriaTO);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/getallmessagesrecebidasbyavaliacao")
	public List<MensageriaTO> getAllMessagesRecebidasByAvaliacao(@RequestBody AvaliacaoTO avaliacaoTO) {
		return mensageriaService.getAllMessagesRecebidasByAvaliacao(avaliacaoTO);
	}

}
