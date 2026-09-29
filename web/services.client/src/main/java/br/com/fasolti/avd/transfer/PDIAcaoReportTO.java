package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;

import java.util.Date;

public class PDIAcaoReportTO extends BaseTO {

	private String acaoParaDesenvolvimento;

	private String dataInicio;

	private String dataFim;

	private String observacoes;

	private String situacao;

	private String competencia;

	private String detalhamento;

	public String getAcaoParaDesenvolvimento() {
		return acaoParaDesenvolvimento;
	}

	public void setAcaoParaDesenvolvimento(String acaoParaDesenvolvimento) {
		this.acaoParaDesenvolvimento = acaoParaDesenvolvimento;
	}

	public String getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(String dataInicio) {
		this.dataInicio = dataInicio;
	}

	public String getDataFim() {
		return dataFim;
	}

	public void setDataFim(String dataFim) {
		this.dataFim = dataFim;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public String getDetalhamento() {
		return detalhamento;
	}

	public void setDetalhamento(String detalhamento) {
		this.detalhamento = detalhamento;
	}

}
