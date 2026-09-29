package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.DepartamentoGestorTO;

public interface DepartamentoGestorService extends AvdService<DepartamentoGestorTO,DepartamentoGestorTO> {

	void atualizarDepartamentoGestor(Long idDepartamento, Long idFuncionario);

	void atualizarDepartamentoGestorEmpresa(Long idEmpresa);

	void atualizarDepartamentoGestorTodos();

}
