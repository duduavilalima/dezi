package br.com.fasolti.avd.transfer;

public class GraficoPreenchimentoTO {
	private String label;
	private Double total;
	private Double count;

	public GraficoPreenchimentoTO(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}
}
