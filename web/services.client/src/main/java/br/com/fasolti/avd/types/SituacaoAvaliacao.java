package br.com.fasolti.avd.types;

public enum SituacaoAvaliacao {
	AG_LIBERACAO (1),
	LIBERADA (2),
	INATIVA (98),
	CANCELADA(99),
	CONCLUIDA(100);

	private Integer code;

	SituacaoAvaliacao(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public static SituacaoAvaliacao valueOf(Integer code) {
		for (SituacaoAvaliacao s : SituacaoAvaliacao.values()) {
			if (s.getCode().equals(code))
				return s;
		}

		return null;
	}
}
