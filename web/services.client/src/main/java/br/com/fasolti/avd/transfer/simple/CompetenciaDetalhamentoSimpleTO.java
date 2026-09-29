package br.com.fasolti.avd.transfer.simple;


import br.com.fasolti.avd.transfer.util.BaseTO;

public class CompetenciaDetalhamentoSimpleTO extends BaseTO {

	private String descricao;

	private Integer ordem;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

}
