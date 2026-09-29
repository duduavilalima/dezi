package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.mobile.FuncionarioMobileTO;
import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.commons.client.transfer.SearchObject;

public class AvaliacaoDimensaoTO extends BaseTO implements SearchObject, Comparable {

	private DimensaoTO dimensao;

	private AvaliacaoTO avaliacao;

	public DimensaoTO getDimensao() {
		return dimensao;
	}

	public void setDimensao(DimensaoTO dimensao) {
		this.dimensao = dimensao;
	}

	public AvaliacaoTO getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoTO avaliacao) {
		this.avaliacao = avaliacao;
	}

	@Override
	public int compareTo(Object o) {
		return getId().compareTo(((AvaliacaoDimensaoTO) o).getId());
	}
}
