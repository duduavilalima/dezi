package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.SituacaoFuncFormacao;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name="funcionario_formacao")
public class FuncionarioFormacao extends BaseAuditedEntity {

	private static final long serialVersionUID = 2069885613776227090L;

	@Column(name="outro_curso", length = 100)
	private String outroCurso;

	@ManyToOne
	@JoinColumn(name="id_curso_especializacao")
	private CursoEspecializacao cursoEspecializacao;

	@ManyToOne
	@JoinColumn(name="id_curso_graduacao")
	private CursoGraduacao cursoGraduacao;

	@ManyToOne
	@JoinColumn(name="id_escolaridade")
	private Escolaridade escolaridade;

	@ManyToOne
	@JoinColumn(name="id_funcionario")
	private Funcionario funcionario;

	@Column(name="id_situacao_conclusao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private SituacaoFuncFormacao situacaoConclusao;

	@ManyToOne
	@JoinColumn(name="id_tipo_curso")
	private TipoCurso tipoCurso;

	public FuncionarioFormacao() {
	}

	public String getOutroCurso() {
		return this.outroCurso;
	}

	public void setOutroCurso(String outroCurso) {
		this.outroCurso = outroCurso;
	}

	public CursoEspecializacao getCursoEspecializacao() {
		return this.cursoEspecializacao;
	}

	public void setCursoEspecializacao(CursoEspecializacao cursoEspecializacao) {
		this.cursoEspecializacao = cursoEspecializacao;
	}

	public CursoGraduacao getCursoGraduacao() {
		return this.cursoGraduacao;
	}

	public void setCursoGraduacao(CursoGraduacao cursoGraduacao) {
		this.cursoGraduacao = cursoGraduacao;
	}

	public Escolaridade getEscolaridade() {
		return this.escolaridade;
	}

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
	}

	public Funcionario getFuncionario() {
		return this.funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public SituacaoFuncFormacao getSituacaoConclusao() {
		return situacaoConclusao;
	}

	public void setSituacaoConclusao(SituacaoFuncFormacao situacaoConclusao) {
		this.situacaoConclusao = situacaoConclusao;
	}

	public TipoCurso getTipoCurso() {
		return this.tipoCurso;
	}

	public void setTipoCurso(TipoCurso tipoCurso) {
		this.tipoCurso = tipoCurso;
	}

}
