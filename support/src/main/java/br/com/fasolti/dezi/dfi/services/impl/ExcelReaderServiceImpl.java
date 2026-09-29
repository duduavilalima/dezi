package br.com.fasolti.dezi.dfi.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.StringJoiner;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.dezi.dfi.commons.InvalidRowException;
import br.com.fasolti.dezi.dfi.commons.UtilsRow;
import br.com.fasolti.dezi.dfi.repositories.EmpresaRepository;
import br.com.fasolti.dezi.dfi.services.ExcelReaderService;
import br.com.fasolti.dezi.dfi.spreadSheet.impl.CadastroAssociacoes;
import br.com.fasolti.dezi.dfi.spreadSheet.impl.CadastroFuncionario;
import br.com.fasolti.dezi.dfi.spreadSheet.impl.cadastroCompetencias;

@Service
public class ExcelReaderServiceImpl implements ExcelReaderService {

	private static final Logger log = LoggerFactory.getLogger(ExcelReaderServiceImpl.class);

	private final String ERRO_LOG = "erro.log";
	private final String RESULT_LOG = "result.log";

	private StringJoiner linhas = new StringJoiner("");

	@Value("${dezi.empresa.name}")
	private String empresaNome;

	@Value("${funcionario.import}")
	private String cadastroFuncionarioIm;

	@Value("${comp.import}")
	private String cadastroCompetenciaIm;

	@Value("${associacoes.import}")
	private String cadastroAssociacoesIm;

	@Autowired
	private EmpresaRepository empresaRepository;

	@Autowired
	private CadastroFuncionario cadastroFuncionario;

	@Autowired
	private cadastroCompetencias cadastroCompetencias;

	@Autowired
	private CadastroAssociacoes cadastroAssociacoes;
	
//	@Autowired
//	private MapUtilsImport mapImport;

	long timeElapsedF;
	long timeElapsedC;
	long timeElapsedA;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = InvalidRowException.class)
	public void getFileXLSX() throws IOException {
		log.info("Criando importação da Planilha Excel...");
		Path erroLog = Paths.get(ERRO_LOG);
		Path resultLog = Paths.get(RESULT_LOG);
		Empresa empresa = empresaRepository.findByNome(empresaNome);
		if (empresa == null) {
			throw new RuntimeException(String.format("Empresa \"%s\" não encontrada!", empresaNome));
		}
		log.info("Empresa encontrada: {} , ID: {}",empresa.getNome(),empresa.getId());
		try {

			if (cadastroFuncionarioIm != null  && !cadastroFuncionarioIm.isEmpty()) {
				File cadastroFuncionarioFile = pathFile(cadastroFuncionarioIm);
				Instant startF = Instant.now();
				cadastroFuncionario.importaDadosPlanilha(WorkbookFactory.create(cadastroFuncionarioFile), empresa);
				Instant finishF = Instant.now();

				timeElapsedF = Duration.between(startF, finishF).toMillis();
				log.info("Tempo de Importação: " + timeElapsedF + "ms");
				UtilsRow.dadosImportados.add("Tempo de Importação: " + timeElapsedF + "ms\n");
			} else {
				log.info("Planilha de cadastro de funcionários não foi fornecida. Ingorando importação.");
			}

			if (cadastroCompetenciaIm != null  && !cadastroCompetenciaIm.isEmpty()) {
				File cadastroCompetenciaFile = pathFile(cadastroCompetenciaIm);
				Instant startC = Instant.now();
				cadastroCompetencias.importaDadosPlanilha(WorkbookFactory.create(cadastroCompetenciaFile), empresa);
				Instant finishC = Instant.now();

				timeElapsedC = Duration.between(startC, finishC).toMillis();
				log.info("Tempo de Importação: " + timeElapsedC + "ms");
				UtilsRow.dadosImportados.add("Tempo de Importação: " + timeElapsedC + "ms\n");
			} else {
				log.info("Planilha de cadastro de competências não foi fornecida. Ingorando importação.");
			}

			if (cadastroAssociacoesIm != null  && !cadastroAssociacoesIm.isEmpty()) {
				File cadastroAssociacoesFile = pathFile(cadastroAssociacoesIm);
				Instant startA = Instant.now();

				//Para a importação individual
				//			cadastroAssociacoes.importaDadosPlanilha(
				//			WorkbookFactory.create(pathFile(cadastroAssociacoesIm)), empresa,
				//			mapImport.getTipoCompetenciaMap(empresa.getId()),
				//			mapImport.getCompetenciaMap(empresa.getId()),
				//			mapImport.getCompetenciaDetalhamentoMap(),
				//			mapImport.getCargoMap(empresa.getId()));

				cadastroAssociacoes.importaDadosPlanilha(
						WorkbookFactory.create(cadastroAssociacoesFile), empresa,
						cadastroCompetencias.getMapTipoCompetencia(),
						cadastroCompetencias.getMapCompetencia(),
						cadastroCompetencias.getMapDetalhamento(),
						cadastroFuncionario.getMapCargo());

				Instant finishA = Instant.now();

				timeElapsedA = Duration.between(startA, finishA).toMillis();
				log.info("Tempo de Importação: " + timeElapsedA + "ms");
				UtilsRow.dadosImportados.add("Tempo de Importação: " + timeElapsedA + "ms\n");
			} else {
				log.info("Planilha de cadastro de associações ao cargo não foi fornecida. Ingorando importação.");
			}


			log.info("Tempo total de importação: {}ms", ((timeElapsedF + timeElapsedC + timeElapsedA)));
		} catch (EncryptedDocumentException | IOException e) {
			UtilsRow.erros.put("import", e.getMessage());
			log.error("Erro geral: {}", e.getMessage());
			e.printStackTrace();
		} finally {
			if (!UtilsRow.erros.isEmpty()) {
				UtilsRow.erros.forEach((k, erro) -> linhas.add(erro));
				Files.write(resultLog, "".toString().getBytes());
				linhas.add("Total de erros encontrados: " + UtilsRow.erros.size());
				linhas.add("\nTempo total de importação: " + ((timeElapsedF + timeElapsedC + timeElapsedA)) + "ms");
				Files.write(erroLog, linhas.toString().getBytes());
				throw new IllegalArgumentException("\tErros encontrado: " + UtilsRow.erros.values());
			} else {
				UtilsRow.dadosImportados.forEach(r -> linhas.add(r + "\n"));
				linhas.add("\nTempo total de importação: " + ((timeElapsedF + timeElapsedC + timeElapsedA)) + "ms");
				Files.write(erroLog, "".toString().getBytes());
				Files.write(resultLog, linhas.toString().getBytes());
			}
		}
	}
	
	private File pathFile(String pathFile) throws FileNotFoundException {
		return new File(Paths.get(System.getProperty("user.dir"), pathFile).toString());
	}
}
