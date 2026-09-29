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

import br.com.fasolti.avd.domain.Departamento;
import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.dezi.dfi.commons.UtilsRow;
import br.com.fasolti.dezi.dfi.repositories.DepartamentoRepository;
import br.com.fasolti.dezi.dfi.sheet.Sheet;

@Component
public class DepartamentoSheet extends Sheet {

	private static final Logger log = LoggerFactory.getLogger(DepartamentoSheet.class);

	@Autowired
	private DepartamentoRepository departamentoRepository;

	public Map<String, Departamento> importAndSaveAsMap(Workbook workbook, String sheetName, Empresa empresa) {
		Map<String, Departamento> mapDep = new HashMap<>();
		log.info("Importando {} ...", sheetName);

		workbook.getSheet(sheetName).forEach(row -> {
			Departamento departamento;
			if (row.getRowNum() > 0) {
				String nome = UtilsRow.getCellValue(0, row, true);
				departamento = departamentoRepository.findByEmpresaIdAndNome(113L, nome);

				if (departamento == null) {
					departamento = new Departamento();
					departamento.setNome(nome);
					String IdDepCliente = UtilsRow.getCellValue(1, row, false);
					departamento
							.setIdDepartamentoCliente(IdDepCliente == null ? "0" + (row.getRowNum() - 2) : IdDepCliente);
					departamento.setEmpresa(empresa);
					departamento.setCreatedBy(createdByName);
					departamento.setCreationDate(new Date());
					departamento.setSituacao(Situacao.ATIVA);
				}

				if (!UtilsRow.containsRowErro(row)) {
					mapDep.put(departamento.getNome(), departamento.getId()==null?departamentoRepository.save(departamento):departamento);
					at.incrementAndGet();
				}
			}
		});
		String result = "Total importado: " + at + ", da Aba: " + sheetName;
		UtilsRow.dadosImportados.add(result);
		log.info("Total importado: {}, da Aba: {}", at, sheetName);
		log.info("Obtendo o Pai do departamento !");
		at.set(0);
		workbook.getSheet(sheetName).forEach(row -> {
			Optional<Departamento> departamentoPai = Optional
					.ofNullable(mapDep.get(UtilsRow.getCellValue(2, row, false)));
			if (row.getRowNum() > 1 && departamentoPai.isPresent()) {
				Departamento departamento = new Departamento();
				departamento = Optional.ofNullable(mapDep.get(UtilsRow.getCellValue(0, row, true)))
						.orElseThrow(() -> new IllegalArgumentException(UtilsRow.erroMsgCustomized(0, row)));
				departamento.setDepartamentoPai(departamentoPai.get());
				if (!UtilsRow.containsRowErro(row)) {
					departamentoRepository.save(departamento);
				}
				at.incrementAndGet();
			} else if (row.getRowNum() > 1) {
				String departamentoPaiNome = UtilsRow.getCellValue(2, row, false);
				if (departamentoPaiNome != null) {
					// UtilsRow.addRowErro(2, row, "Departamento Pai Não encontrado.");
				}
			}
		});
		String result2 = "\tTotal Atualizado com departamento pai: " + at;
		UtilsRow.dadosImportados.add(result2);
		log.info("Total Atualizado: {}, da Aba: {}", at, sheetName);
		return mapDep;
	}

}
