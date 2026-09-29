package br.com.fasolti.avd.domain;

import javax.persistence.*;

@Entity
@Table(name="compet_detalhe_calibrahint")
public class CompDetalhamentoHint extends BaseEntity {

	@Column(name="hint")
	private String hint;

	@Column(name = "comentario_obrigatorio")
	private Boolean comentarioObrigatorio;

	@ManyToOne
	@JoinColumn(name="id_det_competencia")
	private CompetenciaDetalhamento competenciaDetalhamento;

	@ManyToOne
	@JoinColumn(name="id_graduacao")
	private Graduacao graduacao;

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public CompetenciaDetalhamento getCompetenciaDetalhamento() {
		return competenciaDetalhamento;
	}

	public void setCompetenciaDetalhamento(CompetenciaDetalhamento competenciaDetalhamento) {
		this.competenciaDetalhamento = competenciaDetalhamento;
	}

	public Graduacao getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(Graduacao graduacao) {
		this.graduacao = graduacao;
	}

	public Boolean getComentarioObrigatorio() {
		return comentarioObrigatorio;
	}

	public void setComentarioObrigatorio(Boolean comentarioObrigatorio) {
		this.comentarioObrigatorio = comentarioObrigatorio;
	}
}
