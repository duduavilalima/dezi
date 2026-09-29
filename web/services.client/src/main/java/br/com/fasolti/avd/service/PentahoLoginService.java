package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.PentahoSessionTO;
import br.com.fasolti.commons.lang.ServiceException;

public interface PentahoLoginService {
	PentahoSessionTO login() throws ServiceException;
}
