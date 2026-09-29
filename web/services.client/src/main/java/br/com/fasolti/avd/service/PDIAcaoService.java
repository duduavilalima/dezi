package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.PDIAcaoTO;
import br.com.fasolti.avd.transfer.search.PDIAcaoSearchTO;
import br.com.fasolti.avd.types.SituacaoPDI;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;
import java.util.List;

public interface PDIAcaoService extends DeziService<PDIAcaoTO> {

	List<PDIAcaoTO> listarAcaoPDI(Long IdFuncionario);

	List<PDIAcaoTO> listarPorPDIacaoAvaliado(Long IdFuncionario);

	List<PDIAcaoTO> listarAcaoPDIPorSituacao(Long IdFuncionario, SituacaoPDI situacaoPDI);

	List<PDIAcaoTO> listarPorPDIacaoAvaliadoPorSituacao(Long IdFuncionario, SituacaoPDI situacaoPDI);

	List<PDIAcaoTO> filterSearchRelatorioPDI(PDIAcaoSearchTO searchTO);

	String exportarRelatorio(List<PDIAcaoTO> pdiAcaoTOList) throws IOException;

	String imprimirRelatorioJasper(List<PDIAcaoTO> pdiAcaoTOList) throws IOException, JRException;

	void excluirAcao(Long id);
}
