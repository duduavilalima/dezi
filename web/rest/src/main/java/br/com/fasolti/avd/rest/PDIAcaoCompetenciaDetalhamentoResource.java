package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.util.BaseAvdResource;
import br.com.fasolti.avd.service.PDIAcaoCompetenciaDetalhamentoService;
import br.com.fasolti.avd.transfer.PDIAcaoCompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.search.PDIAcaoCompetenciaDetalhamentoSearchTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
@RequestMapping("pdiacaocompdet")
public class PDIAcaoCompetenciaDetalhamentoResource extends BaseAvdResource<PDIAcaoCompetenciaDetalhamentoTO, PDIAcaoCompetenciaDetalhamentoSearchTO> {

	@Autowired
	private PDIAcaoCompetenciaDetalhamentoService pdiAcaoCompDetService;

	@Autowired
	public PDIAcaoCompetenciaDetalhamentoResource(PDIAcaoCompetenciaDetalhamentoService pdiAcaoCompDetService) {
		super(pdiAcaoCompDetService);
	}

	@RequestMapping(method = RequestMethod.GET, value = {"/{avaliadoId:[0-9]+}/list"})
	public List<PDIAcaoCompetenciaDetalhamentoTO> listarAcaoPDI(@PathVariable Long avaliadoId) {
		return pdiAcaoCompDetService.listarPorPDIacaoAvaliado(avaliadoId);
	}

}
