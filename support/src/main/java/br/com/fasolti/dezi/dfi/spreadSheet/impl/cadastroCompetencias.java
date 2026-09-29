package br.com.fasolti.dezi.dfi.spreadSheet.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fasolti.avd.domain.Competencia;
import br.com.fasolti.avd.domain.CompetenciaDetalhamento;
import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.domain.Graduacao;
import br.com.fasolti.avd.domain.ModeloGraduacao;
import br.com.fasolti.avd.domain.TipoCompetencia;
import br.com.fasolti.dezi.dfi.sheet.impl.CompetenciaSheet;
import br.com.fasolti.dezi.dfi.sheet.impl.DetalhamentoSheet;
import br.com.fasolti.dezi.dfi.sheet.impl.EscalaGraduacoesSheet;
import br.com.fasolti.dezi.dfi.sheet.impl.GraduacaoSheet;
import br.com.fasolti.dezi.dfi.sheet.impl.TipoCompetenciaSheet;

@Component
public class cadastroCompetencias extends br.com.fasolti.dezi.dfi.spreadSheet.SpreadSheet {

	private static final Logger log = LoggerFactory.getLogger(cadastroCompetencias.class);

	private final String escalaGraduacoes = "1. Escala de Graduações";
	private final String graduacoes = "2. Graduações";
	private final String tipoCompetencias = "3. Tipo de Competências";
	private final String competencias = "4. Competências";
	private final String detalhamentos = "5. Detalhamentos";

	@Autowired
	private EscalaGraduacoesSheet escalaGraduacoesSheet;

	@Autowired
	private GraduacaoSheet graduacaoSheet;

	@Autowired
	private TipoCompetenciaSheet tipoCompetenciaSheet;

	@Autowired
	private CompetenciaSheet competenciaSheet;

	@Autowired
	private DetalhamentoSheet detalhamentoSheet;
	
	private Map<String, ModeloGraduacao> mapEscalaGraduacoes;
	private Map<Long, Graduacao> mapGraduacoes;
	private Map<String, TipoCompetencia> mapTipoCompetencia;
	private Map<String, Competencia> mapCompetencia;
	private Map<String, CompetenciaDetalhamento> mapDetalhamento;

	

	@Override
	public void importaDadosPlanilha(Workbook workbook, Empresa empresa) throws IOException {
		mapEscalaGraduacoes = escalaGraduacoesSheet.importAndSaveAsMap(workbook, escalaGraduacoes, empresa);
		mapGraduacoes = graduacaoSheet.importAndSaveAsMap(workbook, graduacoes, mapEscalaGraduacoes);
		mapTipoCompetencia = tipoCompetenciaSheet.importAndSaveAsMap(workbook, tipoCompetencias, empresa);
		mapCompetencia = competenciaSheet.importAndSaveAsMap(workbook, competencias, empresa, mapTipoCompetencia);
		mapDetalhamento = detalhamentoSheet.importAndSaveAsMap(workbook, detalhamentos, empresa, mapCompetencia,
				mapEscalaGraduacoes, mapGraduacoes);
		log.info("Fim da importação dos dados!");
		workbook.close();
	}

	public Map<String, TipoCompetencia> getMapTipoCompetencia() {
		return mapTipoCompetencia;
	}

	public Map<String, Competencia> getMapCompetencia() {
		return mapCompetencia;
	}

	public Map<String, CompetenciaDetalhamento> getMapDetalhamento() {
		return mapDetalhamento;
	}

}
