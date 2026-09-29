package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.transfer.ModeloGraduacaoTO;
import br.com.fasolti.avd.transfer.TipoCompetenciaTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;

public class CompetenciaSimpleTO extends BaseAuditedTO {

	private String descricaoAbreviada;

	private String direcionamentoGestor;

	private String direcionamentoNaoGestor;

	private TipoCompetenciaTO tipo;

	private ModeloGraduacaoTO modeloGraduacao;

	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public String getDirecionamentoGestor() {
		return direcionamentoGestor;
	}

	public void setDirecionamentoGestor(String direcionamentoGestor) {
		this.direcionamentoGestor = direcionamentoGestor;
	}

	public String getDirecionamentoNaoGestor() {
		return direcionamentoNaoGestor;
	}

	public void setDirecionamentoNaoGestor(String direcionamentoNaoGestor) {
		this.direcionamentoNaoGestor = direcionamentoNaoGestor;
	}

	public TipoCompetenciaTO getTipo() {
		return tipo;
	}

	public void setTipo(TipoCompetenciaTO tipo) {
		this.tipo = tipo;
	}

	public ModeloGraduacaoTO getModeloGraduacao() {
		return modeloGraduacao;
	}

	public void setModeloGraduacao(ModeloGraduacaoTO modeloGraduacao) {
		this.modeloGraduacao = modeloGraduacao;
	}
}
