package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.service.ConfiguracaoService;
import br.com.fasolti.avd.transfer.ConfiguracaoTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("configuracao")
public class ConfiguracaoResource {

	@Autowired
	private ConfiguracaoService configuracaoService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/configuracaodofuncionario")
	public ConfiguracaoTO configuracaoDoFuncionario(@RequestBody FuncionarioTO funcionario) throws Exception {
		return configuracaoService.findByFuncionarioId(funcionario.getId());
	}

	//FIXME Usar apenas um método para salvar a configuração
	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/persistirlinguagem")
	public HttpEntity<Void> persistirLinguagem(@RequestBody ConfiguracaoTO configuracao) throws Exception {
		//TODO Criar um método que valide o funcionário logado e depois salve
		configuracaoService.saveOrUpdate(configuracao);
		return ResponseEntity.noContent().build();
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/persistirfonte")
	public HttpEntity<Void> persistirFonte(@RequestBody ConfiguracaoTO configuracao) throws Exception {
		configuracaoService.save(configuracao);
		return ResponseEntity.noContent().build();
	}
}
