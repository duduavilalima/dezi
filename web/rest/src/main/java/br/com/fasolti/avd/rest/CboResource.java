package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.service.CboService;
import br.com.fasolti.avd.transfer.AtividadeCBOTO;
import br.com.fasolti.avd.transfer.OcupacaoCBOTO;
import br.com.fasolti.avd.transfer.PerfilCBOTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("cbo")
public class CboResource {

	@Autowired
	private CboService cboService;

	@RequestMapping(method = RequestMethod.GET, value = "/ocupacoes")
	public List<OcupacaoCBOTO> ocupacoes() {
		return cboService.listarTodosCboOcupcoes();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/ocupacao/{id:[0-9]+}/perfis")
	public List<PerfilCBOTO> perfisPorOcupacao(@PathVariable("id") Long ocupacaoId) {
		return cboService.listarPerfisPorOcupacao(ocupacaoId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/perfil/{id:[0-9]+}/atividades")
	public List<AtividadeCBOTO> atividadesPorPerfil(@PathVariable("id") Long perfilId) {
		return cboService.listarAtividadesPorPerfil(perfilId);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/search")
	public List<OcupacaoCBOTO> autocompleteBuscar(@RequestBody OcupacaoCBOTO to) {
		return cboService.autocompleteBuscar(to);
	}

}
