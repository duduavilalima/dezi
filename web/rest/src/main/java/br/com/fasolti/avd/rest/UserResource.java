package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.service.ConfiguracaoService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.ConfiguracaoTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.commons.rest.BaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

//TODO Criar a entidade User
@RestController
@RequestMapping("user")
public class UserResource extends BaseResource<FuncionarioTO> {
	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private ConfiguracaoService configuracaoService;

	@Autowired
	public UserResource(FuncionarioService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, path = "logged")
	public ResponseEntity<UserTO> logged() {
		UserTO user = funcionarioService.getLoggedUser();
		ConfiguracaoTO configuracaoTO = configuracaoService.findByFuncionarioId(user.getId());

		if (configuracaoTO == null) {
			configuracaoTO = new ConfiguracaoTO();
			configuracaoTO.setFuncionario(funcionarioService.findById(funcionarioService.getLoggedUser().getId()));
			configuracaoTO.setLinguagem("pt_BR");
			configuracaoTO.setTamanhoFonte("medium");
			configuracaoService.save(configuracaoTO);
		}
		else if (configuracaoTO != null && configuracaoTO.getLinguagem() != null) {
			configuracaoTO.setLinguagem(configuracaoTO.getLinguagem().trim());
			configuracaoService.saveOrUpdate(configuracaoTO);
		}
		else if (configuracaoTO != null && configuracaoTO.getLinguagem() == null) {
			configuracaoTO.setLinguagem("pt_BR");
			configuracaoService.saveOrUpdate(configuracaoTO);
		}

		user.setConfiguracao(configuracaoTO);
		user.setSenhaCriptografada(null); // Segurança

		return ResponseEntity.ok(user);
	}

	@RequestMapping(method = RequestMethod.GET, path = "roles")
	public ResponseEntity<Collection<? extends GrantedAuthority>> getRoles() {
		return ResponseEntity.ok(funcionarioService.getLoggedUser().getAuthorities());
	}
}
