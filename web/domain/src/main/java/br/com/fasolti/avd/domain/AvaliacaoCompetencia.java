package br.com.fasolti.avd.domain;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "avaliacao_competencia")
public class AvaliacaoCompetencia extends BaseAuditedEntity {

	private static final long serialVersionUID = 667120262614108214L;

	@ManyToOne
	@JoinColumn(name = "id_avaliacao")
	private Avaliacao avaliacao;

	@Column(name = "id_competencia")
	private Long idCompetencia;

	@Column(name = "id_tipo_competencia")
	private Long idTipoCompetencia;

	@Column(name = "descricao_tipo_competencia")
	private String descricaoTipoCompetencia;

	@Column(name = "descricao_abreviada")
	private String descricaoAbreviada;

	@Column(name = "direcionamento_gestor")
	private String direcionamentoGestor;

	@Column(name = "direcionamento_nao_gestor")
	private String direcionamentoNaoGestor;

	@Column(name = "atualizador_original")
	private String atualizadorOriginal;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_atualizacao_original")
	private Date dataAtualizacaoOriginal;

	public AvaliacaoCompetencia() {
	}

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	public Long getIdCompetencia() {
		return idCompetencia;
	}

	public void setIdCompetencia(Long idCompetencia) {
		this.idCompetencia = idCompetencia;
	}

	public Long getIdTipoCompetencia() {
		return idTipoCompetencia;
	}

	public void setIdTipoCompetencia(Long idTipoCompetencia) {
		this.idTipoCompetencia = idTipoCompetencia;
	}

	public String getDescricaoTipoCompetencia() {
		return descricaoTipoCompetencia;
	}

	public void setDescricaoTipoCompetencia(String descricaoTipoCompetencia) {
		this.descricaoTipoCompetencia = descricaoTipoCompetencia;
	}

	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public String getDirecionamentoGestor() {
		return direcionamentoGestor;
	}

	public void setDirecionamentoGestor(String direcionamentoGestor) {
		this.direcionamentoGestor = direcionamentoGestor;
	}

	public String getDirecionamentoNaoGestor() {
		return direcionamentoNaoGestor;
	}

	public void setDirecionamentoNaoGestor(String direcionamentoNaoGestor) {
		this.direcionamentoNaoGestor = direcionamentoNaoGestor;
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
}
