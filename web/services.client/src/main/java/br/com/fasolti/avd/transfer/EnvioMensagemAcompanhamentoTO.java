package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;

import java.util.List;

public class EnvioMensagemAcompanhamentoTO {

	private Boolean envioEmail;
	private String texto;
	private String titulo;
	private List<FuncionarioSimpleTO> listaColaboradores;

	private String avisoEmailInternacionalizado;
	private String linkEmailInternacionalizado;

	public EnvioMensagemAcompanhamentoTO() {
	}

	public Boolean getEnvioEmail() {
		return envioEmail;
	}

	public void setEnvioEmail(Boolean envioEmail) {
		this.envioEmail = envioEmail;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<FuncionarioSimpleTO> getListaColaboradores() {
		return listaColaboradores;
	}

	public void setListaColaboradores(List<FuncionarioSimpleTO> listaColaboradores) {
		this.listaColaboradores = listaColaboradores;
	}

	public String getAvisoEmailInternacionalizado() {
		return avisoEmailInternacionalizado;
	}

	public void setAvisoEmailInternacionalizado(String avisoEmailInternacionalizado) {
		this.avisoEmailInternacionalizado = avisoEmailInternacionalizado;
	}

	public String getLinkEmailInternacionalizado() {
		return linkEmailInternacionalizado;
	}

	public void setLinkEmailInternacionalizado(String linkEmailInternacionalizado) {
		this.linkEmailInternacionalizado = linkEmailInternacionalizado;
	}
}
