package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;

public class AvaliacaoDetCargoCompTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "avaliação competencia detalhamento", notNull = true)
	private AvaliacaoCompDetTO avaliacaoCompDet;
	private Long idDetCargoComp;
	private Long idCargoCompetencia;
	private Long idCompetenciaDetalhamento;
	private Long idGraduacao;
	private Integer pesoDetCargoComp;
	private Integer pesoCargoCompetencia;
	private Integer pesoCargoTipoCompetencia;
	private Boolean aplicavelCargo;
	private String atualizadorOriginal;
	private Date dataAtualizacaoOriginal;

	public AvaliacaoCompDetTO getAvaliacaoCompDet() {
		return avaliacaoCompDet;
	}

	public void setAvaliacaoCompDet(AvaliacaoCompDetTO avaliacaoCompDet) {
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

	public Long getIdGraduacao() {
		return idGraduacao;
	}

	public void setIdGraduacao(Long idGraduacao) {
		this.idGraduacao = idGraduacao;
	}

	public Integer getPesoDetCargoComp() {
		return pesoDetCargoComp;
	}

	public void setPesoDetCargoComp(Integer pesoDetCargoComp) {
		this.pesoDetCargoComp = pesoDetCargoComp;
	}

	public Integer getPesoCargoCompetencia() {
		return pesoCargoCompetencia;
	}

	public void setPesoCargoCompetencia(Integer pesoCargoCompetencia) {
		this.pesoCargoCompetencia = pesoCargoCompetencia;
	}

	public Integer getPesoCargoTipoCompetencia() {
		return pesoCargoTipoCompetencia;
	}

	public void setPesoCargoTipoCompetencia(Integer pesoCargoTipoCompetencia) {
		this.pesoCargoTipoCompetencia = pesoCargoTipoCompetencia;
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
}
