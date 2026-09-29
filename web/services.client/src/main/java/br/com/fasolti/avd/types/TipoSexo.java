package br.com.fasolti.avd.types;

public enum TipoSexo {

	MASCULINO(1,"Masculino"),
	FEMININO(2,"Feminino");

	private Integer code;
	private String nome;

	TipoSexo(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static TipoSexo valueOf(Integer code) {
		for (TipoSexo s : TipoSexo.values()) {
			if (s.getCode().equals(code))
				return s;
		}
		return null;
	}
}
