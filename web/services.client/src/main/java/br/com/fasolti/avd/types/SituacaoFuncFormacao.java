package br.com.fasolti.avd.types;
/**
 * Situação da formação do funcionário
 **/
public enum SituacaoFuncFormacao {
	EM_ANDAMENTO (1, "Em andamento"),
	SUSPENSO (2, "Suspenso"),
	CONCLUIDO (3, "Concluído");


	private Integer code;
	private String nome;

	SituacaoFuncFormacao(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static SituacaoFuncFormacao valueOf(Integer code) {
		for (SituacaoFuncFormacao s : SituacaoFuncFormacao.values()) {
			if (s.getCode().equals(code))
				return s;
		}

		return null;
	}
}
