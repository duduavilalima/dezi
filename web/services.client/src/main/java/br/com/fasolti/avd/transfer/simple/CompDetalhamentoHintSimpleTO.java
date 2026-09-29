package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.transfer.util.BaseTO;

public class CompDetalhamentoHintSimpleTO extends BaseTO {

	private String hint;

	private Boolean comentarioObrigatorio;

	private CompetenciaDetalhamentoSimpleTO competenciaDetalhamento;

	private GraduacaoSimpleTO graduacao;

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

	public CompetenciaDetalhamentoSimpleTO getCompetenciaDetalhamento() {
		return competenciaDetalhamento;
	}

	public void setCompetenciaDetalhamento(CompetenciaDetalhamentoSimpleTO competenciaDetalhamento) {
		this.competenciaDetalhamento = competenciaDetalhamento;
	}

	public GraduacaoSimpleTO getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(GraduacaoSimpleTO graduacao) {
		this.graduacao = graduacao;
	}
}
