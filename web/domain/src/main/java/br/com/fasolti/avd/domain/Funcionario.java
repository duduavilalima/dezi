package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.SituacaoFuncionario;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.avd.types.TipoSexo;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Funcionario extends BaseAuditedEntity {

	private static final long serialVersionUID = -7223127632605475940L;

	@Temporal(TemporalType.DATE)
	private Date admissao;

	@Temporal(TemporalType.DATE)
	@Column(name = "cargo") // FIXME Mudar a tabela para um nome mais descritivo
	private Date dataCargo;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_inicio_recesso")
	private Date dataInicioRecesso;

	@Temporal(TemporalType.DATE)
	@Column(name = "data_fim_recesso")
	private Date dataFimRecesso;

	@Temporal(TemporalType.DATE)
	private Date desligamento;

	@Column(name = "email_funcional")
	private String emailFuncional;

	@Column(name = "foto_nome_arquivo")
	private String fotoNomeArquivo;

	@Column(name = "foto_tamanho_arquivo")
	private Integer fotoTamanhoArquivo;

	private String matricula;

	@Temporal(TemporalType.DATE)
	private Date nascimento;

	private String nome;

	@Column(name = "ponto_focal_rh")
	private Boolean pontoFocalRh;

	@Column(name = "preenche_formacao_e_idiomas")
	private Boolean preencheFormacaoEIdiomas;

	@Column(name = "primeiro_acesso")
	private Boolean primeiroAcesso;

	@Column(name = "senha")
	private String senhaCriptografada;

	@ManyToOne
	@JoinColumn(name = "id_cargo")
	private Cargo cargo;

	@ManyToOne
	@JoinColumn(name = "id_departamento")
	private Departamento departamento;

	@ManyToOne
	@JoinColumn(name = "id_empresa")
	private Empresa empresa;

	@ManyToOne
	@JoinColumn(name = "id_unidade")
	private Unidade unidade;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private Situacao situacao;

	@Column(name = "id_situacao_funcional")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private SituacaoFuncionario situacaoFuncional;

	@Column(name = "id_tipo_perfil_uso")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private TipoPerfil tipoPerfil;

	@Column(name = "id_tipo_sexo")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private TipoSexo tipoSexo;

	@Transient
	private String senhaOriginal;

	public Funcionario() {
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

	public Date getDataInicioRecesso() { return dataInicioRecesso; }

	public void setDataInicioRecesso(Date dataInicioRecesso) { this.dataInicioRecesso = dataInicioRecesso; }

	public Date getDataFimRecesso() { return dataFimRecesso; }

	public void setDataFimRecesso(Date dataFimRecesso) { this.dataFimRecesso = dataFimRecesso; }

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

	public Boolean getPontoFocalRh() {
		return pontoFocalRh;
	}

	public void setPontoFocalRh(Boolean pontoFocalRh) {
		this.pontoFocalRh = pontoFocalRh;
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

	public Cargo getCargo() {
		return cargo;
	}

	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Unidade getUnidade() {
		return unidade;
	}

	public void setUnidade(Unidade unidade) {
		this.unidade = unidade;
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

	public String getSenhaOriginal() {
		return senhaOriginal;
	}

	public void setSenhaOriginal(String senhaOriginal) {
		this.senhaOriginal = senhaOriginal;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [id=" + getId() + ", matricula=" + matricula + "]";
	}

}
