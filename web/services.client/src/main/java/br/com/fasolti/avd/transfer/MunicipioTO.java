package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;
import java.util.StringJoiner;

public class MunicipioTO extends BaseTO {

	private String nome;
	private String uf;
	private String estado;

	public MunicipioTO() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", MunicipioTO.class.getSimpleName() + "[", "]")
			.add("nome='" + nome + "'")
			.add("uf='" + uf + "'")
			.toString();
	}
}
