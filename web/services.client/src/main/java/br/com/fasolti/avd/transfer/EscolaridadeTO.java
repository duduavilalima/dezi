package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class EscolaridadeTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "Nome", max =35, notNull = true)
	private String nome;

	@Validation(name = "Preenchimento outros cursos", notNull = true)
	private Boolean preencheOutrosCursos;

	@Validation(name = "Preenchimento tipo curso", notNull = true)
	private Boolean preencheTipoCurso;

	@Validation(name = "Contratante", notNull = true)
	private ContratanteFasoltiTO contratante;

	@Validation(name = "Situação", notNull = true)
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

	public Boolean getPreencheTipoCurso() {
		return preencheTipoCurso;
	}

	public void setPreencheTipoCurso(Boolean preencheTipoCurso) {
		this.preencheTipoCurso = preencheTipoCurso;
	}

	public ContratanteFasoltiTO getContratante() {
		return contratante;
	}

	public void setContratante(ContratanteFasoltiTO contratante) {
		this.contratante = contratante;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}
}
