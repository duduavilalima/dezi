package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.CargoListaCompetenciaTO;
import br.com.fasolti.avd.transfer.DepartamentoTO;
import br.com.fasolti.avd.transfer.DepartamentoTreeTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;

import java.util.List;

public interface DepartamentoService extends AvdService<DepartamentoTO, DepartamentoTO> {
	List<DepartamentoTO> findAllDepartamentosEmpresaLogada();

	DepartamentoTO findByEmpresaAndIdDepartamentoCliente(Long idEmpresa, String idDepartamentoCliente);

	List<CargoListaCompetenciaTO> obterCargoCompetenciaPorDepartamento(Long idDepartamento);

	List<FuncionarioTO> obterFuncionariosAtivosDoDepartamento(Long idDepartamento);

	List<DepartamentoTreeTO> listarArvoreDeHierarquiaDeDepartamentos();

	List<DepartamentoTO> obterDepartamentosFilhos(Long idDepartamento);

	List<DepartamentoTO> obterDepartamentosDescendentes(DepartamentoTO departamento);

	List<DepartamentoTO> obterDepartamentosAscendentes(DepartamentoTO departamento);

	List<DepartamentoTO> obterDepartamentosUsuarioLogado();

}
