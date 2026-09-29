package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.FuncionarioFormacaoService;
import br.com.fasolti.avd.transfer.FuncionarioFormacaoTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("funcionarioformacao")  //TODO talvez integrar no outro resource (não parece fazer sentido pesquisar separadamente)
public class FuncionarioFormacaoResource extends BaseAvdResource<FuncionarioFormacaoTO, FuncionarioFormacaoTO> {

	@Autowired
	public FuncionarioFormacaoResource(FuncionarioFormacaoService funcionarioFormacaoService) {
		super(funcionarioFormacaoService);
	}
}
