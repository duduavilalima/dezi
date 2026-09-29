package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.search.AvaliacaoSearchTO;

import java.util.List;
import java.util.Map;

public interface GraficoAvaliacaoService extends AvdService<AvaliacaoTO, AvaliacaoSearchTO> {

	ChartGapCompetenciaTO graficoGapCompetencia(AvaliacaoTO to);

	ChartGapCompetenciaTO graficoGapCompetenciaByColaborador(ParticipanteTO participante);

	ChartGapCompetenciaTO graficoGapCompetenciaByCargo(ParticipanteTO participante);

	ChartGapCompetenciaTO montarGraficoGapCompetencia(GapCompetenciaTO graficoGap, List<PreenchimentoTO> preenchimentos);

	void somarPontuacoes(AvaliacaoTO avaliacao, Map<String, List<PontuacaoTO>> mapaPontuacoesDesejadas, Map<String, List<PontuacaoTO>> mapaPontuacoesRegistradas, List<PreenchimentoTO> listaPreenchimentos);

}
