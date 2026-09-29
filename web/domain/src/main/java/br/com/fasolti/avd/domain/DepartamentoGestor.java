package br.com.fasolti.avd.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class DepartamentoGestor extends BaseAuditedEntity  {

	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "id_departamento")
	private Departamento departamento;

	@ManyToOne
	@JoinColumn(name = "id_gestor")
	private Funcionario funcionario;

	public DepartamentoGestor() {
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	@Override
	public String toString() {
		return "DepartamentoGestor{" +
				"departamento=" + departamento.getId() + " - " + departamento.getNome() +
				", funcionario=" + funcionario.getId() + " - " + funcionario.getNome() +
				"} " + super.toString();
	}
}
