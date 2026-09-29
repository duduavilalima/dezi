package br.com.fasolti.avd.transfer;

public class AvaliadorPreenchimentoTO {
	private Long avaliadorId;
	private Integer totalAutoAv;
	private Integer totalAv;
	private Integer totalConsenso;
	private Integer total;

	public Long getAvaliadorId() {
		return avaliadorId;
	}

	public void setAvaliadorId(Long avaliadorId) {
		this.avaliadorId = avaliadorId;
	}

	public Integer getTotalAutoAv() {
		return totalAutoAv;
	}

	public void setTotalAutoAv(Integer totalAutoAv) {
		this.totalAutoAv = totalAutoAv;
	}

	public Integer getTotalAv() {
		return totalAv;
	}

	public void setTotalAv(Integer totalAv) {
		this.totalAv = totalAv;
	}

	public Integer getTotalConsenso() {
		return totalConsenso;
	}

	public void setTotalConsenso(Integer totalConsenso) {
		this.totalConsenso = totalConsenso;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public void incrementTotalAv() {
		if(this.totalAv == null) {
			this.totalAv = 0;
		}
		this.totalAv++;
	}

	public void incrementTotal(Integer total) {
		if(this.total == null) {
			this.total = 0;
		}
		this.total = this.total + total;
	}

	public void incrementTotalConsenso() {
		if(this.totalConsenso == null) {
			this.totalConsenso = 0;
		}
		this.totalConsenso++;
	}

	public void incrementTotalAutoAv() {
		if(this.totalAutoAv == null) {
			this.totalAutoAv = 0;
		}
		this.totalAutoAv++;
	}
}
