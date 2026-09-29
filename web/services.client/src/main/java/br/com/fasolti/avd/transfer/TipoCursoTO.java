package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class TipoCursoTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "Nome", max = 35, notNull = true)
	private String nome;

	@Validation(name = "Preenchimento especializado", notNull = true)
	private Boolean preencheEspecializacao;

	@Validation(name = "Preenchimento graduação", notNull = true)
	private Boolean preencheGraduacao;

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

	public Boolean getPreencheEspecializacao() {
		return preencheEspecializacao;
	}

	public void setPreencheEspecializacao(Boolean preencheEspecializacao) {
		this.preencheEspecializacao = preencheEspecializacao;
	}

	public Boolean getPreencheGraduacao() {
		return preencheGraduacao;
	}

	public void setPreencheGraduacao(Boolean preencheGraduacao) {
		this.preencheGraduacao = preencheGraduacao;
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
