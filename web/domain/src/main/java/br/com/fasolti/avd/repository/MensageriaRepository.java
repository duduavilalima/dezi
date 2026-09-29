package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Mensageria;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensageriaRepository extends QueryDslCrudRepository<Mensageria> {
	List<Mensageria> findByAvaliacaoIdAndTituloOrderByDataHoraEnvioAsc(Long avaliacaoId, String titulo);

	List<Mensageria> findByUsuarioEnvioIdOrderByDataHoraEnvioAsc(@Param("id") Long id);

	List<Mensageria> findByUsuarioDestinoIdOrderByDataHoraEnvioAsc(@Param("id") Long id);

	List<Mensageria> findByAvaliacaoId(Long id);

	@Query("SELECT max(m.numMensagem) FROM Mensageria m")
	Long findMaxNumMensagem();
}
