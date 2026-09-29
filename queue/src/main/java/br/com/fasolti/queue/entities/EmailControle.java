package br.com.fasolti.queue.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import br.com.fasolti.queue.enuns.TipoEmailStatus;

@Entity
@Table(name = "email_controle")
public class EmailControle extends BaseAuditedEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = -8229703275640243946L;

	public EmailControle() {
	}

	@Column(name = "id_destinatario")
	private Long idDestinatario;

	private String email;

	private String assunto;

	private String mensagem;

	private String template;

	@Type(type = "br.com.fasolti.queue.enuns.tipos.CodedEnumType")
	private TipoEmailStatus status;

	@Column(name = "msg_erro")
	private String msgErro;

	public static class EmailControleBuilder {
		private Long idDestinatario;
		private String email;
		private String template;
		private TipoEmailStatus status;
		private String assunto; // optional
		private String mensagem; // optional
		private String msgErro; // optional
		private String criador;
		private Date dataCriacao;
		private String atualizador;
		private Date dataAtualizacao;

		public EmailControleBuilder(Long idDestinatario, String email, String template, TipoEmailStatus status) {
			this.idDestinatario = idDestinatario;
			this.email = email;
			this.template = template;
			this.status = status;
		}

		public EmailControleBuilder setAssunto(String assunto) {
			this.assunto = assunto;
			return this;
		}

		public EmailControleBuilder setMensagem(String mensagem) {
			this.mensagem = mensagem;
			return this;
		}

		public EmailControleBuilder setMsgErro(String msgErro) {
			this.msgErro = msgErro;
			return this;
		}

		public EmailControleBuilder setCriador(String criador) {
			this.criador = criador;
			return this;
		}

		public EmailControleBuilder setDataCriacao(Date dataCriacao) {
			this.dataCriacao = dataCriacao;
			return this;
		}

		public EmailControleBuilder setAtualizador(String atualizador) {
			this.atualizador = atualizador;
			return this;
		}

		public EmailControleBuilder setDataAtualizacao(Date dataAtualizacao) {
			this.dataAtualizacao = dataAtualizacao;
			return this;
		}

		public EmailControle build() {
			return new EmailControle(this);
		}
	}

	private EmailControle(EmailControleBuilder builder) {
		this.idDestinatario = builder.idDestinatario;
		this.email = builder.email;
		this.template = builder.template;
		this.status = builder.status;
		this.assunto = builder.assunto;
		this.mensagem = builder.mensagem;
		this.msgErro = builder.msgErro;
		this.criador = builder.criador;
		this.dataCriacao = builder.dataCriacao;
		this.atualizador = builder.atualizador;
		this.dataAtualizacao = builder.dataAtualizacao;
	}

	public Long getIdDestinatario() {
		return idDestinatario;
	}

	public void setIdDestinatario(Long idDestinatario) {
		this.idDestinatario = idDestinatario;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public TipoEmailStatus getStatus() {
		return status;
	}

	public void setStatus(TipoEmailStatus status) {
		this.status = status;
	}

	public String getMsgErro() {
		return msgErro;
	}

	public void setMsgErro(String msgErro) {
		this.msgErro = msgErro;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@Override
	public String toString() {
		return "EmailControle [idDestinatario=" + idDestinatario + ", email=" + email + ", assunto=" + assunto
				+ ", template=" + template + ", status=" + status + "]";
	}

}
