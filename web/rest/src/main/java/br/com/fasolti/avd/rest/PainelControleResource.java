package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.service.AvaliacaoService;
import br.com.fasolti.avd.service.PainelControleService;
import br.com.fasolti.avd.service.domain.FaseGraficoAvaliacao;
import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.ChartEvolucaoTO;
import br.com.fasolti.avd.transfer.ChartLineElementosTO;
import br.com.fasolti.commons.rest.BaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("avaliacao/{id:[0-9]+}/chart")
@PreAuthorize("hasAnyRole('ROLE_ADMINISTRADOR','ROLE_GESTOR_RH','ROLE_CONSULTOR_RH')")
public class PainelControleResource extends BaseResource<AvaliacaoTO> {

	@Autowired
	private PainelControleService painelControleService;

	@Autowired
	public PainelControleResource(AvaliacaoService avaliacaoService) {
		super(avaliacaoService);
	}

//FIXME Não está sendo utilizado, verificar se pode remover
//	@RequestMapping(method = RequestMethod.POST, value = "/graficogaugepreenchimentos")
//	public ChartEvolucaoTO graficaoGaugeAutoavaliacao(@RequestBody PainelControleTO painelControleTO) throws Exception {
//		if (painelControleTO.getAvaliacao() == null) {
//			new RuntimeException(AVALIACAO_NAO_INFORMADA);
//		}
//		if (painelControleTO.getTipoGrafico() == null) {
//			new RuntimeException(TIPO_DE_GRAFICO_NAO_INFORMADO);
//		}
//		return painelControleService.montarGraficoGaugePlot(painelControleTO.getAvaliacao(), painelControleTO.getTipoGrafico());
//	}

	@RequestMapping(method = RequestMethod.GET, path = "/percentual")
	public ChartEvolucaoTO percentual(@PathVariable("id") Long avaliacaoId, @RequestParam("fase") FaseGraficoAvaliacao faseAvaliacao) {
		AvaliacaoTO avaliacao = service.load(avaliacaoId);
		return painelControleService.montarGraficoGaugePlot(avaliacao, faseAvaliacao);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/graficopreenchimentos")
	public ChartEvolucaoTO graficoPreenchimento(@PathVariable("id") Long avaliacaoId, @RequestParam("fase") FaseGraficoAvaliacao faseAvaliacao) {
		AvaliacaoTO avaliacao = service.load(avaliacaoId);
		return painelControleService.percentualPreenchimento(avaliacao, faseAvaliacao);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/graficolinepreenchimentos")
	public ChartLineElementosTO graficoLineHistoricoPreenchimento(@PathVariable("id") Long avaliacaoId, @RequestParam("dias") Integer dias) {
		AvaliacaoTO avaliacao = service.load(avaliacaoId);

		LocalDate dataFinal = LocalDate.now();
		LocalDate dataInicial = dataFinal.minusDays(dias-1);

		return painelControleService.montarGraficoLineChart(avaliacao, dataInicial, dataFinal);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/graficolinepreenchimentosporperiodo")
	public ChartLineElementosTO graficoLineHistoricoPreenchimentoPorPeriodo(
		@PathVariable("id") Long avaliacaoId,
		@RequestParam("periodoDe") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodoDe,
		@RequestParam("periodoAte") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate periodoAte
	) {
		AvaliacaoTO avaliacao = service.load(avaliacaoId);
		return painelControleService.montarGraficoLineChart(avaliacao, periodoDe, periodoAte);
	}


}
