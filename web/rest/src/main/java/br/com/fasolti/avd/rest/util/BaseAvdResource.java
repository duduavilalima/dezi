package br.com.fasolti.avd.rest.util;

import br.com.fasolti.avd.service.AvdService;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.rest.BaseSearchResource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public abstract class BaseAvdResource<T extends TransferObject, S extends SearchObject> extends BaseSearchResource<T, S> {
	private static final String ATIVAR = "/ativar";
	private static final String INATIVAR = "/inativar";

	protected final AvdService<T, S> avdService;

	public BaseAvdResource(AvdService<T, S> service) {
		super(service);
		this.avdService = service;
	}

	@RequestMapping(method = RequestMethod.POST, value = ATIVAR)
	public final T activate(@RequestBody T object) throws Exception {
		return avdService.activate(object);
	}

	@RequestMapping(method = RequestMethod.POST, value = INATIVAR)
	public final T inactivate(@RequestBody T object) throws Exception {
		return avdService.inactivate(object);
	}

}
