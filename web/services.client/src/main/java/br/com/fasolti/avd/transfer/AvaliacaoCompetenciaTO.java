package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;

public class AvaliacaoCompetenciaTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "avaliação", notNull = true)
	private AvaliacaoTO avaliacao;
	private Long idCompetencia;
	private Long idTipoCompetencia;
	private String descricaoTipoCompetencia;
	private String descricaoAbreviada;
	private String direcionamentoGestor;
	private String direcionamentoNaoGestor;
	private String atualizadorOriginal;
	private Date dataAtualizacaoOriginal;

	public AvaliacaoTO getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoTO avaliacao) {
		this.avaliacao = avaliacao;
	}

	public Long getIdCompetencia() {
		return idCompetencia;
	}

	public void setIdCompetencia(Long idCompetencia) {
		this.idCompetencia = idCompetencia;
	}

	public Long getIdTipoCompetencia() {
		return idTipoCompetencia;
	}

	public void setIdTipoCompetencia(Long idTipoCompetencia) {
		this.idTipoCompetencia = idTipoCompetencia;
	}

	public String getDescricaoTipoCompetencia() {
		return descricaoTipoCompetencia;
	}

	public void setDescricaoTipoCompetencia(String descricaoTipoCompetencia) {
		this.descricaoTipoCompetencia = descricaoTipoCompetencia;
	}

	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public String getDirecionamentoGestor() {
		return direcionamentoGestor;
	}

	public void setDirecionamentoGestor(String direcionamentoGestor) {
		this.direcionamentoGestor = direcionamentoGestor;
	}

	public String getDirecionamentoNaoGestor() {
		return direcionamentoNaoGestor;
	}

	public void setDirecionamentoNaoGestor(String direcionamentoNaoGestor) {
		this.direcionamentoNaoGestor = direcionamentoNaoGestor;
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
