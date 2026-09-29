package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.transfer.EmpresaTO;
import br.com.fasolti.avd.transfer.UnidadeTO;
import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.SituacaoFuncionario;

import java.util.Date;

public class FuncionarioSimpleTO extends BaseTO implements Comparable, Cloneable {
	private String matricula;
	private String nome;
	private String emailFuncional;
	private DepartamentoSimpleTO departamento;
	private UnidadeTO unidade;
	private CargoSimpleTO cargo;
	private Situacao situacao;
	private SituacaoFuncionario situacaoFuncional;
	private Date nascimento;
	private Date admissao;
	private Date dataCargo;
	private EmpresaTO empresa;
	private Date dataInicioRecesso;
	private Date dataFimRecesso;

	//usado na tela de preenchimento por competência
	private String foto;

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmailFuncional() {
		return emailFuncional;
	}

	public void setEmailFuncional(String emailFuncional) {
		this.emailFuncional = emailFuncional;
	}

	public DepartamentoSimpleTO getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoSimpleTO departamento) {
		this.departamento = departamento;
	}

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
	}

	public CargoSimpleTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoSimpleTO cargo) {
		this.cargo = cargo;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public Date getAdmissao() {
		return admissao;
	}

	public void setAdmissao(Date admissao) {
		this.admissao = admissao;
	}

	public EmpresaTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaTO empresa) {
		this.empresa = empresa;
	}

	public Date getDataCargo() {
		return dataCargo;
	}

	public void setDataCargo(Date dataCargo) {
		this.dataCargo = dataCargo;
	}

	public SituacaoFuncionario getSituacaoFuncional() {
		return situacaoFuncional;
	}

	public void setSituacaoFuncional(SituacaoFuncionario situacaoFuncional) {
		this.situacaoFuncional = situacaoFuncional;
	}

	public Date getDataInicioRecesso() { return dataInicioRecesso; }

	public void setDataInicioRecesso(Date dataInicioRecesso) { this.dataInicioRecesso = dataInicioRecesso; }

	public Date getDataFimRecesso() { return dataFimRecesso; }

	public void setDataFimRecesso(Date dataFimRecesso) { this.dataFimRecesso = dataFimRecesso; }

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof FuncionarioSimpleTO)) return false;
		if (!super.equals(o)) return false;

		FuncionarioSimpleTO that = (FuncionarioSimpleTO) o;

		if (getMatricula() != null ? !getMatricula().equals(that.getMatricula()) : that.getMatricula() != null)
			return false;
		if (getNome() != null ? !getNome().equals(that.getNome()) : that.getNome() != null) return false;
		if (getEmailFuncional() != null ? !getEmailFuncional().equals(that.getEmailFuncional()) : that.getEmailFuncional() != null)
			return false;
		if (getSituacao() != that.getSituacao()) return false;
		if (getSituacaoFuncional() != that.getSituacaoFuncional()) return false;
		if (getNascimento() != null ? !getNascimento().equals(that.getNascimento()) : that.getNascimento() != null)
			return false;
		if (getAdmissao() != null ? !getAdmissao().equals(that.getAdmissao()) : that.getAdmissao() != null)
			return false;
		if (getDataCargo() != null ? !getDataCargo().equals(that.getDataCargo()) : that.getDataCargo() != null)
			return false;
		if (getDataInicioRecesso() != null ? !getDataInicioRecesso().equals(that.getDataInicioRecesso()) : that.getDataInicioRecesso() != null)
			return false;
		if (getDataFimRecesso() != null ? !getDataFimRecesso().equals(that.getDataFimRecesso()) : that.getDataFimRecesso() != null)
			return false;
		return getFoto() != null ? getFoto().equals(that.getFoto()) : that.getFoto() == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (getMatricula() != null ? getMatricula().hashCode() : 0);
		result = 31 * result + (getNome() != null ? getNome().hashCode() : 0);
		result = 31 * result + (getEmailFuncional() != null ? getEmailFuncional().hashCode() : 0);
		result = 31 * result + (getSituacao() != null ? getSituacao().hashCode() : 0);
		result = 31 * result + (getSituacaoFuncional() != null ? getSituacaoFuncional().hashCode() : 0);
		result = 31 * result + (getNascimento() != null ? getNascimento().hashCode() : 0);
		result = 31 * result + (getAdmissao() != null ? getAdmissao().hashCode() : 0);
		result = 31 * result + (getDataCargo() != null ? getDataCargo().hashCode() : 0);
		result = 31 * result + (getDataInicioRecesso() != null ? getDataInicioRecesso().hashCode() : 0);
		result = 31 * result + (getDataFimRecesso() != null ? getDataFimRecesso().hashCode() : 0);
		result = 31 * result + (getFoto() != null ? getFoto().hashCode() : 0);
		return result;
	}

	@Override
	public int compareTo(Object o) {
		return ((FuncionarioSimpleTO) o).getNome().compareTo(getNome());
	}

	@Override
	public FuncionarioSimpleTO clone() throws CloneNotSupportedException {
		return (FuncionarioSimpleTO) super.clone();
	}
}
