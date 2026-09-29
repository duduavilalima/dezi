package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.PreenchimentoService;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.PreenchimentoTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("preenchimento")
public class PreenchimentoResource extends BaseAvdResource<PreenchimentoTO, PreenchimentoTO> {

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	public PreenchimentoResource(PreenchimentoService preenchimentoService) {
		super(preenchimentoService);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/reset")
	public List<PreenchimentoTO> reset(ParticipanteTO participante) {
		return preenchimentoService.reset(participante);
	}
}
