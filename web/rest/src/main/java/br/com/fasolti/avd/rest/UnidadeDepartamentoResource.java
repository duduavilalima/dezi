package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.CargoService;
import br.com.fasolti.avd.service.UnidadeDepartamentoService;
import br.com.fasolti.avd.transfer.UnidadeDepartamentoTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("unidadedepartamento")
public class UnidadeDepartamentoResource extends BaseAvdResource<UnidadeDepartamentoTO, UnidadeDepartamentoTO> {

	@Autowired
	private UnidadeDepartamentoService unidadeDepartamentoService;

	@Autowired
	public UnidadeDepartamentoResource(UnidadeDepartamentoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/departamentos")
	public List<UnidadeDepartamentoTO> obterDepartamentosUnidadePorUnidade(@PathVariable("id") Long id) throws Exception {
		return unidadeDepartamentoService.obterDepartamentosUnidadePorUnidade(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/gestor")
	public UnidadeDepartamentoTO obterDepartamentoUnidadePorGestor(@PathVariable("id") Long id) throws Exception {
		return unidadeDepartamentoService.obterDepartamentoUnidadePorGestorId(id);
	}

}
