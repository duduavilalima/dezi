package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.transfer.util.SituationalObject;
import br.com.fasolti.avd.types.Situacao;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Cargo extends BaseAuditedEntity implements SituationalObject, Comparable {

	private static final long serialVersionUID = -8102886235664841980L;

	@Column(name = "id_cargo_cliente")
	private String idCargoCliente;

	@Column(name = "nome", length = 200, nullable = false)
	private String nome;

	@ManyToOne
	@JoinColumn(name = "id_cargo_pai")
	private Cargo cargoPai;

	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;

	@ManyToOne
	@JoinColumn(name = "id_departamento")
	private Departamento departamento;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	@ManyToOne
	@JoinColumn(name = "id_cbo_ocupacao")
	private OcupacaoCBO ocupacaoCBO;

	@ManyToOne
	@JoinColumn(name = "id_cbo_perfil")
	private PerfilCBO perfilCBO;

	public Cargo() {
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

	public Cargo getCargoPai() {
		return cargoPai;
	}

	public void setCargoPai(Cargo cargoPai) {
		this.cargoPai = cargoPai;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	@Override
	public Situacao getSituacao() {
		return situacao;
	}

	@Override
	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public OcupacaoCBO getOcupacaoCBO() {
		return ocupacaoCBO;
	}

	public void setOcupacaoCBO(OcupacaoCBO ocupacaoCBO) {
		this.ocupacaoCBO = ocupacaoCBO;
	}

	public PerfilCBO getPerfilCBO() {
		return perfilCBO;
	}

	public void setPerfilCBO(PerfilCBO perfilCBO) {
		this.perfilCBO = perfilCBO;
	}

	@Override
	public int compareTo(Object o) {
		return getNome().compareTo(((Cargo) o).getNome());
	}
}
