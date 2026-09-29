package br.com.fasolti.avd.transfer.mobile;

import br.com.fasolti.commons.client.transfer.TransferObject;

import java.util.Date;

public class ParticipanteMobileTO implements TransferObject {
	private Long id;
	private Long avaliacaoId;
	private Long avaliadorId;
	private String nomeAvaliador;
	private Long avaliadoId;
	private String nomeAvaliado;
	private String fotoAvaliado;
//	private FuncionarioMobileSimpleTO avaliado;
	private Boolean preenchidaAV1;
	private Boolean preenchidaAV2;
	private Date dataInicioAv1;
	private Date dataInicioAv2;
	private Date limiteAv1;
	private Date limiteAv2;
	private Boolean consensoLiberado;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAvaliacaoId() {
		return avaliacaoId;
	}

	public void setAvaliacaoId(Long avaliacaoId) {
		this.avaliacaoId = avaliacaoId;
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

	public String getNomeAvaliado() {
		return nomeAvaliado;
	}

	public void setNomeAvaliado(String nomeAvaliado) {
		this.nomeAvaliado = nomeAvaliado;
	}

	public String getNomeAvaliador() {
		return nomeAvaliador;
	}

	public void setNomeAvaliador(String nomeAvaliador) {
		this.nomeAvaliador = nomeAvaliador;
	}

	public String getFotoAvaliado() {
		return fotoAvaliado;
	}

	public void setFotoAvaliado(String fotoAvaliado) {
		this.fotoAvaliado = fotoAvaliado;
	}

	public Boolean getPreenchidaAV1() {
		return preenchidaAV1;
	}

	public void setPreenchidaAV1(Boolean preenchidaAV1) {
		this.preenchidaAV1 = preenchidaAV1;
	}

	public Boolean getPreenchidaAV2() {
		return preenchidaAV2;
	}

	public void setPreenchidaAV2(Boolean preenchidaAV2) {
		this.preenchidaAV2 = preenchidaAV2;
	}

	public Date getDataInicioAv1() {
		return dataInicioAv1;
	}

	public void setDataInicioAv1(Date dataInicioAv1) {
		this.dataInicioAv1 = dataInicioAv1;
	}

	public Date getDataInicioAv2() {
		return dataInicioAv2;
	}

	public void setDataInicioAv2(Date dataInicioAv2) {
		this.dataInicioAv2 = dataInicioAv2;
	}

	public Date getLimiteAv1() {
		return limiteAv1;
	}

	public void setLimiteAv1(Date limiteAv1) {
		this.limiteAv1 = limiteAv1;
	}

	public Date getLimiteAv2() {
		return limiteAv2;
	}

	public void setLimiteAv2(Date limiteAv2) {
		this.limiteAv2 = limiteAv2;
	}

	public Boolean getConsensoLiberado() {
		return consensoLiberado;
	}

	public void setConsensoLiberado(Boolean consensoLiberado) {
		this.consensoLiberado = consensoLiberado;
	}
}
