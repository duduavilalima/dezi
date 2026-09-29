package br.com.fasolti.avd.transfer;

public class TipoPerfilTO {
	private Boolean pontoFocal;
	private Boolean gestor;
	private Boolean subordinado;
	private Boolean par;
	private Boolean autoAvaliacao;

	public Boolean getPontoFocal() {
		return pontoFocal;
	}

	public void setPontoFocal(Boolean pontoFocal) {
		this.pontoFocal = pontoFocal;
	}

	public Boolean getGestor() {
		return gestor;
	}

	public void setGestor(Boolean gestor) {
		this.gestor = gestor;
	}

	public Boolean getSubordinado() {
		return subordinado;
	}

	public void setSubordinado(Boolean subordinado) {
		this.subordinado = subordinado;
	}

	public Boolean getPar() {
		return par;
	}

	public void setPar(Boolean par) {
		this.par = par;
	}

	public Boolean getAutoAvaliacao() {
		return autoAvaliacao;
	}

	public void setAutoAvaliacao(Boolean autoAvaliacao) {
		this.autoAvaliacao = autoAvaliacao;
	}
}
