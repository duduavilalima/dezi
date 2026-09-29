package br.com.fasolti.dezi.dfi.spreadSheet;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import br.com.fasolti.avd.domain.Cargo;
import br.com.fasolti.avd.domain.Competencia;
import br.com.fasolti.avd.domain.CompetenciaDetalhamento;
import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.domain.TipoCompetencia;

public abstract class SpreadSheet implements GenericSpreadSheet {

	protected void importaDadosPlanilha(Workbook workbook, Empresa empresa) throws IOException {
	}

	protected void importaDadosPlanilha(Workbook workbook, Empresa empresa,
			Map<String, TipoCompetencia> mapTipoCompetencia, Map<String, Competencia> mapCompetencia,
			Map<String, CompetenciaDetalhamento> mapDetalhamento, Map<String, Cargo> mapCargo) throws IOException {
	}

}
