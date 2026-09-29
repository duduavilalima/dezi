package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.mobile.FaleConoscoMobileTO;

public interface FaleConoscoService extends DeziService<FaleConoscoMobileTO> {

	void salvarFaleConosco(FaleConoscoMobileTO faleConoscoMobileTO);

}
