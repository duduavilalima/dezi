package br.com.fasolti.avd.transfer.mobile;

import java.util.List;

public class CompetenciaMobileTO {

	private Long id; /*ID da avaliacaoCompetencia, não da competencia em si!*/
	private Long avaliacaoId;
	private String descricaoAbreviada;

	private Double porcentagemPreenchimento;
	private String direcionamentoGestor;

	private List<DetalhamentoMobileTO> listaDetalhamentos;

	private TipoCompetenciaMobileTO tipo;
	private Double qtdCompletosPorPreenchimento; //Quantidade de preenchimentos efetuados nesta competencia
	private Integer qtdTotalDePreenchimentos; //Quantidade total de preenchimentos

	public Long getId() {
		return id;
	}

	public TipoCompetenciaMobileTO getTipo() {
		return tipo;
	}

	public void setTipo(TipoCompetenciaMobileTO tipo) {
		this.tipo = tipo;
	}

	public Double getPorcentagemPreenchimento() {
		return porcentagemPreenchimento;
	}

	public void setPorcentagemPreenchimento(Double porcentagemPreenchimento) {
		this.porcentagemPreenchimento = porcentagemPreenchimento;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAvaliacaoId() {
		return avaliacaoId;
	}

	public void setAvaliacaoId(Long avaliacaoId) {
		this.avaliacaoId = avaliacaoId;
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

	public List<DetalhamentoMobileTO> getListaDetalhamentos() {
		return listaDetalhamentos;
	}

	public void setListaDetalhamentos(List<DetalhamentoMobileTO> listaDetalhamentos) {
		this.listaDetalhamentos = listaDetalhamentos;
	}

	public Double getQtdCompletosPorPreenchimento() {
		return qtdCompletosPorPreenchimento;
	}

	public void setQtdCompletosPorPreenchimento(Double qtdCompletosPorPreenchimento) {
		this.qtdCompletosPorPreenchimento = qtdCompletosPorPreenchimento;
	}

	public Integer getQtdTotalDePreenchimentos() {
		return qtdTotalDePreenchimentos;
	}

	public void setQtdTotalDePreenchimentos(Integer qtdTotalDePreenchimentos) {
		this.qtdTotalDePreenchimentos = qtdTotalDePreenchimentos;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CompetenciaMobileTO that = (CompetenciaMobileTO) o;

		return id.equals(that.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

}
