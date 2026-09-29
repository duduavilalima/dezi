package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.TipoCurso;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TipoCursoRepository extends QueryDslCrudRepository<TipoCurso> {

	List<TipoCurso> findByContratanteIdAndSituacao(Long contratanteId, Situacao situacao);

	TipoCurso findByContratanteIdAndNomeIgnoreCase(Long contratanteId, String nome);

}
