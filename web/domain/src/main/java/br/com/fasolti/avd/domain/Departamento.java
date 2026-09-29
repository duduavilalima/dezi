package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.util.TransactionalObject;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
public class Departamento extends BaseAuditedEntity implements TransactionalObject<Situacao> {

	private static final long serialVersionUID = -3770838573443894220L;

	@Column(name = "id_departamento_cliente", length = 10, nullable = false)
	private String idDepartamentoCliente;

	@Column(name = "nome", length = 50, nullable = false)
	private String nome;

	@ManyToOne
	@JoinColumn(name = "id_pai", nullable = true)
	private Departamento departamentoPai;

	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	public Departamento() {
	}

	public String getIdDepartamentoCliente() {
		return idDepartamentoCliente;
	}

	public void setIdDepartamentoCliente(String idDepartamentoCliente) {
		this.idDepartamentoCliente = idDepartamentoCliente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Departamento getDepartamentoPai() {
		return departamentoPai;
	}

	public void setDepartamentoPai(Departamento departamentoPai) {
		this.departamentoPai = departamentoPai;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public Situacao getStatus() {
		return situacao;
	}

	@Override
	public void setStatus(Situacao situacao) {
		this.situacao = situacao;
	}
}
