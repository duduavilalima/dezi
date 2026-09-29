package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.transfer.ModeloGraduacaoTO;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "avaliacao_comp_det")
public class AvaliacaoCompDet extends BaseAuditedEntity{

	private static final long serialVersionUID = 667120262614108214L;

	@ManyToOne
	@JoinColumn(name = "id_avaliacao_competencia")
	private AvaliacaoCompetencia avaliacaoCompetencia;

	@Column(name = "id_competencia_detalhamento")
	private Long idCompetenciaDetalhamento;

	@Column(name = "ordem")
	private Integer ordem;

	@Column(name = "descricao")
	private String descricao;

	@Column(name = "atualizador_original")
	private String atualizadorOriginal;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_atualizacao_original")
	private Date dataAtualizacaoOriginal;

	@Column(name = "id_modelo_graduacao")
	private Long idModeloGraduacao;

	public AvaliacaoCompetencia getAvaliacaoCompetencia() {
		return avaliacaoCompetencia;
	}

	public void setAvaliacaoCompetencia(AvaliacaoCompetencia avaliacaoCompetencia) {
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

	public Long getIdModeloGraduacao() {
		return idModeloGraduacao;
	}

	public void setIdModeloGraduacao(Long idModeloGraduacao) {
		this.idModeloGraduacao = idModeloGraduacao;
	}
}
