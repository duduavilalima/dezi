package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseDeziResource;
import br.com.fasolti.avd.service.CargoCompetenciaService;
import br.com.fasolti.avd.service.CargoService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.CargoCompetenciaTO;
import br.com.fasolti.avd.transfer.CargoTO;
import br.com.fasolti.avd.transfer.CargoTreeTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.lang.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("cargo")
public class CargoResource extends BaseDeziResource<CargoTO> {

	@Autowired
	private CargoCompetenciaService cargoCompetenciaService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	public CargoResource(CargoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "cargosporempresausuariologado")
	public final HttpEntity<List<CargoTO>> cargosPorEmpresaUsuarioLogado() throws Exception {
		return ResponseEntity.ok(cargoService.findAllCargosByUserEmpresaAndAtivos());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/cargoscompetencias")
	public final HttpEntity<List<CargoCompetenciaTO>> findCargosCompetencias(@PathVariable Long id) throws Exception {
		// FIXME Deveria estar no resource apropriado
		return ResponseEntity.ok(cargoCompetenciaService.findAllCargoCompetenciaByCargo(id));
	}

	@RequestMapping(method = RequestMethod.GET, value = ID + "/funcionarios")
	public final List<FuncionarioTO> funcionariosDeUmCargo(@PathVariable Long id) throws Exception {
		return funcionarioService.findByCargoIdAndSituacaodProjections(id, Situacao.ATIVA);
	}

	@RequestMapping(method = RequestMethod.GET, value = ID + "/hierarquia")
	public final List<CargoTreeTO> listarArvoreDeHierarquiaDeCargos(@PathVariable Long id) throws Exception {
		return cargoService.listarArvoreDeHierarquiaDeCargos(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id:[0-9]+}/copy")
	public final ResponseEntity<?> copiarCargos(@PathVariable Long id) throws Exception {
		try {
			return ResponseEntity.ok(cargoService.copiarCargo(id));
		}catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getErrorInfo());
		}
	}

}
