package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;

public class AvaliacaoCompDetHintTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "avaliação competencia detalhamento", notNull = true)
	private AvaliacaoCompDetTO avaliacaoCompDet;
	private Long idHint;
	private Long idGraduacao;
	private String hint;
	private Boolean comentarioObrigatorio;
	private String atualizadorOriginal;
	private Date dataAtualizacaoOriginal;
	private GraduacaoCompetenciaTO graduacao;

	public AvaliacaoCompDetTO getAvaliacaoCompDet() {
		return avaliacaoCompDet;
	}

	public void setAvaliacaoCompDet(AvaliacaoCompDetTO avaliacaoCompDet) {
		this.avaliacaoCompDet = avaliacaoCompDet;
	}

	public Long getIdHint() {
		return idHint;
	}

	public void setIdHint(Long idHint) {
		this.idHint = idHint;
	}

	public Long getIdGraduacao() {
		return idGraduacao;
	}

	public void setIdGraduacao(Long idGraduacao) {
		this.idGraduacao = idGraduacao;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public Boolean getComentarioObrigatorio() {
		return comentarioObrigatorio;
	}

	public void setComentarioObrigatorio(Boolean comentarioObrigatorio) {
		this.comentarioObrigatorio = comentarioObrigatorio;
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

	public GraduacaoCompetenciaTO getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(GraduacaoCompetenciaTO graduacao) {
		this.graduacao = graduacao;
	}
}
