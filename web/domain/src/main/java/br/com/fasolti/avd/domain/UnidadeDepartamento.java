package br.com.fasolti.avd.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "unidade_departamento")
public class UnidadeDepartamento extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "id_unidade", nullable = false)
	private Unidade unidade;

	@ManyToOne
	@JoinColumn(name = "id_departamento", nullable = false)
	private Departamento departamento;

	@ManyToOne
	@JoinColumn(name = "id_gestor", nullable = false)
	private Funcionario gestor;

	public UnidadeDepartamento() {
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Funcionario getGestor() {
		return gestor;
	}

	public void setGestor(Funcionario gestor) {
		this.gestor = gestor;
	}

}
