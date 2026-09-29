package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseDeziResource;
import br.com.fasolti.avd.service.PDIAcaoService;
import br.com.fasolti.avd.transfer.PDIAcaoTO;
import br.com.fasolti.avd.transfer.search.PDIAcaoSearchTO;
import br.com.fasolti.avd.types.SituacaoPDI;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("pdiacao")
public class PDIAcaoResource extends BaseDeziResource<PDIAcaoTO> {

	@Autowired
	private PDIAcaoService pdiAcaoService;

	@Autowired
	public PDIAcaoResource(PDIAcaoService service) {
		super(service);
	}

	@RequestMapping(method = RequestMethod.GET, value = {"/{id:[0-9]+}/list"})
	public List<PDIAcaoTO> listarAcaoPDI(@PathVariable Long id) {
		return pdiAcaoService.listarPorPDIacaoAvaliado(id);
	}

	@RequestMapping(method = RequestMethod.GET, value = {"/{id:[0-9]+}/{situacao}/list"})
	public List<PDIAcaoTO> listarAcaoPDIPorSituacao(@PathVariable Long id, @PathVariable SituacaoPDI situacao) {
		return pdiAcaoService.listarPorPDIacaoAvaliadoPorSituacao(id, situacao);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/searchRelatorio")
	public List<PDIAcaoTO> searchPDI(@RequestBody PDIAcaoSearchTO pdiAcaoSearchTO) {
		return pdiAcaoService.filterSearchRelatorioPDI(pdiAcaoSearchTO);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/exportarRelatorio")
	public HttpEntity<String> exportarRelatorio(@RequestBody List<PDIAcaoTO> pdiAcaoTOList) throws IOException {
		String excelContent = pdiAcaoService.exportarRelatorio(pdiAcaoTOList);

		/*Seta o header para passar o mediaType correto para o frontend e não dar erro de JSON inválido*/
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		return new HttpEntity<String>(excelContent, header);
	}

	@RequestMapping(method = RequestMethod.POST, path = "/imprimirRelatorio")
	public HttpEntity<String> gerarPDF(@RequestBody List<PDIAcaoTO> pdiAcaoTOList) throws IOException, JRException {
		String pdfContent = pdiAcaoService.imprimirRelatorioJasper(pdiAcaoTOList);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "pdf"));
		return new HttpEntity<String>(pdfContent, header);
	}

	/*
	Foi necessário alterar a chamada de exclusão. O método default para exlusão é o BaseCrudResource.delete, porém
	este faz uso do método "getServiceFilter", o qual é implementado pela classe filha de BaseCrudService.
	O método "getServiceFilter" é utilizado em outra parte da funcionalidade de PDI, e faz uso de projeção com
	QueryDSL para filtrar as ações de pdi que o usuário logado tem acesso, porém essa projeção faz referência
	a propriedade "departamento" de funcionário, na exlusão não há como chegar até essa propriedade e o sistema
	gera uma exceção na hora de realizar o filtro.
 	*/
	@RequestMapping(method = RequestMethod.POST, value = {"/{id:[0-9]+}/excluiracao"})
	public ResponseEntity<Void> excluirAcao(@PathVariable Long id) {
		pdiAcaoService.excluirAcao(id);
		return ResponseEntity.noContent().build();
	}
}
