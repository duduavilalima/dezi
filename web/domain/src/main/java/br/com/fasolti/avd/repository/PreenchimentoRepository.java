package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Preenchimento;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PreenchimentoRepository extends QueryDslCrudRepository<Preenchimento> {

	@Query("select preenchimento from br.com.fasolti.avd.domain.Preenchimento preenchimento JOIN FETCH preenchimento.participante " +
		" where preenchimento.participante.avaliacao.id = :idAvaliacao " +
		" order by preenchimento.id")
	List<Preenchimento> findByAvaliacao(@Param(value = "idAvaliacao") Long idAvaliacao);

	@Query("select preenchimento from br.com.fasolti.avd.domain.Preenchimento preenchimento JOIN FETCH preenchimento.participante " +
		" where preenchimento.participante.avaliado.cargo.id = :idCargo " +
		" order by preenchimento.participante.id")
	List<Preenchimento> findByCargoParticipante(@Param(value = "idCargo") Long idCargo);

	List<Preenchimento> findByAvaliacaoCompDetId(Long avaliacaoCompetenciaId);

	List<Preenchimento> findByAvaliacaoCompDetIdAndParticipanteAvaliadorId(Long avaliacaoCompetenciaId, Long avaliadorId);

	List<Preenchimento> findByAvaliacaoCompDetIdAndParticipanteAvaliadoId(Long avaliacaoCompetenciaId, Long avaliadoId);

	List<Preenchimento> findByParticipanteId(Long participanteId);

	Preenchimento findByParticipanteIdAndAvaliacaoCompDetId(Long participanteId, Long avaliacaoCompetenciaId);

	@Query("select preenchimento from br.com.fasolti.avd.domain.Preenchimento preenchimento JOIN FETCH preenchimento.participante " +
		" where " +
		"preenchimento.participante.avaliacao.situacao != :situacao and" +
		" preenchimento.participante.avaliacao.id = :avaliacaoId and" +
		" ( (preenchimento.participante.avaliador.id = :avaliadorId and preenchimento.participante.limiteAv2 is not null and preenchimento.participante.limiteAv2 >= :data) or " +
		" (preenchimento.participante.avaliado.id = :avaliadorId and preenchimento.participante.limiteAv1 is not null and preenchimento.participante.limiteAv1 >= :data) )" +
		" order by preenchimento.participante.avaliacao.id")
	List<Preenchimento> findByAvaliadorAndLimiteAv2AndAvaliacao(@Param(value = "avaliadorId") Long avaliadorId,
																@Param(value = "data") Date data,
																@Param(value = "situacao") SituacaoAvaliacao situacao,
																@Param(value = "avaliacaoId") Long avaliacaoId);

	@Query("select preenchimento from br.com.fasolti.avd.domain.Preenchimento preenchimento JOIN FETCH preenchimento.participante JOIN FETCH preenchimento.avaliacaoCompDet " +
		" where " +
		" preenchimento.avaliacaoCompDet.avaliacaoCompetencia.id = :competenciaId and " +
		"preenchimento.participante.avaliacao.situacao != :situacao and" +
		" ( (preenchimento.participante.avaliador.id = :avaliadorId and preenchimento.participante.limiteAv2 is not null and preenchimento.participante.limiteAv2 >= :data) or" +
		" (preenchimento.participante.avaliado.id = :avaliadorId and preenchimento.participante.limiteAv1 is not null and preenchimento.participante.limiteAv1 >= :data) )" +
		" order by preenchimento.avaliacaoCompDet.avaliacaoCompetencia.id")
	List<Preenchimento> findByAvaliadorAndLimiteAv2AndAvaliacaoAndCompetencia(@Param(value = "avaliadorId") Long avaliadorId,
																			  @Param(value = "data") Date data,
																			  @Param(value = "situacao") SituacaoAvaliacao situacao,
																			  @Param(value = "competenciaId") Long competenciaId);

	@Query("select preenchimento from br.com.fasolti.avd.domain.Preenchimento preenchimento JOIN FETCH preenchimento.participante p JOIN FETCH preenchimento.avaliacaoCompDet JOIN FETCH p.avaliacao av " +
		" where" +
		" av.campanha.empresa.id = :empresaId and" +
		" preenchimento.avaliacaoCompDet.avaliacaoCompetencia.id = :competenciaId and" +
		" preenchimento.participante.avaliacao.situacao = 2 and" +
		" ( (preenchimento.participante.avaliador.id = :avaliadorId and preenchimento.participante.limiteAv2 is not null and preenchimento.participante.limiteAv2 >= current_date) or" +
		" (preenchimento.participante.avaliado.id = :avaliadorId and preenchimento.participante.limiteAv1 is not null" +
		" and preenchimento.participante.limiteAv1 >= current_date) ) " +
		" AND :dataUltimoSync < CASE WHEN preenchimento.dataAtualizacao is not null" +
		" 	THEN preenchimento.dataAtualizacao" +
		" 		ELSE preenchimento.dataCriacao" +
		" 	END" +
		" order by preenchimento.avaliacaoCompDet.avaliacaoCompetencia.id")
	List<Preenchimento> findyByAvaliadorAndCompetenciaAndDataUltimoSync(@Param(value = "avaliadorId") Long avaliadorId,
																		@Param(value = "competenciaId") Long competenciaId,
																		@Param(value = "dataUltimoSync") Date dataUltimoSync,
																		@Param(value = "empresaId") Long empresaId);

	@Query("select preenchimento from br.com.fasolti.avd.domain.Preenchimento preenchimento JOIN FETCH preenchimento.participante p JOIN FETCH p.avaliacao av" +
		" where " +
		" av.campanha.empresa.id = :empresaId and" +
		" preenchimento.participante.avaliacao.situacao = 2 and" +
		" preenchimento.participante.avaliacao.id = :avaliacaoId and" +
		" ( (preenchimento.participante.avaliador.id = :avaliadorId and preenchimento.participante.limiteAv2 is not null and preenchimento.participante.limiteAv2 >= current_date) or " +
		" (preenchimento.participante.avaliado.id = :avaliadorId and preenchimento.participante.limiteAv1 is not null and preenchimento.participante.limiteAv3 is not null " +
		"  and preenchimento.participante.limiteAv1 >= current_date) ) " +
		" AND :dataUltimaSync < CASE WHEN preenchimento.dataAtualizacao is not null " +
		"	THEN preenchimento.dataAtualizacao " +
		"		ELSE preenchimento.dataCriacao " +
		"	END" +
		" order by preenchimento.participante.avaliacao.id")
	List<Preenchimento> findByAvaliadorIdAndAvaliacaoIdAndDataUltimaSync(@Param(value = "avaliadorId") Long avaliadorId,
																		 @Param(value = "avaliacaoId") Long avaliacaoId,
																		 @Param(value = "dataUltimaSync") Date dataUltimaSync,
																		 @Param(value = "empresaId") Long empresaId);


	@Query("select preenchimento from br.com.fasolti.avd.domain.Preenchimento preenchimento JOIN FETCH preenchimento.participante p JOIN FETCH p.avaliacao av" +
		" where " +
		" av.campanha.empresa.id = :empresaId and" +
		" preenchimento.participante.avaliacao.situacao = 2 and" +
		" preenchimento.participante.avaliacao.id = :avaliacaoId and" +
		" ( (preenchimento.participante.avaliador.id = :avaliadorId and preenchimento.participante.limiteAv2 is not null and preenchimento.participante.limiteAv2 >= current_date) or " +
		" (preenchimento.participante.avaliado.id = :avaliadorId and preenchimento.participante.limiteAv1 is not null and preenchimento.participante.limiteAv3 is not null " +
		"  and preenchimento.participante.limiteAv1 >= current_date) ) " +
		" order by preenchimento.participante.avaliacao.id")
	List<Preenchimento> findByAvaliadorIdAndAvaliacaoId(@Param(value = "avaliadorId") Long avaliadorId,
														@Param(value = "avaliacaoId") Long avaliacaoId,
														@Param(value = "empresaId") Long empresaId);


	@Query(value = "SELECT pree.* from avaliacao_preenchimento pree " +
		" INNER JOIN avaliacao_participante ap on pree.id_participante = ap.id" +
		" INNER JOIN avaliacao_comp_det acd on pree.id_avaliacao_comp_det = acd.id" +
		" where ap.id = ?1 and acd.id = ?2 ", nativeQuery = true)
	Object[] obterPreenchimento(Long participanteId, Long avaliacaoCompetenciaId);


	@Query("SELECT  " +
		"p.avaliador.id AS avaliador, " +
		"p.avaliado.id AS avaliado, " +
		"p.avaliacao.id AS avaliacao, " +

		"SUM(CASE " +
		"WHEN (p.avaliador.id = ?1 " +
		"AND (av.preencheAv1 IS TRUE AND p.limiteAv1 IS NOT NULL) " +
		"OR (av.preencheAv2 IS TRUE AND p.limiteAv2 IS NOT NULL)) " +
		"THEN 1 " +
		"ELSE 0 " +
		"END) AS qtd_perg_avaliacao, " +

		"SUM(CASE " +
		"WHEN (" +
		"p.avaliador.id = ?1 " +
		"AND (av.preencheAv1 IS TRUE " +
		"AND p.limiteAv1 IS NOT NULL " +
		"AND preenchimento.graduacaoCompAv1Id IS NOT NULL) " +
		"OR (av.preencheAv2 IS TRUE " +
		"AND p.limiteAv2 IS NOT NULL " +
		"AND preenchimento.graduacaoCompAv2Id IS NOT NULL)" +
		") " +
		"THEN 1 " +
		"ELSE 0 " +
		"END) AS qtd_resp_avaliacao, " +

		"sum(CASE " +
		"WHEN (p.avaliado.id = ?1 " +
		"AND av.preencheAv1 IS TRUE " +
		"AND p.limiteAv1 IS NOT NULL " +
		"AND p.limiteAv3 IS NOT NULL " +
		"AND p.perfilAgente = ?5) " +
		"THEN 1 " +
		"ELSE 0 " +
		"END) AS qtd_perg_auto, " +

		"sum(CASE " +
		"WHEN (p.avaliado.id = ?1 " +
		"AND av.preencheAv1 IS TRUE " +
		"AND p.limiteAv1 IS NOT NULL " +
		"AND p.limiteAv3 IS NOT NULL " +
		"AND p.perfilAgente = ?5 " +
		"AND preenchimento.graduacaoCompAv1Id IS NOT NULL) " +
		"THEN 1 " +
		"ELSE 0 " +
		"END) AS qtd_resp_auto " +

		"FROM br.com.fasolti.avd.domain.Preenchimento preenchimento " +
		"JOIN preenchimento.participante p " +
		"JOIN preenchimento.avaliacaoCompDet acd " +
		"JOIN acd.avaliacaoCompetencia ac " +
		"JOIN p.avaliacao av " +
		"WHERE " +
		"(p.avaliador.id = ?1 OR p.avaliado.id = ?2) " +
		"AND p.avaliacao.id = ?3 " +
		"AND ac.id = ?4 " +
		"GROUP BY p.avaliador.id, p.avaliacao.id, p.avaliado.id " +
		"ORDER BY p.avaliador.id, p.avaliacao.id, p.avaliado.id")
	List<Object[]> listPerguntasERespostasPorComp(Long idAvaliador, Long idAvaliado, Long idAvaliacao, Long idCompetencia, TipoAvaliacaoPerfil tipoAvaliacaoPerfil);


	@Modifying
	@Transactional()
	@Query("delete from avaliacao_preenchimento p where p.participante.id = ?1")
	void excluirPrenchimentoPorParticipante(Long idParticipante);


}
