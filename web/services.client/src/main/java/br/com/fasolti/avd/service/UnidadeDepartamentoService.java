package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.CargoTO;
import br.com.fasolti.avd.transfer.DepartamentoTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.UnidadeDepartamentoTO;

import java.util.List;

public interface UnidadeDepartamentoService extends AvdService<UnidadeDepartamentoTO, UnidadeDepartamentoTO> {

	void atualizaGestorUnidadeDepartamento(UnidadeDepartamentoTO unidadeDepartamentoTO, FuncionarioTO funcionarioTO);

	UnidadeDepartamentoTO obterPorUnidadeEDepartamento(Long unidadeId, Long departamentoId);

	List<UnidadeDepartamentoTO> obterDepartamentosUnidadePorUnidade(Long id);

	UnidadeDepartamentoTO obterDepartamentoUnidadePorGestorId(Long gestorId);

	List<UnidadeDepartamentoTO> obterDepartamentoUnidadePorDepartamento(Long departamentoId);

}
