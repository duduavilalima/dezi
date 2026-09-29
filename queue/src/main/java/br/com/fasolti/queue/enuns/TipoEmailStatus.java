package br.com.fasolti.queue.enuns;

public enum TipoEmailStatus {

	PENDENTE(1),
	ENVIADO(2),
	REENVIO(3),
	ERRO(4),
	RESOLVIDO(5);

	private Integer code;

	public Integer getCode() {
		return code;
	}

	TipoEmailStatus(Integer code) {
		this.code = code;
	}

	public static TipoEmailStatus valueOf(Integer code) {
		for (TipoEmailStatus s : TipoEmailStatus.values()) {
			if (s.getCode().equals(code)) {
				return s;
			}
		}

		return null;
	}

}
