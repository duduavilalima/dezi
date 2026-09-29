package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.BiParametroUsuarioTO;

public interface BiParametroUsuarioService extends AvdService<BiParametroUsuarioTO, BiParametroUsuarioTO> {

	BiParametroUsuarioTO criarParametros(Long campanhaId, Long idFuncionario);
}
