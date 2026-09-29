package br.com.fasolti.dezi.dfi.sheet.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.domain.TipoCompetencia;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.dezi.dfi.commons.UtilsRow;
import br.com.fasolti.dezi.dfi.repositories.TipoCompetenciaRepository;
import br.com.fasolti.dezi.dfi.sheet.Sheet;

@Component
public class TipoCompetenciaSheet extends Sheet {

	private static final Logger log = LoggerFactory.getLogger(TipoCompetenciaSheet.class);

	@Autowired
	private TipoCompetenciaRepository tipoCompetenciaRepository;

	public Map<String, TipoCompetencia> importAndSaveAsMap(Workbook workbook, String sheetName, Empresa empresa) {
		Map<String, TipoCompetencia> map = new HashMap<>();
		log.info("Importando {} ...", sheetName);
		workbook.getSheet(sheetName).forEach(row -> {
			if (row.getRowNum() > 0) {
				TipoCompetencia tipoCompetencia = new TipoCompetencia();
				tipoCompetencia.setNome(UtilsRow.getCellValue(0, row, true));
				tipoCompetencia.setNomeCurto(UtilsRow.getCellValue(0, row, true));
				tipoCompetencia.setDescricao(UtilsRow.getCellValue(1, row, false));
				tipoCompetencia.setIdEmpresa(empresa.getId());
				tipoCompetencia.setCreatedBy(createdByName);
				tipoCompetencia.setCreationDate(new Date());
				tipoCompetencia.setSituacao(Situacao.ATIVA);
				if (!UtilsRow.containsRowErro(row)) {
					map.put(tipoCompetencia.getNome(), tipoCompetenciaRepository.save(tipoCompetencia));
					at.incrementAndGet();
				}
			}
		});
		String result = "Total importado: " + at + ", da Aba: " + sheetName;
		UtilsRow.dadosImportados.add(result);
		log.info("Total importado: {}, da Aba: {}", at, sheetName);
		return map;
	}

}
