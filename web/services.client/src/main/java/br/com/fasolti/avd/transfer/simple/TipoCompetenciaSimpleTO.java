package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.Situacao;

public class TipoCompetenciaSimpleTO extends BaseTO {

	private Situacao situacao;

	private String nome;

	private String descricao;

	private String nomeCurto;

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

	public String getNomeCurto() {
		return nomeCurto;
	}

	public void setNomeCurto(String nomeCurto) {
		this.nomeCurto = nomeCurto;
	}
}
