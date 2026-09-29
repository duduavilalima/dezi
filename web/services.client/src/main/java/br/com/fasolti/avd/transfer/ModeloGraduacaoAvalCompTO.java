package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

public class ModeloGraduacaoAvalCompTO extends BaseTO implements SearchObject {

	private AvaliacaoCompetenciaTO avaliacaoCompetencia;
	private AvaliacaoCompDetTO avaliacaoCompDet;
	private ModeloGraduacaoCompTO modeloGraduacao;

	public AvaliacaoCompetenciaTO getAvaliacaoCompetencia() {
		return avaliacaoCompetencia;
	}

	public void setAvaliacaoCompetencia(AvaliacaoCompetenciaTO avaliacaoCompetencia) {
		this.avaliacaoCompetencia = avaliacaoCompetencia;
	}

	public ModeloGraduacaoCompTO getModeloGraduacao() {
		return modeloGraduacao;
	}

	public void setModeloGraduacao(ModeloGraduacaoCompTO modeloGraduacao) {
		this.modeloGraduacao = modeloGraduacao;
	}

	public AvaliacaoCompDetTO getAvaliacaoCompDet() {
		return avaliacaoCompDet;
	}

	public void setAvaliacaoCompDet(AvaliacaoCompDetTO avaliacaoCompDet) {
		this.avaliacaoCompDet = avaliacaoCompDet;
	}
}
