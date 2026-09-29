package br.com.fasolti.avd.transfer.mobile;

import br.com.fasolti.commons.client.transfer.TransferObject;

import java.util.List;

public class AvaliacaoMobileTO implements Comparable, TransferObject {
	private Long id;
	private String descricaoAbreviada;
	private List<AvaliacaoTipoCompetenciaMobileTO> avaliacaoTipoCompetenciaList;
//	private Long idAvaliador;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricaoAbreviada() {
		return descricaoAbreviada;
	}

	public void setDescricaoAbreviada(String descricaoAbreviada) {
		this.descricaoAbreviada = descricaoAbreviada;
	}

	public List<AvaliacaoTipoCompetenciaMobileTO> getAvaliacaoTipoCompetenciaList() {
		return avaliacaoTipoCompetenciaList;
	}

	public void setAvaliacaoTipoCompetenciaList(List<AvaliacaoTipoCompetenciaMobileTO> avaliacaoTipoCompetenciaList) {
		this.avaliacaoTipoCompetenciaList = avaliacaoTipoCompetenciaList;
	}

	@Override
	public int compareTo(Object o) { return getDescricaoAbreviada().compareTo(((AvaliacaoMobileTO) o).getDescricaoAbreviada()); }
}
