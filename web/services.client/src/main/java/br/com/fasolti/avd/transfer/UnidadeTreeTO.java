package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.Situacao;

import java.util.List;

public class UnidadeTreeTO extends BaseTO {

	private String nome;

	private String descricao;

	private UnidadeTO unidadePai;

	private EmpresaTO empresa;

	private Situacao situacao;

	private List<UnidadeTreeTO> children;

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

	public UnidadeTO getUnidadePai() {
		return unidadePai;
	}

	public void setUnidadePai(UnidadeTO unidadePai) {
		this.unidadePai = unidadePai;
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

	public List<UnidadeTreeTO> getChildren() {
		return children;
	}

	public void setChildren(List<UnidadeTreeTO> children) {
		this.children = children;
	}
}
