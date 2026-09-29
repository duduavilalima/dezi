package br.com.fasolti.avd.transfer;

public class ParticipanteEvolucaoTO {
	private ParticipanteTO participante;

	private Long total;

	private Long preenchimentoAv1;

	private Long preenchimentoAv2;

	private Long preenchimentoAv3;

	public ParticipanteTO getParticipante() {
		return participante;
	}

	public void setParticipante(ParticipanteTO participante) {
		this.participante = participante;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public Long getPreenchimentoAv1() {
		return preenchimentoAv1;
	}

	public void setPreenchimentoAv1(Long preenchimentoAv1) {
		this.preenchimentoAv1 = preenchimentoAv1;
	}

	public Long getPreenchimentoAv2() {
		return preenchimentoAv2;
	}

	public void setPreenchimentoAv2(Long preenchimentoAv2) {
		this.preenchimentoAv2 = preenchimentoAv2;
	}

	public Long getPreenchimentoAv3() {
		return preenchimentoAv3;
	}

	public void setPreenchimentoAv3(Long preenchimentoAv3) {
		this.preenchimentoAv3 = preenchimentoAv3;
	}
}
