package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;

public class GraduacaoCompetenciaTO  extends BaseAuditedTO implements SearchObject {

	@Validation(name = "avaliação competencia", notNull = true)
	private ModeloGraduacaoCompTO modeloGraduacao;
	private Long idGraduacao;
	private Integer ordem;
	private String label;
	private String hint;
	private Integer ponto;
	private String atualizadorOriginal;
	private Date dataAtualizacaoOriginal;
	private String cor;


	public ModeloGraduacaoCompTO getModeloGraduacao() {
		return modeloGraduacao;
	}

	public void setModeloGraduacao(ModeloGraduacaoCompTO modeloGraduacao) {
		this.modeloGraduacao = modeloGraduacao;
	}

	public Long getIdGraduacao() {
		return idGraduacao;
	}

	public void setIdGraduacao(Long idGraduacao) {
		this.idGraduacao = idGraduacao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public Integer getPonto() {
		return ponto;
	}

	public void setPonto(Integer ponto) {
		this.ponto = ponto;
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

	public String getCor() {
		return cor;
	}

	public void setCor(String cor) {
		this.cor = cor;
	}
}
