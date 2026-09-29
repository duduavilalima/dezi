package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.rest.util.LogRequired;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.search.ParticipanteSearchTO;
import br.com.fasolti.avd.types.TipoClient;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("participante")
public class ParticipanteResource
	extends BaseAvdResource<ParticipanteTO, ParticipanteSearchTO> {

	protected static final String COMP_ID = "/{competenciaId:[0-9]+}";
	protected static final String AVALIADOR_ID = "/{avaliadorId:[0-9]+}";
	protected static final String AVALIADO_ID = "/{avaliadoId:[0-9]+}";
	protected static final String AVALIACAO_ID = "/{avaliacaoId:[0-9]+}";
	protected static final String PERFIL_ID = "/{perfilId:[0-9]+}";
	protected static final String CAMPANHA_ID = "/{campanhaId:[0-9]+}";

	@Autowired
	private ParticipanteService participanteService;

	@Autowired
	private CompetenciaDetalhamentoService competenciaDetalhamentoService;

	@Autowired
	private CompetenciaService competenciaService;

	@Autowired
	private PreenchimentoService preenchimentoService;

	@Autowired
	private AvaliacaoCompetenciaService avaliacaoCompetenciaService;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	private CampanhaService campanhaService;

	@Autowired
	private Mapper mapper;

	@Autowired
	private AvaliacaoParticipanteService avaliacaoParticipanteService;

	@Autowired
	private GraduacaoCompetenciaService graduacaoCompetenciaService;

	@Autowired
	private ModeloGraduacaoCompService modeloGraduacaoCompService;

	@Autowired
	private ParticipanteEvolucaoService participanteEvolucaoService;

	@Autowired
	public ParticipanteResource(
		ParticipanteService participanteService) {
		super(participanteService);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/disponiveis")
	public List<ParticipanteTO> disponiveis() {
		return participanteService.listAllAvailable();
	}


	@RequestMapping(method = RequestMethod.GET, path = ID + "/competencias")
	public List<CompetenciaPreenchimentoTO> competencias(@PathVariable("id") Long participanteId) {
		ParticipanteTO participante = participanteService.getById(participanteId);
		return competenciaService.listByParticipante(participante);
	}


	@RequestMapping(method = RequestMethod.POST, path = ID + "/competencias" + COMP_ID)
	@LogRequired(entity = "CompetenciaPreenchimento")
	public CompetenciaPreenchimentoTO saveCompetencia(
		@RequestBody CompetenciaPreenchimentoTO competencia,
		@PathVariable("id") Long participanteId,
		@PathVariable("competenciaId") Long competenciaId
	) {
		return participanteService.salvarCompetencia(competencia, participanteId);
	}

	@RequestMapping(method = RequestMethod.POST, path = ID + "/competencias")
	@LogRequired(entity = "CompetenciaPreenchimento")
	public void saveCompetenciaList(
		@RequestBody List<CompetenciaPreenchimentoTO> competenciaList,
		@PathVariable("id") Long participanteId
	) {
		competenciaList.forEach((competencia) -> {
			participanteService.salvarCompetencia(competencia, participanteId);
		});
	}

	@RequestMapping(method = RequestMethod.GET, path = ID + "/preenchimentos")
	public List<PreenchimentoTO> preenchimentos(@PathVariable("id") Long participanteId) {
		ParticipanteTO participante = participanteService.getById(participanteId);
		return preenchimentoService.listByParticipante(participante);
	}

	@RequestMapping(method = RequestMethod.GET, path = "/avaliador/" + AVALIADOR_ID + "/competencias")
	public List<CompetenciaAvaliacao> competenciasPorAvaliadorEPerfil(@PathVariable("avaliadorId") Long avaliadorId) {

		FuncionarioTO funcionario = funcionarioService.getById(avaliadorId);

		List<CompetenciaAvaliacao> listaCompetencias = competenciaService.listCompetenciaAvaliacaoByAvaliadorAndPeriodo(funcionario, new Date());

		Collections.sort(listaCompetencias, new Comparator<CompetenciaAvaliacao>() {
			@Override
			public int compare(CompetenciaAvaliacao o1, CompetenciaAvaliacao o2) {
				int result = o1.getDescricaoAvaliacao().compareTo(o2.getDescricaoAvaliacao());

				if (result == 0)
					result = o1.getTipoCompetencia().compareTo(o2.getTipoCompetencia());

				if (result == 0)
					result = o1.getDescricaoCompetencia().compareTo(o2.getDescricaoCompetencia());

				return result;
			}
		});

		return listaCompetencias;
	}


	@RequestMapping(method = RequestMethod.POST, path = "/saveAvaliacaoCompDet")
	@LogRequired(entity = "AvaliacaoCompDet")
	public AvaliacaoCompDetTO saveAvaliacaoCompDet(@RequestBody AvaliacaoCompDetTO avaliacaoCompDet) {
		Date dataPreenchimento = new Date();
		//TODO Colocar para atualizar o campo preenchidaAV e a data de conclusão no participante
		for (PreenchimentoTO preenchimentoTO : avaliacaoCompDet.getPreenchimentos()) {

			ModeloGraduacaoCompTO modeloGraduacaoComp = modeloGraduacaoCompService.obterModeloGraduacaoCompProjections(preenchimentoTO.getAvaliacaoCompDet().getIdModeloGraduacao(), preenchimentoTO.getParticipante().getAvaliacao().getId());

			if (preenchimentoTO.getGraduacaoAv1Id() != null) {
				GraduacaoCompetenciaTO graduacaoCompAv1 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacaoProjections(modeloGraduacaoComp.getId(), preenchimentoTO.getGraduacaoAv1Id());
				preenchimentoTO.setGraduacaoCompAv1Id(graduacaoCompAv1.getId());
			}

			if (preenchimentoTO.getGraduacaoAv2Id() != null) {
				GraduacaoCompetenciaTO graduacaoCompAv2 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacaoProjections(modeloGraduacaoComp.getId(), preenchimentoTO.getGraduacaoAv2Id());
				preenchimentoTO.setGraduacaoCompAv2Id(graduacaoCompAv2.getId());
			}

			if (preenchimentoTO.getGraduacaoAv3Id() != null) {
				GraduacaoCompetenciaTO graduacaoCompAv3 = graduacaoCompetenciaService.getByModeloGraduacaoIdAndIdGraduacaoProjections(modeloGraduacaoComp.getId(), preenchimentoTO.getGraduacaoAv3Id());
				preenchimentoTO.setGraduacaoCompAv3Id(graduacaoCompAv3.getId());
			}

			preenchimentoTO.setDataGravacao(dataPreenchimento.getTime());
			preenchimentoTO.setDataPreenchimento(dataPreenchimento.getTime());
			preenchimentoTO.setTipoClient(TipoClient.WEB);
			preenchimentoService.update(preenchimentoTO, preenchimentoTO.getId());

			Long participanteId = preenchimentoTO.getParticipante().getId();
			ParticipanteTO participante = preenchimentoTO.getParticipante();

			ParticipanteEvolucaoTO participanteEvolucao = participanteEvolucaoService.calcularEvolucao(participante);

			long total = participanteEvolucao.getTotal();
			long preenchimentoAv1 = participanteEvolucao.getPreenchimentoAv1();
			long preenchimentoAv2 = participanteEvolucao.getPreenchimentoAv2();

			if (preenchimentoTO.getGraduacaoAv1Id() != null) {
				if (participante.getAvaliacao().getPreencheAv1() && participante.getDataInicioAv1() == null)
					participante.setDataInicioAv1(new Date());

				participante.setPreenchidaAV1(total == preenchimentoAv1);

				if (participante.getPreenchidaAV1())
					participante.setDataConclusaoAv1(new Date());
			}


			if (preenchimentoTO.getGraduacaoAv2Id() != null && participante.getAvaliacao().getPreencheAv2() && participante.getDataInicioAv2() == null) {
				participante.setDataInicioAv2(new Date());
			}

			participante.setPreenchidaAV2(total == preenchimentoAv2);

			if (participante.getDataInicioAv2() == null)
				participante.setDataInicioAv2(new Date());

			if (participante.getPreenchidaAV2())
				participante.setDataConclusaoAv2(new Date());

			if (participante.getConsensoLiberado() != null && participante.getConsensoLiberado() != true && avaliacaoCompDet.getFinalizou() != null && avaliacaoCompDet.getFinalizou() == true) {
				participante.setConsensoLiberado(participante.getPreenchidaAV1() && participante.getPreenchidaAV2());
			}

			participanteService.update(participante, participante.getId());

		}

		return avaliacaoCompDet;
	}

}
