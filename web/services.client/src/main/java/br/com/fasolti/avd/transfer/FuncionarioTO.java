package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.CargoSimpleTO;
import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.transfer.util.SituationalObject;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.SituacaoFuncionario;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.avd.types.TipoSexo;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;

public class FuncionarioTO extends BaseAuditedTO implements SearchObject, SituationalObject {

	@Validation(name = "Data de admissão", notNull = true)
	private Date admissao;

	@Validation(name = "Data início do cargo", notNull = true)
	private Date dataCargo;

	private Date desligamento;

	@Validation(name = "E-mail", max = 40, notNull = true)
	private String emailFuncional;

	@Validation(name = "Foto nome arquivo", max = 500)
	private String fotoNomeArquivo;

	private Integer fotoTamanhoArquivo;

	@Validation(name = "Matrícula", max = 100, notNull = true)
	private String matricula;

	@Validation(name = "Data de nascimento", notNull = true)
	private Date nascimento;

	private Date dataInicioRecesso;

	private Date dataFimRecesso;

	@Validation(name = "Nome", max = 80, notNull = true)
	private String nome;

	@Validation(name = "Preenchimento de formação e idioma", notNull = true)
	private Boolean preencheFormacaoEIdiomas;

	private Boolean primeiroAcesso;

	@Validation(name = "Cargo", notNull = true)
	private CargoSimpleTO cargo;

	@Validation(name = "Departamento", notNull = true)
	private DepartamentoTO departamento;

	@Validation(name = "Empresa", notNull = true)
	private EmpresaSimpleTO empresa;

	private FuncionarioSimpleTO avaliador;

	private FuncionarioSimpleTO revisor;

	@Validation(name = "Situação funcional", notNull = true)
	private SituacaoFuncionario situacaoFuncional;

	@Validation(name = "Tipo de perfil", notNull = true)
	private TipoPerfil tipoPerfil;

	@Validation(name = "Sexo", notNull = true)
	private TipoSexo tipoSexo;

	private Boolean pontoFocalRh;

	@Validation(name = "Situação", notNull = true)
	private Situacao situacao;

	private UnidadeTO unidade;

	public Date getAdmissao() {
		return admissao;
	}

	public void setAdmissao(Date admissao) {
		this.admissao = admissao;
	}

	public Date getDataCargo() {
		return dataCargo;
	}

	public void setDataCargo(Date dataCargo) {
		this.dataCargo = dataCargo;
	}

	public Date getDesligamento() {
		return desligamento;
	}

	public void setDesligamento(Date desligamento) {
		this.desligamento = desligamento;
	}

	public String getEmailFuncional() {
		return emailFuncional;
	}

	public void setEmailFuncional(String emailFuncional) {
		this.emailFuncional = emailFuncional;
	}

	public String getFotoNomeArquivo() {
		return fotoNomeArquivo;
	}

	public void setFotoNomeArquivo(String fotoNomeArquivo) {
		this.fotoNomeArquivo = fotoNomeArquivo;
	}

	public Integer getFotoTamanhoArquivo() {
		return fotoTamanhoArquivo;
	}

	public void setFotoTamanhoArquivo(Integer fotoTamanhoArquivo) {
		this.fotoTamanhoArquivo = fotoTamanhoArquivo;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Date getNascimento() {
		return nascimento;
	}

	public void setNascimento(Date nascimento) {
		this.nascimento = nascimento;
	}

	public Date getDataFimRecesso() { return dataFimRecesso; }

	public void setDataFimRecesso(Date dataFimRecesso) { this.dataFimRecesso = dataFimRecesso; }

	public Date getDataInicioRecesso() { return dataInicioRecesso; }

	public void setDataInicioRecesso(Date dataIncioRecesso) { this.dataInicioRecesso = dataIncioRecesso; }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Boolean getPreencheFormacaoEIdiomas() {
		return preencheFormacaoEIdiomas;
	}

	public void setPreencheFormacaoEIdiomas(Boolean preencheFormacaoEIdiomas) {
		this.preencheFormacaoEIdiomas = preencheFormacaoEIdiomas;
	}

	public Boolean getPrimeiroAcesso() {
		return primeiroAcesso;
	}

	public void setPrimeiroAcesso(Boolean primeiroAcesso) {
		this.primeiroAcesso = primeiroAcesso;
	}

	public CargoSimpleTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoSimpleTO cargo) {
		this.cargo = cargo;
	}

	public DepartamentoTO getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoTO departamento) {
		this.departamento = departamento;
	}

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
	}

	public FuncionarioSimpleTO getAvaliador() {
		return avaliador;
	}

	public void setAvaliador(FuncionarioSimpleTO avaliador) {
		this.avaliador = avaliador;
	}

	public FuncionarioSimpleTO getRevisor() {
		return revisor;
	}

	public void setRevisor(FuncionarioSimpleTO revisor) {
		this.revisor = revisor;
	}

	public SituacaoFuncionario getSituacaoFuncional() {
		return situacaoFuncional;
	}

	public void setSituacaoFuncional(SituacaoFuncionario situacaoFuncional) {
		this.situacaoFuncional = situacaoFuncional;
	}

	public TipoPerfil getTipoPerfil() {
		return tipoPerfil;
	}

	public void setTipoPerfil(TipoPerfil tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}

	public TipoSexo getTipoSexo() {
		return tipoSexo;
	}

	public void setTipoSexo(TipoSexo tipoSexo) {
		this.tipoSexo = tipoSexo;
	}

	public Boolean getPontoFocalRh() {
		return pontoFocalRh;
	}

	public void setPontoFocalRh(Boolean pontoFocalRh) {
		this.pontoFocalRh = pontoFocalRh;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public UnidadeTO getUnidade() {
		return unidade;
	}

	public void setUnidade(UnidadeTO unidade) {
		this.unidade = unidade;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;

		FuncionarioTO that = (FuncionarioTO) o;

		if(!getId().equals(that.getId())) return false;
		return getMatricula().equals(that.getMatricula());

	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + getId().hashCode();
		result = 31 * result + getMatricula().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "FuncionarioTO{" +
			"id='" + getId() + '\'' +
			", matricula='" + matricula + '\'' +
			", nome='" + nome + '\'' +
			"} ";
	}
}
