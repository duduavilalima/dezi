package br.com.fasolti.avd.transfer.search;

import br.com.fasolti.avd.transfer.MensageriaTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

public class MensageriaSearchTO extends MensageriaTO implements SearchObject {

	private Boolean idOriginalIsNull;

	private Integer mensagensNaoLidas;

	public MensageriaSearchTO() {
	}

	public Boolean getIdOriginalIsNull() {
		return idOriginalIsNull;
	}

	public void setIdOriginalIsNull(Boolean idOriginalIsNull) {
		this.idOriginalIsNull = idOriginalIsNull;
	}

	public Integer getMensagensNaoLidas() {
		return mensagensNaoLidas;
	}

	public void setMensagensNaoLidas(Integer mensagensNaoLidas) {
		this.mensagensNaoLidas = mensagensNaoLidas;
	}
}
