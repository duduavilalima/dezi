package br.com.fasolti.avd.transfer;

import java.util.List;

public class TotalAcompanhamentoGraficoTO {

	private List<GraficoPreenchimentoTO> graficos;
	private Double totalmentePreenchidos;
	private Double total;

	public TotalAcompanhamentoGraficoTO() {
	}

	public List<GraficoPreenchimentoTO> getGraficos() {
		return graficos;
	}

	public void setGraficos(List<GraficoPreenchimentoTO> graficos) {
		this.graficos = graficos;
	}

	public Double getTotalmentePreenchidos() {
		return totalmentePreenchidos;
	}

	public void setTotalmentePreenchidos(Double totalmentePreenchidos) {
		this.totalmentePreenchidos = totalmentePreenchidos;
	}

	public Double getTotal() {
		return total;
	}

	public void setTotal(Double total) {
		this.total = total;
	}
}
