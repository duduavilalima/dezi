package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Avaliacao;
import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.domain.Mensageria;
import br.com.fasolti.avd.domain.Participante;
import br.com.fasolti.avd.repository.*;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.service.domain.FaseGraficoAvaliacao;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.search.AvaliacaoSearchTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.types.SituacaoPreenchimento;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.service.util.BaseAvdService;
import com.google.common.util.concurrent.AtomicDouble;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static br.com.fasolti.avd.service.domain.FaseGraficoAvaliacao.*;

@Service
@Transactional(readOnly = true)
public class PainelControleServiceImpl extends BaseAvdService<Avaliacao, AvaliacaoTO, AvaliacaoSearchTO> implements PainelControleService {

	private static Map<FaseGraficoAvaliacao, Predicate<Participante>> predicateMap;

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private EmailControleService emailControleService;

	@Autowired
	private FotoService fotoService;

	@Autowired
	private MensageriaRepository mensageriaRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	static {
		predicateMap = new HashMap<>(4);
		predicateMap.put(AUTOAVALIACAO, Participante::getPreenchidaAV1);
		predicateMap.put(AVALIACAO, Participante::getPreenchidaAV2);
		predicateMap.put(CONSENSO, Participante::getPreenchidaAV3);
		predicateMap.put(REVISAO, Participante::getPreenchidaAV4);
	}

	private static final String NAO_INICIADA = "Não iniciada";
	private static final String PARCIALMENTE_PREENCHIDA = "Parcialmente preenchida";
	private static final String TOTALMENTE_PREENCHIDA = "Totalmente preenchida";

	@Autowired
	private AvaliacaoParticipanteRepository participanteRepository;

	@Autowired
	protected ConfiguracaoService configuracaoService;

	@Autowired
	private ParticipanteService participanteService;

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	public PainelControleServiceImpl(AvaliacaoRepository avaliacaoRepository) {
		super(avaliacaoRepository);
	}

	private long countPreenchidas(List<Participante> participantes, Predicate<Participante> predicate) {
		return participantes.stream().filter(predicate).count();
	}

	@Override
	public ChartEvolucaoTO montarGraficoGaugePlot(AvaliacaoTO avaliacao, FaseGraficoAvaliacao type) {
		List<Participante> listAvs;

		if (avaliacao.getCampanha() != null && avaliacao.getCampanha().getId() != null && avaliacao.getId() == null)
			listAvs = participanteRepository.findByAvaliacaoCampanhaId(avaliacao.getCampanha().getId());
		else if (avaliacao.getId() != null)
			listAvs = participanteRepository.findByAvaliacaoId(avaliacao.getId());
		else
			return null;

		long preenchidas = countPreenchidas(listAvs, predicateMap.get(type));
		int total = listAvs.size();

		long value = total > 0 ? (preenchidas * 100) / total : 0;

		ChartEvolucaoTO evolucao = new ChartEvolucaoTO();
		evolucao.setValue((int) value);
		evolucao.setTotal(total);

		return evolucao;
	}

