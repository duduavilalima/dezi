package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.TipoFoto;
import br.com.fasolti.commons.client.transfer.SearchObject;

public class FotoTO extends BaseTO implements SearchObject {

	private String base64;

	private TipoFoto tipo;

	private FuncionarioSimpleTO funcionario;

	public FotoTO() {}

	public String getBase64() {
		return base64;
	}

	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public TipoFoto getTipo() {
		return tipo;
	}

	public void setTipo(TipoFoto tipo) {
		this.tipo = tipo;
	}

	public FuncionarioSimpleTO getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioSimpleTO funcionario) {
		this.funcionario = funcionario;
	}
}
