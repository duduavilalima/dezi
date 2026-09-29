package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.SituacaoPDI;
import br.com.fasolti.commons.client.transfer.SearchObject;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name="pdi_acao")
public class PDIAcao extends BaseAuditedEntity implements SearchObject {

	public PDIAcao () {}

	@ManyToOne
	@JoinColumn(name = "id_avaliacao_participante")
	private Participante participante;

	@ManyToOne
	@JoinColumn(name = "id_funcionario")
	private Funcionario funcionario;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private SituacaoPDI situacao;

	@Column(name = "acao_desenv")
	private String acaoParaDesenvolvimento;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_prevista_inicio")
	private Date dataPrevistaInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_prevista_fim")
	private Date dataPrevistaFim;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio")
	private Date dataInicio;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_fim")
	private Date dataFim;

	@Column(name = "obs_inicio")
	private String observacaoInicial;

	@Column(name = "obs_fim")
	private String observacaoFinal;

	@Column(name = "revisado")
	private Boolean revisado;

	public Participante getParticipante() {
		return participante;
	}

	public void setParticipante(Participante participante) {
		this.participante = participante;
	}

	public String getAcaoParaDesenvolvimento() {
		return acaoParaDesenvolvimento;
	}

	public void setAcaoParaDesenvolvimento(String acaoParaDesenvolvimento) {
		this.acaoParaDesenvolvimento = acaoParaDesenvolvimento;
	}

	public Date getDataPrevistaInicio() {
		return dataPrevistaInicio;
	}

	public void setDataPrevistaInicio(Date dataPrevistaInicio) {
		this.dataPrevistaInicio = dataPrevistaInicio;
	}

	public Date getDataPrevistaFim() {
		return dataPrevistaFim;
	}

	public void setDataPrevistaFim(Date dataPrevistaFim) {
		this.dataPrevistaFim = dataPrevistaFim;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getObservacaoInicial() {
		return observacaoInicial;
	}

	public void setObservacaoInicial(String observacaoInicial) {
		this.observacaoInicial = observacaoInicial;
	}

	public String getObservacaoFinal() {
		return observacaoFinal;
	}

	public void setObservacaoFinal(String observacaoFinal) {
		this.observacaoFinal = observacaoFinal;
	}

	public Boolean getRevisado() {
		return revisado;
	}

	public void setRevisado(Boolean revisado) {
		this.revisado = revisado;
	}

	public SituacaoPDI getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoPDI situacao) {
		this.situacao = situacao;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public static class Builder {

		private SituacaoPDI situacao;
		private String acaoParaDesenvolvimento;
		private Date dataPrevistaInicio;
		private Date dataPrevistaFim;

		//opcional
		private Funcionario funcionario;
		private Participante participante;
		private Long id;
		private Date dataInicio;
		private Date dataFim;
		private String observacaoInicial;
		private String observacaoFinal;
		private Boolean revisado;

		public Builder(
			SituacaoPDI situacao,
			String acaoParaDesenvolvimento,
			Date dataPrevistaInicio,
			Date dataPrevistaFim
		){
			this.situacao = situacao;
			this.acaoParaDesenvolvimento = acaoParaDesenvolvimento;
			this.dataPrevistaInicio = dataPrevistaInicio;
			this.dataPrevistaFim = dataPrevistaFim;
		}

		public PDIAcao.Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setFuncionario (Funcionario funcionario) {
			this.funcionario = funcionario;
			return this;
		}

		public Builder setParticipante (Participante participante) {
			this.participante = participante;
			return this;
		}

		public Builder setDataInicio (Date dataInicio) {
			this.dataInicio = dataInicio;
			return this;
		}

		public Builder setDataFim (Date dataFim) {
			this.dataFim = dataFim;
			return this;
		}

		public Builder setObservacaoInicial (String observacaoInicial) {
			this.observacaoInicial = observacaoInicial;
			return this;
		}

		public Builder setObservacaoFinal (String observacaoFinal) {
			this.observacaoFinal = observacaoFinal;
			return this;
		}

		public Builder setRevisado (Boolean revisado) {
			this.revisado = revisado;
			return this;
		}

		public PDIAcao build() {return new PDIAcao(this);}
	}

	private PDIAcao(Builder builder) {
		setId(builder.id);
		dataInicio = builder.dataInicio;
		funcionario = builder.funcionario;
		dataFim = builder.dataFim;
		observacaoInicial = builder.observacaoInicial;
		observacaoFinal = builder.observacaoFinal;
		revisado = builder.revisado;
		participante = builder.participante;
		situacao = builder.situacao;
		acaoParaDesenvolvimento = builder.acaoParaDesenvolvimento;
		dataPrevistaInicio = builder.dataPrevistaInicio;
		dataPrevistaFim = builder.dataPrevistaFim;
	}

}
