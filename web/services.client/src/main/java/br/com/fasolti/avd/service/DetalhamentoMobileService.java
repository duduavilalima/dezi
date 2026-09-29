package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.mobile.CompetenciaMobileTO;
import br.com.fasolti.avd.transfer.mobile.DetalhamentoMobileTO;

import java.util.List;

public interface DetalhamentoMobileService extends DeziService<DetalhamentoMobileTO> {

	void updateDetalhamentosMobile(List<DetalhamentoMobileTO> detalhamentoMobileTOList);

}
