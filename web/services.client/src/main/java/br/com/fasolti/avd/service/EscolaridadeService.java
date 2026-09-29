package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.EscolaridadeTO;

import java.util.List;

public interface EscolaridadeService extends AvdService<EscolaridadeTO, EscolaridadeTO> {

	List<EscolaridadeTO> listAllActiveForLoggedUser();

	EscolaridadeTO findByNomeAndContratante(String nome, Long idContratante);

}
