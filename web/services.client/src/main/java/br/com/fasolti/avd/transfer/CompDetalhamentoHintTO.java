package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.commons.client.transfer.SearchObject;
import org.apache.commons.lang3.ObjectUtils;

public class CompDetalhamentoHintTO extends BaseAuditedTO implements SearchObject, Comparable {

	private String hint;

	private Boolean comentarioObrigatorio;

	private CompetenciaDetalhamentoTO competenciaDetalhamento;

	private GraduacaoTO graduacao;

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public CompetenciaDetalhamentoTO getCompetenciaDetalhamento() {
		return competenciaDetalhamento;
	}

	public void setCompetenciaDetalhamento(CompetenciaDetalhamentoTO competenciaDetalhamento) {
		this.competenciaDetalhamento = competenciaDetalhamento;
	}

	public GraduacaoTO getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(GraduacaoTO graduacao) {
		this.graduacao = graduacao;
	}

	public Boolean getComentarioObrigatorio() {
		return comentarioObrigatorio;
	}

	public void setComentarioObrigatorio(Boolean comentarioObrigatorio) {
		this.comentarioObrigatorio = comentarioObrigatorio;
	}

	@Override
	public int compareTo(Object o) {
		return ObjectUtils.compare(getGraduacao().getOrdem(), ((CompDetalhamentoHintTO) o).getGraduacao().getOrdem());
	}
}

