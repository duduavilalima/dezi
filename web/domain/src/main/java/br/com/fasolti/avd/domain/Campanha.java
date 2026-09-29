package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.util.TransactionalObject;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Campanha extends BaseAuditedEntity implements TransactionalObject<Situacao> {

	private static final long serialVersionUID = 5311567188880162845L;

	@Column(name = "descricao", length = 100, nullable = false)
	private String descricao;

	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	@Column(name = "dimensao")
	private Boolean dimensao;

	@Column(name = "av_em_liberacao")
	private Boolean avEmLiberacao;

	public Campanha() {
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Boolean getDimensao() {
		return dimensao;
	}

	public void setDimensao(Boolean dimensao) {
		this.dimensao = dimensao;
	}

	@Override
	public Situacao getStatus() {
		return situacao;
	}

	@Override
	public void setStatus(Situacao status) {
		this.situacao = status;
	}

	public Boolean getAvEmLiberacao() {
		return avEmLiberacao;
	}

	public void setAvEmLiberacao(Boolean avEmLiberacao) {
		this.avEmLiberacao = avEmLiberacao;
	}
}
