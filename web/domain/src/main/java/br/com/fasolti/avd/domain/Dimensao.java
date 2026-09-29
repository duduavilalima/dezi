package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Dimensao extends BaseEntity {

	private static final long serialVersionUID = -3847574027838590977L;

	private String nome;

	private String descricao;

	@Column(name = "situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}
}
