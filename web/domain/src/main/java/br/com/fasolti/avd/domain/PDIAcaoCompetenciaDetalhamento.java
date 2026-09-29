package br.com.fasolti.avd.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="pdi_acao_competencia_detalhamento")
public class PDIAcaoCompetenciaDetalhamento extends BaseEntity {

	public PDIAcaoCompetenciaDetalhamento() {}

	@Transient
	public boolean isNew() {
		return null == this.getId();
	}

	@ManyToOne
	@JoinColumn(name = "id_pdi_acao")
	private PDIAcao pdiAcao;

	@ManyToOne
	@JoinColumn(name = "id_competencia")
	private Competencia competencia;

	@ManyToOne
	@JoinColumn(name = "id_competencia_detalhamento")
	private CompetenciaDetalhamento competenciaDetalhamento;

	public static class Builder {
		private PDIAcao pdiAcao;
		private Competencia competencia;

		//opcional
		private Long id;
		private CompetenciaDetalhamento competenciaDetalhamento;

		public Builder(
			PDIAcao pdiAcao,
			Competencia competencia
		){
			this.pdiAcao = pdiAcao;
			this.competencia = competencia;

		}

		public PDIAcaoCompetenciaDetalhamento.Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setCompetenciaDetalhamento (CompetenciaDetalhamento competenciaDetalhamento) {
			this.competenciaDetalhamento = competenciaDetalhamento;
			return this;
		}

		public PDIAcaoCompetenciaDetalhamento build() {return new PDIAcaoCompetenciaDetalhamento(this);}
	}

	private PDIAcaoCompetenciaDetalhamento(Builder builder) {
		setId(builder.id);
		pdiAcao = builder.pdiAcao;
		competencia = builder.competencia;
		competenciaDetalhamento = builder.competenciaDetalhamento;
	}

	public PDIAcao getPdiAcao() {
		return pdiAcao;
	}

	public void setPdiAcao(PDIAcao pdiAcao) {
		this.pdiAcao = pdiAcao;
	}

	public Competencia getCompetencia() {
		return competencia;
	}

	public void setCompetencia(Competencia competencia) {
		this.competencia = competencia;
	}

	public CompetenciaDetalhamento getCompetenciaDetalhamento() {
		return competenciaDetalhamento;
	}

	public void setCompetenciaDetalhamento(CompetenciaDetalhamento competenciaDetalhamento) {
		this.competenciaDetalhamento = competenciaDetalhamento;
	}

	@Override
	public String toString() {
		return "PDIAcaoCompetenciaDetalhamento{" +
			"pdiAcao=" + pdiAcao +
			", competencia=" + competencia +
			", competenciaDetalhamento=" + competenciaDetalhamento +
			'}';
	}
}
