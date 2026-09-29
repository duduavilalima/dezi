package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.CargoTipoCompetenciaService;
import br.com.fasolti.avd.transfer.CargoTipoCompetenciaTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("cargo/tipocompetencia") //TODO Melhorar esse path, talvez integrar no CargoResource (não parece fazer sentido pesquisar isso fora do cargo)
public class CargoTipoCompetenciaResource extends BaseAvdResource<CargoTipoCompetenciaTO, CargoTipoCompetenciaTO> {

	@Autowired
	private CargoTipoCompetenciaService cargoTipoCompetenciaService;

	@Autowired
	public CargoTipoCompetenciaResource(CargoTipoCompetenciaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/porcargo/{id:[0-9]+}")
	public final List<CargoTipoCompetenciaTO> obterTiposPorCargo(@PathVariable Long id) throws Exception {
		return cargoTipoCompetenciaService.findAllCargoTipoCompetenciaByCargo(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/buscartodos")
	public List<CargoTipoCompetenciaTO> buscarTodosTiposAtivos() {
		return cargoTipoCompetenciaService.buscarTodosTiposAtivos();
	}

}
