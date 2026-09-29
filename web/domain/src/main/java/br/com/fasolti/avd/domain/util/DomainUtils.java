package br.com.fasolti.avd.domain.util;

public class DomainUtils {
	// TODO Mover para a biblioteca
	public static Boolean safeBoolean(Boolean value) {
		if (value == null)
			return Boolean.FALSE;

		return value;
	}
}
