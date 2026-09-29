package br.com.fasolti.service.impl;

import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.util.UsernameParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("development")
public class SecurityServiceDebug extends SecurityServiceImpl {
	@Value("${debug.user.empresaId}")
	private Long empresaId;

	@Value("${debug.user.login}")
	private String login;

	private static UserTO fakePrincipal;

	@Override
	protected Object getPrincipal() {
		if (fakePrincipal == null) {
			fakePrincipal = findUserByUsername(UsernameParser.compose(empresaId, login));
		}

		return fakePrincipal;
	}
}
