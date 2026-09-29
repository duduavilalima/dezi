package br.com.fasolti.avd.types;

public enum GraduacaoCorEnum {
	ORDEM_1(1, "#FF0000"),
	ORDEM_2(2, "#FF9900"),
	ORDEM_3(3, "#F1C232"),
	ORDEM_4(4, "#FFFF00"),
	ORDEM_5(5, "#00FF00"),
	ORDEM_6(6, "#38761D"),
	ORDEM_7(7, "#00FFFF"),
	ORDEM_8(8, "#0000FF"),
	ORDEM_9(9, "#9B77F7"),
	ORDEM_10(10, "#3F2382"),
	ORDEM_11(11, "#000000");

	private Integer code;
	private String nome;

	GraduacaoCorEnum(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static GraduacaoCorEnum valueOf(Integer code) {
		for (GraduacaoCorEnum s : GraduacaoCorEnum.values()) {
			if (s.getCode().equals(code))
				return s;
		}
		return null;
	}
}
