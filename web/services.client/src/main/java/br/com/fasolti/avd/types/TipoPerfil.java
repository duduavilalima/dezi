package br.com.fasolti.avd.types;

public enum TipoPerfil {

	ADMINISTRADOR(3, "Administrador"),
	FUNCIONARIO(4, "Funcionario"),
	CONSULTOR_RH(14, "Consultor de RH"),
	GESTOR_RH(15, "Gestor de RH"),
	GESTOR_AREA(16, "Gestor de Área");

	private Integer code;
	private String nome;

	TipoPerfil(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static TipoPerfil valueOf(Integer code) {
		for (TipoPerfil s : TipoPerfil.values()) {
			if (s.getCode().equals(code))
				return s;
		}
		return null;
	}

	public static TipoPerfil valueOfString(String code) {
		for (TipoPerfil s : TipoPerfil.values()) {
			if (s.getNome().equals(code))
				return s;
		}
		return null;
	}
}
