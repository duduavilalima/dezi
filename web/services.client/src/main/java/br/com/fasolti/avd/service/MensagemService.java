package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.EmailBoasVindasTO;
import br.com.fasolti.avd.transfer.MensagemTO;

public interface MensagemService extends AvdService<MensagemTO, MensagemTO> {

	public void sendWelcomeMessage(EmailBoasVindasTO emailBoasVindasTO);
}

