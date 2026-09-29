package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Avaliacao;
import br.com.fasolti.avd.repository.AvaliacaoRepository;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.search.AvaliacaoSearchTO;
import br.com.fasolti.avd.transfer.simple.GraduacaoSimpleTO;
import br.com.fasolti.service.util.BaseAvdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GraficoAvaliacaoServiceImpl extends BaseAvdService<Avaliacao, AvaliacaoTO, AvaliacaoSearchTO> implements GraficoAvaliacaoService {

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	private AvaliacaoParticipanteService avaliacaoParticipanteService;

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	private CargoCompetenciaService cargoCompetenciaService;

	@Autowired
	private DetalhamentoCargoCompetenciaService detalhamentoCargoCompetenciaService;

	@Autowired
	private GraduacaoService graduacaoService;

	@Autowired
	public GraficoAvaliacaoServiceImpl(AvaliacaoRepository avaliacaoRepository) {
		super(avaliacaoRepository);
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ChartGapCompetenciaTO graficoGapCompetencia(AvaliacaoTO to) {

		Avaliacao avaliacao = repository.findOne(to.getId());
		AvaliacaoTO avaliacaoTO = mapDefault(avaliacao);

		GapCompetenciaTO graficoGap = new GapCompetenciaTO();
		graficoGap.setAvaliacao(avaliacaoTO);
		graficoGap.setTipo("geral");

		List<PreenchimentoTO> listaPreenchimentos = preenchimentoService.findByAvaliacao(to);
		ChartGapCompetenciaTO gapGeral = montarGraficoGapCompetencia(graficoGap, listaPreenchimentos);

		return gapGeral;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ChartGapCompetenciaTO graficoGapCompetenciaByColaborador(ParticipanteTO participante) {

		Avaliacao avaliacao = repository.findOne(participante.getAvaliacao().getId());
		AvaliacaoTO avaliacaoTO = mapDefault(avaliacao);

		GapCompetenciaTO graficoGap = new GapCompetenciaTO();
		graficoGap.setAvaliacao(avaliacaoTO);
		graficoGap.setParticipante(participante);
		graficoGap.setTipo("colaborador");

		List<PreenchimentoTO> listaPreenchimentos = preenchimentoService.listByParticipante(participante);
		ChartGapCompetenciaTO gapColaborador = montarGraficoGapCompetencia(graficoGap, listaPreenchimentos);

		return gapColaborador;
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public ChartGapCompetenciaTO graficoGapCompetenciaByCargo(ParticipanteTO participante) {

		Avaliacao avaliacao = repository.findOne(participante.getAvaliacao().getId());
		AvaliacaoTO avaliacaoTO = mapDefault(avaliacao);

		GapCompetenciaTO graficoGap = new GapCompetenciaTO();
		graficoGap.setAvaliacao(avaliacaoTO);
		graficoGap.setParticipante(participante);
		graficoGap.setTipo("cargo");

		CargoTO cargoTO = new CargoTO();
		cargoTO.setId(participante.getAvaliado().getCargo().getId());

		List<PreenchimentoTO> preenchimentos = new ArrayList<>();
		List<PreenchimentoTO> resultado = preenchimentoService.findByCargo(cargoTO);

		for(PreenchimentoTO preenchimento : resultado){
			if(preenchimento.getParticipante().getAvaliacao().getId().equals(avaliacaoTO.getId()))
				preenchimentos.add(preenchimento);
		}

		ChartGapCompetenciaTO gapCargo = montarGraficoGapCompetencia(graficoGap, preenchimentos);

		return gapCargo;
	}

	@Override
	public ChartGapCompetenciaTO montarGraficoGapCompetencia(GapCompetenciaTO graficoGap, List<PreenchimentoTO> preenchimentos) {
		ChartGapCompetenciaTO chartGapCompetencia = new ChartGapCompetenciaTO();
		Avaliacao avaliacao = repository.findOne(graficoGap.getAvaliacao().getId());

		//As chaves dos mapas são os vertices do gráfico de radar, ou o eixo x do gráfico de barras. Ou seja, são os tipos de competências.
		//Os values dos mapas (a lista -> List<PontuacaoTO>) são as pontuações encontradas para cada tipo de competência.
		Map<String, List<PontuacaoTO>> mapaPontuacoesDesejadas = new HashMap<>();
		Map<String, List<PontuacaoTO>> mapaPontuacoesRegistradas = new HashMap<>();

		//remove o participante recebido por parametro para que esse não seja levado em consideração na estatística
		if(graficoGap.getTipo()!= null && graficoGap.getTipo().equals("cargo") && preenchimentos != null && !preenchimentos.isEmpty()){
			List<PreenchimentoTO> listaPreenchimentos = new ArrayList<>();
			listaPreenchimentos.addAll(preenchimentos);
			removerPreenchimentosDoParticipante(listaPreenchimentos, preenchimentos, graficoGap.getParticipante());
		}

		somarPontuacoes(map(avaliacao), mapaPontuacoesDesejadas, mapaPontuacoesRegistradas, preenchimentos);

		int tamanho = 0;
		if(mapaPontuacoesDesejadas.keySet().size() == mapaPontuacoesRegistradas.keySet().size())
			tamanho = mapaPontuacoesDesejadas.keySet().size();

		if(tamanho < 3)
			chartGapCompetencia.setType("bar");
		else
			chartGapCompetencia.setType("radar");


		Object[][] dados = new Object[2][tamanho];
		dados[0] = obterMediasFinaisDosTiposCompetencia(mapaPontuacoesDesejadas);
		dados[1] = obterMediasFinaisDosTiposCompetencia(mapaPontuacoesRegistradas);

		chartGapCompetencia.setLabels(mapaPontuacoesDesejadas.keySet().toArray());
		chartGapCompetencia.setSeries(new Object[]{"Graduação desejada", "Graduação registrada"});
		chartGapCompetencia.setColors(new Object[]{"#005129", "#D40D15"});
		chartGapCompetencia.setMatrix(dados);

		return chartGapCompetencia;
	}

	@Override
	public void somarPontuacoes(AvaliacaoTO avaliacao, Map<String, List<PontuacaoTO>> mapaPontuacoesDesejadas, Map<String, List<PontuacaoTO>> mapaPontuacoesRegistradas, List<PreenchimentoTO> listaPreenchimentos) {
		if(listaPreenchimentos != null && !listaPreenchimentos.isEmpty()){
			for (PreenchimentoTO preenchimento : listaPreenchimentos) {
				GraduacaoTO graduacao = null;

				if(avaliacao != null){
					if((avaliacao.getPreencheAv4() != null && avaliacao.getPreencheAv4() == true && preenchimento.getGraduacaoAv3Id() != null) || (avaliacao.getPreencheAv3() != null && avaliacao.getPreencheAv3() == true && preenchimento.getGraduacaoAv3Id() != null)) {
						graduacao = graduacaoService.getById(preenchimento.getGraduacaoAv3Id());
					} else if(avaliacao.getPreencheAv2() != null && avaliacao.getPreencheAv2() == true && preenchimento.getGraduacaoAv2Id() != null) {
						graduacao = graduacaoService.getById(preenchimento.getGraduacaoAv2Id());
					}
				}

				agruparPontosPorTipoCompetencia(preenchimento, graduacao, mapaPontuacoesDesejadas, mapaPontuacoesRegistradas);
			}
		}
	}

	private void agruparPontosPorTipoCompetencia(PreenchimentoTO preenchimento, GraduacaoTO graduacaoDaAvaliacaoCompetencia, Map<String, List<PontuacaoTO>> mapaPontuacoesDesejadas, Map<String, List<PontuacaoTO>> mapaPontuacoesRegistradas){
		if(isPreenchimentoValido(preenchimento, graduacaoDaAvaliacaoCompetencia)){
			//A variável tipoCompetencia é o vertice do gráfico de radar, ou o eixo x do gráfico de barras
			String tipoCompetencia = null;

			PontuacaoTO esperada = null;
			PontuacaoTO atingida = null;

			Long idCargo = null;
			Long idCompetenciaDetalhamento = null;
			Long idCompetencia = null;

			tipoCompetencia = preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia().getDescricaoTipoCompetencia();
			idCompetenciaDetalhamento = preenchimento.getAvaliacaoCompDet().getIdCompetenciaDetalhamento();
			idCompetencia = preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia().getIdCompetencia();
			idCargo = preenchimento.getParticipante().getAvaliado().getCargo().getId();


			CargoCompetenciaTO cargoCompetencia = null;

			if(idCargo != null && idCompetencia != null)
				cargoCompetencia = cargoCompetenciaService.findCargoCompetenciaByCargoAndCompetencia(idCargo, idCompetencia);


			if(cargoCompetencia != null && cargoCompetencia.getId() != null && idCompetenciaDetalhamento != null){
				DetalhamentoCargoCompetenciaTO gabarito = detalhamentoCargoCompetenciaService.findDetalhamentoCCByCargoCompetenciaAndCompetenciaDetalhamento(cargoCompetencia.getId(), idCompetenciaDetalhamento);

				if(gabarito != null){
					GraduacaoTO graduacaoDoCargoCompetencia = new GraduacaoTO();
					esperada = calculaPontuacaoDoDetalhamento(graduacaoDoCargoCompetencia, cargoCompetencia);
					atingida = calculaPontuacaoDoDetalhamento(graduacaoDaAvaliacaoCompetencia, cargoCompetencia);
				}
			}

			if(esperada != null && atingida != null && mapaPontuacoesDesejadas.containsKey(tipoCompetencia) && mapaPontuacoesRegistradas.containsKey(tipoCompetencia)){
				mapaPontuacoesDesejadas.get(tipoCompetencia).add(esperada);
				mapaPontuacoesRegistradas.get(tipoCompetencia).add(atingida);
			}
			else if(esperada != null && atingida != null && !mapaPontuacoesDesejadas.containsKey(tipoCompetencia) && !mapaPontuacoesRegistradas.containsKey(tipoCompetencia)){
				List<PontuacaoTO> pontuacoesEsperadas = new ArrayList<>();
				pontuacoesEsperadas.add(esperada);
				List<PontuacaoTO> pontuacoesAtingidas = new ArrayList<>();
				pontuacoesAtingidas.add(atingida);

				mapaPontuacoesDesejadas.put(tipoCompetencia, pontuacoesEsperadas);
				mapaPontuacoesRegistradas.put(tipoCompetencia, pontuacoesAtingidas);
			}

		}
	}

	private boolean isPreenchimentoValido(PreenchimentoTO preenchimento, GraduacaoTO graduacao){
		/*
		* <p>Graduações que tenham pontuações igual a zero não entram no cálculo pois são graduações do tipo "Não graduar" ou "Não se aplica".</p>
		* <p>Se essa graduações entram no cálculo elas distorcem a média final, por isto elas são desprezadas.</p>
		* <p>Logo os preenchimentos que possuem esse tipo de graduação também devem ser desprezados.</p>
		* */
		if (graduacao != null && graduacao.getPonto() > 0){

			if (preenchimento.getAvaliacaoCompDet() != null && preenchimento.getAvaliacaoCompDet().getAvaliacaoCompetencia().getDescricaoTipoCompetencia() != null ) {

				if (preenchimento.getParticipante() != null && preenchimento.getParticipante().getAvaliado() != null &&
					preenchimento.getParticipante().getAvaliado().getCargo() != null) {

					return true;
				}
			}
		}

		return false;
	}

	/**
	 * <p>Calcula "a nota" do detalhamento multiplicando o valor do ponto da graduação pelo peso do cargo competência.</p>
	 * <p>pg = valor do ponto da graduação</p>
	 * <p>pcc = pelo peso do cargo competência</p>
	 * <p>nota = pg x pcc.</p>
	 */
	private PontuacaoTO calculaPontuacaoDoDetalhamento(GraduacaoTO graduacao, CargoCompetenciaTO cargoCompetencia){
		PontuacaoTO pontuacao = null;

		if(cargoCompetencia != null && cargoCompetencia.getId() != null){
			pontuacao = new PontuacaoTO();
//			int nota = graduacao.getPonto() * cargoCompetencia.getPeso();
			int nota = 0;
			pontuacao.setPonto(graduacao.getPonto());
//			pontuacao.setPeso(cargoCompetencia.getPeso());
			pontuacao.setPeso(0);
			pontuacao.setTotal(nota);
		}

		return pontuacao;
	}

	/**
	 * <p>Calcula "a nota" do detalhamento multiplicando o valor do ponto da graduação pelo peso do cargo competência.</p>
	 * <p>pg = valor do ponto da graduação</p>
	 * <p>pcc = pelo peso do cargo competência</p>
	 * <p>nota = pg x pcc.</p>
	 */
	private PontuacaoTO calculaPontuacaoDoDetalhamento(GraduacaoSimpleTO graduacao, CargoCompetenciaTO cargoCompetencia){
		PontuacaoTO pontuacao = null;

		if(cargoCompetencia != null && cargoCompetencia.getId() != null){
			pontuacao = new PontuacaoTO();
//			int nota = graduacao.getPonto() * cargoCompetencia.getPeso();
			int nota = 0;
			pontuacao.setPonto(graduacao.getPonto());
//			pontuacao.setPeso(cargoCompetencia.getPeso());
			pontuacao.setPeso(0);
			pontuacao.setTotal(nota);
		}

		return pontuacao;
	}

	private Object[] obterMediasFinaisDosTiposCompetencia(Map<String, List<PontuacaoTO>> mapa) {
		Object[] data = new Object[mapa.keySet().size()];

		int i = 0;
		for (String chave : mapa.keySet()) {
			List<PontuacaoTO> listaPontuacoes = mapa.get(chave);
			int mediaFinal = calcularMediaFinalDosPontosDosDetalhamentos(listaPontuacoes);
			data[i] = mediaFinal;
			i++;
		}

		return data;
	}

	private int calcularMediaFinalDosPontosDosDetalhamentos(List<PontuacaoTO> listaPontuacoes){
		int somatorioDosPesos = 0;
		int somatorioDasNotas = 0;
		int mediaFinal = 0;

		for (PontuacaoTO pontuacao : listaPontuacoes) {
			somatorioDosPesos+= pontuacao.getPeso();
			somatorioDasNotas+= pontuacao.getTotal();
		}

		try {
			mediaFinal = somatorioDasNotas / somatorioDosPesos;
		}catch (Exception e){
			mediaFinal = 0;
		}

		return mediaFinal;
	}

	private void removerPreenchimentosDoParticipante(List<PreenchimentoTO> preenchimentos, List<PreenchimentoTO> listaPreenchimentos, ParticipanteTO participante){
		if(participante != null && participante.getId() != null) {
			for (int i=0; i<preenchimentos.size(); i++) {
				PreenchimentoTO preench = preenchimentos.get(i);

				GraduacaoTO graduacao = null;

				if((preench.getParticipante().getAvaliacao().getPreencheAv4() != null && preench.getParticipante().getAvaliacao().getPreencheAv4() == true)
					|| (preench.getParticipante().getAvaliacao().getPreencheAv3() != null && preench.getParticipante().getAvaliacao().getPreencheAv3() == true))
							if (preench.getGraduacaoAv3Id() != null) {
						        graduacao = graduacaoService.getById(preench.getGraduacaoAv3Id());
							}
				else if(preench.getParticipante().getAvaliacao().getPreencheAv2() != null && preench.getParticipante().getAvaliacao().getPreencheAv2() == true)
							if (preench.getGraduacaoAv2Id() != null) {
					           graduacao = graduacaoService.getById(preench.getGraduacaoAv2Id());
							}


				if(isPreenchimentoValido(preench, graduacao)){
					if(preench.getParticipante().equals(participante))
						listaPreenchimentos.remove(preench);
				}
			}
		}
	}
}
