package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;
import java.util.List;

public class AvaliacaoCompDetTO extends BaseTO implements SearchObject, Cloneable {

	@Validation(name = "avaliação competencia", notNull = true)
	private AvaliacaoCompetenciaTO avaliacaoCompetencia;
	private Long idCompetenciaDetalhamento;
	private Integer ordem;
	private String descricao;
	private String atualizadorOriginal;
	private Date dataAtualizacaoOriginal;
	private List<PreenchimentoTO> preenchimentos;
	private List<AvaliacaoCompDetHintTO> hints;
	private Long idModeloGraduacao;
	private Boolean finalizou;

	public AvaliacaoCompetenciaTO getAvaliacaoCompetencia() {
		return avaliacaoCompetencia;
	}

	public void setAvaliacaoCompetencia(AvaliacaoCompetenciaTO avaliacaoCompetencia) {
		this.avaliacaoCompetencia = avaliacaoCompetencia;
	}

	public Long getIdCompetenciaDetalhamento() {
		return idCompetenciaDetalhamento;
	}

	public void setIdCompetenciaDetalhamento(Long idCompetenciaDetalhamento) {
		this.idCompetenciaDetalhamento = idCompetenciaDetalhamento;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getAtualizadorOriginal() {
		return atualizadorOriginal;
	}

	public void setAtualizadorOriginal(String atualizadorOriginal) {
		this.atualizadorOriginal = atualizadorOriginal;
	}

	public Date getDataAtualizacaoOriginal() {
		return dataAtualizacaoOriginal;
	}

	public void setDataAtualizacaoOriginal(Date dataAtualizacaoOriginal) {
		this.dataAtualizacaoOriginal = dataAtualizacaoOriginal;
	}

	public List<PreenchimentoTO> getPreenchimentos() {
		return preenchimentos;
	}

	public void setPreenchimentos(List<PreenchimentoTO> preenchimentos) {
		this.preenchimentos = preenchimentos;
	}

	public List<AvaliacaoCompDetHintTO> getHints() {
		return hints;
	}

	public void setHints(List<AvaliacaoCompDetHintTO> hints) {
		this.hints = hints;
	}

	public Long getIdModeloGraduacao() {
		return idModeloGraduacao;
	}

	public void setIdModeloGraduacao(Long idModeloGraduacao) {
		this.idModeloGraduacao = idModeloGraduacao;
	}

	public Boolean getFinalizou() {
		return finalizou;
	}

	public void setFinalizou(Boolean finalizou) {
		this.finalizou = finalizou;
	}

	@Override
	public AvaliacaoCompDetTO clone() {
		try {
			return (AvaliacaoCompDetTO) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public String toString() {
		return "AvaliacaoCompDetTO{" +
			"id=" + getId() +
			"avaliacaoCompetencia=" + avaliacaoCompetencia +
			", idCompetenciaDetalhamento=" + idCompetenciaDetalhamento +
			", idModeloGraduacao=" + idModeloGraduacao +
			", ordem=" + ordem +
			", descricao='" + descricao + '\'' +
			", atualizadorOriginal='" + atualizadorOriginal + '\'' +
			", dataAtualizacaoOriginal=" + dataAtualizacaoOriginal +
			", preenchimentos=" + preenchimentos +
			", hints=" + hints +
			'}';
	}
}
