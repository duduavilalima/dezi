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
import br.com.fasolti.avd.domain.Unidade;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.dezi.dfi.commons.UtilsRow;
import br.com.fasolti.dezi.dfi.repositories.UnidadeRepository;
import br.com.fasolti.dezi.dfi.sheet.Sheet;

@Component
public class UnidadeSheet extends Sheet {

	@Autowired
	private UnidadeRepository unidadeRepository;

	private static final Logger log = LoggerFactory.getLogger(UnidadeSheet.class);

	public Map<String, Unidade> importAndSaveAsMap(Workbook workbook, String sheetName, Empresa empresa) {
		log.info("Importando {} ...", sheetName);
		Map<String, Unidade> map = new HashMap<>();
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet(sheetName);
		sheet.forEach(row -> { // TODO: Verificar se tem PAI?
			Unidade unidade;
			if (row.getRowNum() > 0) {
				unidade = new Unidade();
				unidade.setNome(UtilsRow.getCellValue(0, row, true));
				unidade.setDescricao(UtilsRow.getCellValue(1, row, false));
				unidade.setEmpresa(empresa);
				unidade.setCreatedBy(createdByName);
				unidade.setCreationDate(new Date());
				unidade.setSituacao(Situacao.ATIVA);
				if (UtilsRow.containsRowErro(row)) {
					throw new IllegalArgumentException(
							UtilsRow.erroMsgCustomized(0, row, "Unidade dever ser cadastrado !"));
				} else {
					map.put(unidade.getNome(), unidadeRepository.save(unidade));
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
