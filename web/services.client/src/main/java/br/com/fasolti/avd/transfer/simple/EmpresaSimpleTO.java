package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.transfer.ContratanteFasoltiTO;
import br.com.fasolti.avd.transfer.util.BaseTO;

public class EmpresaSimpleTO extends BaseTO {

	private String nome;

	private String identificador;

	private String host;

	private ContratanteFasoltiTO contratante;

	private Integer versaoBi;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getIdentificador() {
		return identificador;
	}

	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public ContratanteFasoltiTO getContratante() {
		return contratante;
	}

	public void setContratante(ContratanteFasoltiTO contratante) {
		this.contratante = contratante;
	}

	public Integer getVersaoBi() {
		return versaoBi;
	}

	public void setVersaoBi(Integer versaoBi) {
		this.versaoBi = versaoBi;
	}
}
