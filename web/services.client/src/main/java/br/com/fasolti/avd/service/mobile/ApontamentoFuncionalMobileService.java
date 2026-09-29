package br.com.fasolti.avd.service.mobile;

import br.com.fasolti.avd.transfer.mobile.CompetenciaMobileTO;
import br.com.fasolti.avd.transfer.mobile.FuncionarioMobileTO;
import br.com.fasolti.avd.transfer.mobile.TipoCompetenciaMobileTO;

import java.util.List;

/**
 * Interface para o serviço que serve aos propósitos das telas de apontamentos funcionais: Feedforwards e Apontamentos.
 *
 * Este serviço é um detalhe de implementação das telas. Não use-o fora das implementações de entidades derivadas de
 * apontamento funcional!
 */
public interface ApontamentoFuncionalMobileService {

	List<FuncionarioMobileTO> listFuncionariosAvaliadosByLoggedUser();
	List<TipoCompetenciaMobileTO> listTiposCompetenciaByLoggedUserAndFuncionarioId(Long funcionarioId);
	List<CompetenciaMobileTO> listCompetenciaByTipoCompetenciaId(Long tipoCompetenciaId);
}
