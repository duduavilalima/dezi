package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.util.TransactionalObject;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Unidade extends BaseAuditedEntity implements TransactionalObject<Situacao>, Comparable  {

	@Column(name = "nome", length = 255, nullable = false)
	private String nome;

	@Column(name = "descricao", nullable = true)
	private String descricao;

	@ManyToOne
	@JoinColumn(name="id_pai", nullable = true)
	private Unidade unidadePai;

	@ManyToOne
	@JoinColumn(name = "id_empresa", nullable = false)
	private Empresa empresa;

	@Column(name = "id_situacao", nullable = false)
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	public String getNome() {
		return nome;
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

	public Unidade getUnidadePai() {
		return unidadePai;
	}

	public void setUnidadePai(Unidade unidadePai) {
		this.unidadePai = unidadePai;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public int compareTo(Object o) {
		return ((Unidade) o).getNome().compareTo(getNome());
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
