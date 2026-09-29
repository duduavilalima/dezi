package br.com.fasolti.avd.types;

public enum TipoClient {
	WEB(1, "Web"),
	ANDROID(2, "App Android"),
	IOS(3, "App iOS");

	private Integer code;
	private String nome;

	TipoClient(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static TipoClient valueOf(Integer code) {
		for(TipoClient s : TipoClient.values()) {
			if(s.getCode().equals(code))
				return s;
		}
		return null;

	}

}
