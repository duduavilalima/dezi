package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.FuncionarioFormacaoTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;

import java.util.List;

public interface FuncionarioFormacaoService extends AvdService<FuncionarioFormacaoTO, FuncionarioFormacaoTO> {

	List<FuncionarioFormacaoTO> listByFuncionario(FuncionarioTO funcionario);

}
