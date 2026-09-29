package br.com.fasolti.dezi.dfi.sheet.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.dezi.dfi.repositories.CargoRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.dezi.dfi.commons.UtilsRow;
import br.com.fasolti.dezi.dfi.repositories.CargoCompetenciaRepository;
import br.com.fasolti.dezi.dfi.repositories.CargoTipoCompetenciaRepository;
import br.com.fasolti.dezi.dfi.repositories.DetalhamentoCargoCompetenciaRepository;
import br.com.fasolti.dezi.dfi.sheet.Sheet;

@Component
public class AssociacoesCargoSheet extends Sheet {

	private static final Logger log = LoggerFactory.getLogger(AssociacoesCargoSheet.class);

	private static final QCargo qCargo = QCargo.cargo;

	@Autowired
	private CargoTipoCompetenciaRepository cargoTipoCompetenciaRepository;

	@Autowired
	private CargoCompetenciaRepository cargoCompetenciaRepository;

	@Autowired
	private DetalhamentoCargoCompetenciaRepository detalhamentoCargoCompetenciaRepository;

	@Autowired
	private CargoRepository cargoRepository;

	private AtomicInteger ccOrdem = new AtomicInteger(0);
	private AtomicInteger ctcOrdem = new AtomicInteger(0);
	private AtomicInteger ccCount = new AtomicInteger(0);
	private AtomicInteger ctcCount = new AtomicInteger(0);
	private AtomicInteger dccCount = new AtomicInteger(0);

	private Cargo cargo;
	private TipoCompetencia tipoCompetencia;
	private Competencia competencia;
	private CompetenciaDetalhamento competenciaDetalhamento;

	private CargoTipoCompetencia cargoTipoCompetencia;
	private CargoCompetencia cargoCompetencia;
	private DetalhamentoCargoCompetencia detalhamentoCargoCompetencia;

	private String cargoID = "";
	private String cargoAuxID;

	private List<String> listCTCIds = new ArrayList<>();
	private List<String> listCCIds = new ArrayList<>();
	private List<String> listDCCIds = new ArrayList<>();

