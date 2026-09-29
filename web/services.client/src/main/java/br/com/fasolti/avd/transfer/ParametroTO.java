package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;

public class ParametroTO extends BaseTO {

	private String chave;
	private String valor;

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
