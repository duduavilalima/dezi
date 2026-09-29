package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.service.PainelControleService;
import br.com.fasolti.avd.transfer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("acompanhamento")
public class AcompanhamentoResource {

	@Autowired
	private PainelControleService painelControleService;

	@RequestMapping(method = RequestMethod.POST, path = "/avaliacoes/grafico")
	public TotalAcompanhamentoGraficoTO generateGraficoAv1EAv2(@RequestBody FiltroAcompanhamentoTO filtroAcompanhamentoTO) {
		return painelControleService.generateGraficoAv1EAv2(filtroAcompanhamentoTO);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/consensos/grafico")
	public TotalAcompanhamentoGraficoTO generateGraficoAv3(@RequestBody FiltroAcompanhamentoTO filtroAcompanhamentoTO) {
		return painelControleService.generateGraficoAv3(filtroAcompanhamentoTO);
	}

	@RequestMapping( method = RequestMethod.POST, path = "/avaliacoes/dados")
	public List<ColaboradorAcompanhamentoTO> dadosPreenchimentoAv1EAv2(@RequestBody FiltroAcompanhamentoTO filtroAcompanhamentoTO) {
		return painelControleService.dadosPreenchimentoAv1EAv2(filtroAcompanhamentoTO);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/consensos/dados")
	public List<ColaboradorAcompanhamentoTO> dadosPreenchimentoAv3(@RequestBody FiltroAcompanhamentoTO filtroAcompanhamentoTO) {
		return painelControleService.dadosPreenchimentoAv3(filtroAcompanhamentoTO);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/mensagem")
	public PreenchimentosAcompanhamentoTO envioDemensagens(@RequestBody EnvioMensagemAcompanhamentoTO envioMensagemAcompanhamentoTO) {
		return painelControleService.envioDemensagensDeAcompanhamento(envioMensagemAcompanhamentoTO);
	}

}
