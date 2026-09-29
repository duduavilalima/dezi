package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.ConfiguracaoTO;
import br.com.fasolti.commons.client.service.CrudService;

public interface ConfiguracaoService extends CrudService<ConfiguracaoTO> {
	ConfiguracaoTO findByFuncionarioId(Long funcionarioId);
}
