package br.com.fasolti.dezi.dfi.sheet.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fasolti.avd.domain.Competencia;
import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.domain.TipoCompetencia;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.dezi.dfi.commons.UtilsRow;
import br.com.fasolti.dezi.dfi.repositories.CompetenciaRepository;
import br.com.fasolti.dezi.dfi.sheet.Sheet;

@Component
public class CompetenciaSheet extends Sheet {

	private static final Logger log = LoggerFactory.getLogger(CompetenciaSheet.class);

	@Autowired
	private CompetenciaRepository competenciaRepository;

	public Map<String, Competencia> importAndSaveAsMap(Workbook workbook, String sheetName, Empresa empresa,
			Map<String, TipoCompetencia> mapTC) {
		Map<String, Competencia> map = new HashMap<>();
		log.info("Importando {} ...", sheetName);
		workbook.getSheet(sheetName).forEach(row -> {
			if (row.getRowNum() > 0) {
				Competencia competencia = new Competencia();
				competencia.setDescricaoAbreviada(UtilsRow.getCellValue(1, row, true));
				competencia.setDirecionamentoGestor(UtilsRow.getCellValue(2, row, false));
				competencia.setDirecionamentoNaoGestor(UtilsRow.getCellValue(3, row, false));
				competencia
						.setTipo(Optional.ofNullable(mapTC.get(UtilsRow.getCellValue(0, row, false))).orElseGet(() -> {
							UtilsRow.addRowErro(0, row, "Verifique se Tipo de Competências existe em sua aba.");
							return null;
						}));
				competencia.setEmpresa(empresa);
				competencia.setCreatedBy(createdByName);
				competencia.setCreationDate(new Date());
				competencia.setSituacao(Situacao.ATIVA);
				if (!UtilsRow.containsRowErro(row)) {
					map.put(competencia.getDescricaoAbreviada(), competenciaRepository.save(competencia));
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
