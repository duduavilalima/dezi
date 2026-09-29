package br.com.fasolti.avd.service.mobile;

import br.com.fasolti.avd.service.DeziService;
import br.com.fasolti.avd.transfer.mobile.CompetenciaMobileTO;
import br.com.fasolti.avd.transfer.mobile.FeedForwardMobileTO;
import br.com.fasolti.avd.transfer.mobile.FuncionarioMobileTO;
import br.com.fasolti.avd.transfer.mobile.TipoCompetenciaMobileTO;

import java.util.List;

public interface FeedForwardMobileService extends DeziService<FeedForwardMobileTO> {

	List<FuncionarioMobileTO> listFuncionariosAvaliadosByLoggedUser();
	List<TipoCompetenciaMobileTO> listTiposCompetenciaByLoggedUserAndFuncionarioId(Long funcionarioId);
	List<CompetenciaMobileTO> listCompetenciaByTipoCompetenciaId(Long tipoCompetenciaId);
	List<FeedForwardMobileTO> listByLoggedUserAndFuncionarioIdAndTipoCompetenciaId(Long funcionarioId, Long tipoCompetenciaId);

	FeedForwardMobileTO create(FeedForwardMobileTO input);
}
