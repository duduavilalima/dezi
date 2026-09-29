package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.List;

public class ModeloGraduacaoTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "Descrição", max = 80)
	private String descricaoModelo;

	private Integer qtdGraduacoes;

	private Boolean naoGraduar;

	private List<GraduacaoTO> graduacoes;

	@Validation(name = "Empresa", notNull = true)
	private EmpresaSimpleTO empresa;

	@Validation(name = "Situação", notNull = true)
	private Situacao situacao;

	@Validation(name = "Dimensão", notNull = true)
	private DimensaoTO dimensao;

	public String getDescricaoModelo() {
		return descricaoModelo;
	}

	public void setDescricaoModelo(String descricaoModelo) {
		this.descricaoModelo = descricaoModelo;
	}

	public Integer getQtdGraduacoes() {
		return qtdGraduacoes;
	}

	public void setQtdGraduacoes(Integer qtdGraduacoes) {
		this.qtdGraduacoes = qtdGraduacoes;
	}

	public List<GraduacaoTO> getGraduacoes() {
		return graduacoes;
	}

	public void setGraduacoes(List<GraduacaoTO> graduacoes) {
		this.graduacoes = graduacoes;
	}

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public DimensaoTO getDimensao() {
		return dimensao;
	}

	public void setDimensao(DimensaoTO dimensao) {
		this.dimensao = dimensao;
	}

	public Boolean getNaoGraduar() {
		return naoGraduar;
	}

	public void setNaoGraduar(Boolean naoGraduar) {
		this.naoGraduar = naoGraduar;
	}
}
