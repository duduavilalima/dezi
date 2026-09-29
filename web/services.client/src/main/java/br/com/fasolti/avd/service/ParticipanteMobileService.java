package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.mobile.AvaliacaoMobileTO;
import br.com.fasolti.avd.transfer.mobile.ParticipanteMobileTO;

import java.util.List;

public interface ParticipanteMobileService extends DeziService<ParticipanteMobileTO> {

	List<AvaliacaoMobileTO> obterAvaliacaoMobileList();

}
