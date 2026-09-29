package br.com.fasolti.avd.transfer.mobile;

import br.com.fasolti.commons.client.transfer.TransferObject;

import java.util.Date;

public class ApontamentoMobileTO implements TransferObject {

	private Long id;
	private Long avaliadorId;
	private Long avaliadoId;
	private Long competenciaId;

	private Double graduacaoVisual;
	private String registroCritico;
	private Date dataApontamento;

	private FuncionarioMobileTO avaliado;
	private FuncionarioMobileTO avaliador;
	private CompetenciaMobileTO competencia;

	@Override
	public Long getId() {
		return id;
	}

	@Override
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

	public Long getCompetenciaId() {
		return competenciaId;
	}

	public void setCompetenciaId(Long competenciaId) {
		this.competenciaId = competenciaId;
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

	public Date getDataApontamento() {
		return dataApontamento;
	}

	public void setDataApontamento(Date dataApontamento) {
		this.dataApontamento = dataApontamento;
	}

	public FuncionarioMobileTO getAvaliado() {
		return avaliado;
	}

	public void setAvaliado(FuncionarioMobileTO avaliado) {
		this.avaliado = avaliado;
	}

	public FuncionarioMobileTO getAvaliador() {
		return avaliador;
	}

	public void setAvaliador(FuncionarioMobileTO avaliador) {
		this.avaliador = avaliador;
	}

	public CompetenciaMobileTO getCompetencia() {
		return competencia;
	}

	public void setCompetencia(CompetenciaMobileTO competencia) {
		this.competencia = competencia;
	}
}
