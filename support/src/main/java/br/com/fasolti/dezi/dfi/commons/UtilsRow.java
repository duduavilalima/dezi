package br.com.fasolti.dezi.dfi.commons;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.avd.types.TipoSexo;

public final class UtilsRow {

	private UtilsRow() {
	}

	private final static String NAO_E_DATA = "A célula não é uma data. ";
	private final static String CELL_INVALID = "As célula com conteúdo diferente de STRING, DATE e/ou NUMBER. ";
	private final static String CELL_NULL = "As célula não pode conter valor em BRANCO ou NULO. ";
	private final static String CELL_FORM = "A planilha excel não pode conter formula. ";

	public static Map<String, String> erros = new HashMap<>();
	public static List<String> dadosImportados = new ArrayList<>();

	@SuppressWarnings("deprecation")
	public static Boolean RequerPreenchimentoFormacaoIdioma(final String aba, final int coluna, final Cell cell) {
		if (cell.getCellTypeEnum().toString().equals("BOOLEAN")) {
			return cell.getBooleanCellValue();
		} else if ("sim".equalsIgnoreCase(cell.getRichStringCellValue().getString())) {
			return true;
		} else if ("não".equalsIgnoreCase(cell.getRichStringCellValue().getString())) {
			return false;
		} else if ("nao".equalsIgnoreCase(cell.getRichStringCellValue().getString())) {
			return false;
		} else if ("".equalsIgnoreCase(cell.getRichStringCellValue().getString())) {
			return false;
		} else {
			addRowErro(coluna, cell.getRow(), "Valor diferente de vazio,sim,não e/ou nao.");
			return null;
		}
	}

	public static String getCellValue(int coluna, Row row, Boolean tratarNulo) {
		String v = UtilsRow.cellValidate(coluna, row.getCell(coluna, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK));
		if (v == null && tratarNulo) {
			addRowErro(coluna, row);
			return null;
		}
		return v == null ? null : v.trim();

	}

	public static TipoSexo getCellValueGenero(int coluna, Row row) {
		String genero = getCellValue(coluna, row, true);
		if (genero.equalsIgnoreCase("f")) {
			return TipoSexo.FEMININO;
		} else if (genero.equalsIgnoreCase("m")) {
			return TipoSexo.MASCULINO;
		} else {
			addRowErro(coluna, row, "Valor deve ser \"M\" (masculino) ou \"F\" (feminino).");
			return null;
		}
	}

	public static Optional<String> getCellNullable(int coluna, Row row) {
		return Optional.ofNullable(
				UtilsRow.cellValidate(coluna, row.getCell(coluna, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)));
	}

	public static int converterDoubleCell(String cell) {
		double d = Double.parseDouble(cell);
		return (int) d;
	}

	private static String tituliErro(Row row) {
		return row.getSheet().getSheetName() + "-" + row.getRowNum();
	}

	public static String erroMsgCustomized(int coluna, Row row) {
		return erroMsg(coluna, row, "");
	}

	public static String erroMsgCustomized(int coluna, Row row, String optMsg) {
		return erroMsg(coluna, row, optMsg);
	}

	private static String erroMsg(int coluna, Row row, String optMsg) {
		return String.format(
				"Erro encontrado na Aba %1$s, linha %3$d, Coluna %2$d, valor da célula: %4$s. " + optMsg + "\n",
				row.getSheet().getSheetName(), coluna + 1, row.getRowNum() + 1,
				row.getCell(coluna) == null ? CELL_NULL : row.getCell(coluna));
	}

	public static boolean containsRowErro(Row row) {
		return erros.containsKey(tituliErro(row));
	}

	public static void addRowErro(int coluna, Row row, String msg) {
		erros.put(tituliErro(row), UtilsRow.erroMsgCustomized(coluna, row, msg));
	}

	public static void addRowErro(int coluna, Row row) {
		erros.put(tituliErro(row), UtilsRow.erroMsgCustomized(coluna, row));
	}

	public static String tipoPerfilAcentoValid(final int coluna, final Cell cell) {
		Optional<String> tipoPerfil = Optional.ofNullable(cellValidate(coluna, cell));
		if (!tipoPerfil.isPresent()) {
			Row row = cell.getRow();
			addRowErro(10, row);
			return null;
		} else if (tipoPerfil.get().equalsIgnoreCase("Funcionário")) {
			return TipoPerfil.FUNCIONARIO.getNome();
		} else if (tipoPerfil.get().equalsIgnoreCase("Gestor de Area")) {
			return TipoPerfil.GESTOR_AREA.getNome();
		} else {
			return tipoPerfil.get();
		}
	}
	@SuppressWarnings("deprecation")
	public static String cellValidate(final int coluna, final Cell cell) {
		Row row = cell.getRow();
		switch (cell.getCellTypeEnum().toString()) {
		case "STRING":
			return cell.getRichStringCellValue().getString();
		case "NUMERIC":
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue().toString();
			} else {
				return String.valueOf(cell.getNumericCellValue());
			}
		case "BOOLEAN":
			return String.valueOf(cell.getBooleanCellValue());
		case "FORMULA":
			addRowErro(coluna, row, CELL_FORM);
			return null;
		case "BLANK":
			return null;
		default:
			addRowErro(coluna, row, CELL_INVALID);
			return null;
		}
	}
	
	@SuppressWarnings("deprecation")
	// FIXME: Retorna date, refatorar codigo (esta repetido)
	public static Date cellDateValid(final int coluna, final Cell cell) {
		Row row = cell.getRow();
		switch (cell.getCellTypeEnum().toString()) {
		case "STRING":
			return null;
		case "NUMERIC":
			if (DateUtil.isCellDateFormatted(cell)) {
				return cell.getDateCellValue();
			} else {
				addRowErro(coluna, row, NAO_E_DATA);
				return null;
			}
		case "BOOLEAN":
			addRowErro(coluna, row, NAO_E_DATA);
			return null;
		case "FORMULA":
			addRowErro(coluna, row, NAO_E_DATA);
			return null;
		case "BLANK":
			return null;
		default:
			addRowErro(coluna, row, CELL_INVALID);
			return null;
		}
	}

	public static Date cellReturnNewDate(final int coluna, final Cell cell) {
		Date date = cellDateValid(coluna, cell);
		if (date == null) {
			return new Date();
		} else {
			return date;
		}
	}

}
