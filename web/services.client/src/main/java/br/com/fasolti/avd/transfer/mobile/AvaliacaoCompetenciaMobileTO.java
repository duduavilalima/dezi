package br.com.fasolti.avd.transfer.mobile;

import br.com.fasolti.commons.client.transfer.TransferObject;

import java.util.Date;
import java.util.List;

public class AvaliacaoCompetenciaMobileTO implements TransferObject {
	private Long id;
	private String descricaoAbreviada;
	private String direcionamentoGestor;
	private Integer qtdPerguntas;
	private Integer qtdRespostas;
	private Long porcentagemPreenchimento;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
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

	public Integer getQtdPerguntas() {
		return qtdPerguntas;
	}

	public void setQtdPerguntas(Integer qtdPerguntas) {
		this.qtdPerguntas = qtdPerguntas;
	}

	public Integer getQtdRespostas() {
		return qtdRespostas;
	}

	public void setQtdRespostas(Integer qtdRespostas) {
		this.qtdRespostas = qtdRespostas;
	}

	public Long getPorcentagemPreenchimento() {
		return porcentagemPreenchimento;
	}

	public void setPorcentagemPreenchimento(Long porcentagemPreenchimento) {
		this.porcentagemPreenchimento = porcentagemPreenchimento;
	}
}
