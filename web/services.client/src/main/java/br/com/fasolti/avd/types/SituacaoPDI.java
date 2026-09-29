package br.com.fasolti.avd.types;

public enum SituacaoPDI {
	EM_APROVACAO(1, "Em aprovaçao"),
	APROVADO(2, "Aprovado"),
	EM_ANDAMENTO (3, "Em andamento"),
	FINALIZADO (4, "Finalizado");

	private Integer code;
	private String nome;

	SituacaoPDI(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static SituacaoPDI valueOf(Integer code) {
		for (SituacaoPDI s : SituacaoPDI.values()) {
			if (s.getCode().equals(code))
				return s;
		}
		return null;
	}
}
