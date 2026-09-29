package br.com.fasolti.dezi.dfi.sheet.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fasolti.avd.domain.CompDetalhamentoHint;
import br.com.fasolti.avd.domain.Competencia;
import br.com.fasolti.avd.domain.CompetenciaDetalhamento;
import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.domain.Graduacao;
import br.com.fasolti.avd.domain.ModeloGraduacao;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.dezi.dfi.commons.UtilsRow;
import br.com.fasolti.dezi.dfi.repositories.CompDetalhamentoHintRepository;
import br.com.fasolti.dezi.dfi.repositories.CompetenciaDetalhamentoRepository;
import br.com.fasolti.dezi.dfi.sheet.Sheet;

@Component
public class DetalhamentoSheet extends Sheet {

	private static final Logger log = LoggerFactory.getLogger(DetalhamentoSheet.class);

	@Autowired
	private CompetenciaDetalhamentoRepository competenciaDetalhamentoRepository;
	
	@Autowired
	private CompDetalhamentoHintRepository  cdhr;

	private CompetenciaDetalhamento competenciaDetalhamento;
	private CompDetalhamentoHint compDetalhamentoHint;
	private Integer ordemDet = 0;
	private Long idCompAux;

	public Map<String, CompetenciaDetalhamento> importAndSaveAsMap(Workbook workbook, String sheetName, Empresa empresa,
			Map<String, Competencia> mapC, Map<String, ModeloGraduacao> mapMG, Map<Long, Graduacao> mapG) {
		Map<String, CompetenciaDetalhamento> map = new HashMap<>();
		log.info("Importando {} ...", sheetName);
		workbook.getSheet(sheetName).forEach(row -> {
			if (row.getRowNum() > 0) {
				competenciaDetalhamento = new CompetenciaDetalhamento();
				competenciaDetalhamento.setCompetencia(
						Optional.ofNullable(mapC.get(UtilsRow.getCellValue(0, row, true))).orElseGet(() -> {
							UtilsRow.addRowErro(0, row, "Verifique se Competências existe em sua aba.");
							return null;
						}));
				competenciaDetalhamento.setModeloGraduacao(
						Optional.ofNullable(mapMG.get(UtilsRow.getCellValue(2, row, true))).orElseGet(() -> {
							UtilsRow.addRowErro(2, row, "Verifique se Escala graduação existe em sua aba.");
							return null;
						}));
				competenciaDetalhamento.setDescricao(UtilsRow.getCellValue(1, row, false));
				addOrdem();
				competenciaDetalhamento.setCreatedBy(createdByName);
				competenciaDetalhamento.setCreationDate(new Date());
				competenciaDetalhamento.setSituacao(Situacao.ATIVA);
				if (!UtilsRow.containsRowErro(row)) {
					competenciaDetalhamento = competenciaDetalhamentoRepository.save(competenciaDetalhamento);
					map.put(competenciaDetalhamento.getDescricao(),competenciaDetalhamento);
					mapG.forEach((k,v) -> {
						compDetalhamentoHint = new CompDetalhamentoHint();
						if (v.getModeloGraduacao().equals(competenciaDetalhamento.getModeloGraduacao())) {
							compDetalhamentoHint.setGraduacao(v);
							compDetalhamentoHint.setComentarioObrigatorio(UtilsRow.RequerPreenchimentoFormacaoIdioma(sheetName, 3, row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));
							compDetalhamentoHint.setCompetenciaDetalhamento(competenciaDetalhamento);
							compDetalhamentoHint.setHint(v.getHint());
							cdhr.save(compDetalhamentoHint);
						}
					});
					at.incrementAndGet();
				}
			}
		});
		String result = "Total importado: " + at + ", da Aba: " + sheetName;
		UtilsRow.dadosImportados.add(result);
		log.info("Total importado: {}, da Aba: {}", at, sheetName);
		return map;
	}
	
	private void addOrdem() {
		if (this.ordemDet == 0 || !competenciaDetalhamento.getCompetencia().getId().equals(this.idCompAux)) {
			this.idCompAux = competenciaDetalhamento.getCompetencia().getId();
			this.ordemDet = 1;
			competenciaDetalhamento.setOrdem(this.ordemDet);
		}else {
			competenciaDetalhamento.setOrdem(++this.ordemDet);
		}
	}

}
