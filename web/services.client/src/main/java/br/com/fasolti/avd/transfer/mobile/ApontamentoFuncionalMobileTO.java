package br.com.fasolti.avd.transfer.mobile;


import br.com.fasolti.commons.client.transfer.TransferObject;

import java.util.Date;

public class ApontamentoFuncionalMobileTO implements TransferObject {

	private Long id;
	private Long avaliadorId;
	private Long avaliadoId;
	private Double graduacaoVisual;
	private String registroCritico;
	private String feedforward;
	private CompetenciaMobileTO competencia;
	private Date dataApontamento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAvaliadorId() {
		return avaliadorId;
	}

	public void setAvaliadorId(Long avaliadorId) {
		this.avaliadorId = avaliadorId;
	}

	public Long getAvaliadoId() {
		return avaliadoId;
	}

	public void setAvaliadoId(Long avaliadoId) {
		this.avaliadoId = avaliadoId;
	}

	public Double getGraduacaoVisual() {
		return graduacaoVisual;
	}

	public void setGraduacaoVisual(Double graduacaoVisual) {
		this.graduacaoVisual = graduacaoVisual;
	}

	public String getRegistroCritico() {
		return registroCritico;
	}

	public void setRegistroCritico(String registroCritico) {
		this.registroCritico = registroCritico;
	}

	public String getFeedforward() {
		return feedforward;
	}

	public void setFeedforward(String feedforward) {
		this.feedforward = feedforward;
	}

	public CompetenciaMobileTO getCompetencia() {
		return competencia;
	}

	public void setCompetencia(CompetenciaMobileTO competencia) {
		this.competencia = competencia;
	}

	public Date getDataApontamento() {
		return dataApontamento;
	}

	public void setDataApontamento(Date dataApontamento) {
		this.dataApontamento = dataApontamento;
	}
}
