package br.com.fasolti.queue.commons;

public class GenericsInfo {

	private final Class<?>[] types;

	GenericsInfo(Class<?>[] types) {
		this.types = types;
	}

	@SuppressWarnings("unchecked")
	public <T> Class<T> getType(int index) {
		return (Class<T>) this.types[index];
	}

	public <T> Class<T> getType() {
		return this.getType(0);
	}

	public Class<?>[] getTypes() {
		return this.types;
	}
}
