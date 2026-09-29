package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

public class UnidadeDepartamentoTO extends BaseTO implements SearchObject {

	private UnidadeTO unidade;
	private DepartamentoTO departamento;
	private FuncionarioTO gestor;
	private Boolean visualizarPorUnidade;

	public UnidadeDepartamentoTO() {
	}

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
	}

	public DepartamentoTO getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoTO departamento) {
		this.departamento = departamento;
	}

	public FuncionarioTO getGestor() {
		return gestor;
	}

	public void setGestor(FuncionarioTO gestor) {
		this.gestor = gestor;
	}

	public Boolean getVisualizarPorUnidade() {
		return visualizarPorUnidade;
	}

	public void setVisualizarPorUnidade(Boolean visualizarPorUnidade) {
		this.visualizarPorUnidade = visualizarPorUnidade;
	}
}
