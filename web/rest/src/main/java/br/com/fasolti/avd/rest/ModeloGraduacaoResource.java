package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.ModeloGraduacaoService;
import br.com.fasolti.avd.transfer.ModeloGraduacaoTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("modelo") //TODO O nome está genérico demais
public class ModeloGraduacaoResource extends BaseAvdResource<ModeloGraduacaoTO, ModeloGraduacaoTO> {

	@Autowired
	public ModeloGraduacaoResource(ModeloGraduacaoService service) {
		super(service);
	}

}
