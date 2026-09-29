package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Idioma extends BaseAuditedEntity {

	private static final long serialVersionUID = -1129098797774903585L;

	private String nome;

	@ManyToOne
	@JoinColumn(name = "id_contratante_fasolti")
	private ContratanteFasolti contratante;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	public Idioma() {
	}

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public ContratanteFasolti getContratante() {
		return this.contratante;
	}

	public void setContratante(ContratanteFasolti contratante) {
		this.contratante = contratante;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
}
