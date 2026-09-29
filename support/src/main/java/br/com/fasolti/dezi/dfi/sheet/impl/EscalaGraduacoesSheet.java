package br.com.fasolti.dezi.dfi.sheet.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.fasolti.avd.domain.Dimensao;
import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.domain.ModeloGraduacao;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.dezi.dfi.commons.UtilsRow;
import br.com.fasolti.dezi.dfi.repositories.DimensaoRepository;
import br.com.fasolti.dezi.dfi.repositories.ModeloGraduacaoRepository;
import br.com.fasolti.dezi.dfi.sheet.Sheet;

@Component
public class EscalaGraduacoesSheet extends Sheet {

	private static final Logger log = LoggerFactory.getLogger(EscalaGraduacoesSheet.class);

	@Value("${default.dimensao.id}")
	private Long defaultDimensaoId;

	@Autowired
	private ModeloGraduacaoRepository modeloGraduacaoRepository;

	@Autowired
	private DimensaoRepository dimensaoRepository;

	public Map<String, ModeloGraduacao> importAndSaveAsMap(Workbook workbook, String sheetName, Empresa empresa) {
		Optional<Dimensao> dimensao = dimensaoRepository.findById(defaultDimensaoId);
		if (!dimensao.isPresent()) {
			UtilsRow.addRowErro(0, workbook.getSheet(sheetName).getRow(0), "Dimensão Não encontrada.");
		}
		Map<String, ModeloGraduacao> map = new HashMap<>();
		log.info("Importando {} ...", sheetName);
		workbook.getSheet(sheetName).forEach(row -> {
			if (row.getRowNum() > 0) {
				ModeloGraduacao modeloGraduacao = new ModeloGraduacao();
				modeloGraduacao.setDescricaoModelo(UtilsRow.getCellValue(0, row, true));
				modeloGraduacao.setDimensao(dimensao.get());
				modeloGraduacao.setEmpresa(empresa);
				modeloGraduacao.setCreatedBy(createdByName);
				modeloGraduacao.setCreationDate(new Date());
				modeloGraduacao.setSituacao(Situacao.ATIVA);
				modeloGraduacao.setNaoGraduar(true);
				if (!UtilsRow.containsRowErro(row)) {
					map.put(modeloGraduacao.getDescricaoModelo(), modeloGraduacaoRepository.save(modeloGraduacao));
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
