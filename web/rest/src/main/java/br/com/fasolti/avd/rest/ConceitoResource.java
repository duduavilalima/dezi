package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.AvdService;
import br.com.fasolti.avd.service.ConceitoService;
import br.com.fasolti.avd.transfer.ConceitoTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("conceito")
public class ConceitoResource extends BaseAvdResource<ConceitoTO, ConceitoTO>{

	@Autowired
	private ConceitoService conceitoService;

	@Autowired
	public ConceitoResource(ConceitoService service) {
		super(service);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "conceitosdaempresalogada")
	public final List<ConceitoTO> conceitosDaEmpresaLogada() throws Exception {
		return conceitoService.findAllConceitosEmpresaLogada();
	}
}
