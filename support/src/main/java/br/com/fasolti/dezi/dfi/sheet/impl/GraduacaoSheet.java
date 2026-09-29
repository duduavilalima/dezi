package br.com.fasolti.dezi.dfi.sheet.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.core.BooleanBuilder;

import br.com.fasolti.avd.domain.Graduacao;
import br.com.fasolti.avd.domain.ModeloGraduacao;
import br.com.fasolti.avd.domain.QGraduacao;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.dezi.dfi.commons.UtilsRow;
import br.com.fasolti.dezi.dfi.repositories.GraduacaoRepository;
import br.com.fasolti.dezi.dfi.repositories.ModeloGraduacaoRepository;
import br.com.fasolti.dezi.dfi.sheet.Sheet;

@Component
public class GraduacaoSheet extends Sheet {

	private static final Logger log = LoggerFactory.getLogger(GraduacaoSheet.class);

	@Autowired
	private GraduacaoRepository graduacaoRepository;

	@Autowired
	private ModeloGraduacaoRepository modeloGraduacaoRepository;

	private Map<Long, Graduacao> map = new HashMap<>();
	private Set<ModeloGraduacao> modeloGraduacaoList = new HashSet<>();
	private Graduacao graduacao;
	private ModeloGraduacao modeloGraduacao;
	private Long auxIdGrad;
	private Integer ordem = 0;

	public Map<Long, Graduacao> importAndSaveAsMap(Workbook workbook, String sheetName,
			Map<String, ModeloGraduacao> mapMG) {
		log.info("Importando {} ...", sheetName);
		workbook.getSheet(sheetName).forEach(row -> {
			if (row.getRowNum() > 0) {
				graduacao = new Graduacao();
				modeloGraduacao = Optional.ofNullable(mapMG.get(UtilsRow.getCellValue(0, row, true)))
						.orElseGet(() -> {
							UtilsRow.addRowErro(0, row, "Verifique se Escala graduação existe em sua aba.");
							return null;
						});
				
				graduacao.setModeloGraduacao(modeloGraduacao);
				modeloGraduacaoList.add(modeloGraduacao);
				graduacao.setLabel(UtilsRow.getCellValue(1, row, true));
				graduacao.setPonto(UtilsRow.converterDoubleCell(UtilsRow.getCellValue(2, row, true)));
				graduacao.setHint(UtilsRow.getCellValue(3, row, false));
				addOrdemECorDaGraduacao();
				graduacao.setCreatedBy(createdByName);
				graduacao.setCreationDate(new Date());
				graduacao.setSituacao(Situacao.ATIVA);
				if (!UtilsRow.containsRowErro(row)) {
					graduacao = graduacaoRepository.save(graduacao);
					at.incrementAndGet();
					map.put(graduacao.getId(), graduacao);
				}
			}
		});
		modeloGraduacaoList.forEach(mg -> {
			BooleanBuilder bb = new BooleanBuilder();
			int quantidadeGraduacao = (int) graduacaoRepository
					.count(bb.and(QGraduacao.graduacao.modeloGraduacao.id.eq(mg.getId())));
			mg.setQtdGraduacoes(quantidadeGraduacao);
			modeloGraduacaoRepository.save(mg);
		});
		String result = "Total importado: " + at + ", da Aba: " + sheetName;
		UtilsRow.dadosImportados.add(result);
		log.info("Total importado: {}, da Aba: {}", at, sheetName);
		return map;
	}
	
	private void addOrdemECorDaGraduacao() {
		if (this.ordem == 0) {
			auxIdGrad = modeloGraduacao.getId();
		}
		if(this.ordem <= 5) {
			if(this.modeloGraduacao.getId().equals(this.auxIdGrad)) {
				this.graduacao.setOrdem(++this.ordem);
				this.graduacao.setCor(getCorPelaOrdem(this.ordem));
			}else {
				this.ordem = 1;
				this.graduacao.setOrdem(this.ordem);
				this.graduacao.setCor(getCorPelaOrdem(this.ordem));
				auxIdGrad = modeloGraduacao.getId();
			}
		}else {
			this.graduacao.setOrdem(++this.ordem);
		}
	}
	
	private String getCorPelaOrdem(Integer ordem) {
		Map<Integer, String> cor = new HashMap<Integer, String>();
		cor.put(1, "#f44336");
		cor.put(2, "#782b90");
		cor.put(3, "#fdb913");
		cor.put(4, "#4aa741");
		cor.put(5, "#1c9ad6");
		return cor.get(ordem);
	}
}
