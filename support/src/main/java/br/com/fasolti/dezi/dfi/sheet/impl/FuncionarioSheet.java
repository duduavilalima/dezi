package br.com.fasolti.dezi.dfi.sheet.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fasolti.avd.domain.Cargo;
import br.com.fasolti.avd.domain.Configuracao;
import br.com.fasolti.avd.domain.Departamento;
import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.domain.Unidade;
import br.com.fasolti.avd.domain.UnidadeDepartamento;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.SituacaoFuncionario;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.dezi.dfi.commons.EmailValidator;
import br.com.fasolti.dezi.dfi.commons.UtilsRow;
import br.com.fasolti.dezi.dfi.repositories.ConfiguracaoRepository;
import br.com.fasolti.dezi.dfi.repositories.FuncionarioRepository;
import br.com.fasolti.dezi.dfi.repositories.UnidadeDepartamentoRepository;
import br.com.fasolti.dezi.dfi.sheet.Sheet;

@Component
public class FuncionarioSheet extends Sheet {

	private static final Logger log = LoggerFactory.getLogger(FuncionarioSheet.class);

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private UnidadeDepartamentoRepository uniDepRepository;

	@Autowired
	private ConfiguracaoRepository configuracaoRepository;

	private List<Departamento> departGestorList = new ArrayList<>();
	private Funcionario func;
	private Configuracao configuracao;
	private UnidadeDepartamento ud;

	public void importAndSave(Workbook workbook, String sheetName, Empresa empresa, Map<String, Cargo> mapCargo,
			Map<String, Departamento> mapDepartamento, Map<String, Unidade> mapUnidade) {
		log.info("Importando {} ...", sheetName);
		org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheet(sheetName);
		if (sheet != null) {
			sheet.forEach(row -> {
				if (row.getRowNum() > 0) {
					func = new Funcionario();
					configuracao = new Configuracao();
					func.setMatricula(UtilsRow.getCellValue(0, row, true)); // 1
					func.setNome(UtilsRow.getCellValue(1, row, true));// 2
					String email = UtilsRow.getCellValue(2, row, true);
					if (EmailValidator.isValid(email)) {
						func.setEmailFuncional(email);// 3
					} else {
						UtilsRow.addRowErro(2, row, "Email é inválido.");
					}
					func.setTipoSexo(UtilsRow.getCellValueGenero(3, row));// 4
					func.setNascimento(
							UtilsRow.cellDateValid(4, row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));// 5
					func.setAdmissao(UtilsRow.cellDateValid(5, row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));// 6
					func.setSituacaoFuncional(SituacaoFuncionario.valueOfString(UtilsRow.getCellValue(6, row, true)));// 7
					func.setDataInicioRecesso(
							UtilsRow.cellDateValid(7, row.getCell(7, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));// 8
					func.setDataFimRecesso(
							UtilsRow.cellDateValid(8, row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));// 9
					func.setTipoPerfil(TipoPerfil.valueOfString(
							UtilsRow.tipoPerfilAcentoValid(9, row.getCell(9, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))));// 10
					func.setUnidade(
							Optional.ofNullable(mapUnidade.get(UtilsRow.getCellValue(10, row, true))).orElseGet(() -> {
								UtilsRow.addRowErro(10, row, "Verifique se este Unidade existe em sua aba.");
								return null;
							})); // 11
					func.setDepartamento(
							Optional.ofNullable(mapDepartamento.get(UtilsRow.getCellValue(11, row, true))).orElseGet(() -> {
								UtilsRow.addRowErro(10, row, "Verifique se este Setor existe em sua aba.");
								return null;
							})); // 12
					func.setCargo(Optional.ofNullable(mapCargo.get(UtilsRow.getCellValue(12, row, true))).orElseGet(() -> {
						UtilsRow.addRowErro(10, row, "Verifique se este Cargo existe em sua aba.");
						return null;
					})); // 13
					func.setDataCargo(
							UtilsRow.cellReturnNewDate(13, row.getCell(13, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK))); // 14
					func.setPreencheFormacaoEIdiomas(UtilsRow.RequerPreenchimentoFormacaoIdioma(sheetName, 15,
							row.getCell(15, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));
					func.setSenhaCriptografada("$2a$10$waSi97YiKZfuStP571XDOuJw8jRxFMNcDuGz9uimgMGP6qAbpmHYW");
					func.setEmpresa(empresa);
					func.setCreatedBy(createdByName);
					func.setCreationDate(new Date());
					func.setSituacao(Situacao.ATIVA);
					if (!UtilsRow.containsRowErro(row)) { // FIXME: Refatorar
						if (UtilsRow.getCellNullable(14, row).isPresent()
								&& UtilsRow.getCellNullable(14, row).get().equalsIgnoreCase("sim")) {
							ud = new UnidadeDepartamento();
							func.setPontoFocalRh(Boolean.TRUE);
							if (departGestorList.isEmpty() || !departGestorList.contains(func.getDepartamento())) {
								func = funcionarioRepository.save(func);
								ud.setGestor(func);
								ud.setDepartamento(func.getDepartamento());
								ud.setUnidade(func.getUnidade());
								uniDepRepository.save(ud);
								configuracao.setFuncionario(func);
								configuracaoRepository.save(configuracao);
								departGestorList.add(func.getDepartamento());
							} else {
								UtilsRow.addRowErro(14, row, "Já existe gestor para este departamento: Departamento "
										+ func.getDepartamento().getNome() + ", " + func.getNome());
							}
						} else if (!UtilsRow.containsRowErro(row)) {
							func.setPontoFocalRh(Boolean.FALSE);
							func = funcionarioRepository.save(func);
							configuracao.setFuncionario(func);
							configuracaoRepository.save(configuracao);
						}

						at.incrementAndGet();
					}
				}
			});
		}
		String result = "Total importado: " + at + ", da Aba: " + sheetName;
		UtilsRow.dadosImportados.add(result);
		log.info("Total importado: {}, da Aba: {}", at, sheetName);
	}

}
