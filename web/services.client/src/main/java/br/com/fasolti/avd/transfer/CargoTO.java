package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.DepartamentoSimpleTO;
import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.validation.Validation;

public class CargoTO extends BaseAuditedTO implements Comparable,Cloneable {

	private DepartamentoSimpleTO departamento;

	@Validation(name = "Id Cargo Cliente", max = 20, notNull = true)
	private String idCargoCliente;

	@Validation(name = "Nome", max = 200, notNull = true)
	private String nome;

	@Validation(name = "Empresa", notNull = true)
	private EmpresaSimpleTO empresa;

	@Validation(name = "Situação", notNull = true)
	private Situacao situacao;

	private OcupacaoCBOTO ocupacaoCBO;

	private PerfilCBOTO perfilCBO;

	private CargoTO cargoPai;

	public DepartamentoSimpleTO getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoSimpleTO departamento) {
		this.departamento = departamento;
	}

	public String getIdCargoCliente() {
		return idCargoCliente;
	}

	public void setIdCargoCliente(String idCargoCliente) {
		this.idCargoCliente = idCargoCliente;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
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

	public OcupacaoCBOTO getOcupacaoCBO() {
		return ocupacaoCBO;
	}

	public void setOcupacaoCBO(OcupacaoCBOTO ocupacaoCBO) {
		this.ocupacaoCBO = ocupacaoCBO;
	}

	public PerfilCBOTO getPerfilCBO() {
		return perfilCBO;
	}

	public void setPerfilCBO(PerfilCBOTO perfilCBO) {
		this.perfilCBO = perfilCBO;
	}

	@Override
	public int compareTo(Object o) {
		return getNome().compareTo(((CargoTO) o).getNome());
	}

	@Override
	public CargoTO clone() throws CloneNotSupportedException {
		return (CargoTO) super.clone();
	}

}
