package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.FuncionarioIdiomaService;
import br.com.fasolti.avd.transfer.FuncionarioIdiomaTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("funcionarioidioma")  //TODO talvez integrar no outro resource (não parece fazer sentido pesquisar separadamente)
public class FuncionarioIdiomaResource extends BaseAvdResource<FuncionarioIdiomaTO, FuncionarioIdiomaTO> {

	@Autowired
	public FuncionarioIdiomaResource(FuncionarioIdiomaService funcionarioIdiomaService) {
		super(funcionarioIdiomaService);
	}
}
