package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.EmpresaService;
import br.com.fasolti.avd.transfer.EmpresaTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("empresa")
public class EmpresaResource extends BaseAvdResource<EmpresaTO, EmpresaTO> {

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	public EmpresaResource(EmpresaService service) {
		super(service);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/contratantedousuariologado")
	public final List<EmpresaTO> contratantedousuariologado() throws Exception {
		return empresaService.findAllEmpresasContratanteUsuarioLogado();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/contratantedousuario")
	public final List<EmpresaTO> contratantedousuario(@RequestBody FuncionarioTO funcionarioTO) throws Exception {
		return empresaService.findAllEmpresasContratanteUsuario(funcionarioTO);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/todas")
	public final List<EmpresaTO> obterTodasAsEmpresas() throws Exception {
		return empresaService.empresasTodasAsEmpresas();
	}

}
