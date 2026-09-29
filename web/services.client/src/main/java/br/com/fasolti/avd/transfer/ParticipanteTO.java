package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;

public class ParticipanteTO extends BaseAuditedTO implements Cloneable {

	private String comentarioFinalAvaliado;

	private String comentarioFinalAvaliador;

	private String comentarioFinalRevisor;

	@Validation(name = "Consenso liberado", notNull = false)
	private Boolean consensoLiberado;

	@Validation(name = "Revisão liberada", notNull = false)
	private Boolean revisaoLiberada;

	private Boolean revisado;

	private String pdi;

	@Validation(name = "Avaliação", notNull = true)
	private AvaliacaoTO avaliacao;

	@Validation(name = "Avaliado", notNull = true)
	private FuncionarioSimpleTO avaliado;

	@Validation(name = "Avaliador")
	private FuncionarioSimpleTO avaliador;

	private FuncionarioSimpleTO revisor;

	private Date limiteAv1;

	private Date limiteAv2;

	private Date limiteAv3;

	private Date limiteAv4;

	private Boolean preenchidaAV1;

	private Boolean preenchidaAV2;

	private Boolean preenchidaAV3;

	private Boolean preenchidaAV4;

	private Boolean consensoIniciado;

	private Boolean revisaoIniciada;

	private Date dataConclusaoAv1;

	private Date dataConclusaoAv2;

	private Date dataConclusaoAv3;

	private Date dataConclusaoAv4;

	private Date dataInicioAv1;

	private Date dataInicioAv2;

	private Date dataInicioAv3;

	private Date dataInicioAv4;

	private TipoAvaliacaoPerfil perfilAgente;

	private Date dataInicioRecesso;

	private Date dataFimRecesso;

	private Long idCargoAvaliado;

	private String nomeCargoAvaliado;

	private Long idDeptoAvaliado;

	private String nomeDeptoAvaliado;

	public String getComentarioFinalAvaliado() {
		return comentarioFinalAvaliado;
	}

	public void setComentarioFinalAvaliado(String comentarioFinalAvaliado) {
		this.comentarioFinalAvaliado = comentarioFinalAvaliado;
	}

	public String getComentarioFinalAvaliador() {
		return comentarioFinalAvaliador;
	}

	public void setComentarioFinalAvaliador(String comentarioFinalAvaliador) {
		this.comentarioFinalAvaliador = comentarioFinalAvaliador;
	}

	public String getComentarioFinalRevisor() {
		return comentarioFinalRevisor;
	}

	public void setComentarioFinalRevisor(String comentarioFinalRevisor) {
		this.comentarioFinalRevisor = comentarioFinalRevisor;
	}

	public Boolean getConsensoLiberado() {
		return consensoLiberado;
	}

	public void setConsensoLiberado(Boolean consensoLiberado) {
		this.consensoLiberado = consensoLiberado;
	}

	public Boolean getRevisaoLiberada() {
		return revisaoLiberada;
	}

	public void setRevisaoLiberada(Boolean revisaoLiberada) {
		this.revisaoLiberada = revisaoLiberada;
	}

	public String getPdi() {
		return pdi;
	}

	public void setPdi(String pdi) {
		this.pdi = pdi;
	}

