package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.CursoGraduacao;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CursoGraduacaoRepository extends QueryDslCrudRepository<CursoGraduacao> {

	List<CursoGraduacao> findByContratanteIdAndSituacao(Long contratanteId, Situacao situacao);

	CursoGraduacao findByContratanteIdAndNomeIgnoreCase(Long contratanteId, String nome);

}
