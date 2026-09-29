package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.SituacaoFuncIdioma;
import br.com.fasolti.commons.client.transfer.SearchObject;

public class FuncionarioIdiomaTO extends BaseAuditedTO implements SearchObject {

	private FuncionarioTO funcionario;
	private IdiomaTO idioma;
	private SituacaoFuncIdioma situacaoNivelFala;
	private SituacaoFuncIdioma situacaoNivelEscrita;
	private SituacaoFuncIdioma situacaoNivelLeitura;

	public FuncionarioTO getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioTO funcionario) {
		this.funcionario = funcionario;
	}

	public IdiomaTO getIdioma() {
		return idioma;
	}

	public void setIdioma(IdiomaTO idioma) {
		this.idioma = idioma;
	}

	public SituacaoFuncIdioma getSituacaoNivelFala() {
		return situacaoNivelFala;
	}

	public void setSituacaoNivelFala(SituacaoFuncIdioma situacaoNivelFala) {
		this.situacaoNivelFala = situacaoNivelFala;
	}

	public SituacaoFuncIdioma getSituacaoNivelEscrita() {
		return situacaoNivelEscrita;
	}

	public void setSituacaoNivelEscrita(SituacaoFuncIdioma situacaoNivelEscrita) {
		this.situacaoNivelEscrita = situacaoNivelEscrita;
	}

	public SituacaoFuncIdioma getSituacaoNivelLeitura() {
		return situacaoNivelLeitura;
	}

	public void setSituacaoNivelLeitura(SituacaoFuncIdioma situacaoNivelLeitura) {
		this.situacaoNivelLeitura = situacaoNivelLeitura;
	}
}
