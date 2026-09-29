package br.com.fasolti.avd.types;

public enum Situacao {
	ATIVA (1),
	INATIVA (98);

	private Integer code;

	Situacao(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public static Situacao valueOf(Integer code) {
		for (Situacao s : Situacao.values()) {
			if (s.getCode().equals(code))
				return s;
		}

		return null;
	}
}
