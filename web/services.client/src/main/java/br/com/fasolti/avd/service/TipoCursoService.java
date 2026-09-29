package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.TipoCursoTO;

import java.util.List;

public interface TipoCursoService extends AvdService<TipoCursoTO, TipoCursoTO> {

	List<TipoCursoTO> findByContratanteUsuarioLogadoSituacaoAtivo();

	TipoCursoTO findByNomeAndContratante(String nome, Long idContratante);

}
