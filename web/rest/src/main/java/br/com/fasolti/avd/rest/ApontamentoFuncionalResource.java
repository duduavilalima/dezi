package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.ApontamentoFuncionalService;
import br.com.fasolti.avd.transfer.ApontamentoFuncionalTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.mobile.ApontamentoFuncionalMobileTO;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("apontamentofuncional")
public class ApontamentoFuncionalResource
	extends BaseAvdResource<ApontamentoFuncionalTO, ApontamentoFuncionalTO> {

	@Autowired
	private ApontamentoFuncionalService apontamentoFuncionalService;

	@Autowired
	private Mapper mapper;

	@Autowired
	public ApontamentoFuncionalResource(ApontamentoFuncionalService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/porparticipante")
	public List<ApontamentoFuncionalTO> avaliacoesPorCampanha(@RequestBody ParticipanteTO participante) throws Exception {
		// TODO Criar um método básico para isso
		FuncionarioTO funcionario = mapper.map(participante.getAvaliado(), FuncionarioTO.class);
		return apontamentoFuncionalService.apontamentosPorFuncionario(funcionario);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/mobile/apontamentoslistar")
	public List<ApontamentoFuncionalMobileTO> apontamentoPorFuncionarioMobile(@RequestBody Long idAvaliado, Long idAvaliador) throws Exception {
		return apontamentoFuncionalService.apontamentoPorFuncionarioMobile(idAvaliado, idAvaliador);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/mobile/usuarioapontamentoslistar")
	public List<ApontamentoFuncionalMobileTO> apontamentoMobile(@RequestBody Long idUsuario) throws Exception {
		return apontamentoFuncionalService.apontamentoMobile(idUsuario);
	}

}
