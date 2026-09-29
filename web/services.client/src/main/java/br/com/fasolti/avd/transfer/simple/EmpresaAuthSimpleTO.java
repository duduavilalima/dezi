package br.com.fasolti.avd.transfer.simple;

/**
 * Usado na tela de login da aplicação.
 */
public class EmpresaAuthSimpleTO {

	private Long id;
	private String nome;
	private String host;
	private Boolean feedforward;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Boolean getFeedforward() {
		return feedforward;
	}

	public void setFeedforward(Boolean feedforward) {
		this.feedforward = feedforward;
	}
}
