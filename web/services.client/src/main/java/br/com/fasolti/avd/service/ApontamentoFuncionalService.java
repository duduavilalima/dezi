package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.ApontamentoFuncionalTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.mobile.ApontamentoFuncionalMobileTO;

import java.util.List;

public interface ApontamentoFuncionalService extends AvdService<ApontamentoFuncionalTO, ApontamentoFuncionalTO> {

	List<ApontamentoFuncionalTO> apontamentosPorFuncionario(FuncionarioTO funcionario);

	void salvarApontamentoFuncionalMobile(ApontamentoFuncionalMobileTO apontamentoTO);

	List<ApontamentoFuncionalMobileTO> apontamentoPorFuncionarioMobile(Long idAvaliado, Long idAvaliador);

	List<ApontamentoFuncionalMobileTO> apontamentoMobile(Long idUsuario);


}
