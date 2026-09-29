package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.SituacaoPDI;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class PDIAcaoTO extends BaseAuditedTO implements SearchObject {

	public PDIAcaoTO() {}

	@Validation(name = "Avaliaçao Participante", notNull = false)
	private ParticipanteTO participante;

	@Validation(name = "Funcionario", notNull = false)
	private FuncionarioTO funcionario;

	@Validation(name = "Situaçao PDI", notNull = true)
	private SituacaoPDI situacao;

	private String acaoParaDesenvolvimento;

	@Validation(name = "Data prevista Inicio", notNull = true)
	private Date dataPrevistaInicio;

	@Validation(name = "Data prevista Fim", notNull = true)
	private Date dataPrevistaFim;

	@Validation(name = "Data Inicio")
	private Date dataInicio;

	@Validation(name = "Data Fim")
	private Date dataFim;

	@Validation(name = "Obs Inicial")
	private String observacaoInicial;

	@Validation(name = "Obs Final")
	private String observacaoFinal;

	@Validation(name = "Revisado")
	private Boolean revisado;

	private List<PDIAcaoCompetenciaDetalhamentoTO> pdiAcaoCompetencias;

	public static class Builder {

		private SituacaoPDI situacao;
		private String acaoParaDesenvolvimento;
		private Date dataPrevistaInicio;
		private Date dataPrevistaFim;

		//opcional
		private FuncionarioTO funcionario;
		private ParticipanteTO participante;
		private Long id;
		private Date dataInicio;
		private Date dataFim;
		private String observacaoInicial;
		private String observacaoFinal;
		private Boolean revisado;
		private List<PDIAcaoCompetenciaDetalhamentoTO> pdiAcaoCompetenciaDetalhamentoTOList;

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

		public PDIAcaoTO.Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setFuncionario (FuncionarioTO funcionario) {
			this.funcionario = funcionario;
			return this;
		}


		public Builder setParticipante (ParticipanteTO participante) {
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

		public Builder setRevisado (List<PDIAcaoCompetenciaDetalhamentoTO> pdiAcaoCompetenciaDetalhamentoTOList) {
			this.pdiAcaoCompetenciaDetalhamentoTOList = pdiAcaoCompetenciaDetalhamentoTOList;
			return this;
		}

		public PDIAcaoTO build() {return new PDIAcaoTO(this);}
	}

	private PDIAcaoTO(Builder builder) {
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
		pdiAcaoCompetencias = builder.pdiAcaoCompetenciaDetalhamentoTOList;
	}

	public ParticipanteTO getParticipante() {
		return participante;
	}

	public void setParticipante(ParticipanteTO participante) {
		this.participante = participante;
	}

	public SituacaoPDI getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoPDI situacao) {
		this.situacao = situacao;
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

	public List<PDIAcaoCompetenciaDetalhamentoTO> getPdiAcaoCompetencias() {
		return pdiAcaoCompetencias;
	}

	public void setPdiAcaoCompetencias(List<PDIAcaoCompetenciaDetalhamentoTO> pdiAcaoCompetencias) {
		this.pdiAcaoCompetencias = pdiAcaoCompetencias;
	}

	public FuncionarioTO getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioTO funcionario) {
		this.funcionario = funcionario;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		PDIAcaoTO pdiAcaoTO = (PDIAcaoTO) o;
		return participante.equals(pdiAcaoTO.participante) &&
			situacao == pdiAcaoTO.situacao &&
			Objects.equals(acaoParaDesenvolvimento, pdiAcaoTO.acaoParaDesenvolvimento) &&
			Objects.equals(dataPrevistaInicio, pdiAcaoTO.dataPrevistaInicio) &&
			Objects.equals(dataPrevistaFim, pdiAcaoTO.dataPrevistaFim) &&
			Objects.equals(dataInicio, pdiAcaoTO.dataInicio) &&
			Objects.equals(dataFim, pdiAcaoTO.dataFim) &&
			Objects.equals(observacaoInicial, pdiAcaoTO.observacaoInicial) &&
			Objects.equals(observacaoFinal, pdiAcaoTO.observacaoFinal);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), participante, situacao, acaoParaDesenvolvimento, dataPrevistaInicio, dataPrevistaFim, dataInicio, dataFim, observacaoInicial, observacaoFinal);
	}

	@Override
	public String toString() {
		return "PDIAcao{" +
			"situacao=" + situacao +
			", acaoParaDesenvolvimento='" + acaoParaDesenvolvimento + '\'' +
			", dataInicio=" + dataInicio +
			", dataFim=" + dataFim +
			", revisado=" + revisado +
			'}';
	}
}
