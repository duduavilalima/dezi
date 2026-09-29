package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.CargoCompetenciaService;
import br.com.fasolti.avd.transfer.CargoCompetenciaTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("cargo/competencia") //TODO Melhorar esse path, talvez integrar no CargoResource (não parece fazer sentido pesquisar isso fora do cargo)
public class CargoCompetenciaResource extends BaseAvdResource<CargoCompetenciaTO, CargoCompetenciaTO> {

	@Autowired
	private CargoCompetenciaService cargoCompetenciaService;

	@Autowired
	public CargoCompetenciaResource(CargoCompetenciaService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{idCargo:[0-9]+}/tipo/{idTipoCompetencia:[0-9]+}")
	public List<CargoCompetenciaTO> obterCargoCompetenciaPorCargoAndTipoCompetencia(@PathVariable Long idCargo, @PathVariable Long idTipoCompetencia) {
		return cargoCompetenciaService.obterCargoCompetenciaPorCargoAndTipoCompetencia(idCargo, idTipoCompetencia);
	}

}
