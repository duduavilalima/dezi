package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.SituacaoFuncFormacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

public class FuncionarioFormacaoTO extends BaseAuditedTO implements SearchObject {

	@Validation(name = "Outro curso", max = 100, notNull = false)
	private String outroCurso;

	private CursoEspecializacaoTO cursoEspecializacao;

	private CursoGraduacaoTO cursoGraduacao;

	@Validation(name = "Escolaridade", notNull = true)
	private EscolaridadeTO escolaridade;

	@Validation(name = "Funcionário", notNull = true)
	private FuncionarioTO funcionario;

	@Validation(name = "Situação conclusão", notNull = true)
	private SituacaoFuncFormacao situacaoConclusao;

	private TipoCursoTO tipoCurso;

	public String getOutroCurso() {
		return outroCurso;
	}

	public void setOutroCurso(String outroCurso) {
		this.outroCurso = outroCurso;
	}

	public CursoEspecializacaoTO getCursoEspecializacao() {
		return cursoEspecializacao;
	}

	public void setCursoEspecializacao(CursoEspecializacaoTO cursoEspecializacao) {
		this.cursoEspecializacao = cursoEspecializacao;
	}

	public CursoGraduacaoTO getCursoGraduacao() {
		return cursoGraduacao;
	}

	public void setCursoGraduacao(CursoGraduacaoTO cursoGraduacao) {
		this.cursoGraduacao = cursoGraduacao;
	}

	public EscolaridadeTO getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(EscolaridadeTO escolaridade) {
		this.escolaridade = escolaridade;
	}

	public FuncionarioTO getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioTO funcionario) {
		this.funcionario = funcionario;
	}

	public SituacaoFuncFormacao getSituacaoConclusao() {
		return situacaoConclusao;
	}

	public void setSituacaoConclusao(SituacaoFuncFormacao situacaoConclusao) {
		this.situacaoConclusao = situacaoConclusao;
	}

	public TipoCursoTO getTipoCurso() {
		return tipoCurso;
	}

	public void setTipoCurso(TipoCursoTO tipoCurso) {
		this.tipoCurso = tipoCurso;
	}
}
