package br.com.fasolti.avd.transfer;

/**
 * Created by jose.ricardo on 18/07/16.
 */
public class ChartGapCompetenciaTO {
	private String type;
	private Object[] labels;
	private Object[] series;
	private Object[] colors;
	private Object[][] matrix;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object[] getLabels() {
		return labels;
	}

	public void setLabels(Object[] labels) {
		this.labels = labels;
	}

	public Object[] getSeries() {
		return series;
	}

	public void setSeries(Object[] series) {
		this.series = series;
	}

	public Object[] getColors() {
		return colors;
	}

	public void setColors(Object[] colors) {
		this.colors = colors;
	}

	public Object[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(Object[][] matrix) {
		this.matrix = matrix;
	}
}
