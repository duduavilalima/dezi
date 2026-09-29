package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.ConceitoTO;

import java.util.List;

public interface ConceitoService extends AvdService<ConceitoTO, ConceitoTO>{
	List<ConceitoTO> findAllConceitosEmpresaLogada();
}
