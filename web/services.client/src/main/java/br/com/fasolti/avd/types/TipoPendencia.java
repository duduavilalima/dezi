package br.com.fasolti.avd.types;

public enum TipoPendencia {

	COLABORADORES(1, "Colaboradores"),
	CARGO(2, "Cargo"),
	DIMENSAO(3, "Dimensão");

	private Integer code;
	private String nome;

	TipoPendencia(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static TipoPendencia valueOf(Integer code) {
		for (TipoPendencia s : TipoPendencia.values()) {
			if (s.getCode().equals(code))
				return s;
		}
		return null;
	}

}
