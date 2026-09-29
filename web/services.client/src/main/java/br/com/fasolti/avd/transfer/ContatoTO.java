package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;

public class ContatoTO {

	private FuncionarioSimpleTO funcionario;
	private EmpresaTO empresa;
	private String assunto;
	private String classificacao;
	private String mensagem;

	public ContatoTO() {

	}

	public FuncionarioSimpleTO getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioSimpleTO funcionario) {
		this.funcionario = funcionario;
	}

	public EmpresaTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaTO empresa) {
		this.empresa = empresa;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(String classificacao) {
		this.classificacao = classificacao;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	@Override
	public String toString() {
		return "ContatoTO{" +
			"funcionario=" + funcionario +
			", empresa=" + empresa +
			", assunto='" + assunto + '\'' +
			", classificacao='" + classificacao + '\'' +
			", mensagem='" + mensagem + '\'' +
			'}';
	}
}
