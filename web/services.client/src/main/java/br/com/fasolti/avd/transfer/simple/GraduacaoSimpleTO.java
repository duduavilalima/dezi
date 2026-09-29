package br.com.fasolti.avd.transfer.simple;


import br.com.fasolti.avd.transfer.util.BaseTO;

public class GraduacaoSimpleTO extends BaseTO {
	private String label;

	private Integer ordem;

	private Integer ponto;

	private String hint;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public Integer getPonto() {
		return ponto;
	}

	public void setPonto(Integer ponto) {
		this.ponto = ponto;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}
}
