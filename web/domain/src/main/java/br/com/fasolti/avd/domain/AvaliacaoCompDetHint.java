package br.com.fasolti.avd.domain;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "avaliacao_comp_det_hint")
public class AvaliacaoCompDetHint extends BaseAuditedEntity{

	private static final long serialVersionUID = 667120262614108214L;

	@ManyToOne
	@JoinColumn(name = "id_avaliacao_comp_det")
	private AvaliacaoCompDet avaliacaoCompDet;

	@Column(name = "id_hint")
	private Long idHint;

	@Column(name = "id_graduacao")
	private Long idGraduacao;

	@Column(name = "hint")
	private String hint;

	@Column(name = "comentario_obrigatorio")
	private Boolean comentarioObrigatorio;

	@Column(name = "atualizador_original")
	private String atualizadorOriginal;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_atualizacao_original")
	private Date dataAtualizacaoOriginal;

	public AvaliacaoCompDetHint(){
	}

	public br.com.fasolti.avd.domain.AvaliacaoCompDet getAvaliacaoCompDet() {
		return avaliacaoCompDet;
	}

	public void setAvaliacaoCompDet(br.com.fasolti.avd.domain.AvaliacaoCompDet avaliacaoCompDet) {
		this.avaliacaoCompDet = avaliacaoCompDet;
	}

	public Long getIdHint() {
		return idHint;
	}

	public void setIdHint(Long idHint) {
		this.idHint = idHint;
	}

	public Long getIdGraduacao() {
		return idGraduacao;
	}

	public void setIdGraduacao(Long idGraduacao) {
		this.idGraduacao = idGraduacao;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public Boolean getComentarioObrigatorio() {
		return comentarioObrigatorio;
	}

	public void setComentarioObrigatorio(Boolean comentarioObrigatorio) {
		this.comentarioObrigatorio = comentarioObrigatorio;
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
