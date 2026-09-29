package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class IdiomaTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "Nome", max = 15, notNull = true)
	private String nome;

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
