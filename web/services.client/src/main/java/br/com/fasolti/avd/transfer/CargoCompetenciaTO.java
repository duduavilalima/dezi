package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class CargoCompetenciaTO extends BaseAuditedTO implements SearchObject,Cloneable {

	private Integer ordem;

	@Validation(name = "Peso", notNull = true)
	private Long peso;

	@Validation(name = "Cargo", notNull = true)
	private CargoTO cargo;

	@Validation(name = "Competência", notNull = true)
	private CompetenciaTO competencia;

	@Validation(name = "Situação", notNull = true)
	private Situacao situacao;

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

	public CargoTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoTO cargo) {
		this.cargo = cargo;
	}

	public CompetenciaTO getCompetencia() {
		return competencia;
	}

	public void setCompetencia(CompetenciaTO competencia) {
		this.competencia = competencia;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public CargoCompetenciaTO clone() throws CloneNotSupportedException {
		return (CargoCompetenciaTO) super.clone();
	}
}
