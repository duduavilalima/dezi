package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class CargoTipoCompetenciaTO extends BaseAuditedTO implements SearchObject,Cloneable {

	private Integer ordem;

	@Validation(name = "Peso", notNull = true)
	private Integer peso;

	@Validation(name = "Cargo", notNull = true)
	private CargoTO cargo;

	@Validation(name = "Tipo competência", notNull = true)
	private TipoCompetenciaTO tipoCompetencia;

	@Validation(name = "Situação", notNull = true)
	private Situacao situacao;

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public CargoTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoTO cargo) {
		this.cargo = cargo;
	}

	public TipoCompetenciaTO getTipoCompetencia() {
		return tipoCompetencia;
	}

	public void setTipoCompetencia(TipoCompetenciaTO tipoCompetencia) {
		this.tipoCompetencia = tipoCompetencia;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public CargoTipoCompetenciaTO clone() throws CloneNotSupportedException {
		return (CargoTipoCompetenciaTO) super.clone();
	}
}
