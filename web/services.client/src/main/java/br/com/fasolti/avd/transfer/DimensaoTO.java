package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.List;

public class DimensaoTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "Dimensão", max = 60, notNull = true)
	private String nome;

	private String descricao;

	@Validation(name = "Situação", notNull = true)
	private Situacao situacao;

	private EmpresaSimpleTO empresa;

	private List<DimensaoCompetenciaDetalhamentoTO> detalhamentos;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
	}

	public List<DimensaoCompetenciaDetalhamentoTO> getDetalhamentos() {
		return detalhamentos;
	}

	public void setDetalhamentos(List<DimensaoCompetenciaDetalhamentoTO> detalhamentos) {
		this.detalhamentos = detalhamentos;
	}
}
