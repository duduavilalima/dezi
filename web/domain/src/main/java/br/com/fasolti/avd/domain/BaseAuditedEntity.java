package br.com.fasolti.avd.domain;

import br.com.fasolti.commons.domain.AuditedObject;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@MappedSuperclass
public class BaseAuditedEntity extends BaseEntity implements AuditedObject {

	private static final long serialVersionUID = -4417520154865236663L;

	@Column(nullable = false, updatable = false)
	protected String criador;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao", nullable = false, updatable = false)
	protected Date dataCriacao;

	@Column(nullable = true)
	protected String atualizador;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_atualizacao", nullable = true)
	protected Date dataAtualizacao;

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
