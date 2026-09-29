package br.com.fasolti.avd.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Mensageria extends BaseEntity {

	@Column(name = "data_hora_abertura")
	private Date dataHoraAbertura;

	@Column(name = "data_hora_envio")
	private Date dataHoraEnvio;

	@ManyToOne
	@JoinColumn(name = "id_avaliacao")
	private Avaliacao avaliacao;

	@ManyToOne
	@JoinColumn(name = "id_destino")
	private Funcionario usuarioDestino;

	@ManyToOne
	@JoinColumn(name = "id_envio")
	private Funcionario usuarioEnvio;

	@ManyToOne
	@JoinColumn(name = "id_original")
	private Mensageria mensagemOriginal;

	@Column(name = "id_original", insertable = false, updatable = false)
	private Long idOriginal;

	private String texto;

	private String titulo;

	@ManyToOne
	@JoinColumn(name = "id_av_destino")
	private Avaliacao avaliacaoDestino;

	@ManyToOne
	@JoinColumn(name = "id_av_envio")
	private Avaliacao avaliacaoEnvio;

	@Column(name = "num_mensagem")
	private Long numMensagem;

	public Mensageria() {
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

	public Avaliacao getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(Avaliacao avaliacao) {
		this.avaliacao = avaliacao;
	}

	public Funcionario getUsuarioDestino() {
		return usuarioDestino;
	}

	public void setUsuarioDestino(Funcionario usuarioDestino) {
		this.usuarioDestino = usuarioDestino;
	}

	public Funcionario getUsuarioEnvio() {
		return usuarioEnvio;
	}

	public void setUsuarioEnvio(Funcionario usuarioEnvio) {
		this.usuarioEnvio = usuarioEnvio;
	}

	public Mensageria getMensagemOriginal() {
		return mensagemOriginal;
	}

	public void setMensagemOriginal(Mensageria mensagemOriginal) {
		this.mensagemOriginal = mensagemOriginal;
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

	public Avaliacao getAvaliacaoDestino() { return avaliacaoDestino; }

	public void setAvaliacaoDestino(Avaliacao avaliacaoDestino) { this.avaliacaoDestino = avaliacaoDestino; }

	public Avaliacao getAvaliacaoEnvio() { return avaliacaoEnvio; }

	public void setAvaliacaoEnvio(Avaliacao avaliacaoEnvio) { this.avaliacaoEnvio = avaliacaoEnvio; }

	public Long getNumMensagem() {
		return numMensagem;
	}

	public void setNumMensagem(Long numMensagem) {
		this.numMensagem = numMensagem;
	}
}
