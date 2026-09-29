package br.com.fasolti.service.lang;

import br.com.fasolti.avd.service.domain.SimpleCredentials;

public class PentahoInvalidCredentialsException extends PentahoLoginException {
	public PentahoInvalidCredentialsException(SimpleCredentials credentials) {
		super("Invalid Pentaho Credentials");
	}
}
