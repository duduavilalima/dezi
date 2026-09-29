package br.com.fasolti.avd.types;

import java.util.Arrays;

public enum SituacaoPreenchimento {

	NAO_PREENCH (1, "Não preenchida"),
	PARCIALMENTE_PREENCH (2, "Parcialmente preenchida"),
	TOTALMENTE_PREENCH (3, "Totalmente preenchida"),
	NAO_APLICAVEL (0, "Não aplicável");

	private final Integer code;
	private final String nome;

	SituacaoPreenchimento(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static SituacaoPreenchimento valueOf(Integer code) {
		return Arrays.stream(SituacaoPreenchimento.values())
			.filter(s -> s.getCode().equals(code))
			.findFirst()
			.orElse(null);
	}
}
