package br.com.fasolti.avd.types;

public enum TipoAvMobile {
	AV1 (1),
	AV2 (2),
	AV3 (3);

	private Integer code;

	TipoAvMobile(Integer code) {
		this.code = code;
	}

	public Integer getCode() {
		return code;
	}

	public static TipoAvMobile valueOf(Integer code) {
		for (TipoAvMobile s : TipoAvMobile.values()) {
			if (s.getCode().equals(code))
				return s;
		}

		return null;
	}
}
