package br.com.fasolti.avd.transfer.util;

import br.com.fasolti.commons.domain.AuditedObject;

import java.util.Date;

public abstract class BaseAuditedTO extends BaseTO implements AuditedObject {

	// FIXME Padronizar com os nomes da biblioteca

	private String criador;

	private Date dataCriacao;

	private String atualizador;

	private Date dataAtualizacao;

	public final String getCriador() {
		return criador;
	}

	public final void setCriador(String criador) {
		this.criador = criador;
	}

	public final Date getDataCriacao() {
		return dataCriacao;
	}

	public final void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public final String getAtualizador() {
		return atualizador;
	}

	public final void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	public final Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public final void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@Override
	public String getCreatedBy() {
		return criador;
	}

	@Override
	public void setCreatedBy(String name) {
		this.criador = name;
	}

	@Override
	public Date getCreationDate() {
		return dataCriacao;
	}

	@Override
	public void setCreationDate(Date date) {
		this.dataCriacao = date;
	}

	@Override
	public String getModifiedBy() {
		return atualizador;
	}

	@Override
	public void setModifiedBy(String name) {
		this.atualizador = name;
	}

	@Override
	public Date getModificationDate() {
		return dataAtualizacao;
	}

	@Override
	public void setModificationDate(Date date) {
		this.dataAtualizacao = date;
	}
}
