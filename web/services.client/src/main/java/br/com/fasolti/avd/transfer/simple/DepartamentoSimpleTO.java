package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.transfer.util.BaseTO;

public class DepartamentoSimpleTO extends BaseTO {

	private String idDepartamentoCliente;

	private String nome;

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

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
}
