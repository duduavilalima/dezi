package br.com.fasolti.avd.rest.util;

import br.com.fasolti.avd.service.DeziService;
import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.lang.InvalidParametersException;
import br.com.fasolti.commons.rest.BaseCrudResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class BaseDeziResource<T extends TransferObject>
	extends BaseCrudResource<T>
{
	private static final String ACTIVATE = ID + "/activate";
	private static final String DEACTIVATE = ID + "/deactivate";

	protected final DeziService<T> deziService;

	public BaseDeziResource(DeziService<T> service) {
		super(service);
		this.deziService = service;
	}

	@RequestMapping(method = RequestMethod.PUT, value = ACTIVATE)
	public final T activate(@PathVariable Long id) {
		if (id == null) throw new InvalidParametersException("Invalid ID");
		return deziService.activate(id);
	}

	@RequestMapping(method = RequestMethod.PUT, value = DEACTIVATE)
	public final T deactivate(@PathVariable Long id) {
		if (id == null) throw new InvalidParametersException("Invalid ID");
		return deziService.deactivate(id);
	}
}
