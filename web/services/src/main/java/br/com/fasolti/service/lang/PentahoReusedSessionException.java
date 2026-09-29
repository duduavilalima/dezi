package br.com.fasolti.service.lang;

public class PentahoReusedSessionException extends PentahoLoginException {
	public PentahoReusedSessionException() {
		super("Pentaho session reused. New login is required.");
	}
}
