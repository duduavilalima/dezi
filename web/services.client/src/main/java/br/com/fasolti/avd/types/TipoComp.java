package br.com.fasolti.avd.types;

public enum TipoComp {

	ORGANIZACIONAL(5, "Competências Organizacionais"),
	GERENCIAL(6, "Competências Gerenciais"),
	TECNICA(7, "Competências Técnicas"),
	COMPORTAMENTAL(11, "Competências Comportamentais"),
	RESPONSABILIDADES(100, "Responsabilidades");

	private Integer code;
	private String nome;

	TipoComp(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static TipoComp valueOf(Integer code) {
		for (TipoComp s : TipoComp.values()) {
			if (s.getCode().equals(code))
				return s;
		}
		return null;
	}
}
