package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.TipoConceito;
import br.com.fasolti.commons.client.transfer.SearchObject;

import java.math.BigDecimal;

public class ConceitoTO  extends BaseTO implements SearchObject {

	private String nome;
	private String descricao;
	private BigDecimal pontuacaoFinal;
	private BigDecimal pontuacaoInicial;
	private EmpresaTO empresa;
	private Situacao situacao;
	private TipoConceito tipo;

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

	public BigDecimal getPontuacaoFinal() {
		return pontuacaoFinal;
	}

	public void setPontuacaoFinal(BigDecimal pontuacaoFinal) {
		this.pontuacaoFinal = pontuacaoFinal;
	}

	public BigDecimal getPontuacaoInicial() {
		return pontuacaoInicial;
	}

	public void setPontuacaoInicial(BigDecimal pontuacaoInicial) {
		this.pontuacaoInicial = pontuacaoInicial;
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

	public TipoConceito getTipo() {
		return tipo;
	}

	public void setTipo(TipoConceito tipo) {
		this.tipo = tipo;
	}
}
