package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.transfer.EmpresaTO;
import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.Situacao;

import java.util.List;

public class ModeloGraduacaoSimpleTO extends BaseTO {

	private String descricaoModelo;

	private Integer qtdGraduacoes;

	private Boolean naoGraduar;

	private List<GraduacaoSimpleTO> graduacoes;

	private EmpresaTO empresa;

	private Situacao situacao;

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

	public Boolean getNaoGraduar() {
		return naoGraduar;
	}

	public void setNaoGraduar(Boolean naoGraduar) {
		this.naoGraduar = naoGraduar;
	}

	public List<GraduacaoSimpleTO> getGraduacoes() {
		return graduacoes;
	}

	public void setGraduacoes(List<GraduacaoSimpleTO> graduacoes) {
		this.graduacoes = graduacoes;
	}

	public EmpresaTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaTO empresa) {
		this.empresa = empresa;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
}
