package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.mobile.AvaliacaoCompetenciaMobileTO;
import br.com.fasolti.avd.transfer.mobile.AvaliacaoMobileTO;
import br.com.fasolti.avd.transfer.mobile.AvaliacaoTipoCompetenciaMobileTO;
import br.com.fasolti.avd.transfer.mobile.ParticipanteMobileTO;

import java.util.List;

public interface AvaliacaoCompetenciaMobileService extends DeziService<AvaliacaoCompetenciaMobileTO> {

	List<AvaliacaoCompetenciaMobileTO> listByTipoCompetenciaId(Long tipoCompetenciaId);

}
