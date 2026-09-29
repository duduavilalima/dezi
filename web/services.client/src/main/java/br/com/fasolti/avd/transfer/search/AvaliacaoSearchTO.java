package br.com.fasolti.avd.transfer.search;

import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

import java.util.List;

public class AvaliacaoSearchTO extends AvaliacaoTO implements SearchObject {

	private List<Long> listaCampanha;

	public List<Long> getListaCampanha() {
		return listaCampanha;
	}

	public void setListaCampanha(List<Long> listaCampanha) {
		this.listaCampanha = listaCampanha;
	}
}
