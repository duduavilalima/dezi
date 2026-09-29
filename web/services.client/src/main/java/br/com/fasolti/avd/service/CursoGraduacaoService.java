package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.CursoGraduacaoTO;

import java.util.List;

public interface CursoGraduacaoService extends AvdService<CursoGraduacaoTO, CursoGraduacaoTO> {

	List<CursoGraduacaoTO> listAtivos();

	CursoGraduacaoTO findByNomeAndContratante(String nome, Long idContratante);

}

