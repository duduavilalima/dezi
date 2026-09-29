package br.com.fasolti.avd.types;

public enum TipoConceito {

	TIPOCOMPETENCIA(1, "Tipo Competência"),
	COMPETENCIA(2, "Competência"),
	DETALHAMENTO(3, "Detalhamento");

	private Integer code;
	private String nome;

	TipoConceito(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static TipoConceito valueOf(Integer code) {
		for (TipoConceito s : TipoConceito.values()) {
			if (s.getCode().equals(code))
				return s;
		}
		return null;
	}
}
