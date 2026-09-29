package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class DimensaoCompetenciaDetalhamentoTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "Dimensao")
	private DimensaoTO dimensao;

	@Validation(name = "Competencia Detalhamento")
	private CompetenciaDetalhamentoTO competenciaDetalhamento;

	public DimensaoTO getDimensao() {
		return dimensao;
	}

	public void setDimensao(DimensaoTO dimensao) {
		this.dimensao = dimensao;
	}

	public CompetenciaDetalhamentoTO getCompetenciaDetalhamento() {
		return competenciaDetalhamento;
	}

	public void setCompetenciaDetalhamento(CompetenciaDetalhamentoTO competenciaDetalhamento) {
		this.competenciaDetalhamento = competenciaDetalhamento;
	}
}
