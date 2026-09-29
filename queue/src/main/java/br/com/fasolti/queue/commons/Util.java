package br.com.fasolti.queue.commons;

public final class Util {

	public static String varName(final Class<?> clazz) {
		String className = clazz.getSimpleName();
		char c[] = className.toCharArray();
		c[0] = Character.toLowerCase(c[0]);
		return new String(c);
	}

}
