package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.AvaliacaoCompDetHint;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoCompDetHintRepository extends QueryDslCrudRepository<AvaliacaoCompDetHint> {

	AvaliacaoCompDetHint findByAvaliacaoCompDetIdAndIdHint(Long avaliacaoCompDetId, Long hintId);

	List<AvaliacaoCompDetHint> findByAvaliacaoCompDetId(Long avaliacaoCompDetId);

	List<AvaliacaoCompDetHint> findByIdGraduacao(Long graduacaoId);
}
