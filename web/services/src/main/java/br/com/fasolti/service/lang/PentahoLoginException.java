package br.com.fasolti.service.lang;

import org.hibernate.service.spi.ServiceException;

public class PentahoLoginException extends ServiceException {
	public PentahoLoginException(String message) {
		super(message);
	}

	public PentahoLoginException(String message, Throwable root) {
		super(message, root);
	}
}
