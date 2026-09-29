package br.com.fasolti.avd.service.domain;

public class SimpleCredentials {
	private String username;

	private String password;

	public SimpleCredentials(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
