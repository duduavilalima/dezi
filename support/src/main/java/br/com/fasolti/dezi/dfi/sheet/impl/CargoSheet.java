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

import br.com.fasolti.avd.domain.Cargo;
import br.com.fasolti.avd.domain.Departamento;
import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.dezi.dfi.commons.UtilsRow;
import br.com.fasolti.dezi.dfi.repositories.CargoRepository;
import br.com.fasolti.dezi.dfi.sheet.Sheet;

@Component
public class CargoSheet extends Sheet {

	@Autowired
	private CargoRepository cargoRepository;

	private static final Logger log = LoggerFactory.getLogger(CargoSheet.class);

	public Map<String, Cargo> importAndSaveAsMap(Workbook workbook, String sheetName, Empresa empresa,
			Map<String, Departamento> map) {
		Map<String, Cargo> mapCargo = new HashMap<>();
		log.info("Importando {} ...", sheetName);
		workbook.getSheet(sheetName).forEach(row -> {
			if (row.getRowNum() > 0) {
				Departamento cellDepartamento = Optional.ofNullable(map.get(UtilsRow.getCellValue(1, row, false)))
						.orElseGet(() -> null);
				String nome = UtilsRow.getCellValue(0, row, true);
				Cargo cargo = cargoRepository.findByEmpresaIdAndNome(113L, nome);

				if (cargo == null) {
					cargo = new Cargo();
					cargo.setNome(nome);
					cargo.setDepartamento(cellDepartamento);
					String IdCargoCliente = UtilsRow.getCellValue(2, row, false);
					cargo.setIdCargoCliente(IdCargoCliente == null ? "0" + (row.getRowNum() - 2) : IdCargoCliente);
					cargo.setEmpresa(empresa);
					cargo.setCreatedBy(createdByName);
					cargo.setCreationDate(new Date());
					cargo.setSituacao(Situacao.ATIVA);
				}

				if (!UtilsRow.containsRowErro(row)) {
					mapCargo.put(cargo.getNome(), cargo.getId()==null?cargoRepository.save(cargo):cargo);
					at.incrementAndGet();
				}
			}
		});
		String result = "Total importado: " + at + ", da Aba: " + sheetName;
		UtilsRow.dadosImportados.add(result);
		log.info("Total importado: {}, da Aba: {}", at, sheetName);
		return mapCargo;
	}

}
