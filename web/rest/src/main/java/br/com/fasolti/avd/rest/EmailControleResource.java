package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.EmailControleService;
import br.com.fasolti.avd.transfer.EmailControleTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("email")
public class EmailControleResource extends BaseAvdResource<EmailControleTO, EmailControleTO> {

	@Autowired
	private EmailControleService emailControleService;

	@Autowired
	public EmailControleResource(EmailControleService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/consultaremailscomErro")
	public List<EmailControleTO> consultarEmailsComErro() {
		return emailControleService.consultarEmailsComErro();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/consultaremailspendentes")
	public List<EmailControleTO> consultarEmailsPendentes() {
		return emailControleService.consultarEmailsPendentes();
	}

}
