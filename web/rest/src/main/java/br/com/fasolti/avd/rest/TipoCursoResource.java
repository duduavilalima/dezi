package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.TipoCursoService;
import br.com.fasolti.avd.transfer.TipoCursoTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("tipocurso")
public class TipoCursoResource
	extends BaseAvdResource<TipoCursoTO, TipoCursoTO> {

	@Autowired
	private TipoCursoService tipoCursoService;

	@Autowired
	public TipoCursoResource(TipoCursoService service) {
		super(service);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "porcontratanteusuariologado")
	public final List<TipoCursoTO> porContratanteUsuarioLogado() throws Exception {
		// TODO Verificar esse método, parece que não funciona como esperado
		return tipoCursoService.findByContratanteUsuarioLogadoSituacaoAtivo();
	}

}
