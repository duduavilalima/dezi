package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;
import br.com.fasolti.commons.util.TransactionalObject;

import java.util.List;

public class CompetenciaTO extends BaseAuditedTO implements TransactionalObject<Situacao>, Comparable, SearchObject {

	@Validation(name = "Descrição abreviada", max = 150, notNull = true)
	private String descricaoAbreviada;

	private String direcionamentoGestor;

	private String direcionamentoNaoGestor;

	private EmpresaSimpleTO empresa;

	@Validation(name = "Tipo de competência", notNull = true)
	private TipoCompetenciaTO tipo;

	private ModeloGraduacaoTO modeloGraduacao;

	private Situacao situacao;

	private List<CompetenciaDetalhamentoTO> detalhamentos;

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

	public String getDirecionamentoNaoGestor() {
		return direcionamentoNaoGestor;
	}

	public void setDirecionamentoNaoGestor(String direcionamentoNaoGestor) {
		this.direcionamentoNaoGestor = direcionamentoNaoGestor;
	}

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
	}

	public TipoCompetenciaTO getTipo() {
		return tipo;
	}

	public void setTipo(TipoCompetenciaTO tipo) {
		this.tipo = tipo;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public List<CompetenciaDetalhamentoTO> getDetalhamentos() {
		return detalhamentos;
	}

	public void setDetalhamentos(List<CompetenciaDetalhamentoTO> detalhamentos) {
		this.detalhamentos = detalhamentos;
	}

	public ModeloGraduacaoTO getModeloGraduacao() {
		return modeloGraduacao;
	}

	public void setModeloGraduacao(ModeloGraduacaoTO modeloGraduacao) {
		this.modeloGraduacao = modeloGraduacao;
	}

	@Override
	public int compareTo(Object o) {
		return getDescricaoAbreviada().compareTo(((CompetenciaTO) o).getDescricaoAbreviada());
	}

	@Override
	public Situacao getStatus() {
		return situacao;
	}

	@Override
	public void setStatus(Situacao situacao) {
		this.situacao = situacao;
	}
}
