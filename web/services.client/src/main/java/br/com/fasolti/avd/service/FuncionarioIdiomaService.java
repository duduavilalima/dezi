package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.FuncionarioIdiomaTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;

import java.util.List;

public interface FuncionarioIdiomaService extends AvdService<FuncionarioIdiomaTO, FuncionarioIdiomaTO> {

	List<FuncionarioIdiomaTO> listByFuncionario(FuncionarioTO funcionario);

}
