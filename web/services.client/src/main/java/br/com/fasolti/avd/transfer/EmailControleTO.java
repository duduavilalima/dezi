package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.TipoEmailStatus;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;
import java.util.Objects;

public class EmailControleTO extends BaseTO  implements SearchObject {

	@Validation(name = "Id funcionário destinatário")
	private Long idDestinatario;

	@Validation(name = "E-mail", max = 60, notNull = true)
	private String email;

	@Validation(name = "Assunto E-mail", max = 255)
	private String assunto;

	@Validation(name = "Mensagem E-mail")
	private String mensagem;

	@Validation(name = "Template E-mail")
	private String template;

	@Validation(name = "Status do E-mail", notNull = true)
	private TipoEmailStatus status;

	@Validation(name = "Mensagem de Erro")
	private String msgErro;

	@Validation(name = "Criador")
	private String criador;

	@Validation(name = "Data de Criação")
	private Date dataCriacao;

	@Validation(name = "Atualizador")
	private String atualizador;

	@Validation(name = "data de atualização")
	private Date dataAtualizacao;

	public EmailControleTO() {
	}

	public static Builder builder(
		Long idDestinatario,
		String email,
		String template,
		TipoEmailStatus status
	) {
		return new Builder(
			idDestinatario,
			email,
			template,
			status
		);
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

		public Builder setId(Long id) {
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

		public EmailControleTO build() {
			return new EmailControleTO(this);
		}
	}

	private EmailControleTO(Builder builder) {
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
		return "EmailControleTO{" +
			", idDestinatario=" + idDestinatario +
			", email='" + email + '\'' +
			", assunto='" + assunto + '\'' +
			", mensagem='" + mensagem + '\'' +
			'}';
	}

}
