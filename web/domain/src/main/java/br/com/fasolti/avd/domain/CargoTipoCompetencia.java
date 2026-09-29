package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "cargo_tipo_competencia")
public class CargoTipoCompetencia extends BaseAuditedEntity {

	private Integer ordem;

	@Column(name = "peso", nullable = false)
	private Long peso;

	@ManyToOne
	@JoinColumn(name = "id_cargo")
	private Cargo cargo;

	@ManyToOne
	@JoinColumn(name = "id_tipo_competencia")
	private TipoCompetencia tipoCompetencia;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	public CargoTipoCompetencia() {
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Long getPeso() {
		return peso;
	}

	public void setPeso(Long peso) {
		this.peso = peso;
	}

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public TipoCompetencia getTipoCompetencia() {
		return tipoCompetencia;
	}

	public void setTipoCompetencia(TipoCompetencia tipoCompetencia) {
		this.tipoCompetencia = tipoCompetencia;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
}
