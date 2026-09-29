package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.AvaliacaoTipoCompetencia;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoTipoCompetenciaRepository extends QueryDslCrudRepository<AvaliacaoTipoCompetencia> {

	List<AvaliacaoTipoCompetencia> findByAvaliacaoId(Long id);

	List<AvaliacaoTipoCompetencia> findByTipoCompetenciaId(Long id);

	List<AvaliacaoTipoCompetencia> findByAvaliacaoIdAndTipoCompetenciaId(Long idAvaliacao, Long idTipo);

	List<AvaliacaoTipoCompetencia> findByAvaliacaoIdAndPerfilAgente(Long idAvaliacao, TipoAvaliacaoPerfil perfilAgenteId);

	@Query(value = "select distinct id_tipo from avaliacao_tipo_competencia where id_avaliacao = ?1", nativeQuery = true)
	List<Object> findIdsTiposCompetenciaByAvaliacaoId(Long idAvaliacao);

}
