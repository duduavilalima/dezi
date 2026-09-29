package br.com.fasolti.avd.transfer.campanhasrapidas;

import br.com.fasolti.avd.transfer.EmpresaTO;
import br.com.fasolti.avd.transfer.MunicipioTO;
import br.com.fasolti.avd.transfer.UnidadeTO;
import br.com.fasolti.avd.transfer.simple.CargoSimpleTO;
import br.com.fasolti.avd.transfer.simple.DepartamentoSimpleTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.SituacaoFuncionario;
import br.com.fasolti.avd.types.TipoSexo;

import java.util.Date;

public class HomeOfficeBrasilTO {
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
	private TipoSexo tipoSexo;
	//usado na tela de preenchimento por competência
	private String foto;
	private MunicipioTO municipio;
	private Boolean isAmbienteEmpresa;
	private Boolean isDiagnostico;

	public Boolean getIsAmbienteEmpresa() {
		return isAmbienteEmpresa;
	}

	public void setIsAmbienteEmpresa(Boolean ambienteEmpresa) {
		isAmbienteEmpresa = ambienteEmpresa;
	}

	public Boolean getIsDiagnostico() {
		return isDiagnostico;
	}

	public void setIsDiagnostico(Boolean diagnostico) {
		isDiagnostico = diagnostico;
	}

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

	public SituacaoFuncionario getSituacaoFuncional() {
		return situacaoFuncional;
	}

	public void setSituacaoFuncional(SituacaoFuncionario situacaoFuncional) {
		this.situacaoFuncional = situacaoFuncional;
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

	public Date getDataCargo() {
		return dataCargo;
	}

	public void setDataCargo(Date dataCargo) {
		this.dataCargo = dataCargo;
	}

	public EmpresaTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaTO empresa) {
		this.empresa = empresa;
	}

	public Date getDataInicioRecesso() {
		return dataInicioRecesso;
	}

	public void setDataInicioRecesso(Date dataInicioRecesso) {
		this.dataInicioRecesso = dataInicioRecesso;
	}

	public Date getDataFimRecesso() {
		return dataFimRecesso;
	}

	public void setDataFimRecesso(Date dataFimRecesso) {
		this.dataFimRecesso = dataFimRecesso;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public TipoSexo getTipoSexo() {
		return tipoSexo;
	}

	public void setTipoSexo(TipoSexo tipoSexo) {
		this.tipoSexo = tipoSexo;
	}

	public MunicipioTO getMunicipio() {
		return municipio;
	}

	public void setMunicipio(MunicipioTO municipio) {
		this.municipio = municipio;
	}
}
