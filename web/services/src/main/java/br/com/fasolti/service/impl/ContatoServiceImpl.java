package br.com.fasolti.service.impl;

import br.com.fasolti.avd.service.ContatoService;
import br.com.fasolti.avd.service.EmailControleService;
import br.com.fasolti.avd.transfer.ContatoTO;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContatoServiceImpl implements ContatoService {

	public static final String EMAIL_CONTATO_SUPORTE = "email.contato.suporte";

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private EmailControleService emailControleService;

	@Override
	public void enviarMensagem(ContatoTO contato) {
		emailControleService.sendEmailEnviarMensagem(contato);
	}

}
