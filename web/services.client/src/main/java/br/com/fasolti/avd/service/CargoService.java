package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.simple.CargoSimpleTO;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;

import java.util.List;
import java.util.Map;

public interface CargoService extends DeziService<CargoTO> {

	List<CargoTO> findAllCargosByEmpresaAndAtivos(EmpresaTO empresa);
	List<CargoTO> findAllCargosByUserEmpresaAndAtivos();

	CargoTO findByEmpresaAndIdCargoClienteIgnoreCase(Long idEmpresa, String idCargoCliente);

	List<CargoTO> findByEmpresaIdAndNomeIgnoreCase(Long empresaId, String nome);

    List<FuncionarioTO> obterFuncionariosAtivosDoCargo(Long idCargo);

	List<CargoTreeTO> listarArvoreDeHierarquiaDeCargos(Long idCargo);

	List<CargoTO> obterCargosFilhos(Long idCargo);

	CargoTO copiarCargo(Long idCargo);

	List<CargoTO> obterCargosDescendentes(CargoTO cargo);

	List<PendenciaTO> validarParaLiberacao(CargoSimpleTO cargo, List<AvaliacaoTipoCompetenciaTO> idsTiposCompetencia, Map<Long, List<DetalhamentoCargoCompetenciaTO>> detalhamentosPorCargo, AvaliacaoTO avaliacao);

	Map<TipoAvaliacaoPerfil, List<DetalhamentoCargoCompetenciaTO>> agruparPerguntasPorPerfilAgente(List<DetalhamentoCargoCompetenciaTO> listaDetalhamentos, CargoSimpleTO cargo, AvaliacaoTO avaliacao, List<AvaliacaoTipoCompetenciaTO> listaTiposDoAvaliadores);
}
