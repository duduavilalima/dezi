package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.commons.client.service.CrudService;
import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.lang.ServiceException;

import javax.annotation.Nonnull;

public interface DeziService<T extends TransferObject> extends CrudService<T> {

	T activate(@Nonnull Long id) throws ServiceException;

	T deactivate(@Nonnull Long id) throws ServiceException;

	UserTO getLoggedUser();
}
