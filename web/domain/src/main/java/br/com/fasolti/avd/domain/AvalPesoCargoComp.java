package br.com.fasolti.avd.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "aval_peso_cargo_comp")
public class AvalPesoCargoComp extends BaseEntity implements Serializable {

	@Column(name = "peso_cargo_competencia")
	private Long pesoCargoCompetencia;

	@Column(name = "peso_cargo_tipo_competencia")
	private Long pesoCargoTipoCompetencia;

	@Column(name = "id_cargo")
	private Long idCargo;

	@Column(name = "id_avaliacao")
	private Long idAvaliacao;

	@Column(name = "id_aval_competencia")
	private Long idAvalCompetencia;

	@Column(name = "id_preenchimento")
	private Long idPreenchimento;

	public Long getPesoCargoCompetencia() {
		return pesoCargoCompetencia;
	}

	public void setPesoCargoCompetencia(Long pesoCargoCompetencia) {
		this.pesoCargoCompetencia = pesoCargoCompetencia;
	}

	public Long getPesoCargoTipoCompetencia() {
		return pesoCargoTipoCompetencia;
	}

	public void setPesoCargoTipoCompetencia(Long pesoCargoTipoCompetencia) {
		this.pesoCargoTipoCompetencia = pesoCargoTipoCompetencia;
	}

	public Long getIdCargo() {
		return idCargo;
	}

	public void setIdCargo(Long idCargo) {
		this.idCargo = idCargo;
	}

	public Long getIdAvaliacao() {
		return idAvaliacao;
	}

	public void setIdAvaliacao(Long idAvaliacao) {
		this.idAvaliacao = idAvaliacao;
	}

	public Long getIdAvalCompetencia() {
		return idAvalCompetencia;
	}

	public void setIdAvalCompetencia(Long idAvalCompetencia) {
		this.idAvalCompetencia = idAvalCompetencia;
	}

	public Long getIdPreenchimento() {
		return idPreenchimento;
	}

	public void setIdPreenchimento(Long idPreenchimento) {
		this.idPreenchimento = idPreenchimento;
	}
}
