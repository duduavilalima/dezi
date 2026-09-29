package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.CompetenciaSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.List;

public class CompetenciaDetalhamentoTO extends BaseAuditedTO implements  SearchObject {

	@Validation(name = "Detalhamento", notNull = true)
	private String descricao;

	private Integer ordem;

	@Validation(name = "Competência", notNull = true)
	private CompetenciaSimpleTO competencia;

	@Validation(name = "Situação", notNull = true)
	private Situacao situacao;

	private ModeloGraduacaoTO modeloGraduacao;

	private List<CompDetalhamentoHintTO> competenciaDetalhamentosCalibrahint;

	public CompetenciaDetalhamentoTO() {
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

	public List<CompDetalhamentoHintTO> getCompetenciaDetalhamentosCalibrahint() {
		return competenciaDetalhamentosCalibrahint;
	}

	public void setCompetenciaDetalhamentosCalibrahint(List<CompDetalhamentoHintTO> competenciaDetalhamentosCalibrahint) {
		this.competenciaDetalhamentosCalibrahint = competenciaDetalhamentosCalibrahint;
	}

	public ModeloGraduacaoTO getModeloGraduacao() {
		return modeloGraduacao;
	}

	public void setModeloGraduacao(ModeloGraduacaoTO modeloGraduacao) {
		this.modeloGraduacao = modeloGraduacao;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	@Override
	public String toString() {
		return "CompetenciaDetalhamentoTO{" +
				"descricao='" + descricao + '\'' +
				", ordem=" + ordem +
				", competencia=" + competencia +
				", situacao=" + situacao +
				", modeloGraduacao=" + modeloGraduacao +
				", competenciaDetalhamentosCalibrahint=" + competenciaDetalhamentosCalibrahint +
				"} " + super.toString();
	}
}
