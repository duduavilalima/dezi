package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "modelo_graduacao")
public class ModeloGraduacao extends BaseAuditedEntity {

	private static final long serialVersionUID = 5123322487048115745L;

	@Column(name = "descricao_modelo")
	private String descricaoModelo;

	@Column(name = "qtd_graduacoes")
	private Integer qtdGraduacoes;

	@Column(name = "nao_graduar")
	private Boolean naoGraduar;

	@ManyToOne
	@JoinColumn(name = "id_dimensao")
	private Dimensao dimensao;

	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;


	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	public ModeloGraduacao() {
	}

	public String getDescricaoModelo() {
		return descricaoModelo;
	}

	public void setDescricaoModelo(String descricaoModelo) {
		this.descricaoModelo = descricaoModelo;
	}

	public Integer getQtdGraduacoes() {
		return qtdGraduacoes;
	}

	public void setQtdGraduacoes(Integer qtdGraduacoes) {
		this.qtdGraduacoes = qtdGraduacoes;
	}

	public Dimensao getDimensao() {
		return dimensao;
	}

	public void setDimensao(Dimensao dimensao) {
		this.dimensao = dimensao;
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

	public Boolean getNaoGraduar() {
		return naoGraduar;
	}

	public void setNaoGraduar(Boolean naoGraduar) {
		this.naoGraduar = naoGraduar;
	}
}
