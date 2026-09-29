package br.com.fasolti.avd.transfer;

import br.com.fasolti.avd.transfer.util.BaseTO;
import br.com.fasolti.avd.types.SituacaoAuth;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.validation.Validation;

import java.util.Date;

public class LogAuthTO extends BaseTO implements SearchObject {

	@Validation(name = "user_Name", max = 100, notNull = true)
	private String userName;
	private Date data;
	@Validation(name = "ip", max = 40)
	private String ip;
	@Validation(name = "Situação", notNull = true)
	private SituacaoAuth situacao;

	public static class Builder {
		private String userName;
		private Date data;
		private String ip;
		private SituacaoAuth situacao;

		public Builder(String userName, SituacaoAuth situacao) {
			this.userName = userName;
			this.situacao = situacao;
		}

		public Builder setData(Date data) {
			this.data = data;
			return this;
		}

		public Builder setIp(String ip) {
			this.ip = ip;
			return this;
		}

		public LogAuthTO build() {
			return new LogAuthTO(this);
		}
	}

	private LogAuthTO(Builder builder) {
		userName = builder.userName;
		data = builder.data;
		ip = builder.ip;
		situacao = builder.situacao;
	}

	public LogAuthTO() {}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public SituacaoAuth getSituacao() {
		return situacao;
	}

	public void setSituacao(SituacaoAuth situacao) {
		this.situacao = situacao;
	}

	@Override
	public String toString() {
		return "LogAuthTO{" +
			"userName='" + userName + '\'' +
			", data=" + data +
			", ip='" + ip + '\'' +
			", situacao=" + situacao +
			'}';
	}
}
