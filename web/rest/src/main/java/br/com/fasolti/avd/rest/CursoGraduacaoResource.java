package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.CursoGraduacaoService;
import br.com.fasolti.avd.transfer.CursoGraduacaoTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("curso/graduacao") //TODO Melhorar esse path, talvez integrar no CursoResource (não parece fazer sentido pesquisar isso fora do curso)
public class CursoGraduacaoResource
	extends BaseAvdResource<CursoGraduacaoTO, CursoGraduacaoTO> {

	@Autowired
	private CursoGraduacaoService cursoGraduacaoService;

	@Autowired
	public CursoGraduacaoResource(CursoGraduacaoService service) {
		super(service);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "porcontratanteusuariologado")
	public final List<CursoGraduacaoTO> porContratanteUsuarioLogado() throws Exception {
		return cursoGraduacaoService.listAtivos();
	}

}
