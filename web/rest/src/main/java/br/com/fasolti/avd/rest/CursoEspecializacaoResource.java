package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.CursoEspecializacaoService;
import br.com.fasolti.avd.transfer.CursoEspecializacaoTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("curso/especializacao") //TODO Melhorar esse path, talvez integrar no CursoResource (não parece fazer sentido pesquisar isso fora do curso)
public class CursoEspecializacaoResource
	extends BaseAvdResource<CursoEspecializacaoTO, CursoEspecializacaoTO> {

	@Autowired
	private CursoEspecializacaoService cursoEspecializacaoService;

	@Autowired
	public CursoEspecializacaoResource(CursoEspecializacaoService service) {
		super(service);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "porcontratanteusuariologado")
	public final List<CursoEspecializacaoTO> porContratanteUsuarioLogado() throws Exception {
		return cursoEspecializacaoService.findByContratanteUsuarioLogadoAndSituacaoAtivo();
	}
}
