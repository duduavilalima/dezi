package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.DepartamentoService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.CargoListaCompetenciaTO;
import br.com.fasolti.avd.transfer.DepartamentoTO;
import br.com.fasolti.avd.transfer.DepartamentoTreeTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.types.Situacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("departamento")
public class DepartamentoResource extends BaseAvdResource<DepartamentoTO, DepartamentoTO> {

	@Autowired
	private DepartamentoService departamentoService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Override
	protected Enum valueOfStatus(String status) {
		return Situacao.valueOf(status);
	}

	@Autowired
	public DepartamentoResource(DepartamentoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "departamentosdaempresalogada")
	public final List<DepartamentoTO> departamentosdaempresalogada() throws Exception {
		return departamentoService.findAllDepartamentosEmpresaLogada();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/funcionarios")
	public final List<FuncionarioTO> funcionariosDeUmDepartamento(@PathVariable Long id) throws Exception {
		return funcionarioService.obterFuncionariosAtivosDoDepartamento(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/cargocompetencia")
	public final List<CargoListaCompetenciaTO> cargoCompetencia(@PathVariable Long id) throws Exception {
		return departamentoService.obterCargoCompetenciaPorDepartamento(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/arvore")
	public final List<DepartamentoTreeTO> listarArvoreDeHierarquiaDeDepartamentos() throws Exception {
		return departamentoService.listarArvoreDeHierarquiaDeDepartamentos();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/arvorefilhos")
	public final List<DepartamentoTO> listarArvoreDeHierarquiaDeDepartamentos(@PathVariable Long id) throws Exception {
		DepartamentoTO departamentoTO = new DepartamentoTO();
		departamentoTO.setId(id);

		return departamentoService.obterDepartamentosDescendentes(departamentoTO);
	}

	/*
	Recupera a a hierarquia de departamentos, a qual o usuário logado tem acesso, de acordo com o perfil do usuário
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/departamentosfuncionariologado")
	public final List<DepartamentoTO> listarDepartamentosUsuarioLogado() throws Exception {
		return departamentoService.obterDepartamentosUsuarioLogado();
	}



}
