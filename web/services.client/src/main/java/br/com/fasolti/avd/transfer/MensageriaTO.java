package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.simple.AvaliacaoSimpleTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.transfer.simple.UserTOSimple;
import br.com.fasolti.avd.transfer.util.BaseTO;

import java.util.Date;

public class MensageriaTO extends BaseTO {

	private Date dataHoraAbertura;
	private Date dataHoraEnvio;
	private AvaliacaoSimpleTO avaliacao;
	private UserTOSimple usuarioDestino;
	private UserTOSimple usuarioEnvio;
	private MensageriaTO mensagemOriginal;
	private Long idDestino;
	private Long idEnvio;
	private Long idOriginal;
	private String texto;
	private String titulo;
	private Boolean envioEmail;
	private Integer totalEnviados;
	private String avisoEmailInternacionalizado;
	private String linkEmailInternacionalizado;
	private AvaliacaoSimpleTO avaliacaoDestino;
	private AvaliacaoSimpleTO avaliacaoEnvio;
	private Long numMensagem;

	public MensageriaTO() {
	}

	public Date getDataHoraAbertura() {
		return dataHoraAbertura;
	}

	public void setDataHoraAbertura(Date dataHoraAbertura) {
		this.dataHoraAbertura = dataHoraAbertura;
	}

	public Date getDataHoraEnvio() {
		return dataHoraEnvio;
	}

	public void setDataHoraEnvio(Date dataHoraEnvio) {
		this.dataHoraEnvio = dataHoraEnvio;
	}

	public AvaliacaoSimpleTO getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(AvaliacaoSimpleTO avaliacao) {
		this.avaliacao = avaliacao;
	}

	public UserTOSimple getUsuarioDestino() {
		return usuarioDestino;
	}

	public void setUsuarioDestino(UserTOSimple usuarioDestino) {
		this.usuarioDestino = usuarioDestino;
	}

	public UserTOSimple getUsuarioEnvio() {
		return usuarioEnvio;
	}

	public void setUsuarioEnvio(UserTOSimple usuarioEnvio) {
		this.usuarioEnvio = usuarioEnvio;
	}

	public MensageriaTO getMensagemOriginal() {
		return mensagemOriginal;
	}

	public void setMensagemOriginal(MensageriaTO mensagemOriginal) {
		this.mensagemOriginal = mensagemOriginal;
	}

	public Long getIdDestino() {
		return idDestino;
	}

	public void setIdDestino(Long idDestino) {
		this.idDestino = idDestino;
	}

	public Long getIdEnvio() {
		return idEnvio;
	}

	public void setIdEnvio(Long idEnvio) {
		this.idEnvio = idEnvio;
	}

	public Long getIdOriginal() {
		return idOriginal;
	}

	public void setIdOriginal(Long idOriginal) {
		this.idOriginal = idOriginal;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Boolean getEnvioEmail() {
		return envioEmail;
	}

	public void setEnvioEmail(Boolean envioEmail) {
		this.envioEmail = envioEmail;
	}

	public Integer getTotalEnviados() {
		return totalEnviados;
	}

	public void setTotalEnviados(Integer totalEnviados) {
		this.totalEnviados = totalEnviados;
	}

	public String getAvisoEmailInternacionalizado() {
		return avisoEmailInternacionalizado;
	}

	public void setAvisoEmailInternacionalizado(String avisoEmailInternacionalizado) {
		this.avisoEmailInternacionalizado = avisoEmailInternacionalizado;
	}

	public String getLinkEmailInternacionalizado() {
		return linkEmailInternacionalizado;
	}

	public void setLinkEmailInternacionalizado(String linkEmailInternacionalizado) {
		this.linkEmailInternacionalizado = linkEmailInternacionalizado;
	}

	public AvaliacaoSimpleTO getAvaliacaoDestino() { return avaliacaoDestino; }

	public void setAvaliacaoDestino(AvaliacaoSimpleTO avaliacaoDestino) { this.avaliacaoDestino = avaliacaoDestino; }

	public AvaliacaoSimpleTO getAvaliacaoEnvio() { return avaliacaoEnvio; }

	public void setAvaliacaoEnvio(AvaliacaoSimpleTO avaliacaoEnvio) { this.avaliacaoEnvio = avaliacaoEnvio;

	}

	public Long getNumMensagem() {
		return numMensagem;
	}

	public void setNumMensagem(Long numMensagem) {
		this.numMensagem = numMensagem;
	}
}
