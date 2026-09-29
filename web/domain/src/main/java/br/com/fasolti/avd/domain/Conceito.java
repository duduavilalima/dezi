package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.TipoComp;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class Conceito extends BaseEntity {

	private static final long serialVersionUID = -2820302146907155108L;

	@Column(name = "nome")
	private String nome;

	@Column(name = "descricao")
	private String descricao;

	@Column(name="pontuacao_final")
	private BigDecimal pontuacaoFinal;

	@Column(name="pontuacao_inicial")
	private BigDecimal pontuacaoInicial;

	@ManyToOne
	@JoinColumn(name="id_empresa")
	private Empresa empresa;

	@Column(name="id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	@Column(name="id_tipo_conceito")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private TipoComp tipo; //Somente para persistir o Conceito, não existe tipo para conceito atualmente.//

	public Conceito() {
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getPontuacaoFinal() {
		return this.pontuacaoFinal;
	}

	public void setPontuacaoFinal(BigDecimal pontuacaoFinal) {
		this.pontuacaoFinal = pontuacaoFinal;
	}

	public BigDecimal getPontuacaoInicial() {
		return this.pontuacaoInicial;
	}

	public void setPontuacaoInicial(BigDecimal pontuacaoInicial) {
		this.pontuacaoInicial = pontuacaoInicial;
	}

	public Empresa getEmpresa() {
		return this.empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public TipoComp getTipo() {
		return this.tipo;
	}

	public void setTipo(TipoComp tipo) {
		this.tipo = tipo;
	}

}
