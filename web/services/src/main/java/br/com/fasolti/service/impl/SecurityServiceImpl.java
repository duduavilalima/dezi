package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.repository.FuncionarioRepository;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.util.UsernameParser;
import br.com.fasolti.commons.services.security.BaseSecurityService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("production")
public class SecurityServiceImpl extends BaseSecurityService<UserTO> {
	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private Mapper mapper;

	@Override
	public UserTO findUserByUsername(String username) {
		UsernameParser parser = UsernameParser.of(username);

		Long empresaId = parser.extractEmpresa();
		String matricula = parser.extractMatricula();

		Funcionario funcionario = funcionarioRepository.findForLogin(empresaId, matricula, Situacao.ATIVA);

		return mapper.map(funcionario, UserTO.class);
	}
}
