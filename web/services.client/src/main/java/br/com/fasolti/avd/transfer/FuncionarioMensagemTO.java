package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.CargoSimpleTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.transfer.util.BaseAuditedTO;
import br.com.fasolti.avd.types.*;

import java.util.Date;

public class FuncionarioMensagemTO extends BaseAuditedTO {

	private Date admissao;

	private Date dataCargo;

	private Date desligamento;

	private String emailFuncional;

	private String fotoNomeArquivo;

	private Integer fotoTamanhoArquivo;

	private String matricula;

	private Date nascimento;

	private String nome;

	private Boolean preencheFormacaoEIdiomas;

	private Boolean primeiroAcesso;

	private String senhaCriptografada;

	private CargoSimpleTO cargo;

	private DepartamentoTO departamento;

	private EmpresaTO empresa;

	private FuncionarioSimpleTO avaliador;

	private FuncionarioSimpleTO revisor;

	private SituacaoFuncionario situacaoFuncional;

	private TipoPerfil tipoPerfil;

	private TipoSexo tipoSexo;

	private Boolean pontoFocalRh;

	private String senhaOriginal;

	private Situacao situacao;

	private ConfiguracaoTO configuracao;

	private TipoPerfilTO tipoPerfis;


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

	public String getSenhaCriptografada() {
		return senhaCriptografada;
	}

	public void setSenhaCriptografada(String senhaCriptografada) {
		this.senhaCriptografada = senhaCriptografada;
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

	public EmpresaTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaTO empresa) {
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

	public String getSenhaOriginal() {
		return senhaOriginal;
	}

	public void setSenhaOriginal(String senhaOriginal) {
		this.senhaOriginal = senhaOriginal;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public ConfiguracaoTO getConfiguracao() {
		return configuracao;
	}

	public void setConfiguracao(ConfiguracaoTO configuracao) {
		this.configuracao = configuracao;
	}

	public TipoPerfilTO getTipoPerfis() {
		return tipoPerfis;
	}

	public void setTipoPerfis(TipoPerfilTO tipoPerfis) {
		this.tipoPerfis = tipoPerfis;
	}
}
