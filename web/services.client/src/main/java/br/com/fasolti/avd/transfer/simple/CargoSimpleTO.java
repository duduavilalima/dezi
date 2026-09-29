package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.Situacao;

public class CargoSimpleTO extends BaseTO implements Comparable {

	private String idCargoCliente;

	private String nome;

	private Situacao situacao;

	public String getIdCargoCliente() {
		return idCargoCliente;
	}

	public void setIdCargoCliente(String idCargoCliente) {
		this.idCargoCliente = idCargoCliente;
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

	@Override
	public int compareTo(Object o) {
		return getNome().compareTo(((CargoSimpleTO) o).getNome());
	}
}
