package br.com.fasolti.dezi.dfi.spreadSheet.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.fasolti.avd.domain.Cargo;
import br.com.fasolti.avd.domain.Departamento;
import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.domain.Unidade;
import br.com.fasolti.dezi.dfi.sheet.impl.CargoSheet;
import br.com.fasolti.dezi.dfi.sheet.impl.DepartamentoSheet;
import br.com.fasolti.dezi.dfi.sheet.impl.FuncionarioSheet;
import br.com.fasolti.dezi.dfi.sheet.impl.UnidadeSheet;

@Component
public class CadastroFuncionario extends br.com.fasolti.dezi.dfi.spreadSheet.SpreadSheet {

	private static final Logger log = LoggerFactory.getLogger(CadastroFuncionario.class);

//	private final String unidades = "1. Unidades";
//	private final String setores = "2. Setores";
//	private final String cargos = "3. Cargos";
//	private final String funcionarios = "4. Funcionários";

	private final String unidades = "Unidades";
	private final String setores = "Setores";
	private final String cargos = "Cargos";
	private final String funcionarios = "Funcionarios";

	@Autowired
	private UnidadeSheet unidadeSheet;

	@Autowired
	private DepartamentoSheet departamentoSheet;

	@Autowired
	private CargoSheet cargoSheet;

	@Autowired
	private FuncionarioSheet funcionarioSheet;

	private Map<String, Departamento> mapDepartamento = new HashMap<>();
	private Map<String, Cargo> mapCargo = new HashMap<>();
	private Map<String, Unidade> mapUnidade = new HashMap<>();

	@Override
	public void importaDadosPlanilha(Workbook workbook, Empresa empresa) throws IOException {
		mapUnidade = unidadeSheet.importAndSaveAsMap(workbook, unidades, empresa);
		mapDepartamento = departamentoSheet.importAndSaveAsMap(workbook, setores, empresa);
		mapCargo = cargoSheet.importAndSaveAsMap(workbook, cargos, empresa, mapDepartamento);
		funcionarioSheet.importAndSave(workbook, funcionarios, empresa, mapCargo, mapDepartamento, mapUnidade);
		log.info("Fim da importação dos dados!");
		workbook.close();
	}

	public Map<String, Departamento> getMapDepartamento() {
		return mapDepartamento;
	}

	public Map<String, Cargo> getMapCargo() {
		return mapCargo;
	}
}
