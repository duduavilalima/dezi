package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.DepartamentoSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.Situacao;

import java.util.List;

public class DepartamentoTreeTO extends BaseTO {

	private String idDepartamentoCliente;

	private String nome;

	private DepartamentoSimpleTO departamentoPai;

	private Situacao situacao;

	private List<DepartamentoTreeTO> children;

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

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public List<DepartamentoTreeTO> getChildren() {
		return children;
	}

	public void setChildren(List<DepartamentoTreeTO> children) {
		this.children = children;
	}
}
