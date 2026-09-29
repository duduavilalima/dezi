package br.com.fasolti.avd.domain;

import javax.persistence.*;

@Entity
@Table(name = "cbo_atividade")
public class AtividadeCBO extends BaseEntity {

	@ManyToOne
	@JoinColumn(name="id_cbo_perfil")
	private PerfilCBO perfil;

	@Column(name = "ordem")
	private Integer ordem;

	@Column(name = "descricao_atividade")
	private String descricao;

	public AtividadeCBO() {
	}

	public PerfilCBO getPerfil() {
		return perfil;
	}

	public void setPerfil(PerfilCBO perfil) {
		this.perfil = perfil;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
