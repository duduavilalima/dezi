package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.CursoEspecializacao;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoEspecializacaoRepository extends QueryDslCrudRepository<CursoEspecializacao> {

	List<CursoEspecializacao> findByContratanteIdAndSituacao(Long contratanteId, Situacao situacao);

	CursoEspecializacao findByContratanteIdAndNomeIgnoreCase(Long contratanteId, String nome);

}