	private LocalDate toLocalDate(Date date) {
		return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	@Override
	public ChartLineElementosTO montarGraficoLineChart(AvaliacaoTO avaliacao, LocalDate dataInicial, LocalDate dataFinal) {
		List<Participante> listaAv1 = null;
		List<Participante> listaAv2 = null;
		List<Participante> listaAv3 = null;

		Long empresaId = getLoggedUser().getEmpresa().getId();

		if (avaliacao == null) {
			listaAv1 = participanteRepository.findByDataConclusaoAV1(empresaId, dataInicial, dataFinal);
			listaAv2 = participanteRepository.findByDataConclusaoAV2(empresaId, dataInicial, dataFinal);
			listaAv3 = participanteRepository.findByDataConclusaoAV3(empresaId, dataInicial, dataFinal);
		} else if (avaliacao.getCampanha() != null && avaliacao.getCampanha().getId() != null && avaliacao.getId() == null) {
			listaAv1 = participanteRepository.findByCampanhaAndDataConclusaoAV1(avaliacao.getCampanha().getId(), dataInicial, dataFinal);
			listaAv2 = participanteRepository.findByCampanhaAndDataConclusaoAV2(avaliacao.getCampanha().getId(), dataInicial, dataFinal);
			listaAv3 = participanteRepository.findByCampanhaAndDataConclusaoAV3(avaliacao.getCampanha().getId(), dataInicial, dataFinal);
		} else if (avaliacao.getId() != null) {
			listaAv1 = participanteRepository.findByAvaliacaoAndDataConclusaoAV1(avaliacao.getId(), dataInicial, dataFinal);
			listaAv2 = participanteRepository.findByAvaliacaoAndDataConclusaoAV2(avaliacao.getId(), dataInicial, dataFinal);
			listaAv3 = participanteRepository.findByAvaliacaoAndDataConclusaoAV3(avaliacao.getId(), dataInicial, dataFinal);
		}

		List<Object> listaEixoX = new ArrayList<>();
		List<Object> listaAV1 = new ArrayList<>();
		List<Object> listaAV2 = new ArrayList<>();
		List<Object> listaAV3 = new ArrayList<>();

		listaEixoX.add("x");
		listaAV1.add("Autoavaliação");
		listaAV2.add("Avaliação");
		listaAV3.add("Consenso");

		int quantidadeAv1 = 0;
		int quantidadeAv2 = 0;
		int quantidadeAv3 = 0;

		int quantidadeDias = (int) ChronoUnit.DAYS.between(dataInicial, dataFinal);

		for (int i = 0; i < quantidadeDias + 1; i++) {
			LocalDate data = dataInicial.plusDays(i);
			String periodo = data.toString();

			for (Participante participanteAV1 : listaAv1) {
				//TODO Usar LocalDate na origem
				LocalDate dataConclusao = toLocalDate(participanteAV1.getDataConclusaoAv1());
				if (data.isEqual(dataConclusao)) {
					quantidadeAv1++;
				}
			}

			for (Participante participanteAV2 : listaAv2) {
				//TODO Usar LocalDate na origem
				LocalDate dataConclusao = toLocalDate(participanteAV2.getDataConclusaoAv2());
				if (data.isEqual(dataConclusao)) {
					quantidadeAv2 = 1 + quantidadeAv2;
				}
			}

			for (Participante participanteAV3 : listaAv3) {
				//TODO Usar LocalDate na origem
				LocalDate dataConclusao = toLocalDate(participanteAV3.getDataConclusaoAv3());
				if (data.isEqual(dataConclusao)) {
					quantidadeAv3++;
				}
			}

			listaEixoX.add(periodo);
			listaAV1.add(quantidadeAv1);
			listaAV2.add(quantidadeAv2);
			listaAV3.add(quantidadeAv3);
		}

		List<Object> colunas = new ArrayList<>();
		colunas.add(listaEixoX.toArray());
		colunas.add(listaAV1.toArray());
		colunas.add(listaAV2.toArray());
		colunas.add(listaAV3.toArray());

		ChartLineElementosTO data = new ChartLineElementosTO();
		data.setColumns(colunas.toArray());

		return data;
	}

	@Override
	public ChartEvolucaoTO percentualPreenchimento(AvaliacaoTO avaliacao, FaseGraficoAvaliacao type) {
		int percentual = 0;
		ChartEvolucaoTO grafico = new ChartEvolucaoTO();

		// TODO Melhorar e reduzir esse código usando Predicates para mudar os critérios em vez de usar todos esses métodos de busca

		Long empresaId = getLoggedUser().getEmpresa().getId();

		if (AUTOAVALIACAO == type) {
			int totalAv1;
			int totalAv1Concluidas;
			List<Participante> listaAv1 = new ArrayList<>();
			List<Participante> listaAv1Concluidas = new ArrayList<>();
			LocalDate dataCriacaoAvaliacao;

			if (avaliacao != null && avaliacao.getId() == null && avaliacao.getCampanha() == null) {
				listaAv1 = participanteRepository.findAv1ByEmpresa(empresaId);
				if (listaAv1 != null && !listaAv1.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv1.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv1Concluidas = participanteRepository.findByDataConclusaoAV1(empresaId, dataCriacaoAvaliacao, dataFinal);
				}
			} else if (avaliacao != null && avaliacao.getId() == null && avaliacao.getCampanha() != null) {
				listaAv1 = participanteRepository.findAv1ByCampanha(avaliacao.getCampanha().getId());
				if (listaAv1 != null && !listaAv1.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv1.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv1Concluidas = participanteRepository.findByCampanhaAndDataConclusaoAV1(avaliacao.getCampanha().getId(), dataCriacaoAvaliacao, dataFinal);
				}
			} else if (avaliacao != null && avaliacao.getId() != null) {
				listaAv1 = participanteRepository.obterTotalAv1PorAvaliacao(avaliacao.getId());
				if (listaAv1 != null && !listaAv1.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv1.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv1Concluidas = participanteRepository.findByAvaliacaoAndDataConclusaoAV1(avaliacao.getId(), dataCriacaoAvaliacao, dataFinal);
				}
			}

			totalAv1 = listaAv1 != null && !listaAv1.isEmpty() ? listaAv1.size() : 0;
			totalAv1Concluidas = listaAv1Concluidas != null && !listaAv1Concluidas.isEmpty() ? listaAv1Concluidas.size() : 0;

			if (totalAv1 > 0 && totalAv1Concluidas > 0)
				percentual = (totalAv1Concluidas * 100) / totalAv1;
		} else if (AVALIACAO == type) {
			int totalAv2;
			int totalAv2Concluidas;
			List<Participante> listaAv2 = new ArrayList<>();
			List<Participante> listaAv2Concluidas = new ArrayList<>();
			LocalDate dataCriacaoAvaliacao;

			if (avaliacao != null && avaliacao.getId() == null && avaliacao.getCampanha() == null) {
				listaAv2 = participanteRepository.findAv2ByEmpresa(empresaId);
				if (listaAv2 != null && !listaAv2.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv2.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv2Concluidas = participanteRepository.findByDataConclusaoAV2(empresaId, dataCriacaoAvaliacao, dataFinal);
				}
			} else if (avaliacao != null && avaliacao.getId() == null && avaliacao.getCampanha() != null) {
				listaAv2 = participanteRepository.findAv2ByCampanha(avaliacao.getCampanha().getId());
				if (listaAv2 != null && !listaAv2.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv2.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv2Concluidas = participanteRepository.findByCampanhaAndDataConclusaoAV2(avaliacao.getCampanha().getId(), dataCriacaoAvaliacao, dataFinal);
				}
			} else if (avaliacao != null && avaliacao.getId() != null) {
				listaAv2 = participanteRepository.obterTotalAv2PorAvaliacao(avaliacao.getId());
				if (listaAv2 != null && !listaAv2.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv2.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv2Concluidas = participanteRepository.findByAvaliacaoAndDataConclusaoAV2(avaliacao.getId(), dataCriacaoAvaliacao, dataFinal);
				}
			}

			totalAv2 = listaAv2 != null && !listaAv2.isEmpty() ? listaAv2.size() : 0;
			totalAv2Concluidas = listaAv2Concluidas != null && !listaAv2Concluidas.isEmpty() ? listaAv2Concluidas.size() : 0;

			if (totalAv2 > 0 && totalAv2Concluidas > 0)
				percentual = (totalAv2Concluidas * 100) / totalAv2;
		} else if (CONSENSO == type) {
			int totalAv3 = 0;
			int totalAv3Concluidas = 0;
			List<Participante> listaAv3 = new ArrayList<>();
			List<Participante> listaAv3Concluidas = new ArrayList<>();
			LocalDate dataCriacaoAvaliacao = null;

			if (avaliacao != null && avaliacao.getId() == null && avaliacao.getCampanha() == null) {
				listaAv3 = participanteRepository.findAv3ByEmpresa(empresaId);
				if (listaAv3 != null && !listaAv3.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv3.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv3Concluidas = participanteRepository.findByDataConclusaoAV3(empresaId, dataCriacaoAvaliacao, dataFinal);
				}
			} else if (avaliacao != null && avaliacao.getId() == null && avaliacao.getCampanha() != null) {
				listaAv3 = participanteRepository.findAv3ByCampanha(avaliacao.getCampanha().getId());
				if (listaAv3 != null && !listaAv3.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv3.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv3Concluidas = participanteRepository.findByCampanhaAndDataConclusaoAV3(avaliacao.getCampanha().getId(), dataCriacaoAvaliacao, dataFinal);
				}
			} else if (avaliacao != null && avaliacao.getId() != null) {
				listaAv3 = participanteRepository.obterTotalAv3PorAvaliacao(avaliacao.getId());
				if (listaAv3 != null && !listaAv3.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv3.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv3Concluidas = participanteRepository.findByAvaliacaoAndDataConclusaoAV3(avaliacao.getId(), dataCriacaoAvaliacao, dataFinal);
				}
			}

			totalAv3 = listaAv3 != null && !listaAv3.isEmpty() ? listaAv3.size() : 0;
			totalAv3Concluidas = listaAv3Concluidas != null && !listaAv3Concluidas.isEmpty() ? listaAv3Concluidas.size() : 0;

			if (totalAv3 > 0 && totalAv3Concluidas > 0)
				percentual = (totalAv3Concluidas * 100) / totalAv3;
		} else if (REVISAO == type) {
			int totalAv4 = 0;
			int totalAv4Concluidas = 0;
			List<Participante> listaAv4 = new ArrayList<>();
			List<Participante> listaAv4Concluidas = new ArrayList<>();
			LocalDate dataCriacaoAvaliacao = null;

			if (avaliacao != null && avaliacao.getId() == null && avaliacao.getCampanha() == null) {
				listaAv4 = participanteRepository.findAv4ByEmpresa(empresaId);
				if (listaAv4 != null && !listaAv4.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv4.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv4Concluidas = participanteRepository.findByDataConclusaoAV4(empresaId, dataCriacaoAvaliacao, dataFinal);
				}
			} else if (avaliacao != null && avaliacao.getId() == null && avaliacao.getCampanha() != null) {
				listaAv4 = participanteRepository.findAv4ByCampanha(avaliacao.getCampanha().getId());
				if (listaAv4 != null && !listaAv4.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv4.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv4Concluidas = participanteRepository.findByCampanhaAndDataConclusaoAV4(avaliacao.getCampanha().getId(), dataCriacaoAvaliacao, dataFinal);
				}
			} else if (avaliacao != null && avaliacao.getId() != null) {
				listaAv4 = participanteRepository.findAv4ByAvaliacao(avaliacao.getId());
				if (listaAv4 != null && !listaAv4.isEmpty()) {
					dataCriacaoAvaliacao = toLocalDate(listaAv4.get(0).getAvaliacao().getCreationDate());
					LocalDate dataFinal = dataCriacaoAvaliacao.plusYears(2); // FIXME Critério arbitrário
					listaAv4Concluidas = participanteRepository.findByAvaliacaoAndDataConclusaoAV4(avaliacao.getId(), dataCriacaoAvaliacao, dataFinal);
				}
			}

			totalAv4 = listaAv4 != null && !listaAv4.isEmpty() ? listaAv4.size() : 0;
			totalAv4Concluidas = listaAv4Concluidas != null && !listaAv4Concluidas.isEmpty() ? listaAv4Concluidas.size() : 0;

			if (totalAv4 > 0 && totalAv4Concluidas > 0)
				percentual = (totalAv4Concluidas * 100) / totalAv4;
		}

		grafico.setValue(percentual);

		return grafico;
	}

	/**
	 * Faz o cálculo de preenchimentos agrupados por avaliadores (colaboradores)
	 * do gráfico de consensos (av3) do módulo de acompanhamentos.
	 *
	 * @param filtroAcompanhamentoTO O filtro de pesquisa.
	 * @return TotalAcompanhamentoGraficoTO Dados para fazer o gráfico de av3.
	 */
	@Override
	public TotalAcompanhamentoGraficoTO generateGraficoAv3(FiltroAcompanhamentoTO filtroAcompanhamentoTO) {

		// Participantes da avaliação de acordo com o filtro
		List<ParticipanteTO> listaParticipantes = filtrarParticipantes(filtroAcompanhamentoTO);

		//Lista usada para agrupar por avaliadores
		List<AvaliadorPreenchimentoTO> findAvaliadores = new ArrayList<>();
		// TO de retorno para o gráfico.
		TotalAcompanhamentoGraficoTO totalAcompanhamentoGraficoTO = new TotalAcompanhamentoGraficoTO();
		totalAcompanhamentoGraficoTO.setGraficos(new ArrayList<>());

		// Gráfico terá essas 3 divisões.
		if (filtroAcompanhamentoTO.getSituacao() == null) {
			totalAcompanhamentoGraficoTO.getGraficos().add(new GraficoPreenchimentoTO(SituacaoPreenchimento.NAO_PREENCH.getNome()));
			totalAcompanhamentoGraficoTO.getGraficos().add(new GraficoPreenchimentoTO(SituacaoPreenchimento.PARCIALMENTE_PREENCH.getNome()));
			totalAcompanhamentoGraficoTO.getGraficos().add(new GraficoPreenchimentoTO(SituacaoPreenchimento.TOTALMENTE_PREENCH.getNome()));
		} else {
			totalAcompanhamentoGraficoTO.getGraficos().add(new GraficoPreenchimentoTO(filtroAcompanhamentoTO.getSituacao().getNome()));

		}

		// Contagem das divisões sendo inicializadas.
		final AtomicDouble naoPreenchido = new AtomicDouble(0);
		final AtomicDouble parcialmente = new AtomicDouble(0);
		final AtomicDouble totalmente = new AtomicDouble(0);

		listaParticipantes.forEach(participanteTO -> {
			AvaliadorPreenchimentoTO findAvaliador = findOrCreateAvaliador(findAvaliadores, participanteTO);

			if (participanteTO.getLimiteAv3() != null) {
				List<PreenchimentoTO> listaPreenchimentos = preenchimentoService.obterListaDePreenchimentoDoParticipante(participanteTO);
				listaPreenchimentos.forEach(preenchimentoTO -> {
					if (preenchimentoTO.getGraduacaoAv3Id() != null) {
						findAvaliador.incrementTotalConsenso();
					}
				});
				findAvaliador.incrementTotal(listaPreenchimentos.size());
			}
		});

		findAvaliadores.stream().forEach(avaliadorPreenchimentoTO -> {
			contabilizaPreenchimentoGraficos(avaliadorPreenchimentoTO, "consenso", totalmente, parcialmente, naoPreenchido);
		});

		totalAcompanhamentoGraficoTO.setTotal(totalmente.get() + parcialmente.get() + naoPreenchido.get());
		totalAcompanhamentoGraficoTO.setTotalmentePreenchidos(totalmente.get());
		// Aqui, adicionamos as divisões confome contagem anterior.
		contabilizaDivisoesGrafico(totalAcompanhamentoGraficoTO, totalmente.get(), parcialmente.get(), naoPreenchido.get());

		return totalAcompanhamentoGraficoTO;
	}

	/**
	 * Faz as divisões do gráfico de acordo com os totalizadores
	 *
	 * @param totalAcompanhamentoGraficoTO
	 * @param totalmente
	 * @param parcialmente
	 * @param naoPreenchido
	 */
	private void contabilizaDivisoesGrafico(TotalAcompanhamentoGraficoTO totalAcompanhamentoGraficoTO, Double totalmente, Double parcialmente, Double naoPreenchido) {
		totalAcompanhamentoGraficoTO.getGraficos().stream().forEach(graficoPreenchimentoTO -> {
			if (graficoPreenchimentoTO.getLabel().equals(SituacaoPreenchimento.NAO_PREENCH.getNome())) {
				graficoPreenchimentoTO.setCount(naoPreenchido);
			} else if (graficoPreenchimentoTO.getLabel().equals(SituacaoPreenchimento.PARCIALMENTE_PREENCH.getNome())) {
				graficoPreenchimentoTO.setCount(parcialmente);
			} else if (graficoPreenchimentoTO.getLabel().equals(SituacaoPreenchimento.TOTALMENTE_PREENCH.getNome())) {
				graficoPreenchimentoTO.setCount(totalmente);
			}
		});
	}

	/**
	 * Faz o cálculo de preenchimentos agrupados por avaliadores (colaboradores)
	 * do gráfico de autoavaliacao (av1) e avaliação (av2) do módulo de acompanhamentos.
	 *
	 * @param filtroAcompanhamentoTO O filtro de pesquisa.
	 * @return TotalAcompanhamentoGraficoTO Dados para fazer o gráfico de av1 e av2.
	 */
	@Override
	public TotalAcompanhamentoGraficoTO generateGraficoAv1EAv2(FiltroAcompanhamentoTO filtroAcompanhamentoTO) {
		// Participantes da avaliação de acordo com o filtro
		List<ParticipanteTO> listaParticipantes = filtrarParticipantes(filtroAcompanhamentoTO);

		//Lista usada para agrupar por avaliadores
		List<AvaliadorPreenchimentoTO> findAvaliadores = new ArrayList<>();
		// TO de retorno para o gráfico.
		TotalAcompanhamentoGraficoTO totalAcompanhamentoGraficoTO = new TotalAcompanhamentoGraficoTO();
		totalAcompanhamentoGraficoTO.setGraficos(new ArrayList<>());

		// Gráfico terá essas 3 divisões.
		if (filtroAcompanhamentoTO.getSituacao() == null) {
			totalAcompanhamentoGraficoTO.getGraficos().add(new GraficoPreenchimentoTO(SituacaoPreenchimento.NAO_PREENCH.getNome()));
			totalAcompanhamentoGraficoTO.getGraficos().add(new GraficoPreenchimentoTO(SituacaoPreenchimento.PARCIALMENTE_PREENCH.getNome()));
			totalAcompanhamentoGraficoTO.getGraficos().add(new GraficoPreenchimentoTO(SituacaoPreenchimento.TOTALMENTE_PREENCH.getNome()));
		} else {
			totalAcompanhamentoGraficoTO.getGraficos().add(new GraficoPreenchimentoTO(filtroAcompanhamentoTO.getSituacao().getNome()));

		}

		// Contagem das divisões sendo inicializadas.
		final AtomicDouble naoPreenchido = new AtomicDouble(0);
		final AtomicDouble parcialmente = new AtomicDouble(0);
		final AtomicDouble totalmente = new AtomicDouble(0);

		listaParticipantes.forEach(participanteTO -> {
			AvaliadorPreenchimentoTO findAvaliador = findOrCreateAvaliador(findAvaliadores, participanteTO);

			if (participanteTO.getLimiteAv1() != null || participanteTO.getAvaliacao().getLimiteAv2() != null) {
				List<PreenchimentoTO> listaPreenchimentos = preenchimentoService.obterListaDePreenchimentoDoParticipante(participanteTO);
				listaPreenchimentos.forEach(preenchimentoTO -> {
					if (preenchimentoTO.getGraduacaoAv1Id() != null) {
						findAvaliador.incrementTotalAutoAv();
					}

					if (preenchimentoTO.getGraduacaoAv2Id() != null) {
						findAvaliador.incrementTotalAv();
					}
				});
				/*Caso este avaliador já tenha preenchido
				avaliação (av2) e não possua autoAv (Av1)
				setar com o valor de av2 para não influenciar no cálculo.*/
				if (((!participanteTO.getAvaliado().getId().equals(participanteTO.getAvaliador().getId())) || participanteTO.getAvaliacao().getLimiteAv2() == null)
					&& (findAvaliador.getTotalAv() != null && findAvaliador.getTotalAv() > 0)) {
					findAvaliador.setTotalAutoAv(findAvaliador.getTotalAv());
				}
				findAvaliador.incrementTotal(listaPreenchimentos.size());
			}
		});

		findAvaliadores.stream().forEach(avaliadorPreenchimentoTO -> {
			if (avaliadorPreenchimentoTO.getTotal() != null) {
				contabilizaPreenchimentoGraficos(avaliadorPreenchimentoTO, "avaliacao", totalmente, parcialmente, naoPreenchido);
			}
		});
		totalAcompanhamentoGraficoTO.setTotal(totalmente.get() + parcialmente.get() + naoPreenchido.get());
		totalAcompanhamentoGraficoTO.setTotalmentePreenchidos(totalmente.get());

		// Aqui, adicionamos as divisões confome contagem anterior.
		contabilizaDivisoesGrafico(totalAcompanhamentoGraficoTO, totalmente.get(), parcialmente.get(), naoPreenchido.get());

		return totalAcompanhamentoGraficoTO;
	}

	private void contabilizaPreenchimentoGraficos(AvaliadorPreenchimentoTO avaliadorPreenchimentoTO, String tipoGrafico, AtomicDouble totalmente, AtomicDouble parcialmente, AtomicDouble naoPreenchido) {
		if (tipoGrafico == "avaliacao") {
			if ((avaliadorPreenchimentoTO.getTotalAv() != null && avaliadorPreenchimentoTO.getTotalAv() == avaliadorPreenchimentoTO.getTotal())
				&& (avaliadorPreenchimentoTO.getTotalAutoAv() == null || avaliadorPreenchimentoTO.getTotalAutoAv() == avaliadorPreenchimentoTO.getTotal())) {
				totalmente.getAndAdd(1);
			} else if ((avaliadorPreenchimentoTO.getTotalAv() != null && avaliadorPreenchimentoTO.getTotalAv() > 0)
				&& (avaliadorPreenchimentoTO.getTotalAutoAv() != null && avaliadorPreenchimentoTO.getTotalAutoAv() > 0)) {
				parcialmente.getAndAdd(1);
			} else {
				naoPreenchido.getAndAdd(1);
			}
		}
		if (tipoGrafico == "consenso") {
			if (avaliadorPreenchimentoTO.getTotal() != null) {
				if (avaliadorPreenchimentoTO.getTotalConsenso() != null &&
					(avaliadorPreenchimentoTO.getTotalConsenso() == avaliadorPreenchimentoTO.getTotal())) {

					totalmente.getAndAdd(1);
				} else if (avaliadorPreenchimentoTO.getTotalConsenso() != null && avaliadorPreenchimentoTO.getTotalConsenso() > 0) {
					parcialmente.getAndAdd(1);
				} else {
					naoPreenchido.getAndAdd(1);
				}
			}
		}

	}

	/**
	 * Método usado para gerar o gráfico!
	 * <p>
	 * Caso não exista este avaliador na lista, cria-o e
	 * já seta dados pertinentes e o adciona à lista de avaliadores.
	 * Caso o encontre, apenas retorna-o.
	 *
	 * @param findAvaliadorList
	 * @param participanteTO
	 * @return AvaliadorPreenchimentoTO
	 */
	private AvaliadorPreenchimentoTO findOrCreateAvaliador(List<AvaliadorPreenchimentoTO> findAvaliadorList, ParticipanteTO participanteTO) {
		return findAvaliadorList
			.stream()
			.filter(avPreench -> avPreench.getAvaliadorId().equals(participanteTO.getAvaliador().getId()))
			.findAny().orElseGet(() -> {
				AvaliadorPreenchimentoTO avaliadorPreenchimentoTO = new AvaliadorPreenchimentoTO();
				avaliadorPreenchimentoTO.setAvaliadorId(participanteTO.getAvaliador().getId());
				if (participanteTO.getAvaliado().getId().equals(participanteTO.getAvaliador().getId())) {
					avaliadorPreenchimentoTO.setTotalAutoAv(0);
				}
				if (participanteTO.getAvaliacao().getLimiteAv2() != null) {
					avaliadorPreenchimentoTO.setTotalAv(0);
				}
				if (participanteTO.getAvaliacao().getLimiteAv3() != null) {
					avaliadorPreenchimentoTO.setTotalConsenso(0);
				}
				findAvaliadorList.add(avaliadorPreenchimentoTO);
				return avaliadorPreenchimentoTO;
			});
	}

	/**
	 * Ao clicar no gráfico de autoavaliação (av1) e avaliacao (av2),
	 * faz o cálculo em porcentagens do preenchimento dos avaliadores.
	 * <p>
	 * OBS.: O filtro por situação só é executado no final
	 * pois precisamos fazer o cálculo de todos os colaboradores antes
	 * para saber qual é a situação atual do preenchimento,
	 * agrupado por colaboradores
	 *
	 * @param filtroAcompanhamentoTO Filtragem de dados.
	 * @return List<ColaboradorAcompanhamentoTO> Lista de avaliadores com seus respectivos preenchimentos apresentados em porcentagem (av1 e av2).
	 */
	@Override
	public List<ColaboradorAcompanhamentoTO> dadosPreenchimentoAv1EAv2(FiltroAcompanhamentoTO filtroAcompanhamentoTO) {
		List<ColaboradorAcompanhamentoTO> listaColaboradorAcompanhamentos = new ArrayList<>();
		List<ParticipanteTO> listaParticipantes = filtrarParticipantes(filtroAcompanhamentoTO);

		listaParticipantes.forEach(participanteTO -> {
			if (participanteTO.getLimiteAv1() != null || participanteTO.getLimiteAv2() != null) {
				List<PreenchimentoTO> listaPreenchimentos = preenchimentoService.obterListaDePreenchimentoDoParticipante(participanteTO);
				AtomicInteger totalPreenchidoAv1 = new AtomicInteger(0);
				AtomicInteger totalPreenchidoAv2 = new AtomicInteger(0);

				listaPreenchimentos.forEach(preenchimentoTO -> {
					if (preenchimentoTO.getGraduacaoAv1Id() != null) {
						totalPreenchidoAv1.getAndIncrement();
					}

					if (preenchimentoTO.getGraduacaoAv2Id() != null) {
						totalPreenchidoAv2.getAndIncrement();
					}
				});

				ColaboradorAcompanhamentoTO colaborador = findOrCreateColaboradorByGrafico(participanteTO, listaColaboradorAcompanhamentos, "avaliacao");
				//Contabiliza os preenchimentos feitos da AV1
				if (participanteTO.getAvaliado().getId().equals(participanteTO.getAvaliador().getId())) {
					colaborador.setPossuiAutoavaliacao(true);
					contabilizaPreenchimentoAvaliacoes(colaborador, "AV1", totalPreenchidoAv1.get());
				}
				//Contabiliza os preenchimentos feitos da AV2
				if (participanteTO.getLimiteAv2() != null) {
					contabilizaPreenchimentoAvaliacoes(colaborador, "AV2", totalPreenchidoAv2.get());
				}
				colaborador.setTotalPreenchimentos(colaborador.getTotalPreenchimentos() + listaPreenchimentos.size());

				/*Caso este avaliador já tenha preenchido
				avaliação (av2) e não possua autoAv (Av1)
				setar com o valor de av2 para não influenciar no cálculo.*/
				if (((!participanteTO.getAvaliado().getId().equals(participanteTO.getAvaliador().getId())) || participanteTO.getAvaliacao().getLimiteAv2() == null)
					&& (colaborador.getPercentualAvaliacao() != null && colaborador.getPercentualAvaliacao() > 0)) {
					colaborador.setPossuiAutoavaliacao(false);
					colaborador.setPercentualAutoavaliacao(colaborador.getPercentualAvaliacao());
				}
			}
		});

		//Seta a situação do preenchimento para cada colaborador
		listaColaboradorAcompanhamentos.forEach(colaboradorAcompanhamentoTO -> {
			calculaPercentualPreenchimentoAvaliacoes(colaboradorAcompanhamentoTO);
			verificaSituacaoPreenchimentoAvaliacoes(colaboradorAcompanhamentoTO);
			if(!colaboradorAcompanhamentoTO.getPossuiAutoavaliacao()) {
				colaboradorAcompanhamentoTO.setPercentualAutoavaliacao(null);
			}
		});
		//Filtra os colaboradores pela situação
		return filtrarDadosSituacaoPreenchimento(filtroAcompanhamentoTO, listaColaboradorAcompanhamentos);
	}

	/**
	 * Recebe um filtro com o id do avaliador selecionado,
	 * e, a partir dele, obtém seus respectivos valiados
	 * e seus percentuais de consenso
	 * <p>
	 * OBS.: O filtro por situação só é executado no final
	 * pois precisamos fazer o cálculo de todos os colaboradores antes
	 * para saber qual é a situação atual do preenchimento,
	 * agrupado por colaboradores
	 *
	 * @param filtroAcompanhamentoTO
	 * @return List<ColaboradorAcompanhamentoTO>
	 */
	@Override
	public List<ColaboradorAcompanhamentoTO> dadosPreenchimentoAv3(FiltroAcompanhamentoTO filtroAcompanhamentoTO) {
		List<ColaboradorAcompanhamentoTO> listaColaboradorAcompanhamentos = new ArrayList<>();
		List<ParticipanteTO> listaParticipantes = filtrarParticipantes(filtroAcompanhamentoTO);

		listaParticipantes.forEach(participanteTO -> {
			if (participanteTO.getLimiteAv3() != null) {
				List<PreenchimentoTO> listaPreenchimentos = preenchimentoService.obterListaDePreenchimentoDoParticipante(participanteTO);
				AtomicInteger totalPreenchidoAv3 = new AtomicInteger(0);

				listaPreenchimentos.forEach(preenchimentoTO -> {
					if (preenchimentoTO.getGraduacaoAv3Id() != null) {
						totalPreenchidoAv3.getAndIncrement();
					}
				});
				ColaboradorAcompanhamentoTO colaborador = findOrCreateColaboradorByGrafico(participanteTO, listaColaboradorAcompanhamentos, "consenso");

				//Cria e adiciona o avaliado na lista
				colaborador.getAvaliadoConsensoList().add(createAvaliadoConsenso(participanteTO, totalPreenchidoAv3.doubleValue(), listaPreenchimentos.size()));
			}
		});

		listaColaboradorAcompanhamentos.forEach(colaboradorAcompanhamentoTO -> {
			//Calcula o percentual de preenchimento
			colaboradorAcompanhamentoTO
				.getAvaliadoConsensoList()
				.stream()
				.map((AvaliadoPreenchimentoTO avaliado) -> calculaPercentualPreenchimentoConsenso(avaliado))
				.collect(Collectors.toList());

			//Seta a situação do preenchimento para cada colaborador
			verificaSituacaoConsenso(colaboradorAcompanhamentoTO);
		});
		//Filtra os colaboradores pela situação do preenchimento
		return filtrarDadosSituacaoPreenchimento(filtroAcompanhamentoTO, listaColaboradorAcompanhamentos);
	}

	private AvaliadoPreenchimentoTO calculaPercentualPreenchimentoConsenso(AvaliadoPreenchimentoTO avaliado) {
		double consenso = avaliado.getPercentualConsenso() * 100d;
		double totalConsenso = avaliado.getTotalPreenchimentos() == 0d ? 1d : avaliado.getTotalPreenchimentos();

		avaliado.setPercentualConsenso((double) Math.round(consenso / totalConsenso));
		return avaliado;
	}

	@Override
	@Transactional
	public PreenchimentosAcompanhamentoTO envioDemensagensDeAcompanhamento(EnvioMensagemAcompanhamentoTO envioMensagemAcompanhamentoTO) {
		Date dataEnvio = new Date();

		for (FuncionarioSimpleTO funcionario : envioMensagemAcompanhamentoTO.getListaColaboradores()) {
			Mensageria mensageria = new Mensageria();
			mensageria.setDataHoraEnvio(dataEnvio);
			mensageria.setUsuarioDestino(mapper.map(funcionario, Funcionario.class));
			mensageria.setUsuarioEnvio(mapper.map(getLoggedUser(), Funcionario.class));
			mensageria.setTitulo(envioMensagemAcompanhamentoTO.getTitulo());
			mensageria.setTexto(envioMensagemAcompanhamentoTO.getTexto());
			mensageriaRepository.save(mensageria);
		}

		if (envioMensagemAcompanhamentoTO.getEnvioEmail() != null && envioMensagemAcompanhamentoTO.getEnvioEmail()) {
			emailControleService.sendEmailEnvioDeMensagensDeAcompanhamento(envioMensagemAcompanhamentoTO);
		}

		return null;
	}

	/**
	 * Situacao do preenchimento:
	 * Caso for nulo, quer dizer que esta opção não existe nesta avaliação
	 * (por isso, é setado com um valor "neutro" para não influenciar no cálculo);
	 * Caso todos os tipos de avaliação (av1 e av2) forem 100, quer dizer que é totalmente preenchida;
	 * Caso todos os tipos de avaliação (av1 e av2) forem 0, quer dizer que não começou o preenchimento (não preenchida);
	 * E caso contrário, é parcialmente preenchida;
	 *
	 * @param colaboradorAcompanhamentoTO
	 * @return void
	 */
	private void verificaSituacaoPreenchimentoAvaliacoes(ColaboradorAcompanhamentoTO colaboradorAcompanhamentoTO) {
		if (Optional.of(colaboradorAcompanhamentoTO).map(ColaboradorAcompanhamentoTO::getPercentualAutoavaliacao).orElse(100d).equals(100d)
			&& Optional.of(colaboradorAcompanhamentoTO).map(ColaboradorAcompanhamentoTO::getPercentualAvaliacao).orElse(100d).equals(100d)) {
			colaboradorAcompanhamentoTO.setSituacao(SituacaoPreenchimento.TOTALMENTE_PREENCH);

		} else if (Optional.of(colaboradorAcompanhamentoTO).map(ColaboradorAcompanhamentoTO::getPercentualAutoavaliacao).orElse(0d).equals(0d)
			&& Optional.of(colaboradorAcompanhamentoTO).map(ColaboradorAcompanhamentoTO::getPercentualAvaliacao).orElse(0d).equals(0d)) {
			colaboradorAcompanhamentoTO.setSituacao(SituacaoPreenchimento.NAO_PREENCH);

		} else {
			colaboradorAcompanhamentoTO.setSituacao(SituacaoPreenchimento.PARCIALMENTE_PREENCH);
		}
	}

	/**
	 * Situacao do preenchimento:
	 * Caso todos os avaliados possuírem 100% no consenso, quer dizer que é totalmente preenchida
	 * Caso todos os  avaliados possuírem 0% no consenso, quer dizer que não começou o preenchimento (não preenchida);
	 * E caso contrário, é parcialmente preenchida;
	 *
	 * @param colaboradorAcompanhamentoTO
	 * @return void
	 */
	private void verificaSituacaoConsenso(ColaboradorAcompanhamentoTO colaboradorAcompanhamentoTO) {
		if (colaboradorAcompanhamentoTO
			.getAvaliadoConsensoList()
			.stream()
			.allMatch(avaliadoPreenchimentoTO -> avaliadoPreenchimentoTO.getPercentualConsenso().equals(100d))) {
			colaboradorAcompanhamentoTO.setSituacao(SituacaoPreenchimento.TOTALMENTE_PREENCH);
		} else if (colaboradorAcompanhamentoTO
			.getAvaliadoConsensoList()
			.stream()
			.allMatch(avaliadoPreenchimentoTO -> avaliadoPreenchimentoTO.getPercentualConsenso().equals(0d))) {
			colaboradorAcompanhamentoTO.setSituacao(SituacaoPreenchimento.NAO_PREENCH);
		} else {
			colaboradorAcompanhamentoTO.setSituacao(SituacaoPreenchimento.PARCIALMENTE_PREENCH);
		}
	}

	private List<ParticipanteTO> filtrarParticipantes(FiltroAcompanhamentoTO filtroAcompanhamentoTO) {
		/*Caso o usuário logado seja do tipo perfil gestor de área,
		 *  deve ter acesso apenas  aos dados do seu departamento (setor).
		 *Caso for um funcionário, deverá estourar uma exceção
		 *Caso contrário, traz os participantes independente do departamento (setor).
		 */
		UserTO loggedUser = getLoggedUser();
		List<ParticipanteTO> participantes;
		if (loggedUser.getTipoPerfil().equals(TipoPerfil.GESTOR_AREA)) {
			participantes = participanteService.obterParticipantesPorAvaliacaoAndDepartamentoAndEmpresa(filtroAcompanhamentoTO.getAvaliacao().getId(), loggedUser.getDepartamento().getId());
		} else if (loggedUser.getTipoPerfil().equals(TipoPerfil.FUNCIONARIO)) {
			throw new ValidationException("validation.not-allowed", "acompanhamentos");
		} else {
			participantes = participanteService.obterParticipantesPorAvaliacaoAndEmpresa(filtroAcompanhamentoTO.getAvaliacao().getId());
		}
		return participantes.stream().filter((ParticipanteTO part) -> {
			return
				(filtroAcompanhamentoTO.getColaborador() == null || filtroAcompanhamentoTO.getColaborador().getId().equals(part.getAvaliador().getId())) &&
					(filtroAcompanhamentoTO.getDepartamento() == null || filtroAcompanhamentoTO.getDepartamento().getId().equals(part.getAvaliador().getDepartamento().getId()));
		}).collect(Collectors.toList());
	}

	private List<ColaboradorAcompanhamentoTO> filtrarDadosSituacaoPreenchimento(FiltroAcompanhamentoTO filtroAcompanhamentoTO, List<ColaboradorAcompanhamentoTO> colaboradorAcompanhamentoTOList) {
		//Verifica se possui a situação no filtro
		return colaboradorAcompanhamentoTOList
			.stream()
			.filter((ColaboradorAcompanhamentoTO acompanhamentoTO) ->
				filtroAcompanhamentoTO.getSituacao() == null
					|| filtroAcompanhamentoTO.getSituacao().getCode().equals(acompanhamentoTO.getSituacao().getCode()))
			.collect(Collectors.toList());
	}

	/**
	 * Contabiliza o total de preenchimentos de um colaborador
	 *
	 * @param colaboradorAcompanhamentoTO
	 * @param tipoAvaliacao
	 * @param totalPreenchido
	 */
	private void contabilizaPreenchimentoAvaliacoes(ColaboradorAcompanhamentoTO colaboradorAcompanhamentoTO, String tipoAvaliacao, int totalPreenchido) {
		if (tipoAvaliacao == "AV1") {
			if (colaboradorAcompanhamentoTO.getPercentualAutoavaliacao() == null) {
				colaboradorAcompanhamentoTO.setPercentualAutoavaliacao(0d);
			}

			colaboradorAcompanhamentoTO.setPercentualAutoavaliacao(colaboradorAcompanhamentoTO.getPercentualAutoavaliacao() + totalPreenchido);
		}
		if (tipoAvaliacao == "AV2") {
			if (colaboradorAcompanhamentoTO.getPercentualAvaliacao() == null) {
				colaboradorAcompanhamentoTO.setPercentualAvaliacao(0d);
			}

			colaboradorAcompanhamentoTO.setPercentualAvaliacao(colaboradorAcompanhamentoTO.getPercentualAvaliacao() + totalPreenchido);
		}
	}

	/**
	 * Método usado para gerar a lista de colaboradores a partir do gŕafico!
	 * <p>
	 * Procura o colaborador na lista:
	 * Caso não o encontre, cria uma instância e seta os valores pertinentes (varia de avaliacao para consenso)
	 * Caso o encontre, apenas retorna-o.
	 *
	 * @param participanteTO
	 * @param colaboradorAcompanhamentoTOList
	 * @param tipoGrafico
	 * @return ColaboradorAcompanhamentoTO
	 */
	private ColaboradorAcompanhamentoTO findOrCreateColaboradorByGrafico(ParticipanteTO participanteTO, List<ColaboradorAcompanhamentoTO> colaboradorAcompanhamentoTOList, String tipoGrafico) {
		if (tipoGrafico == "avaliacao") {
			return colaboradorAcompanhamentoTOList
				.stream()
				.filter(colaboradorAcompanhamentoTO -> colaboradorAcompanhamentoTO.getColaborador().getId().equals(participanteTO.getAvaliador().getId()))
				.findAny().orElseGet(() -> {
						ColaboradorAcompanhamentoTO colaboradorAcompanhamentoTO = new ColaboradorAcompanhamentoTO();
						colaboradorAcompanhamentoTO.setColaborador(participanteTO.getAvaliador());
						colaboradorAcompanhamentoTO.setTotalPreenchimentos(0d);
						colaboradorAcompanhamentoTO.setPossuiAutoavaliacao(false);
						colaboradorAcompanhamentoTOList.add(colaboradorAcompanhamentoTO);
						return colaboradorAcompanhamentoTO;
					}
				);
		}
		if (tipoGrafico == "consenso") {
			return colaboradorAcompanhamentoTOList
				.stream()
				.filter(colaboradorAcompanhamentoTO -> colaboradorAcompanhamentoTO.getColaborador().getId().equals(participanteTO.getAvaliador().getId()))
				.findAny().orElseGet(() -> {
						ColaboradorAcompanhamentoTO colaboradorAcompanhamentoTO = new ColaboradorAcompanhamentoTO();
						colaboradorAcompanhamentoTO.setColaborador(participanteTO.getAvaliador());
						//Cria a lista de avaliados
						colaboradorAcompanhamentoTO.setAvaliadoConsensoList(new ArrayList<>());
						colaboradorAcompanhamentoTOList.add(colaboradorAcompanhamentoTO);
						return colaboradorAcompanhamentoTO;
					}
				);
		}
		return null;
	}

	private void calculaPercentualPreenchimentoAvaliacoes(ColaboradorAcompanhamentoTO colaboradorAcompanhamentoTO) {
		double total = colaboradorAcompanhamentoTO.getTotalPreenchimentos() == 0d ? 1d : colaboradorAcompanhamentoTO.getTotalPreenchimentos();

		if (colaboradorAcompanhamentoTO.getPercentualAvaliacao() != null) {
			double autoAv = colaboradorAcompanhamentoTO.getPercentualAvaliacao() * 100d;
			colaboradorAcompanhamentoTO.setPercentualAutoavaliacao((double) Math.round(autoAv / total));

		}
		if (colaboradorAcompanhamentoTO.getPercentualAvaliacao() != null) {
			double av = colaboradorAcompanhamentoTO.getPercentualAvaliacao() * 100d;
			colaboradorAcompanhamentoTO.setPercentualAvaliacao((double) Math.round(av / total));
		}
	}

	private AvaliadoPreenchimentoTO createAvaliadoConsenso(ParticipanteTO participanteTO, Double totalPreenchido, Integer totalPreenchimentos) {
		AvaliadoPreenchimentoTO avaliadoPreenchimentoTO = new AvaliadoPreenchimentoTO();
		FuncionarioSimpleTO avaliado = participanteTO.getAvaliado();
		avaliado.setFoto(fotoService.obterAvatarFuncionario(avaliado.getId()).getBase64());
		avaliadoPreenchimentoTO.setPercentualConsenso(totalPreenchido);
		avaliadoPreenchimentoTO.setTotalPreenchimentos(totalPreenchimentos);
		avaliadoPreenchimentoTO.setAvaliado(participanteTO.getAvaliado());
		return avaliadoPreenchimentoTO;
	}

}
