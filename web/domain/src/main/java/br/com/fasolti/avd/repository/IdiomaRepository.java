package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Idioma;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdiomaRepository extends QueryDslCrudRepository<Idioma> {

	List<Idioma> findByContratanteIdAndSituacao(Long contratanteId, Situacao situacao);

	Idioma findByContratanteIdAndNomeIgnoreCase(Long contratanteId, String nome);

}
