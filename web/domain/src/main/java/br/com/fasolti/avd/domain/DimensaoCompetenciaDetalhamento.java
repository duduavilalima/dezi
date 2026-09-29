package br.com.fasolti.avd.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DimensaoCompetenciaDetalhamento extends BaseEntity {

	private static final long serialVersionUID = -3847574027838590977L;

	@ManyToOne
	@JoinColumn(name = "id_dimensao")
	private Dimensao dimensao;

	@ManyToOne
	@JoinColumn(name = "id_competencia_detalhamento")
	private CompetenciaDetalhamento competenciaDetalhamento;

	public Dimensao getDimensao() {
		return dimensao;
	}

	public void setDimensao(Dimensao dimensao) {
		this.dimensao = dimensao;
	}

	public CompetenciaDetalhamento getCompetenciaDetalhamento() {
		return competenciaDetalhamento;
	}

	public void setCompetenciaDetalhamento(CompetenciaDetalhamento competenciaDetalhamento) {
		this.competenciaDetalhamento = competenciaDetalhamento;
	}
}
