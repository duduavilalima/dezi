package br.com.fasolti.avd.types;
/**
 * Niveis de idioma do funcionário
 */
public enum SituacaoFuncIdioma {
	BASICO (1, "Básico"),
	INTERMEDIARIO (2, "Intermediário"),
	AVANCADO (3, "Avançado"),
	NATIVO (4, "Nativo");

	private Integer code;
	private String nome;

	SituacaoFuncIdioma(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}


	public static SituacaoFuncIdioma valueOf(Integer code) {
		for (SituacaoFuncIdioma s : SituacaoFuncIdioma.values()) {
			if (s.getCode().equals(code))
				return s;
		}

		return null;
	}
}
