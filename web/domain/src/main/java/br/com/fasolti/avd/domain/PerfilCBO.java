package br.com.fasolti.avd.domain;

import javax.persistence.*;

@Entity
@Table(name = "cbo_perfil")
public class PerfilCBO extends BaseEntity {

	@ManyToOne
	@JoinColumn(name="id_cbo_ocupacao")
	private OcupacaoCBO ocupacao;

	@Column(name = "perfil_cbo")
	private String perfil;

	@Column(name = "descricao_perfil")
	private String descricao;

	public PerfilCBO() {
	}

	public OcupacaoCBO getOcupacao() {
		return ocupacao;
	}

	public void setOcupacao(OcupacaoCBO ocupacao) {
		this.ocupacao = ocupacao;
	}

	public String getPerfil() {
		return perfil;
	}

	public void setPerfil(String perfil) {
		this.perfil = perfil;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
