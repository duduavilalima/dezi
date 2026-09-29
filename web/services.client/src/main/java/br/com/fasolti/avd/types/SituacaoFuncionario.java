package br.com.fasolti.avd.types;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Situação da formação do funcionário
 **/
public enum SituacaoFuncionario {

	EM_ATIVIDADE (1, "Em atividade", false),
	FERIAS (2, "Férias", true),
	LICENCA_MATERNIDADE (3, "Licença maternidade", true),
	LICENCA_SEM_REMUNERACAO (4, "Licença sem remuneração", true),
	DISPENSA_MEDICA (5, "Dispensa médica", true),
	SERVICO_MILITAR_OBRIGATORIO (6, "Serviço militar obrigatório", true),
	LICENCA_PATERINDADE (7, "Licença paternidade", true),
	LUTO (8, "Luto", true),
	NUPCIAS (9, "Núpcias", true),
	LICENCA_ADOCAO (10, "Licença adoção", true),
	DESLIGADO (11, "Desligado(a)", false),
	RECESSO (12, "Recesso", true);

	private Integer code;
	private String nome;
	private boolean recesso;

	SituacaoFuncionario(Integer code, String nome, boolean recesso) {
		this.code = code;
		this.nome = nome;
		this.recesso = recesso;
	}

	public Integer getCode() {
		return code;
	}

	public String getNome() {
		return nome;
	}

	public boolean isRecesso() {
		return recesso;
	}

	public static SituacaoFuncionario valueOf(Integer code) {
		for (SituacaoFuncionario s : SituacaoFuncionario.values()) {
			if (s.getCode().equals(code))
				return s;
		}

		return null;
	}

	public static SituacaoFuncionario valueOfString(String code) {
		for (SituacaoFuncionario s : SituacaoFuncionario.values()) {
			if (s.getNome().equals(code))
				return s;
		}

		return null;
	}

	public static Collection<SituacaoFuncionario> findByRecesso() {
		return Collections.unmodifiableSet(Arrays.stream(values())
			.filter(SituacaoFuncionario::isRecesso)
			.collect(Collectors.toSet()));
	}

	public static Collection<SituacaoFuncionario> findByNotRecesso() {
		return Collections.unmodifiableSet(Arrays.stream(values())
			.filter(e -> !e.isRecesso())
			.collect(Collectors.toSet()));
	}
}
