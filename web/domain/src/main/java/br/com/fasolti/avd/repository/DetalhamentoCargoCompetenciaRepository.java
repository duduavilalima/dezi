package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.DetalhamentoCargoCompetencia;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalhamentoCargoCompetenciaRepository extends QueryDslCrudRepository<DetalhamentoCargoCompetencia> {

	List<DetalhamentoCargoCompetencia> findByCargoCompetenciaId(Long cargoCompetenciaId);

	DetalhamentoCargoCompetencia findByCargoCompetenciaIdAndCompetenciaDetalhamentoId(Long cargoCompetenciaId, Long competenciaDetalhamentoId);

	@Query("select detCargoComp from DetalhamentoCargoCompetencia detCargoComp JOIN FETCH detCargoComp.cargoCompetencia " +
		"JOIN FETCH detCargoComp.competenciaDetalhamento " +
		"where detCargoComp.cargoCompetencia.situacao = :idSituacao " +
		"and detCargoComp.cargoCompetencia.cargo.id = :idCargo " +
		"and detCargoComp.cargoCompetencia.cargo.situacao = :idSituacao " +
		"and detCargoComp.competenciaDetalhamento.situacao = :idSituacao " +
		"and detCargoComp.competenciaDetalhamento.competencia.situacao = :idSituacao " +
		"and detCargoComp.aplicavelAoCargo = true ")
		//colocar o and para  aplicaveis
	List<DetalhamentoCargoCompetencia> findDetalhamentosAplicaveisAoCargo(@Param(value = "idCargo") Long idCargo,
																		  @Param(value = "idSituacao") Situacao idSituacao);

}
