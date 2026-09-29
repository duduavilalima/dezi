package br.com.fasolti.avd.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;
import java.util.StringJoiner;

@Entity(name = "municipios")
public class Municipio extends BaseEntity {

	@Column(name = "nome", length = 50, nullable = false, updatable = false, insertable = false)
	private String nome;
	@Column(name = "uf", length = 2, nullable = false, updatable = false, insertable = false)
	private String uf;
	@Column(name = "estado", length = 35, nullable = false, updatable = false, insertable = false)
	private String estado;

	public Municipio() {
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Municipio)) return false;
		if (!super.equals(o)) return false;
		Municipio municipio = (Municipio) o;
		return getNome().equals(municipio.getNome()) &&
			getUf().equals(municipio.getUf());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getNome(), getUf());
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Municipio.class.getSimpleName() + "[", "]")
			.add("nome='" + nome + "'")
			.add("uf='" + uf + "'")
			.toString();
	}
}
