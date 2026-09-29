package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Participante;
import br.com.fasolti.avd.domain.Pendencia;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PendenciaRepository extends QueryDslCrudRepository<Pendencia> {

	List<Pendencia> findByAvaliacaoId(Long idAvaliacao);

	@Query("select pendencia from Pendencia pendencia " +
		" where pendencia.avaliacao.id = :idAvaliacao and (pendencia.cargo.id = :idCargo or pendencia.avaliado.id = :idAvaliado)  order by pendencia.id")
	List<Pendencia> findByAvaliacaoIdAndCargoIdOrAvaliadoId(@Param(value = "idAvaliacao") Long idAvaliacao, @Param(value = "idCargo") Long idCargo, @Param(value = "idAvaliado") Long idAvaliado);

}
