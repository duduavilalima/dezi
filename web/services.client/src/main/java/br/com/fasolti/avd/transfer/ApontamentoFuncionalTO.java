package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.CompetenciaSimpleTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;

public class ApontamentoFuncionalTO extends BaseTO implements SearchObject {

	@Validation(name = "avalaido", notNull = true)
	private FuncionarioSimpleTO avaliado;

	@Validation(name = "avalaidor", notNull = true)
	private FuncionarioSimpleTO avaliador;

	@Validation(name = "data apontamento", notNull = true)
	private Date dataApontamento;

	@Validation(name = "graduação visual", notNull = true)
	private Double graduacaoVisual;

	@Validation(name = "registro crítico")
	private String registroCritico;

	private String feedforward;

	private CompetenciaSimpleTO competencia;

	public ApontamentoFuncionalTO() {
	}

	public FuncionarioSimpleTO getAvaliado() {
		return avaliado;
	}

	public void setAvaliado(FuncionarioSimpleTO avaliado) {
		this.avaliado = avaliado;
	}

	public FuncionarioSimpleTO getAvaliador() {
		return avaliador;
	}

	public void setAvaliador(FuncionarioSimpleTO avaliador) {
		this.avaliador = avaliador;
	}

	public Date getDataApontamento() {
		return dataApontamento;
	}

	public void setDataApontamento(Date dataApontamento) {
		this.dataApontamento = dataApontamento;
	}

	public Double getGraduacaoVisual() {
		return graduacaoVisual;
	}

	public void setGraduacaoVisual(Double graduacaoVisual) {
		this.graduacaoVisual = graduacaoVisual;
	}

	public String getRegistroCritico() {
		return registroCritico;
	}

	public void setRegistroCritico(String registroCritico) {
		this.registroCritico = registroCritico;
	}

	public String getFeedforward() {
		return feedforward;
	}

	public void setFeedforward(String feedforward) {
		this.feedforward = feedforward;
	}

	public CompetenciaSimpleTO getCompetencia() {
		return competencia;
	}

	public void setCompetencia(CompetenciaSimpleTO competencia) {
		this.competencia = competencia;
	}
}
