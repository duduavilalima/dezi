package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.types.TipoPerfil;

public class EmailBoasVindasTO {
	private EmpresaSimpleTO empresa;
	private TipoPerfil perfil;
	private String assunto;
	private String mensagem;
	private FuncionarioTO colaborador;

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
	}

	public TipoPerfil getPerfil() {
		return perfil;
	}

	public void setPerfil(TipoPerfil perfil) {
		this.perfil = perfil;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public FuncionarioTO getColaborador() {
		return colaborador;
	}

	public void setColaborador(FuncionarioTO colaborador) {
		this.colaborador = colaborador;
	}
}
