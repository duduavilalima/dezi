package br.com.fasolti.avd.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Objects;
import java.util.StringJoiner;

@Entity(name = "endereco")
public class Endereco extends BaseEntity {

	// TODO: Vai ser simples no início. Apenas id mesmo. Se no futuro precisar do objeto, só modificar.
	@Column(name = "id_funcionario")
	private Long idFuncionario;
	// TODO: Vai ser simples no início. Apenas id mesmo. Se no futuro precisar do objeto, só modificar.
	@Column(name = "id_municipio")
	private Long idMunicipio;

	public Endereco() { }

	public Long getIdFuncionario() {
		return idFuncionario;
	}

	public void setIdFuncionario(Long idFuncionario) {
		this.idFuncionario = idFuncionario;
	}

	public Long getIdMunicipio() {
		return idMunicipio;
	}

	public void setIdMunicipio(Long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Endereco)) return false;
		if (!super.equals(o)) return false;
		Endereco endereco = (Endereco) o;
		return getIdFuncionario().equals(endereco.getIdFuncionario()) &&
			getIdMunicipio().equals(endereco.getIdMunicipio());
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), getIdFuncionario(), getIdMunicipio());
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", Endereco.class.getSimpleName() + "[", "]")
			.add("idFuncionario=" + idFuncionario)
			.add("idMunicipio=" + idMunicipio)
			.toString();
	}
}
