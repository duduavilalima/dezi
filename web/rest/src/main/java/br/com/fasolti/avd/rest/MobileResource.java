package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.LogRequired;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.mobile.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("mobile")
public class MobileResource {

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private ApontamentoFuncionalService apontamentoFuncionalService;

	@Autowired
	private FotoService fotoService;

	@Autowired
	private TipoCompetenciaService tipoCompetenciaService;

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	private CompetenciaService competenciaService;

	@Autowired
	private ParticipanteService participanteService;

	@Autowired
	private FaleConoscoService faleConoscoService;

	@Autowired
	private ParticipanteMobileService participanteMobileService;

	@Autowired
	private PreenchimentoMobileService preenchimentoMobileService;

	@Autowired
	private AvaliacaoTipoCompetenciaMobileService avaliacaoTipoCompetenciaMobileService;

	@Autowired
	private DetalhamentoMobileService detalhamentoMobileService;

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/avaliados")
	public List<FuncionarioMobileTO> obterAvaliadosDoFuncionario(@RequestBody UsuarioMobileTO avaliador) throws Exception {
		return funcionarioService.obterAvaliadosAtivosPorFuncionarioDaEmpresa(avaliador);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.POST, value = "/apontamentos")
	@LogRequired(entity = "ApontamentoMobile")
	public final ApontamentoFuncionalMobileTO salvarApontamentoDoFuncionario(@RequestBody ApontamentoFuncionalMobileTO apontamentoTO) throws Exception {
		apontamentoFuncionalService.salvarApontamentoFuncionalMobile(apontamentoTO);
		return apontamentoTO;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/funcionariologado")
	public final FuncionarioMobileTO obterFuncionarioMobile(@RequestBody FuncionarioMobileTO usuario) throws Exception {
		return funcionarioService.obterFuncionarioMobile(usuario);
	}

	/*@RequestMapping(method = RequestMethod.POST, value = "/listaravaliacoes")
	public final AvaliacaoSyncMobileTO obterAvaliacoesDoAvaliadorMobile(@RequestBody AvaliacaoSyncMobileTO avaliacao) throws Exception {
		avaliacao.setDataInicioSync(new Date());
		return avaliacaoService.obterAvaliacoesDoAvaliadorMobile(avaliacao);
	}*/

	/*@RequestMapping(method = RequestMethod.POST, value = "/listardetalhamentos")
	public final CompetenciaMobileTO obterCompetenciaMobileTOFullObject(@RequestBody CompetenciaMobileTO competenciaMobileTO) throws Exception {
		competenciaMobileTO.setDataInicioSync(new Date());
		return competenciaService.obterCompetenciaMobileTOFullObject(competenciaMobileTO);
	}*/

	@RequestMapping(method = RequestMethod.GET, value = "/avaliacao/all")
	public List<AvaliacaoMobileTO> obterAvaliacoesMobile() {
		return participanteMobileService.obterAvaliacaoMobileList();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/avaliacao/{id:[0-9]+}")
	public AvaliacaoMobileTO obterAvaliacaoMobile(@PathVariable("id") Long avaliacaoId) {
		return preenchimentoMobileService.listAvTipoCompetenciaPorAvaliacao(avaliacaoId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "avaliacao/competencia/{id:[0-9]+}")
	public final CompetenciaMobileTO obterPreenchimentoMobilePorCompetencia(@PathVariable("id") Long competenciaId) {
		return preenchimentoMobileService.listPreenchimentoPorCompetencia(competenciaId);
	}

	@ResponseBody
	@RequestMapping(method = RequestMethod.PUT, value = "/avaliacao/competencia/detalhamento")
	@LogRequired(entity = "DetalhamentoMobile")
	public void updateDetalhamentoMobile(@RequestBody List<DetalhamentoMobileTO> detalhamentoMobileTOList) {
		detalhamentoMobileService.updateDetalhamentosMobile(detalhamentoMobileTOList);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/faleconosco")
	public void salvarFaleConosco(@RequestBody FaleConoscoMobileTO faleConoscoMobileTO) {
		faleConoscoService.salvarFaleConosco(faleConoscoMobileTO);
	}

}

