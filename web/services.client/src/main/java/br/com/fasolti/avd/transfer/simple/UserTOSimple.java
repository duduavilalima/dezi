package br.com.fasolti.avd.transfer.simple;

import br.com.fasolti.avd.transfer.ConfiguracaoTO;
import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.avd.util.UsernameParser;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

@JsonIgnoreProperties
public class UserTOSimple extends BaseTO {
	private String matricula;

	private String nome;

	private String emailFuncional;

	private String senhaCriptografada;

	private TipoPerfil tipoPerfil;

	private Boolean primeiroAcesso;

	private String fotoNomeArquivo;

	private Integer fotoTamanhoArquivo;

	private CargoSimpleTO cargo;

	private EmpresaSimpleTO empresa;

	private Situacao situacao;

	private ConfiguracaoTO configuracao;

	public UserTOSimple() {
	}

	public UserTOSimple(Long id, String nome) {
		this.nome = nome;
		super.setId(id);
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

	public String getSenhaCriptografada() {
		return senhaCriptografada;
	}

	public void setSenhaCriptografada(String senhaCriptografada) {
		this.senhaCriptografada = senhaCriptografada;
	}

	public TipoPerfil getTipoPerfil() {
		return tipoPerfil;
	}

	public void setTipoPerfil(TipoPerfil tipoPerfil) {
		this.tipoPerfil = tipoPerfil;
	}

	public Boolean getPrimeiroAcesso() {
		return primeiroAcesso;
	}

	public void setPrimeiroAcesso(Boolean primeiroAcesso) {
		this.primeiroAcesso = primeiroAcesso;
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

	public CargoSimpleTO getCargo() {
		return cargo;
	}

	public void setCargo(CargoSimpleTO cargo) {
		this.cargo = cargo;
	}

	public EmpresaSimpleTO getEmpresa() {
		return empresa;
	}

	public void setEmpresa(EmpresaSimpleTO empresa) {
		this.empresa = empresa;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		UserTOSimple that = (UserTOSimple) o;
		return matricula.equals(that.matricula) &&
			nome.equals(that.nome) &&
			emailFuncional.equals(that.emailFuncional);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), matricula, nome, emailFuncional);
	}
}
