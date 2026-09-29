package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;

public class AvaliadoPreenchimentoTO {
	private FuncionarioSimpleTO avaliado;
	private Double percentualConsenso;
	private Integer totalPreenchimentos;

	public FuncionarioSimpleTO getAvaliado() {
		return avaliado;
	}

	public void setAvaliado(FuncionarioSimpleTO avaliado) {
		this.avaliado = avaliado;
	}

	public Double getPercentualConsenso() {
		return percentualConsenso;
	}

	public void setPercentualConsenso(Double percentualConsenso) {
		this.percentualConsenso = percentualConsenso;
	}

	public Integer getTotalPreenchimentos() {
		return totalPreenchimentos;
	}

	public void setTotalPreenchimentos(Integer totalPreenchimentos) {
		this.totalPreenchimentos = totalPreenchimentos;
	}
}
