package br.com.fasolti.avd.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class AvaliacaoDimensao extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "id_dimensao")
	private Dimensao dimensao;

	@ManyToOne
	@JoinColumn(name = "id_avaliacao")
	private Avaliacao avaliacao;

	public Dimensao getDimensao() {
		return dimensao;
	}

	public void setDimensao(Dimensao dimensao) {
		this.dimensao = dimensao;
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}
}
