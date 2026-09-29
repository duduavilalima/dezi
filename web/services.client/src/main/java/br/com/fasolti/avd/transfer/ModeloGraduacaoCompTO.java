package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;
import java.util.List;

public class ModeloGraduacaoCompTO extends BaseTO implements SearchObject{

	@Validation(name = "avaliação competencia", notNull = true)
	private AvaliacaoCompetenciaTO avaliacaoCompetencia;
	private Long idModeloGraduacao;
	private String descricaoModelo;
	private Boolean naoGraduar;
	private Long idDimensao;
	private Long idEmpresa;
	private String atualizadorOriginal;
	private Date dataAtualizacaoOriginal;
	private List<GraduacaoCompetenciaTO> graduacoes;
	private Long idAvaliacao;

	public AvaliacaoCompetenciaTO getAvaliacaoCompetencia() {
		return avaliacaoCompetencia;
	}

	public void setAvaliacaoCompetencia(AvaliacaoCompetenciaTO avaliacaoCompetencia) {
		this.avaliacaoCompetencia = avaliacaoCompetencia;
	}

	public Long getIdModeloGraduacao() {
		return idModeloGraduacao;
	}

	public void setIdModeloGraduacao(Long idModeloGraduacao) {
		this.idModeloGraduacao = idModeloGraduacao;
	}

	public String getDescricaoModelo() {
		return descricaoModelo;
	}

	public void setDescricaoModelo(String descricaoModelo) {
		this.descricaoModelo = descricaoModelo;
	}

	public Boolean getNaoGraduar() {
		return naoGraduar;
	}

	public void setNaoGraduar(Boolean naoGraduar) {
		this.naoGraduar = naoGraduar;
	}

	public Long getIdDimensao() {
		return idDimensao;
	}

	public void setIdDimensao(Long idDimensao) {
		this.idDimensao = idDimensao;
	}

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
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

	public List<GraduacaoCompetenciaTO> getGraduacoes() {
		return graduacoes;
	}

	public void setGraduacoes(List<GraduacaoCompetenciaTO> graduacoes) {
		this.graduacoes = graduacoes;
	}

	public Long getIdAvaliacao() {
		return idAvaliacao;
	}

	public void setIdAvaliacao(Long idAvaliacao) {
		this.idAvaliacao = idAvaliacao;
	}
}
