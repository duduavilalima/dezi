package br.com.fasolti.avd.rest.campanhasrapidas;

import br.com.fasolti.avd.rest.AuthResource;
import br.com.fasolti.avd.service.AvaliacaoService;
import br.com.fasolti.avd.service.MensageriaService;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.campanhasrapidas.HomeOfficeBrasilTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.util.UsernameParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("homeofficebrasil")
public class HomeOfficeBrasilResource {

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	private MensageriaService mensageriaService;

	@Autowired
	private AuthResource authResource;

	@Value("${homeoffice.empresaId}")
	private Long empresaId;

	@Value("${homeoffice.user.pass}")
	private String pass;

	@Value("${homeoffice.user.login}")
	private String login;

	public HomeOfficeBrasilResource() {
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> cadastro(@RequestBody HomeOfficeBrasilTO objectTO) throws CloneNotSupportedException {
		doAuthLogin();
		if (objectTO.getIsDiagnostico() == null && objectTO.getIsAmbienteEmpresa() == null) {
			return ResponseEntity.badRequest().build();
		}
		FuncionarioTO funcionarioTO = avaliacaoService.homeOfficeBrasilCriarFuncionario(objectTO);

		if (objectTO.getIsDiagnostico() != null && objectTO.getIsDiagnostico()) {
			FuncionarioSimpleTO funcionarioSimpleTO = avaliacaoService.homeOfficeBrasilSalvarParticipante(funcionarioTO);
			mensageriaService.homeOfficeBrasilMensagemBoasVindas(funcionarioSimpleTO);
		}

		if (objectTO.getIsAmbienteEmpresa() != null && objectTO.getIsAmbienteEmpresa()) {
			mensageriaService.homeOfficeBrasilMensagemAmbienteEmpresa(funcionarioTO);
		}
		doAuthLogout();
		return ResponseEntity.ok().build();
	}

	private void doAuthLogin() {
		String username = UsernameParser.compose(empresaId, login);
		Authentication auth = authResource.authenticate(username, pass);
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	private void doAuthLogout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

}
