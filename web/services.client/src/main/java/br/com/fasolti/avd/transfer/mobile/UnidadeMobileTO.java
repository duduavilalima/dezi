package br.com.fasolti.avd.transfer.mobile;

public class UnidadeMobileTO {

	private Long id;
	private String nome;
	private UnidadeMobileTO unidadePai;

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

	public UnidadeMobileTO getUnidadePai() {
		return unidadePai;
	}

	public void setUnidadePai(UnidadeMobileTO unidadePai) {
		this.unidadePai = unidadePai;
	}
}
