package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.commons.client.service.SearchService;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.lang.ServiceException;

public interface AvdService<T extends TransferObject, S extends SearchObject> extends SearchService<T, S> {

	T activate(T object) throws ServiceException;

	T inactivate(T object) throws ServiceException;

	UserTO getLoggedUser();
}
