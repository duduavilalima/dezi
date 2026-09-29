package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.types.Situacao;

import java.util.List;

public class ArvoreTipoCompetenciaTO {

	public Long id;

	public Long idPai;

	private EmpresaTO empresa;

	private Situacao situacao;

	private String nome;

	private String descricao;

	private List<ArvoreTipoCompetenciaTO> children;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdPai() {
		return idPai;
	}

	public void setIdPai(Long idPai) {
		this.idPai = idPai;
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

	public List<ArvoreTipoCompetenciaTO> getChildren() {
		return children;
	}

	public void setChildren(List<ArvoreTipoCompetenciaTO> children) {
		this.children = children;
	}

}
