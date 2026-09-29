package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.rest.util.LogRequired;
import br.com.fasolti.avd.service.AvaliacaoService;
import br.com.fasolti.avd.service.FuncionarioFormacaoService;
import br.com.fasolti.avd.service.FuncionarioIdiomaService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.FuncionarioFormacaoTO;
import br.com.fasolti.avd.transfer.FuncionarioIdiomaTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.TrocaDeSenha;
import br.com.fasolti.avd.types.SituacaoFuncionario;
import br.com.fasolti.commons.lang.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("funcionario")
public class FuncionarioResource extends BaseAvdResource<FuncionarioTO, FuncionarioTO> {

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private FuncionarioIdiomaService funcionarioIdiomaService;

	@Autowired
	private FuncionarioFormacaoService funcionarioFormacaoService;

	@Autowired
	private AvaliacaoService avaliacaoService;

	@Autowired
	public FuncionarioResource(
		FuncionarioService funcionarioService) {
		super(funcionarioService);
		this.funcionarioService = funcionarioService;
		this.funcionarioIdiomaService = funcionarioIdiomaService;
		this.funcionarioFormacaoService = funcionarioFormacaoService;
	}

	@LogRequired(entity = "TrocaDeSenha")
	@RequestMapping(method = RequestMethod.POST, value = "/trocarsenha")
	public ResponseEntity trocarSenha(@RequestBody TrocaDeSenha to) {
		// FIXME O usuário deve vir no request e não considerar que é sempre o logado
		try {
			funcionarioService.trocarSenha(to);
			return ResponseEntity.noContent().build();
		} catch (ValidationException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getErrorInfo());
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/idiomas")
	public List<FuncionarioIdiomaTO> idiomas(@PathVariable("id") Long funcionarioId) {
		FuncionarioTO funcionario = funcionarioService.getById(funcionarioId);
		return funcionarioIdiomaService.listByFuncionario(funcionario);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/formacoes")
	public List<FuncionarioFormacaoTO> formacoes(@PathVariable("id") Long funcionarioId) {
		FuncionarioTO funcionario = funcionarioService.getById(funcionarioId);
		return funcionarioFormacaoService.listByFuncionario(funcionario);
	}

	//FIXME: Remover esse método e substituir as referencias pelo método padrao
	@RequestMapping(method = {RequestMethod.GET},value = {"/getsemfiltro/{id:[0-9]+}"})
	public FuncionarioTO getByIdSemFiltro(@PathVariable Long id) {
		return funcionarioService.load(id);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/funcionariosparaavaliacao")
	public final List<FuncionarioTO> funcionariosParaAvaliacao(@RequestBody AvaliacaoTO to) throws Exception {
		return funcionarioService.funcionariosParaAvaliacao(to);
	}

	@RequestMapping(method = RequestMethod.GET, value = {"/{id:[0-9]+}/subordinados/{idAvaliacao:[0-9]+}"})
	public List<FuncionarioTO> subordinados(@PathVariable("id") Long funcionarioId, @PathVariable("idAvaliacao") Long avaliacaoId) {
		FuncionarioTO avaliador = funcionarioService.load(funcionarioId);
		AvaliacaoTO avaliacao = avaliacaoService.load(avaliacaoId);
		return funcionarioService.obterSubordinadosNaoSelecionadosPorAvaliadoEAvaliacao(avaliador, avaliacao);
	}

	@RequestMapping(method = RequestMethod.GET, value = {"/situacao/{situacao}"})
	public List<FuncionarioTO> obterColaboradoresPorSituacao(@PathVariable("situacao") SituacaoFuncionario situacao) {
		return funcionarioService.findBySituacaoProjections(situacao);
	}

	@RequestMapping(method = RequestMethod.GET, value = {"/colaboradorespdi"})
	public List<FuncionarioTO> obterColaboradoresPDI() {
		return funcionarioService.obterColaboradoresPDI();
	}

	@RequestMapping(method = RequestMethod.GET, value = {"/loadcolaboradorpdi/{id:[0-9]+}"})
	public FuncionarioTO loadColaboradorPDI(@PathVariable("id") Long funcionarioId) {
		return funcionarioService.loadColaboradorPDI(funcionarioId);
	}

}
