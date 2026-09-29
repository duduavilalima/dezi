package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.ContratanteFasoltiService;
import br.com.fasolti.avd.transfer.ContratanteFasoltiTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("contratante")
public class ContratanteFasoltiResource
	extends BaseAvdResource<ContratanteFasoltiTO, ContratanteFasoltiTO> {

	@Autowired
	private ContratanteFasoltiService contratanteFasoltiService;

	@Autowired
	public ContratanteFasoltiResource(ContratanteFasoltiService service) {
		super(service);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/usuariologado")
	public final ContratanteFasoltiTO contratanteDoUsuarioLogado() throws Exception {
		//FIXME Verificar se tem alguem usando isso no frontend e passar a usar diretamente a info que está no funcionário
		return contratanteFasoltiService.getLoggedUser().getEmpresa().getContratante();
	}

}
