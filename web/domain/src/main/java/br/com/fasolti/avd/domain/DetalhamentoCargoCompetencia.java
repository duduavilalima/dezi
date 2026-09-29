package br.com.fasolti.avd.domain;

import javax.persistence.*;

@Entity
@Table(name = "detalhamento_cargo_competencia")
public class DetalhamentoCargoCompetencia extends BaseAuditedEntity {

	@ManyToOne
	@JoinColumn(name="id_cargo_competencia")
	private CargoCompetencia cargoCompetencia;

	@ManyToOne
	@JoinColumn(name="id_competencia_detalhamento")
	private CompetenciaDetalhamento competenciaDetalhamento;

	@Column(name = "aplicavel_cargo", nullable = false)
	private Boolean aplicavelAoCargo;

	@Column(name = "peso")
	private Integer peso;

	public DetalhamentoCargoCompetencia() {
	}

	public CargoCompetencia getCargoCompetencia() {
		return this.cargoCompetencia;
	}

	public void setCargoCompetencia(CargoCompetencia cargoCompetencia) {
		this.cargoCompetencia = cargoCompetencia;
	}

	public CompetenciaDetalhamento getCompetenciaDetalhamento() {
		return this.competenciaDetalhamento;
	}

	public void setCompetenciaDetalhamento(CompetenciaDetalhamento competenciaDetalhamento) {
		this.competenciaDetalhamento = competenciaDetalhamento;
	}

	public Boolean getAplicavelAoCargo() {
		return aplicavelAoCargo;
	}

	public void setAplicavelAoCargo(Boolean aplicavelAoCargo) {
		this.aplicavelAoCargo = aplicavelAoCargo;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}
}
