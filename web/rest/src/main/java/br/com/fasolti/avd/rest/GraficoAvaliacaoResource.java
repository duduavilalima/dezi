package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.service.GraficoAvaliacaoService;
import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.ChartGapCompetenciaTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("graficoavaliacao") //TODO Usar um nome mais genérico
public class GraficoAvaliacaoResource {

	public static final String AVALIACAO_NAO_INFORMADA = "Avaliação não informada.";
	public static final String TIPO_DE_GRAFICO_NAO_INFORMADO = "Tipo de gráfico não informado.";
	public static final String PARTICIPANTE_NAO_INFORMADO = "Participante não informado.";
	public static final String CARGO_NAO_INFORMADO = "Cargo não informado.";

	@Autowired
	private GraficoAvaliacaoService graficoAvaliacaoService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/chartradarbyavaliacao")
	public ChartGapCompetenciaTO chartRadarByAvaliacao(@RequestBody AvaliacaoTO avaliacaoTO) throws Exception {
		if (avaliacaoTO == null) {
			new RuntimeException(AVALIACAO_NAO_INFORMADA);
		}
		return graficoAvaliacaoService.graficoGapCompetencia(avaliacaoTO);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/chartradaravaliacaobycolaborador")
	public ChartGapCompetenciaTO chartRadarAvaliacaoByColaborador(@RequestBody ParticipanteTO participante) throws Exception {
		if (participante == null) {
			new RuntimeException(PARTICIPANTE_NAO_INFORMADO);
		}
		if (participante.getAvaliacao() == null) {
			new RuntimeException(AVALIACAO_NAO_INFORMADA);
		}
		return graficoAvaliacaoService.graficoGapCompetenciaByColaborador(participante);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/chartradaravaliacaobycargo")
	public ChartGapCompetenciaTO chartRadarAvaliacaoByCargo(@RequestBody ParticipanteTO participante) throws Exception {
		if (participante == null) {
			new RuntimeException(PARTICIPANTE_NAO_INFORMADO);
		}
		if (participante.getAvaliacao() == null) {
			new RuntimeException(AVALIACAO_NAO_INFORMADA);
		}
		if (participante.getAvaliado() == null || participante.getAvaliado().getCargo() == null) {
			new RuntimeException(CARGO_NAO_INFORMADO);
		}
		return graficoAvaliacaoService.graficoGapCompetenciaByCargo(participante);
	}
}
