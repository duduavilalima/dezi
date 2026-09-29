package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.MensagemService;
import br.com.fasolti.avd.transfer.EmailBoasVindasTO;
import br.com.fasolti.avd.transfer.MensagemTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("mensagem")
public class MensagemResource
	extends BaseAvdResource<MensagemTO, MensagemTO> {

	@Autowired
	private MensagemService mensagemService;

	@Autowired
	public MensagemResource(MensagemService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/enviarmensagemboasvindas")
	public void enviarMensagemBoasVindas(@RequestBody EmailBoasVindasTO emailBoasVindasTO) throws Exception{
		this.mensagemService.sendWelcomeMessage(emailBoasVindasTO);
	}

}
