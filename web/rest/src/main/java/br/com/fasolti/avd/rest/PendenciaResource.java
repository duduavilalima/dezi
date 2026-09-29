package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.PendenciaService;
import br.com.fasolti.avd.transfer.PendenciaTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("pendencia")
public class PendenciaResource extends BaseAvdResource<PendenciaTO, PendenciaTO> {

	@Autowired
	private PendenciaService pendenciaService;

	@Autowired
	public PendenciaResource(PendenciaService pendenciaService) {
		super(pendenciaService);
	}

}
