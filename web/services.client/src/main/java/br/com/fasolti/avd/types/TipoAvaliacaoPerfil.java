package br.com.fasolti.avd.types;

public enum TipoAvaliacaoPerfil {

	AUTOAVALIACAO(100, "Autoavaliação"),
	GESTOR(101, "Gestor"),
	SUBORDINADO(102, "Subordinados"),
	PARES(103, "Pares");
//	CLIENTE_INTERNO(104,"Cliente interno"),
//	OUTROS(105, "Outros");

	private Integer code;
	private String nome;

	TipoAvaliacaoPerfil(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static TipoAvaliacaoPerfil valueOf(Integer code) {
		for (TipoAvaliacaoPerfil s : TipoAvaliacaoPerfil.values()) {
			if (s.getCode().equals(code))
				return s;
		}
		return null;
	}
}
