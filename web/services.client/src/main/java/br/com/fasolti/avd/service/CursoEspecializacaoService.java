package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.CursoEspecializacaoTO;

import java.util.List;

public interface CursoEspecializacaoService extends AvdService<CursoEspecializacaoTO, CursoEspecializacaoTO> {

	List<CursoEspecializacaoTO> findByContratanteUsuarioLogadoAndSituacaoAtivo();

	CursoEspecializacaoTO findByNomeAndContratante(String nome, Long idContratante);

}
