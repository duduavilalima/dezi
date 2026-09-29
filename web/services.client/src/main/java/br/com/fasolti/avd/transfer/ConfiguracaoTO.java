package br.com.fasolti.avd.transfer;


import br.com.fasolti.avd.transfer.util.BaseTO;

import java.util.Date;

public class ConfiguracaoTO extends BaseTO {

	public ConfiguracaoTO() {}

	private ConfiguracaoTO(Builder builder){
		setId(builder.id);
		funcionario = builder.funcionario;
		linguagem = builder.linguagem;
		tamanhoFonte = builder.tamanhoFonte;
		dataUltimoAceite = builder.dataUltimoAceite;
	}

	private String linguagem;

	private String tamanhoFonte;

	private FuncionarioTO funcionario;

	private Date dataUltimoAceite;

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

	public FuncionarioTO getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(FuncionarioTO funcionario) {
		this.funcionario = funcionario;
	}

	public Date getDataUltimoAceite() {
		return dataUltimoAceite;
	}

	public void setDataUltimoAceite(Date dataUltimoAceite) {
		this.dataUltimoAceite = dataUltimoAceite;
	}

	public static class Builder {

		private FuncionarioTO funcionario;

		//Optonal
		private Long id;
		private String linguagem;
		private String tamanhoFonte;
		private Date dataUltimoAceite;

		public Builder(FuncionarioTO funcionario){
			this.funcionario = funcionario;
		}

		public ConfiguracaoTO.Builder setId(Long id) {
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

		public ConfiguracaoTO build() {return new ConfiguracaoTO(this);}

	}
}
