package br.com.fasolti.avd.transfer.mobile;


public class EmpresaMobileTO {

	private Long id;
	private String nome;
	private Boolean isFeedforward;

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

	public Boolean getFeedforward() {
		return isFeedforward;
	}

	public void setFeedforward(Boolean feedforward) {
		isFeedforward = feedforward;
	}
}
