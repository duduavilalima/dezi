package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.service.domain.FaseGraficoAvaliacao;

public class GapCompetenciaTO {

	private FaseGraficoAvaliacao tipoGrafico;

	private AvaliacaoTO avaliacao;

	private ParticipanteTO participante;

	private String tipo;

	public FaseGraficoAvaliacao getTipoGrafico() {
		return tipoGrafico;
	}

	public void setTipoGrafico(FaseGraficoAvaliacao tipoGrafico) {
		this.tipoGrafico = tipoGrafico;
	}

	public AvaliacaoTO getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoTO avaliacao) {
		this.avaliacao = avaliacao;
	}

	public ParticipanteTO getParticipante() {
		return participante;
	}

	public void setParticipante(ParticipanteTO participante) {
		this.participante = participante;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}
