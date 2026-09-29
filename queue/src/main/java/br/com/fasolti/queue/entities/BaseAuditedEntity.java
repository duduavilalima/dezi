package br.com.fasolti.queue.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class BaseAuditedEntity extends BaseEntity {

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

	public String getCreatedBy() {
		return criador;
	}

	public void setCreatedBy(String name) {
		this.criador = name;
	}

	public Date getCreationDate() {
		return dataCriacao;
	}

	public void setCreationDate(Date date) {
		this.dataCriacao = date;
	}

	public String getModifiedBy() {
		return atualizador;
	}

	public void setModifiedBy(String name) {
		this.atualizador = name;
	}

	public Date getModificationDate() {
		return dataAtualizacao;
	}

	public void setModificationDate(Date date) {
		this.dataAtualizacao = date;
	}

	@PreUpdate
	public void preUpdate() {
		dataAtualizacao = new Date();
	}

	@PrePersist
	public void prePersist() {
		final Date atual = new Date();
		dataCriacao = atual;
		dataAtualizacao = atual;
	}

}
