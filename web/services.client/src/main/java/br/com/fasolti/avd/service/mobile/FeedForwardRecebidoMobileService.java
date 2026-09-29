package br.com.fasolti.avd.service.mobile;

import br.com.fasolti.avd.service.DeziService;
import br.com.fasolti.avd.transfer.mobile.FeedForwardRecebidoMobileTO;
import br.com.fasolti.avd.transfer.mobile.TipoCompetenciaMobileTO;

import java.util.List;

public interface FeedForwardRecebidoMobileService extends DeziService<FeedForwardRecebidoMobileTO> {

	List<TipoCompetenciaMobileTO> listTipoCompetenciaByLoggedFuncionario();
	List<FeedForwardRecebidoMobileTO> listByTipoCompetenciaIdAndLoggedFuncionario(Long tipoCompetenciaId);
}
