package br.com.fasolti.avd.types;

public enum SituacaoAuth {
	LOGIN_OK(1, "Login OK"),
	AUTENTICACAO_INVALIDA(2, "Autenticação inválida"),
	USUARIO_SUSPENSO (3, "Usuário suspenso"),
	USUARIO_BLOQUEADO (4, "Usuario bloqueado"),
	IP_BLOQUEADO (5, "IP bloqueado");

	private Integer code;
	private String nome;

	SituacaoAuth(Integer code, String nome) {
		this.code = code;
		this.nome = nome;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public static SituacaoAuth valueOf(Integer code) {
		for (SituacaoAuth s : SituacaoAuth.values()) {
			if (s.getCode().equals(code))
				return s;
		}
		return null;
	}
}
