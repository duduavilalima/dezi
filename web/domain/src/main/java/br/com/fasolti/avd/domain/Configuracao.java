package br.com.fasolti.avd.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Configuracao extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -851857334245748477L;

	private String linguagem;

	@Column(name="tamanho_fonte")
	private String tamanhoFonte;

	@Column(name="data_ultimo_aceite")
	private Date dataUltimoAceite;

	@ManyToOne
	@JoinColumn(name="id_funcionario")
	private Funcionario funcionario;

	public Configuracao() {
	}

	public String getLinguagem() {
		return linguagem;
	}

	public void setLinguagem(String linguagem) {
		this.linguagem = linguagem;
	}

	public String getTamanhoFonte() {
		return tamanhoFonte;
	}

	public void setTamanhoFonte(String tamanhoFonte) {
		this.tamanhoFonte = tamanhoFonte;
	}

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}

	public Date getDataUltimoAceite() {
		return dataUltimoAceite;
	}

	public void setDataUltimoAceite(Date dataUltimoAceite) {
		this.dataUltimoAceite = dataUltimoAceite;
	}

	public static class Builder {

		private Funcionario funcionario;

		//Optonal
		private Long id;
		private String linguagem;
		private String tamanhoFonte;
		private Date dataUltimoAceite;

		public Builder(Funcionario funcionario){
			this.funcionario = funcionario;
		}

		public Configuracao.Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setLinguagem(String linguagem) {
			this.linguagem = linguagem;
			return this;
		}

		public Builder setTamanhoFonte(String tamanhoFonte) {
			this.tamanhoFonte = tamanhoFonte;
			return this;
		}

		public Builder setDataUltimoAceite(Date dataUltimoAceite) {
			this.dataUltimoAceite = dataUltimoAceite;
			return this;
		}

		public Configuracao build() {return new Configuracao(this);}

	}

	private Configuracao(Builder builder){
		setId(builder.id);
		funcionario = builder.funcionario;
		linguagem = builder.linguagem;
		tamanhoFonte = builder.tamanhoFonte;
		dataUltimoAceite = builder.dataUltimoAceite;
	}
}
