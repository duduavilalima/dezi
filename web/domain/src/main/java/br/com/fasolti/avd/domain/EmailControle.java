package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.TipoEmailStatus;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "email_controle")
public class EmailControle extends BaseEntity {

	@Transient
	public boolean isNew() {
		return null == this.getId();
	}

	@Column(name = "id_destinatario")
	private Long idDestinatario;

	private String email;

	private String assunto;

	private String mensagem;

	private String template;

	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private TipoEmailStatus status;

	@Column(name = "msg_erro")
	private String msgErro;

	@Column(nullable = false, updatable = false)
	private String criador;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_criacao", nullable = false, updatable = false)
	private Date dataCriacao;

	@Column(nullable = true)
	private String atualizador;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_atualizacao", nullable = true)
	private Date dataAtualizacao;

	public EmailControle() {
	}

	public static class Builder {
		private Long idDestinatario;
		private String email;
		private String template;
		private TipoEmailStatus status;

		//opcional
		private Long id;
		private String assunto;
		private String mensagem;
		private String msgErro;
		private String criador;
		private Date dataCriacao;
		private String atualizador;
		private Date dataAtualizacao;

		public Builder(
			Long idDestinatario,
			String email,
			String template,
			TipoEmailStatus status
		) {
			this.idDestinatario = idDestinatario;
			this.email = email;
			this.template = template;
			this.status = status;
		}

		public EmailControle.Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setAssunto(String assunto) {
			this.assunto = assunto;
			return this;
		}

		public Builder setMensagem(String mensagem) {
			this.mensagem = mensagem;
			return this;
		}

		public Builder setMsgErro(String msgErro) {
			this.msgErro = msgErro;
			return this;
		}

		public Builder setCriador(String criador) {
			this.criador = criador;
			return this;
		}

		public Builder setDataCriacao(Date dataCriacao) {
			this.dataCriacao = dataCriacao;
			return this;
		}

		public Builder setAtualizador(String atualizador) {
			this.atualizador = atualizador;
			return this;
		}

		public Builder setDataAtualizacao(Date dataAtualizacao) {
			this.dataAtualizacao = dataAtualizacao;
			return this;
		}

		public EmailControle build() {
			return new EmailControle(this);
		}
	}

	private EmailControle(Builder builder) {
		setId(builder.id);
		idDestinatario = builder.idDestinatario;
		email = builder.email;
		template = builder.template;
		status = builder.status;
		assunto = builder.assunto;
		mensagem = builder.mensagem;
		msgErro = builder.msgErro;
		criador = builder.criador;
		dataCriacao = builder.dataCriacao;
		atualizador = builder.atualizador;
		dataAtualizacao = builder.dataAtualizacao;
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

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
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

	public String getCriador() {
		return criador;
	}

	public void setCriador(String criador) {
		this.criador = criador;
	}

	public Date getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public String getAtualizador() {
		return atualizador;
	}

	public void setAtualizador(String atualizador) {
		this.atualizador = atualizador;
	}

	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}

	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}

	@Override
	public String toString() {
		return "EmailControle{" +
			", idDestinatario=" + idDestinatario +
			", email='" + email + '\'' +
			", assunto='" + assunto + '\'' +
			", mensagem='" + mensagem + '\'' +
			'}';
	}
}
