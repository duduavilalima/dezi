package br.com.fasolti.avd.transfer;

public class PentahoSessionTO {
	private final String server;

	private final String sessionId;

	public PentahoSessionTO(String server, String sessionId) {
		this.server = server;
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getServer() {
		return server;
	}
}
