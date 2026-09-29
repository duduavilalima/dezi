package br.com.fasolti.avd.domain;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "avaliacao_det_cargo_comp")
public class AvaliacaoDetCargoComp extends BaseAuditedEntity {

	private static final long serialVersionUID = 667120262614108214L;

	@ManyToOne
	@JoinColumn(name = "id_avaliacao_comp_det")
	private AvaliacaoCompDet avaliacaoCompDet;

	@Column(name = "id_det_cargo_comp")
	private Long idDetCargoComp;

	@Column(name = "id_cargo_competencia")
	private Long idCargoCompetencia;

	@Column(name = "id_competencia_detalhamento")
	private Long idCompetenciaDetalhamento;

	@Column(name = "peso_det_cargo_comp")
	private Integer pesoDetCargoComp;

	@Column(name = "peso_cargo_competencia")
	private Long pesoCargoCompetencia;

	@Column(name = "peso_cargo_tipo_competencia")
	private Long pesoCargoTipoCompetencia;

	@Column(name = "aplicavel_cargo")
	private Boolean aplicavelCargo;

	@Column(name = "atualizador_original")
	private String atualizadorOriginal;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_atualizacao_original")
	private Date dataAtualizacaoOriginal;


	public AvaliacaoCompDet getAvaliacaoCompDet() {
		return avaliacaoCompDet;
	}

	public void setAvaliacaoCompDet(AvaliacaoCompDet avaliacaoCompDet) {
		this.avaliacaoCompDet = avaliacaoCompDet;
	}

	public Long getIdDetCargoComp() {
		return idDetCargoComp;
	}

	public void setIdDetCargoComp(Long idDetCargoComp) {
		this.idDetCargoComp = idDetCargoComp;
	}

	public Long getIdCargoCompetencia() {
		return idCargoCompetencia;
	}

	public void setIdCargoCompetencia(Long idCargoCompetencia) {
		this.idCargoCompetencia = idCargoCompetencia;
	}

	public Long getIdCompetenciaDetalhamento() {
		return idCompetenciaDetalhamento;
	}

	public void setIdCompetenciaDetalhamento(Long idCompetenciaDetalhamento) {
		this.idCompetenciaDetalhamento = idCompetenciaDetalhamento;
	}

	public Integer getPesoDetCargoComp() {
		return pesoDetCargoComp;
	}

	public void setPesoDetCargoComp(Integer pesoDetCargoComp) {
		this.pesoDetCargoComp = pesoDetCargoComp;
	}

	public Long getPesoCargoCompetencia() {
		return pesoCargoCompetencia;
	}

	public void setPesoCargoCompetencia(Long pesoCargoCompetencia) {
		this.pesoCargoCompetencia = pesoCargoCompetencia;
	}

	public Boolean getAplicavelCargo() {
		return aplicavelCargo;
	}

	public void setAplicavelCargo(Boolean aplicavelCargo) {
		this.aplicavelCargo = aplicavelCargo;
	}

	public String getAtualizadorOriginal() {
		return atualizadorOriginal;
	}

	public void setAtualizadorOriginal(String atualizadorOriginal) {
		this.atualizadorOriginal = atualizadorOriginal;
	}

	public Date getDataAtualizacaoOriginal() {
		return dataAtualizacaoOriginal;
	}

	public void setDataAtualizacaoOriginal(Date dataAtualizacaoOriginal) {
		this.dataAtualizacaoOriginal = dataAtualizacaoOriginal;
	}

	public void setPesoCargoTipoCompetencia(Long pesoCargoTipoCompetencia) {
		this.pesoCargoTipoCompetencia = pesoCargoTipoCompetencia;
	}

	public Long getPesoCargoTipoCompetencia() {
		return pesoCargoTipoCompetencia;
	}
}
