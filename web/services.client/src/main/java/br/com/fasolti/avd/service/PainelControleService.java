package br.com.fasolti.avd.service;

import br.com.fasolti.avd.service.domain.FaseGraficoAvaliacao;
import br.com.fasolti.avd.transfer.*;

import java.time.LocalDate;
import java.util.List;

public interface PainelControleService {
	ChartEvolucaoTO montarGraficoGaugePlot(AvaliacaoTO avaliacaoTO, FaseGraficoAvaliacao type);

	ChartLineElementosTO montarGraficoLineChart(AvaliacaoTO to, LocalDate dataInicial, LocalDate dataFinal);

	ChartEvolucaoTO percentualPreenchimento(AvaliacaoTO avaliacaoTO, FaseGraficoAvaliacao type);

	TotalAcompanhamentoGraficoTO generateGraficoAv1EAv2(FiltroAcompanhamentoTO filtroAcompanhamentoTO);

	TotalAcompanhamentoGraficoTO generateGraficoAv3(FiltroAcompanhamentoTO filtroAcompanhamentoTO);

	List<ColaboradorAcompanhamentoTO> dadosPreenchimentoAv1EAv2(FiltroAcompanhamentoTO filtroAcompanhamentoTO);

	List<ColaboradorAcompanhamentoTO> dadosPreenchimentoAv3(FiltroAcompanhamentoTO filtroAcompanhamentoTO);

	PreenchimentosAcompanhamentoTO envioDemensagensDeAcompanhamento(EnvioMensagemAcompanhamentoTO envioMensagemAcompanhamentoTO);
}
