package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.SituacaoFuncIdioma;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "funcionario_idioma")
public class FuncionarioIdioma extends BaseAuditedEntity {

	private static final long serialVersionUID = -1583022471924986760L;

	@ManyToOne
	@JoinColumn(name = "id_funcionario")
	private Funcionario funcionario;

	@ManyToOne
	@JoinColumn(name = "id_idioma")
	private Idioma idioma;

	@Column(name = "id_situacao_nivel_fala")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private SituacaoFuncIdioma situacaoNivelFala;

	@Column(name = "id_situacao_nivel_escrita")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private SituacaoFuncIdioma situacaoNivelEscrita;

	@Column(name = "id_situacao_nivel_leitura")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private SituacaoFuncIdioma situacaoNivelLeitura;

	public FuncionarioIdioma() {
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Idioma getIdioma() {
		return idioma;
	}

	public void setIdioma(Idioma idioma) {
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
