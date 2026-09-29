package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.service.FeedForwardRecebidoService;
import br.com.fasolti.avd.transfer.mobile.FeedForwardMobileTO;
import br.com.fasolti.avd.transfer.mobile.UsuarioMobileTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("feedsRecebidos")
public class FeedForwardRecebidoResource {

	@Autowired
	private FeedForwardRecebidoService feedForwardsRecebidosService;


	@ResponseBody
	@RequestMapping(method = RequestMethod.POST)
	public List<FeedForwardMobileTO> obterFeedForwardsRecebidos(@RequestBody UsuarioMobileTO avaliado) throws Exception {
		 return feedForwardsRecebidosService.obterFeedForwardsRecebidos(avaliado);
	}

}
