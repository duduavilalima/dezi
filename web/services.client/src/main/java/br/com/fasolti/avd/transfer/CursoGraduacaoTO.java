package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;

public class CursoGraduacaoTO extends BaseAuditedTO implements SearchObject {

	private String nome;

	private Boolean preencheOutrosCursos;

	private ContratanteFasoltiTO contratante;

	private Situacao situacao;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getPreencheOutrosCursos() {
		return preencheOutrosCursos;
	}

	public void setPreencheOutrosCursos(Boolean preencheOutrosCursos) {
		this.preencheOutrosCursos = preencheOutrosCursos;
	}

	public void setContratante(ContratanteFasoltiTO contratante) {
		this.contratante = contratante;
	}

	public ContratanteFasoltiTO getContratante() {
		return contratante;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
}
