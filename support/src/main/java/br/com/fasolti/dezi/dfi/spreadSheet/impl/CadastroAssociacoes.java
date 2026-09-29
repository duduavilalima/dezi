package br.com.fasolti.dezi.dfi.spreadSheet.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fasolti.avd.domain.Cargo;
import br.com.fasolti.avd.domain.Competencia;
import br.com.fasolti.avd.domain.CompetenciaDetalhamento;
import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.domain.TipoCompetencia;
import br.com.fasolti.dezi.dfi.sheet.impl.AssociacoesCargoSheet;
import br.com.fasolti.dezi.dfi.spreadSheet.SpreadSheet;

@Component
public class CadastroAssociacoes extends SpreadSheet {

	private static final Logger log = LoggerFactory.getLogger(CadastroAssociacoes.class);

	private final String AssociacoesCargo = "Associações ao cargo";

	@Autowired
	private AssociacoesCargoSheet associacoesCargoSheet;

	@Override
	public void importaDadosPlanilha(Workbook workbook, Empresa empresa,
			Map<String, TipoCompetencia> mapTipoCompetencia, Map<String, Competencia> mapCompetencia,
			Map<String, CompetenciaDetalhamento> mapDetalhamento, Map<String, Cargo> mapCargo) throws IOException {
		associacoesCargoSheet.importAndSaveAsMap(workbook, AssociacoesCargo, empresa, mapTipoCompetencia,
				mapCompetencia, mapDetalhamento, mapCargo);
		log.info("Fim da importação dos dados!");
		workbook.close();
	}

}
