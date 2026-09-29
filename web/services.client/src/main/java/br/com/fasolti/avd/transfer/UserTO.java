package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.CargoSimpleTO;
import br.com.fasolti.avd.transfer.simple.DepartamentoSimpleTO;
import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
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

@JsonIgnoreProperties
public class UserTO extends BaseTO implements UserDetails {
	private String matricula;

	private String nome;

	private String emailFuncional;

	private String senhaCriptografada;

	private TipoPerfil tipoPerfil;

	private Boolean primeiroAcesso;

	private String fotoNomeArquivo;

	private Integer fotoTamanhoArquivo;

	private CargoSimpleTO cargo;

	private DepartamentoSimpleTO departamento;

	private EmpresaSimpleTO empresa;

	private Situacao situacao;

	private ConfiguracaoTO configuracao;

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

	public DepartamentoSimpleTO getDepartamento() {
		return departamento;
	}

	public void setDepartamento(DepartamentoSimpleTO departamento) {
		this.departamento = departamento;
	}

	@Override
	public String getUsername() {
		return UsernameParser.compose(empresa.getId(), matricula);
	}

	@Override
	public boolean isEnabled() {
		return situacao == Situacao.ATIVA;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Definir o critério
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Definir o critério
		return true;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Definir o critério
		return true;
	}

	@Override
	public String getPassword() {
		return senhaCriptografada;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// FIXME: Perfis atuais do sistema. Refatorar código e banco para funcionário x perfil, e não funcionário/tipo.
		// TODO Definir com serão os níveis de segurança
		return Collections.singleton(new SimpleGrantedAuthority(String.format("ROLE_%s", this.tipoPerfil)));
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof UserDetails))
			return false;

		UserDetails userDetails = (UserDetails) obj;

		return this.getUsername().equals(userDetails.getUsername());
	}

	@Override
	public int hashCode() {
		return this.getUsername().hashCode();
	}

}