	public void importAndSaveAsMap(Workbook workbook, String sheetName, Empresa empresa,
			Map<String, TipoCompetencia> mapTipoCompetencia, Map<String, Competencia> mapCompetencia,
			Map<String, CompetenciaDetalhamento> mapDetalhamento, Map<String, Cargo> mapCargo) {
		log.info("Importando {} ...", sheetName);
		workbook.getSheet(sheetName).forEach(row -> {
			String cell0 = Optional.ofNullable(UtilsRow.getCellValue(0, row, true)).orElseGet(() -> {
				UtilsRow.addRowErro(0, row, "Verifique se Cargo existe na Planilha de importação da mesma.");
				return null;
			});

			String cell3 = UtilsRow.getCellValue(3, row, true);
			String cell5 = UtilsRow.getCellValue(5, row, true);
			String cell7 = UtilsRow.getCellValue(7, row, true);

			if (row.getRowNum() > 1 && validNaoAplicado(cell3, cell5, cell7)) {
				detalhamentoCargoCompetencia = new DetalhamentoCargoCompetencia();
				String departamento = UtilsRow.getCellValue(1, row, true);
				mapCargo.forEach((cargoKey, cargoValue) -> {
					if (cargoKey.equalsIgnoreCase(cell0)
							&& cargoValue.getDepartamento().getNome().equalsIgnoreCase(departamento)) {
						cargo = cargoValue;
					}
				});
				if (cargo == null) {
					cargo = cargoRepository.findOne(
							qCargo.nome.equalsIgnoreCase(cell0)
								.and(qCargo.departamento.nome.equalsIgnoreCase(departamento))
								.and(qCargo.empresa.id.eq(empresa.getId())
								.and(qCargo.situacao.eq(Situacao.ATIVA)))
						).orElse(null);
				}
				if (cargo != null) {
					cargoID = cargo.getId().toString();
					if (cargoAuxID == null) {
						cargoAuxID = cargoID;
					}
				} else {
					UtilsRow.addRowErro(3, row, String.format("Cargo \"%s\" não existe ou não foi previamente importado!", cell0));
					return;
				}
				tipoCompetencia = Optional.ofNullable(mapTipoCompetencia.get(cell3)).orElseGet(() -> {
					UtilsRow.addRowErro(3, row,
							"Verifique se Tipo Competencia existe na Planilha de importação da mesma.");
					return null;
				});
				String pesoTipoCompetencia = UtilsRow.getCellValue(4, row, true);

				competencia = Optional.ofNullable(mapCompetencia.get(cell5)).orElseGet(() -> {
					UtilsRow.addRowErro(5, row, "Verifique se Competencia existe na Planilha de importação da mesma.");
					return null;
				});
				String pesoCompetencia = UtilsRow.getCellValue(6, row, true);

				competenciaDetalhamento = Optional.ofNullable(mapDetalhamento.get(cell7)).orElseGet(() -> {
					UtilsRow.addRowErro(7, row, "Verifique se Detalhamento existe na Planilha de importação da mesma.");
					return null;
				});
				String pesoDetalhamento = UtilsRow.getCellValue(8, row, true);
				
				if (!cargoAuxID.equals(cargoID)) {
					ccOrdem.set(0);
					ctcOrdem.set(0);
					cargoAuxID = cargoID;
				}
				if (!UtilsRow.containsRowErro(row)) {
					String ctcID = cargoID + "-" + tipoCompetencia.getId().toString();
					if (listCTCIds.stream().filter(x -> x.equals(ctcID)).collect(Collectors.toList()).isEmpty()) {
						listCTCIds.add(ctcID);
						cargoTipoCompetencia = new CargoTipoCompetencia();
						cargoTipoCompetencia.setCreatedBy(createdByName);
						cargoTipoCompetencia.setCreationDate(new Date());
						cargoTipoCompetencia.setSituacao(Situacao.ATIVA);
						cargoTipoCompetencia.setOrdem(ctcOrdem.incrementAndGet());
						cargoTipoCompetencia.setCargo(cargo);
						cargoTipoCompetencia.setTipoCompetencia(tipoCompetencia);
						cargoTipoCompetencia.setPeso(UtilsRow.converterDoubleCell(pesoTipoCompetencia));
						cargoTipoCompetenciaRepository.save(cargoTipoCompetencia);
						ctcCount.incrementAndGet();
					}
				}
				if (!UtilsRow.containsRowErro(row)) {
					String ccID = cargoID + "-" + competencia.getId().toString();
					if (listCCIds.stream().filter(x -> x.equals(ccID)).collect(Collectors.toList()).isEmpty()) {
						listCCIds.add(ccID);
						cargoCompetencia = new CargoCompetencia();
						cargoCompetencia.setCreatedBy(createdByName);
						cargoCompetencia.setCreationDate(new Date());
						cargoCompetencia.setSituacao(Situacao.ATIVA);
						cargoCompetencia.setOrdem(ccOrdem.incrementAndGet());
						cargoCompetencia.setCargo(cargo);
						cargoCompetencia.setCompetencia(competencia);
						cargoCompetencia.setPeso(UtilsRow.converterDoubleCell(pesoCompetencia));
						cargoCompetencia = cargoCompetenciaRepository.save(cargoCompetencia);
						ccCount.incrementAndGet();
					}
				}
				if (!UtilsRow.containsRowErro(row)) {
					String ccCdID = cargoCompetencia.getId().toString() + "-" + competenciaDetalhamento.getId().toString();
					if (listDCCIds.stream().filter(x -> x.equals(ccCdID)).collect(Collectors.toList()).isEmpty()) {
						listDCCIds.add(ccCdID);
						detalhamentoCargoCompetencia.setCompetenciaDetalhamento(competenciaDetalhamento);
						detalhamentoCargoCompetencia.setCargoCompetencia(cargoCompetencia);
						detalhamentoCargoCompetencia.setCreatedBy(createdByName);
						detalhamentoCargoCompetencia.setCreationDate(new Date());
						detalhamentoCargoCompetencia.setAplicavelAoCargo(true);
						detalhamentoCargoCompetencia.setPeso(UtilsRow.converterDoubleCell(pesoDetalhamento));
						detalhamentoCargoCompetenciaRepository.save(detalhamentoCargoCompetencia);
						dccCount.incrementAndGet();
					}
				}

				if (!UtilsRow.containsRowErro(row)) {
					at.incrementAndGet();
				}

			}
		});
		log.info("Total cargo TipoCompetencia: {}, da Aba: {}", ctcCount, sheetName);
		log.info("Total cargo Competencia: {}, da Aba: {}", ccCount, sheetName);
		log.info("Total detalhamento Cargo Competencia: {}, da Aba: {}", dccCount, sheetName);
		String result = "Total importado: " + at + ", da Aba: " + sheetName;
		UtilsRow.dadosImportados.add(result);
		log.info("Total de registros lido: {}, da Aba: {}", at, sheetName);
	}

	private Boolean validNaoAplicado(String... cells) {
		for (int i = 0; i < cells.length; i++) {
			if (cells[i] == null || cells[i].equalsIgnoreCase("Não se aplica")) {
				return false;
			}
		}
		return true;
	}
}