	public AvaliacaoTO getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoTO avaliacao) {
		this.avaliacao = avaliacao;
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

	public FuncionarioSimpleTO getRevisor() {
		return revisor;
	}

	public void setRevisor(FuncionarioSimpleTO revisor) {
		this.revisor = revisor;
	}

	public Date getLimiteAv1() {
		return limiteAv1;
	}

	public void setLimiteAv1(Date limiteAv1) {
		this.limiteAv1 = limiteAv1;
	}

	public Date getLimiteAv2() {
		return limiteAv2;
	}

	public void setLimiteAv2(Date limiteAv2) {
		this.limiteAv2 = limiteAv2;
	}

	public Date getLimiteAv3() {
		return limiteAv3;
	}

	public void setLimiteAv3(Date limiteAv3) {
		this.limiteAv3 = limiteAv3;
	}

	public Date getLimiteAv4() {
		return limiteAv4;
	}

	public void setLimiteAv4(Date limiteAv4) {
		this.limiteAv4 = limiteAv4;
	}

	public Boolean getPreenchidaAV1() {
		return preenchidaAV1;
	}

	public void setPreenchidaAV1(Boolean preenchidaAV1) {
		this.preenchidaAV1 = preenchidaAV1;
	}

	public Boolean getPreenchidaAV2() {
		return preenchidaAV2;
	}

	public void setPreenchidaAV2(Boolean preenchidaAV2) {
		this.preenchidaAV2 = preenchidaAV2;
	}

	public Boolean getPreenchidaAV3() {
		return preenchidaAV3;
	}

	public void setPreenchidaAV3(Boolean preenchidaAV3) {
		this.preenchidaAV3 = preenchidaAV3;
	}

	public Boolean getPreenchidaAV4() {
		return preenchidaAV4;
	}

	public void setPreenchidaAV4(Boolean preenchidaAV4) {
		this.preenchidaAV4 = preenchidaAV4;
	}

	public Boolean getConsensoIniciado() {
		return consensoIniciado;
	}

	public void setConsensoIniciado(Boolean consensoIniciado) {
		this.consensoIniciado = consensoIniciado;
	}

	public Boolean getRevisaoIniciada() {
		return revisaoIniciada;
	}

	public void setRevisaoIniciada(Boolean revisaoIniciada) {
		this.revisaoIniciada = revisaoIniciada;
	}

	public Date getDataConclusaoAv1() {
		return dataConclusaoAv1;
	}

	public void setDataConclusaoAv1(Date dataConclusaoAv1) {
		this.dataConclusaoAv1 = dataConclusaoAv1;
	}

	public Date getDataConclusaoAv2() {
		return dataConclusaoAv2;
	}

	public void setDataConclusaoAv2(Date dataConclusaoAv2) {
		this.dataConclusaoAv2 = dataConclusaoAv2;
	}

	public Date getDataConclusaoAv3() {
		return dataConclusaoAv3;
	}

	public void setDataConclusaoAv3(Date dataConclusaoAv3) {
		this.dataConclusaoAv3 = dataConclusaoAv3;
	}

	public Date getDataConclusaoAv4() {
		return dataConclusaoAv4;
	}

	public void setDataConclusaoAv4(Date dataConclusaoAv4) {
		this.dataConclusaoAv4 = dataConclusaoAv4;
	}

	public Date getDataInicioAv1() {
		return dataInicioAv1;
	}

	public void setDataInicioAv1(Date dataInicioAv1) {
		this.dataInicioAv1 = dataInicioAv1;
	}

	public Date getDataInicioAv2() {
		return dataInicioAv2;
	}

	public void setDataInicioAv2(Date dataInicioAv2) {
		this.dataInicioAv2 = dataInicioAv2;
	}

	public Date getDataInicioAv3() {
		return dataInicioAv3;
	}

	public void setDataInicioAv3(Date dataInicioAv3) {
		this.dataInicioAv3 = dataInicioAv3;
	}

	public Date getDataInicioAv4() {
		return dataInicioAv4;
	}

	public void setDataInicioAv4(Date dataInicioAv4) {
		this.dataInicioAv4 = dataInicioAv4;
	}

	public TipoAvaliacaoPerfil getPerfilAgente() {
		return perfilAgente;
	}

	public void setPerfilAgente(TipoAvaliacaoPerfil perfilAgente) {
		this.perfilAgente = perfilAgente;
	}

	public Date getDataInicioRecesso() {
		return dataInicioRecesso;
	}

	public void setDataInicioRecesso(Date dataInicioRecesso) {
		this.dataInicioRecesso = dataInicioRecesso;
	}

	public Date getDataFimRecesso() {
		return dataFimRecesso;
	}

	public void setDataFimRecesso(Date dataFimRecesso) {
		this.dataFimRecesso = dataFimRecesso;
	}

	public Boolean getRevisado() {
		return revisado;
	}

	public void setRevisado(Boolean revisado) {
		this.revisado = revisado;
	}

	public Long getIdCargoAvaliado() {
		return idCargoAvaliado;
	}

	public void setIdCargoAvaliado(Long idCargoAvaliado) {
		this.idCargoAvaliado = idCargoAvaliado;
	}

	public String getNomeCargoAvaliado() {
		return nomeCargoAvaliado;
	}

	public void setNomeCargoAvaliado(String nomeCargoAvaliado) {
		this.nomeCargoAvaliado = nomeCargoAvaliado;
	}

	public Long getIdDeptoAvaliado() {
		return idDeptoAvaliado;
	}

	public void setIdDeptoAvaliado(Long idDeptoAvaliado) {
		this.idDeptoAvaliado = idDeptoAvaliado;
	}

	public String getNomeDeptoAvaliado() {
		return nomeDeptoAvaliado;
	}

	public void setNomeDeptoAvaliado(String nomeDeptoAvaliado) {
		this.nomeDeptoAvaliado = nomeDeptoAvaliado;
	}

	@Override
	public ParticipanteTO clone() throws CloneNotSupportedException {
		return (ParticipanteTO) super.clone();
	}

}
