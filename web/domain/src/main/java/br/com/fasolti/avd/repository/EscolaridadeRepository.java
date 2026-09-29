package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Escolaridade;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EscolaridadeRepository extends QueryDslCrudRepository<Escolaridade> {

	List<Escolaridade> findByContratanteIdAndSituacao(Long contratanteId, Situacao situacao);

	Escolaridade findByContratanteIdAndNomeIgnoreCase(Long contratanteId, String nome);

}
