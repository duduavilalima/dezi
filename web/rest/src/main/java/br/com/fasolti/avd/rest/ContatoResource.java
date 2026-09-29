package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.service.ContatoService;
import br.com.fasolti.avd.transfer.ContatoTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("contato")
public class ContatoResource {

	@Autowired
	private ContatoService contatoService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "enviar")
	public final ResponseEntity<Void> enviar(@RequestBody ContatoTO contatoTO) {
		contatoService.enviarMensagem(contatoTO);
		return ResponseEntity.noContent().build();
	}
}
