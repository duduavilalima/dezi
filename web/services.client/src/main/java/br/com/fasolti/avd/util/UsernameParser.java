package br.com.fasolti.avd.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UsernameParser {
	private static String USERNAME_SEPARATOR = "%";

	private static String USERNAME_PATTERN = "(.*?)" + USERNAME_SEPARATOR + "(.*)";

	private final String username;

	public static String compose(Long empresaId, String matricula) {
		return empresaId.toString() + USERNAME_SEPARATOR + matricula;
	}

	public static UsernameParser of(String username) {
		return new UsernameParser(username);
	}

	private UsernameParser(String username) {
		this.username = username;
	}

	public Long extractEmpresa() {
		Matcher matcher = Pattern.compile(USERNAME_PATTERN).matcher(username);
		if (!matcher.matches())
			return null;

		String strId = matcher.group(1);

		try {
			return Long.parseLong(strId);
		} catch (NumberFormatException e) {
			// TODO Logar
			return null;
		}
	}

	public String extractMatricula() {
		Matcher matcher = Pattern.compile(USERNAME_PATTERN).matcher(username);
		if (!matcher.matches())
			return null;

		return matcher.group(2);
	}

}
