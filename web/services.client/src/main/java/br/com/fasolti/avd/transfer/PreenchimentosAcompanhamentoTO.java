package br.com.fasolti.avd.transfer;

import java.util.List;

public class PreenchimentosAcompanhamentoTO {

	private List<ColaboradorAcompanhamentoTO> naoIniciados;
	private List<ColaboradorAcompanhamentoTO> parcialmentePreenchidos;
	private List<ColaboradorAcompanhamentoTO> totalmentePreenchidos;

	public PreenchimentosAcompanhamentoTO() {
	}

	public List<ColaboradorAcompanhamentoTO> getNaoIniciados() {
		return naoIniciados;
	}

	public void setNaoIniciados(List<ColaboradorAcompanhamentoTO> naoIniciados) {
		this.naoIniciados = naoIniciados;
	}

	public List<ColaboradorAcompanhamentoTO> getParcialmentePreenchidos() {
		return parcialmentePreenchidos;
	}

	public void setParcialmentePreenchidos(List<ColaboradorAcompanhamentoTO> parcialmentePreenchidos) {
		this.parcialmentePreenchidos = parcialmentePreenchidos;
	}

	public List<ColaboradorAcompanhamentoTO> getTotalmentePreenchidos() {
		return totalmentePreenchidos;
	}

	public void setTotalmentePreenchidos(List<ColaboradorAcompanhamentoTO> totalmentePreenchidos) {
		this.totalmentePreenchidos = totalmentePreenchidos;
	}
}
