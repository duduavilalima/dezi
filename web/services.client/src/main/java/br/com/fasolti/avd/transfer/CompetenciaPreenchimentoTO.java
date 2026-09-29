package br.com.fasolti.avd.transfer;


import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseTO;

import java.util.List;

public class CompetenciaPreenchimentoTO extends BaseTO {

	private String descricaoAbreviada;

	private String direcionamentoGestor;

	private String direcionamentoNaoGestor;

	private EmpresaSimpleTO empresa;

	private ModeloGraduacaoCompTO modeloGraduacao;

	private List<AvaliacaoCompDetTO> detalhamentos;

	//Foram criados estes campos de PDI aqui, pois o usuário preenche competências no front-end
	private String acaoPDI;

	private String comentarioFinalAvaliado;

	private String comentarioFinalAvaliador;

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

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
	}

	public ModeloGraduacaoCompTO getModeloGraduacao() {
		return modeloGraduacao;
	}

	public void setModeloGraduacao(ModeloGraduacaoCompTO modeloGraduacao) {
		this.modeloGraduacao = modeloGraduacao;
	}

	public List<AvaliacaoCompDetTO> getDetalhamentos() {
		return detalhamentos;
	}

	public void setDetalhamentos(List<AvaliacaoCompDetTO> detalhamentos) {
		this.detalhamentos = detalhamentos;
	}

	public String getAcaoPDI() {
		return acaoPDI;
	}

	public void setAcaoPDI(String acaoPDI) {
		this.acaoPDI = acaoPDI;
	}

	public String getComentarioFinalAvaliado() {
		return comentarioFinalAvaliado;
	}

	public void setComentarioFinalAvaliado(String comentarioFinalAvaliado) {
		this.comentarioFinalAvaliado = comentarioFinalAvaliado;
	}

	public String getComentarioFinalAvaliador() {
		return comentarioFinalAvaliador;
	}

	public void setComentarioFinalAvaliador(String comentarioFinalAvaliador) {
		this.comentarioFinalAvaliador = comentarioFinalAvaliador;
	}
}
