package br.com.fasolti.avd.types;

public enum TipoFoto {

	FOTO_PERFIL(13, "Foto perfil"),
	FOTO_AVATAR(2, "Foto avatar");


	private Integer code;
	private String nome;

	TipoFoto(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static TipoFoto valueOf(Integer code) {
		for (TipoFoto s : TipoFoto.values()) {
			if (s.getCode().equals(code))
				return s;
		}
		return null;
	}
}
