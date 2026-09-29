package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.ParticipanteEvolucaoTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.PreenchimentoTO;
import br.com.fasolti.commons.client.service.Service;

public interface ParticipanteEvolucaoService extends Service<PreenchimentoTO> {
	ParticipanteEvolucaoTO calcularEvolucao(ParticipanteTO participante);
}
