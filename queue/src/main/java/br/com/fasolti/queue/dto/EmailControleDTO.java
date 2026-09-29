package br.com.fasolti.queue.dto;

import java.util.Date;
import java.util.Objects;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import br.com.fasolti.queue.enuns.TipoEmailStatus;

public class EmailControleDTO extends BaseEntityDTO {

	public EmailControleDTO() {
	}

	private Long idDestinatario;

	@Email
	@NotEmpty(message = "E-mail não pode ser vazio.")
	private String email;

	private String assunto;

	private String mensagem;

	private String template;

	@NotEmpty(message = "Status não pode ser vazio.")
	private TipoEmailStatus status;

	private String msgErro;

	private String criador;

	private Date dataCriacao;

	private String atualizador;

	private Date dataAtualizacao;

	public static class EmailControleDTOBuilder {
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

		public EmailControleDTOBuilder(Long idDestinatario, String email, String template, TipoEmailStatus status) {
			this.idDestinatario = idDestinatario;
			this.email = email;
			this.template = template;
			this.status = status;
		}

		public EmailControleDTOBuilder setAssunto(String assunto) {
			this.assunto = assunto;
			return this;
		}

		public EmailControleDTOBuilder setMensagem(String mensagem) {
			this.mensagem = mensagem;
			return this;
		}

		public EmailControleDTOBuilder setMsgErro(String msgErro) {
			this.msgErro = msgErro;
			return this;
		}

		public EmailControleDTOBuilder setCriador(String criador) {
			this.criador = criador;
			return this;
		}

		public EmailControleDTOBuilder setDataCriacao(Date dataCriacao) {
			this.dataCriacao = dataCriacao;
			return this;
		}

		public EmailControleDTOBuilder setAtualizador(String atualizador) {
			this.atualizador = atualizador;
			return this;
		}

		public EmailControleDTOBuilder setDataAtualizacao(Date dataAtualizacao) {
			this.dataAtualizacao = dataAtualizacao;
			return this;
		}

		public EmailControleDTO build() {
			return new EmailControleDTO(this);
		}
	}

	private EmailControleDTO(EmailControleDTOBuilder builder) {
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

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
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
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		EmailControleDTO that = (EmailControleDTO) o;
		if (!getId().equals(that.getId())) {
			return false;
		}
		return Objects.equals(idDestinatario, that.idDestinatario) && Objects.equals(email, that.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), idDestinatario, email);
	}

	@Override
	public String toString() {
		return "" + "EmailControleDTO{" + "id='" + getId() + '\'' + "idDestinatario=" + idDestinatario + ", email='"
				+ email + '\'' + ", status=" + status + '}';
	}

}
