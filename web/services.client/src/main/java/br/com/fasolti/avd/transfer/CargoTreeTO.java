package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.DepartamentoSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.Situacao;

import java.util.List;

public class CargoTreeTO extends BaseTO {

	private String idCargoCliente;

	private String nome;

	private String name;

	private Situacao situacao;

	private CargoTO cargoPai;

	private DepartamentoSimpleTO departamento;

	private List<CargoTreeTO> children;

	public String getIdCargoCliente() {
		return idCargoCliente;
	}

	public void setIdCargoCliente(String idCargoCliente) {
		this.idCargoCliente = idCargoCliente;
	}

	public String getNome() {
		return nome;
	}

	public String getName() {
		return this.nome;
	}

	public void setName(String name) {
		this.nome = name;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public CargoTO getCargoPai() {
		return cargoPai;
	}

	public void setCargoPai(CargoTO cargoPai) {
		this.cargoPai = cargoPai;
	}

	public DepartamentoSimpleTO getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoSimpleTO departamento) {
		this.departamento = departamento;
	}

	public List<CargoTreeTO> getChildren() {
		return children;
	}

	public void setChildren(List<CargoTreeTO> children) {
		this.children = children;
	}
}
