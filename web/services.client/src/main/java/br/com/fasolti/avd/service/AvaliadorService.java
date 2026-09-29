package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.mobile.FuncionarioFotoTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;

import java.util.List;

public interface AvaliadorService extends AvdService<FuncionarioTO, FuncionarioTO> {

	List<FuncionarioFotoTO> fotosAvaliacoesAtivas(Long avaliadorId);

	FuncionarioSimpleTO findAvaliador(FuncionarioSimpleTO avaliado);

}
