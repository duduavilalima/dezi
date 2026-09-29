package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.ParametroTO;

import java.util.List;

public interface ParametroService {
	ParametroTO buscaPorChave(String chave);
	ParametroTO save(ParametroTO parametro);
	List<ParametroTO> buscaTodos();
}
