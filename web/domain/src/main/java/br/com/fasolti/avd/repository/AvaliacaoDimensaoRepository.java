package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.AvaliacaoDimensao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoDimensaoRepository extends QueryDslCrudRepository<AvaliacaoDimensao> {

	List<AvaliacaoDimensao> findByAvaliacaoId(Long idAvaliacao);
}
