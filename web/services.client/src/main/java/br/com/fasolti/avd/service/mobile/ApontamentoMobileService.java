package br.com.fasolti.avd.service.mobile;

import br.com.fasolti.avd.service.DeziService;
import br.com.fasolti.avd.transfer.mobile.*;

import java.util.List;

public interface ApontamentoMobileService extends DeziService<ApontamentoMobileTO> {

	List<FuncionarioMobileTO> listFuncionariosAvaliadosByLoggedUser();
	List<TipoCompetenciaMobileTO> listTiposCompetenciaByLoggedUserAndFuncionarioId(Long funcionarioId);
	List<CompetenciaMobileTO> listCompetenciaByTipoCompetenciaId(Long tipoCompetenciaId);
	List<ApontamentoMobileTO> listByLoggedUserAndFuncionarioIdAndTipoCompetenciaId(Long funcionarioId, Long tipoCompetenciaId);

	ApontamentoMobileTO create(ApontamentoMobileTO input);
}
