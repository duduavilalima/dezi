package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name="contratante_fasolti")
public class ContratanteFasolti extends BaseEntity {

	private static final long serialVersionUID = 6272395282686897417L;

	@Column(name = "nome", length = 40, nullable = false)
	private String nome;

	@Column(name="id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	public ContratanteFasolti() {
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
