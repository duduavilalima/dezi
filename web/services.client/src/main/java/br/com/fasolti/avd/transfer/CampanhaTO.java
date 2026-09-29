package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;
import br.com.fasolti.commons.util.TransactionalObject;

public class CampanhaTO extends BaseAuditedTO implements TransactionalObject<Situacao>, SearchObject {

	@Validation(name = "Descrição", max =100, notNull = true)
	private String descricao;

	@Validation(name = "Contratante", notNull = true)
	private EmpresaSimpleTO empresa;

	@Validation(name = "Situação", notNull = true)
	private Situacao situacao;

	private Long totalAvaliacoes;

	private Boolean dimensao;

	private Boolean avEmLiberacao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
	}

	@Override
	public Situacao getStatus() {
		return situacao;
	}

	@Override
	public void setStatus(Situacao situacao) {
		this.situacao = situacao;
	}

	public Long getTotalAvaliacoes() {
		return totalAvaliacoes;
	}

	public void setTotalAvaliacoes(Long totalAvaliacoes) {
		this.totalAvaliacoes = totalAvaliacoes;
	}

	public Boolean getDimensao() {
		return dimensao;
	}

	public void setDimensao(Boolean dimensao) {
		this.dimensao = dimensao;
	}

	@Override
	public String toString() {
		return String.format("Campanha '%s'", descricao);
	}

	public Boolean getAvEmLiberacao() {
		return avEmLiberacao;
	}

	public void setAvEmLiberacao(Boolean avEmLiberacao) {
		this.avEmLiberacao = avEmLiberacao;
	}
}
