package br.com.fasolti.avd.types;

public enum TipoMensagem {

	INFORMACAO(8, "Informação"),
	ATENCAO(9, "Atenção"),
	ERRO(10, "Erro");

	private Integer code;
	private String nome;

	TipoMensagem(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static TipoMensagem valueOf(Integer code) {
		for (TipoMensagem s : TipoMensagem.values()) {
			if (s.getCode().equals(code))
				return s;
		}
		return null;
	}
}
