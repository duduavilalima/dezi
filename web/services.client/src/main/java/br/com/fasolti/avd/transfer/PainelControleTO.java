package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.service.domain.FaseGraficoAvaliacao;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.transfer.util.SituationalObject;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;

import java.util.Date;

public class PainelControleTO extends BaseAuditedTO implements SituationalObject, SearchObject {

	private AvaliacaoTO avaliacao;

	private Situacao situacao;

	private String periodo;

	private Date periodoDe;

	private Date periodoAte;

	private FaseGraficoAvaliacao tipoGrafico;

	@Override
	public Situacao getSituacao() {
		return this.situacao;
	}

	@Override
	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public AvaliacaoTO getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoTO avaliacao) {
		this.avaliacao = avaliacao;
	}

	public String getPeriodo() {
		return periodo;
	}

	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}

	public Date getPeriodoDe() {
		return periodoDe;
	}

	public void setPeriodoDe(Date periodoDe) {
		this.periodoDe = periodoDe;
	}

	public Date getPeriodoAte() {
		return periodoAte;
	}

	public void setPeriodoAte(Date periodoAte) {
		this.periodoAte = periodoAte;
	}

	public FaseGraficoAvaliacao getTipoGrafico() {
		return tipoGrafico;
	}

	public void setTipoGrafico(FaseGraficoAvaliacao tipoGrafico) {
		this.tipoGrafico = tipoGrafico;
	}
}

