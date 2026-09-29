package br.com.fasolti.avd.transfer;

public class ChartEvolucaoTO {

	// TODO Separar value em percentual e preenchidas e refletir nos gráficos

	private Integer total;

	private Integer value;

	public ChartEvolucaoTO() {
	}

	public ChartEvolucaoTO(Integer value, Integer total) {
		this.value = value;
		this.total = total;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
