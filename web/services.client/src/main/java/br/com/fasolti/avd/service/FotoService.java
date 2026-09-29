package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.FotoTO;
import br.com.fasolti.avd.transfer.mobile.FuncionarioFotoTO;

import java.io.IOException;

public interface FotoService extends AvdService<FotoTO, FotoTO> {
	FotoTO obterFotoPerfilFuncionario(Long funcionarioId);

	FotoTO obterAvatarFuncionario(Long funcionarioId);
}
