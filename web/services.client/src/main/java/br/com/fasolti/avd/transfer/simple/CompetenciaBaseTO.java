package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;

public class CompetenciaBaseTO extends BaseAuditedTO implements Comparable {

	private String descricaoAbreviada;

	private TipoCompetenciaSimpleTO tipo;

	private Situacao situacao;

	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public TipoCompetenciaSimpleTO getTipo() {
		return tipo;
	}

	public void setTipo(TipoCompetenciaSimpleTO tipo) {
		this.tipo = tipo;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public int compareTo(Object o) {
		return getDescricaoAbreviada().compareTo(((CompetenciaBaseTO) o).getDescricaoAbreviada());
	}
}
