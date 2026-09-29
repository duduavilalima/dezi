package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.service.PentahoLoginService;
import br.com.fasolti.avd.transfer.PentahoSessionTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("pentaho")
public class PentahoResource {

	@Autowired
	private PentahoLoginService pentahoLoginService;

	@RequestMapping(method = RequestMethod.GET, path = "/login")
	public PentahoSessionTO login() {
		return pentahoLoginService.login();
	}
}
