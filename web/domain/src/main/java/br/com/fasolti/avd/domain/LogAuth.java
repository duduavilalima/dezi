package br.com.fasolti.avd.domain;

import br.com.fasolti.avd.types.SituacaoAuth;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "log_auth")
public class LogAuth extends BaseEntity {

	@Column(name = "user_name")
	private String userName;

	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@Column(name = "ip")
	private String ip;

	@Column(name = "id_situacao")
	@Type(type = "br.com.fasolti.avd.domain.tipos.CodedEnumType")
	private SituacaoAuth situacao;

	public static class Builder {
		private final String userName;
		private Date data;
		private String ip;
		private final SituacaoAuth situacao;

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

		public LogAuth build() {
			return new LogAuth(this);
		}
	}

	private LogAuth(Builder builder) {
		userName = builder.userName;
		data = builder.data;
		ip = builder.ip;
		situacao = builder.situacao;
	}

	public LogAuth() {}

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
		return "LogAuth{" +
			"userName='" + userName + '\'' +
			", data=" + data +
			", ip='" + ip + '\'' +
			", situacao=" + situacao +
			'}';
	}
}
