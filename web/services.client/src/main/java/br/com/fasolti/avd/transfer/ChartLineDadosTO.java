package br.com.fasolti.avd.transfer;

/**
 * Created by jose.ricardo on 18/07/16.
 */
public class ChartLineDadosTO {
	private Object x;
	private Object y;

	public ChartLineDadosTO() {
	}

	public ChartLineDadosTO(Object x, Object y) {
		this.x = x;
		this.y = y;
	}

	public Object getX() {
		return x;
	}

	public void setX(Object x) {
		this.x = x;
	}

	public Object getY() {
		return y;
	}

	public void setY(Object y) {
		this.y = y;
	}
}
