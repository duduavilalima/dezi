package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

public class PDIAcaoCompetenciaDetalhamentoTO extends BaseTO implements SearchObject {

	private PDIAcaoTO pdiAcao;

	private CompetenciaTO competencia;

	private CompetenciaDetalhamentoTO competenciaDetalhamento;

	public PDIAcaoTO getPdiAcao() {
		return pdiAcao;
	}

	public void setPdiAcao(PDIAcaoTO pdiAcao) {
		this.pdiAcao = pdiAcao;
	}

	public CompetenciaTO getCompetencia() {
		return competencia;
	}

	public void setCompetencia(CompetenciaTO competencia) {
		this.competencia = competencia;
	}

	public CompetenciaDetalhamentoTO getCompetenciaDetalhamento() {
		return competenciaDetalhamento;
	}

	public void setCompetenciaDetalhamento(CompetenciaDetalhamentoTO competenciaDetalhamento) {
		this.competenciaDetalhamento = competenciaDetalhamento;
	}

	public PDIAcaoCompetenciaDetalhamentoTO() {}

	public static class Builder {
		private PDIAcaoTO pdiAcao;

		//opcional
		private Long id;
		private CompetenciaTO competencia;
		private CompetenciaDetalhamentoTO competenciaDetalhamento;

		public Builder(
			PDIAcaoTO pdiAcao
		){
			this.pdiAcao = pdiAcao;

		}

		public PDIAcaoCompetenciaDetalhamentoTO.Builder setId(Long id) {
			this.id = id;
			return this;
		}
		public Builder setCompetencia (CompetenciaTO competencia) {
			this.competencia = competencia;
			return this;
		}

		public Builder setCompetenciaDetalhamento (CompetenciaDetalhamentoTO competenciaDetalhamento) {
			this.competenciaDetalhamento = competenciaDetalhamento;
			return this;
		}

		public PDIAcaoCompetenciaDetalhamentoTO build() {return new PDIAcaoCompetenciaDetalhamentoTO(this);}
	}

	private PDIAcaoCompetenciaDetalhamentoTO(Builder builder) {
		setId(builder.id);
		pdiAcao = builder.pdiAcao;
		competencia = builder.competencia;
		competenciaDetalhamento = builder.competenciaDetalhamento;
	}
}
