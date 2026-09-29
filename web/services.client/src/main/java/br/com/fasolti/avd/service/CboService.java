package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.AtividadeCBOTO;
import br.com.fasolti.avd.transfer.OcupacaoCBOTO;
import br.com.fasolti.avd.transfer.PerfilCBOTO;

import java.util.List;

public interface CboService {
	List<OcupacaoCBOTO> listarTodosCboOcupcoes();

	List<PerfilCBOTO> listarPerfisPorOcupacao(Long ocupacaoId);

	List<AtividadeCBOTO> listarAtividadesPorPerfil(Long perfilId);

    List<OcupacaoCBOTO> autocompleteBuscar(OcupacaoCBOTO to);
}
