package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

public class DetalhamentoCargoCompetenciaTO extends BaseAuditedTO implements SearchObject, Cloneable {

	private CargoCompetenciaTO cargoCompetencia;

	private CompetenciaDetalhamentoTO competenciaDetalhamento;

	private Boolean aplicavelAoCargo;

	private Integer peso;

	//TODO: Criar situacao

	public DetalhamentoCargoCompetenciaTO() {
	}

	public CargoCompetenciaTO getCargoCompetencia() {
		return cargoCompetencia;
	}

	public void setCargoCompetencia(CargoCompetenciaTO cargoCompetencia) {
		this.cargoCompetencia = cargoCompetencia;
	}

	public CompetenciaDetalhamentoTO getCompetenciaDetalhamento() {
		return competenciaDetalhamento;
	}

	public void setCompetenciaDetalhamento(CompetenciaDetalhamentoTO competenciaDetalhamento) {
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

	@Override
	public DetalhamentoCargoCompetenciaTO clone() throws CloneNotSupportedException {
		return (DetalhamentoCargoCompetenciaTO) super.clone();
	}

}
