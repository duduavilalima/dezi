package br.com.fasolti.avd.domain;

import javax.persistence.Column;
import javax.persistence.Entity;


@Entity(name = "aval_dim_comp_det")
public class AvalDimComDet extends BaseEntity {

	@Column(name = "id_avaliacao")
	private Long idAvaliacao;

	@Column(name = "id_dimensao")
	private Long idDimensao;

	@Column(name = "id_comp_det")
	private Long idCompDet;

	@Column(name = "dimensao_nome")
	private String dimensaoNome;

	@Column(name = "dimensao_descricao")
	private String dimensaoDescricao;

	public Long getIdAvaliacao() {
		return idAvaliacao;
	}

	public void setIdAvaliacao(Long idAvaliacao) {
		this.idAvaliacao = idAvaliacao;
	}

	public Long getIdDimensao() {
		return idDimensao;
	}

	public void setIdDimensao(Long idDimensao) {
		this.idDimensao = idDimensao;
	}

	public Long getIdCompDet() {
		return idCompDet;
	}

	public void setIdCompDet(Long idCompDet) {
		this.idCompDet = idCompDet;
	}

	public String getDimensaoNome() {
		return dimensaoNome;
	}

	public void setDimensaoNome(String dimensaoNome) {
		this.dimensaoNome = dimensaoNome;
	}

	public String getDimensaoDescricao() {
		return dimensaoDescricao;
	}

	public void setDimensaoDescricao(String dimensaoDescricao) {
		this.dimensaoDescricao = dimensaoDescricao;
	}
}
