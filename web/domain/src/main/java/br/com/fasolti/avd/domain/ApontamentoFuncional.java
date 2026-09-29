package br.com.fasolti.avd.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "apontamento_funcional")
public class ApontamentoFuncional extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "id_avaliado", nullable = false)
	private Funcionario avaliado;

	@ManyToOne
	@JoinColumn(name = "id_avaliador", nullable = false)
	private Funcionario avaliador;

    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_apontamento", nullable = false)
	private Date dataApontamento;

	@Column(name = "graduacao_visual", nullable = false, precision = 1)
	private Double graduacaoVisual;

	@Column(name = "texto")
	private String registroCritico;

	@Column(name = "feedforward")
	private String feedforward;

    @ManyToOne
    @JoinColumn(name = "id_competencia", nullable = false)
	private Competencia competencia;

	public ApontamentoFuncional() {
	}

	public Funcionario getAvaliado() {
		return avaliado;
	}

	public void setAvaliado(Funcionario avaliado) {
		this.avaliado = avaliado;
	}

	public Funcionario getAvaliador() {
		return avaliador;
	}

	public void setAvaliador(Funcionario avaliador) {
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

	public Competencia getCompetencia() {
		return competencia;
	}

	public void setCompetencia(Competencia competencia) {
		this.competencia = competencia;
	}
}
