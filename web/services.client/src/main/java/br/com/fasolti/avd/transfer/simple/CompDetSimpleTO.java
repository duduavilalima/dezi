package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.Situacao;

public class CompDetSimpleTO extends BaseTO {

	private String descricao;

	private Integer ordem;

	private CompetenciaSimpleTO competencia;

	private Situacao situacao;

	private ModeloGraduacaoSimpleTO modeloGraduacao;

	public CompDetSimpleTO() {

	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	public CompetenciaSimpleTO getCompetencia() {
		return competencia;
	}

	public void setCompetencia(CompetenciaSimpleTO competencia) {
		this.competencia = competencia;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public ModeloGraduacaoSimpleTO getModeloGraduacao() {
		return modeloGraduacao;
	}

	public void setModeloGraduacao(ModeloGraduacaoSimpleTO modeloGraduacao) {
		this.modeloGraduacao = modeloGraduacao;
	}
}
