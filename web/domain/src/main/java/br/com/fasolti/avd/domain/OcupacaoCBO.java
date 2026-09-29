package br.com.fasolti.avd.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cbo_ocupacao")
public class OcupacaoCBO extends BaseEntity {

	@Column(name = "codigo_cbo")
	private String codigo;

	@Column(name = "descricao_ocupacao")
	private String descricao;

	public OcupacaoCBO() {
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
