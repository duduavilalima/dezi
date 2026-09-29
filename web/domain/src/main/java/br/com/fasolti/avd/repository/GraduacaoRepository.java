package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Graduacao;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraduacaoRepository extends QueryDslCrudRepository<Graduacao> {

	List<Graduacao> findByModeloGraduacaoIdOrderByOrdemAsc(Long modeloGraduacaoId);

	List<Graduacao> findByModeloGraduacaoIdAndSituacaoOrderByOrdemAsc(Long modeloGraduacaoId, Situacao situacao);

	List<Graduacao> findByModeloGraduacaoIdAndSituacaoOrderByPontoAsc(Long modeloGraduacaoId, Situacao situacao);

	Graduacao findByModeloGraduacaoIdAndLabelIgnoreCase(Long modeloGraduacaoId, String label);

}
