package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

public class DepartamentoGestorTO extends BaseAuditedTO implements  SearchObject {

	private FuncionarioTO gestorTO;

	private DepartamentoTO departamentoTO;

	public FuncionarioTO getGestorTO() {
		return gestorTO;
	}

	public void setGestorTO(FuncionarioTO gestorTO) {
		this.gestorTO = gestorTO;
	}

	public DepartamentoTO getDepartamentoTO() {
		return departamentoTO;
	}

	public void setDepartamentoTO(DepartamentoTO departamentoTO) {
		this.departamentoTO = departamentoTO;
	}
}
