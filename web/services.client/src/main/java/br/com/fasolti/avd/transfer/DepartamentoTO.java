package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.DepartamentoSimpleTO;
import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;
import br.com.fasolti.commons.util.TransactionalObject;

public class DepartamentoTO extends BaseAuditedTO implements TransactionalObject<Situacao>, SearchObject {

	@Validation(name = "Id do departemanto cliente", notNull = true)
	private String idDepartamentoCliente;

	@Validation(name = "Nome", max = 50, notNull = true)
	private String nome;

	private DepartamentoSimpleTO departamentoPai;

	@Validation(name = "Empresa", notNull = true)
	private EmpresaSimpleTO empresa;

	@Validation(name = "Situação", notNull = true)
	private Situacao situacao;

	public String getIdDepartamentoCliente() {
		return idDepartamentoCliente;
	}

	public void setIdDepartamentoCliente(String idDepartamentoCliente) {
		this.idDepartamentoCliente = idDepartamentoCliente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public DepartamentoSimpleTO getDepartamentoPai() {
		return departamentoPai;
	}

	public void setDepartamentoPai(DepartamentoSimpleTO departamentoPai) {
		this.departamentoPai = departamentoPai;
	}

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public Situacao getStatus() {
		return situacao;
	}

	@Override
	public void setStatus(Situacao situacao) {
		this.situacao = situacao;
	}
}
