package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.IdiomaTO;

import java.util.List;

public interface IdiomaService extends AvdService<IdiomaTO, IdiomaTO> {

	List<IdiomaTO> findIdiomasByContratanteAndSituacao();

	IdiomaTO findByNomeAndContratante(String nome, Long idContratante);

}
