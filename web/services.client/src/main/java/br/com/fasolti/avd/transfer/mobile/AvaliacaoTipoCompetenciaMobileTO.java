package br.com.fasolti.avd.transfer.mobile;

import br.com.fasolti.commons.client.transfer.TransferObject;

import java.util.List;

public class AvaliacaoTipoCompetenciaMobileTO implements Comparable, TransferObject {
	private Long id; /*Id do Tipo de Competencia, nao da AVALIACAO tipo competencia!*/
	private String descricaoTipoCompetencia;
	private List<CompetenciaMobileTO> listaCompetencias;
	private Integer diasRestantes;
	private Double porcentagemPreenchimentoPorCompetencia;
	private Double porcentagemPreenchimentoPorPreenchimento;

	private Double qtdCompletosPorCompetencia;
	private Double qtdCompletosPorPreenchimento; //Quantidade geral de preenchimentos efetuados em todas as competencias
	private Integer qtdTotalPorPreenchimento; //Quantidade total de preenchimentos de todas as competencias

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricaoTipoCompetencia() {
		return descricaoTipoCompetencia;
	}

	public void setDescricaoTipoCompetencia(String descricaoTipoCompetencia) {
		this.descricaoTipoCompetencia = descricaoTipoCompetencia;
	}

	public List<CompetenciaMobileTO> getListaCompetencias() {
		return listaCompetencias;
	}

	public void setListaCompetencias(List<CompetenciaMobileTO> listaCompetencias) {
		this.listaCompetencias = listaCompetencias;
	}

	public Integer getDiasRestantes() {
		return diasRestantes;
	}

	public void setDiasRestantes(Integer diasRestantes) {
		this.diasRestantes = diasRestantes;
	}


	public Double getPorcentagemPreenchimentoPorCompetencia() {
		return porcentagemPreenchimentoPorCompetencia;
	}

	public void setPorcentagemPreenchimentoPorCompetencia(Double porcentagemPreenchimentoPorCompetencia) {
		this.porcentagemPreenchimentoPorCompetencia = porcentagemPreenchimentoPorCompetencia;
	}

	public Double getPorcentagemPreenchimentoPorPreenchimento() {
		return porcentagemPreenchimentoPorPreenchimento;
	}

	public void setPorcentagemPreenchimentoPorPreenchimento(Double porcentagemPreenchimentoPorPreenchimento) {
		this.porcentagemPreenchimentoPorPreenchimento = porcentagemPreenchimentoPorPreenchimento;
	}

	public Double getQtdCompletosPorCompetencia() {
		return qtdCompletosPorCompetencia;
	}

	public void setQtdCompletosPorCompetencia(Double qtdCompletosPorCompetencia) {
		this.qtdCompletosPorCompetencia = qtdCompletosPorCompetencia;
	}

	public Double getQtdCompletosPorPreenchimento() {
		return qtdCompletosPorPreenchimento;
	}

	public void setQtdCompletosPorPreenchimento(Double qtdCompletosPorPreenchimento) {
		this.qtdCompletosPorPreenchimento = qtdCompletosPorPreenchimento;
	}

	public Integer getQtdTotalPorPreenchimento() {
		return qtdTotalPorPreenchimento;
	}

	public void setQtdTotalPorPreenchimento(Integer qtdTotalPorPreenchimento) {
		this.qtdTotalPorPreenchimento = qtdTotalPorPreenchimento;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AvaliacaoTipoCompetenciaMobileTO that = (AvaliacaoTipoCompetenciaMobileTO) o;

		return id.equals(that.id);
	}


	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public int compareTo(Object o) {
		return getDescricaoTipoCompetencia().compareTo(((AvaliacaoTipoCompetenciaMobileTO) o).getDescricaoTipoCompetencia());
	}
}
