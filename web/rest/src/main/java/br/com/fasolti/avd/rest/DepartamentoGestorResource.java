package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.DepartamentoGestorService;
import br.com.fasolti.avd.transfer.DepartamentoGestorTO;
import br.com.fasolti.avd.types.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("departamentogestor")
public class DepartamentoGestorResource extends BaseAvdResource<DepartamentoGestorTO, DepartamentoGestorTO> {

	@Autowired
	DepartamentoGestorService departamentoGestorService;

	@Override
	protected Enum valueOfStatus(String status) {
		return Situacao.valueOf(status);
	}

	@Autowired
	public DepartamentoGestorResource(DepartamentoGestorService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "relacionar")
	public final void relacionarDepartamentoGestor() throws Exception {
		departamentoGestorService.atualizarDepartamentoGestorTodos();
//		return new DepartamentoGestorTO();
	}


}
