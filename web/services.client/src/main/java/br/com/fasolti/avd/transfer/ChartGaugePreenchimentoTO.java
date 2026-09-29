package br.com.fasolti.avd.transfer;

/**
 * Created by jose.ricardo on 26/08/16.
 */
public class ChartGaugePreenchimentoTO {
	private String color;
	private String type = "gauge";
	private Integer min = 0;
	private Integer max = 100;
	private Integer value;
	private Integer width = 45;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getMin() {
		return min;
	}

	public void setMin(Integer min) {
		this.min = min;
	}

	public Integer getMax() {
		return max;
	}

	public void setMax(Integer max) {
		this.max = max;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}
}
