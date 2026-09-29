package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Participante;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface AvaliacaoParticipanteRepository extends QueryDslCrudRepository<Participante>{

	List<Participante> findByAvaliacaoId(Long avaliacaoId);

	List<Participante> findByAvaliacaoCampanhaId(Long campanhaId);

	List<Participante> findByAvaliadoIdAndAvaliacaoId(Long avaliadoId, Long avaliacaoId);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.id = :idAvaliacao and participante.limiteAv1 is not null order by participante.id")
	List<Participante> obterTotalAv1PorAvaliacao(@Param(value = "idAvaliacao") Long idAvaliacao);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.id = :idAvaliacao and participante.limiteAv2 is not null order by participante.id")
	List<Participante> obterTotalAv2PorAvaliacao(@Param(value = "idAvaliacao") Long idAvaliacao);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.id = :idAvaliacao and participante.limiteAv3 is not null order by participante.id")
	List<Participante> obterTotalAv3PorAvaliacao(@Param(value = "idAvaliacao") Long idAvaliacao);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.empresa.id = :idEmpresa and participante.avaliacao.preencheAv1 = true order by participante.id")
	List<Participante> findAv1ByEmpresa(@Param(value = "idEmpresa") Long idEmpresa);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.empresa.id = :idEmpresa and participante.avaliacao.preencheAv2 = true order by participante.id")
	List<Participante> findAv2ByEmpresa(@Param(value = "idEmpresa") Long idEmpresa);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.empresa.id = :idEmpresa and participante.avaliacao.preencheAv3 = true order by participante.id")
	List<Participante> findAv3ByEmpresa(@Param(value = "idEmpresa") Long idEmpresa);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.empresa.id = :idEmpresa and participante.avaliacao.preencheAv4 = true order by participante.id")
	List<Participante> findAv4ByEmpresa(@Param(value = "idEmpresa") Long idEmpresa);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.id = :idCampanha and participante.avaliacao.preencheAv1 = true order by participante.id")
	List<Participante> findAv1ByCampanha(@Param(value = "idCampanha") Long idCampanha);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.id = :idCampanha and participante.avaliacao.preencheAv2 = true order by participante.id")
	List<Participante> findAv2ByCampanha(@Param(value = "idCampanha") Long idCampanha);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.id = :idCampanha and participante.avaliacao.preencheAv3 = true order by participante.id")
	List<Participante> findAv3ByCampanha(@Param(value = "idCampanha") Long idCampanha);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.id = :idCampanha and participante.avaliacao.preencheAv4 = true order by participante.id")
	List<Participante> findAv4ByCampanha(@Param(value = "idCampanha") Long idCampanha);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.id = :idAvaliacao and participante.avaliacao.preencheAv4 = true order by participante.id")
	List<Participante> findAv4ByAvaliacao(@Param(value = "idAvaliacao") Long idAvaliacao);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.empresa.id = :idEmpresa and " +
		" participante.dataConclusaoAv1 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByDataConclusaoAV1(
		@Param(value = "idEmpresa") Long idEmpresa,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.empresa.id = :idEmpresa and " +
		" participante.dataConclusaoAv2 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByDataConclusaoAV2(
		@Param(value = "idEmpresa") Long idEmpresa,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.empresa.id = :idEmpresa and " +
		" participante.dataConclusaoAv3 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByDataConclusaoAV3(
		@Param(value = "idEmpresa") Long idEmpresa,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.empresa.id = :idEmpresa and " +
		" participante.dataConclusaoAv4 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByDataConclusaoAV4(
		@Param(value = "idEmpresa") Long idEmpresa,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.id = :idAvaliacao and participante.dataConclusaoAv1 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByAvaliacaoAndDataConclusaoAV1(
		@Param(value = "idAvaliacao") Long idAvaliacao,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.id = :idAvaliacao and participante.dataConclusaoAv2 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByAvaliacaoAndDataConclusaoAV2(
		@Param(value = "idAvaliacao") Long idAvaliacao,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.id = :idAvaliacao and participante.dataConclusaoAv3 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByAvaliacaoAndDataConclusaoAV3(
		@Param(value = "idAvaliacao") Long idAvaliacao,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.id = :idAvaliacao and participante.dataConclusaoAv4 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByAvaliacaoAndDataConclusaoAV4(
		@Param(value = "idAvaliacao") Long idAvaliacao,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.id = :idCampanha and participante.dataConclusaoAv1 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByCampanhaAndDataConclusaoAV1(
		@Param(value = "idCampanha") Long idCampanha,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.id = :idCampanha and participante.dataConclusaoAv2 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByCampanhaAndDataConclusaoAV2(
		@Param(value = "idCampanha") Long idCampanha,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.id = :idCampanha and participante.dataConclusaoAv3 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByCampanhaAndDataConclusaoAV3(
		@Param(value = "idCampanha") Long idCampanha,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.id = :idCampanha and participante.dataConclusaoAv4 between :dataInicial and :dataFinal order by participante.id")
	List<Participante> findByCampanhaAndDataConclusaoAV4(
		@Param(value = "idCampanha") Long idCampanha,
		@Param(value = "dataInicial") LocalDate dataInicial,
		@Param(value = "dataFinal") LocalDate dataFinal);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliado.id = :idAvaliado and participante.avaliacao.id = :idAvaliacao and" +
		" participante.perfilAgente =:tipo order by participante.id")
	List<Participante> findByAvaliadorPorAvaliadoAndAvaliacaoAndTipo(
		@Param(value = "idAvaliado") Long idAvaliado,
		@Param(value = "idAvaliacao") Long idAvaliacao,
		@Param(value = "tipo") TipoAvaliacaoPerfil tipo);

	@Query(value = "SELECT DISTINCT ON (ap.id_avaliado) ap.* from avaliacao_participante ap where ap.id_avaliacao = ?1" +
		" ORDER BY ap.id_avaliado", nativeQuery = true)
	List<Object[]> obterAvaliadosPorAvaliacao(Long idAvaliacao);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.empresa.id = :idEmpresa and participante.limiteAv1 is not null order by participante.id")
	List<Participante> findByAV1(@Param(value = "idEmpresa") Long idEmpresa);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.empresa.id = :idEmpresa and participante.limiteAv2 is not null order by participante.id")
	List<Participante> findByAV2(@Param(value = "idEmpresa") Long idEmpresa);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.empresa.id = :idEmpresa  and participante.limiteAv3 is not null order by participante.id")
	List<Participante> findByAV3(@Param(value = "idEmpresa") Long idEmpresa);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.id = :idCampanha and participante.limiteAv1 is not null order by participante.id")
	List<Participante> findByCampanhaAV1(@Param(value = "idCampanha") Long idCampanha);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.id = :idCampanha and participante.limiteAv2 is not null order by participante.id")
	List<Participante> findByCampanhaAV2(@Param(value = "idCampanha") Long idCampanha);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.campanha.id = :idCampanha and participante.limiteAv3 is not null order by participante.id")
	List<Participante> findByCampanhaAV3(@Param(value = "idCampanha") Long idCampanha);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.id = :idAvaliacao and participante.limiteAv1 is not null order by participante.id")
	List<Participante> findByAvaliacaoAV1(@Param(value = "idAvaliacao") Long idAvaliacao);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.id = :idAvaliacao and participante.limiteAv2 is not null order by participante.id")
	List<Participante> findByAvaliacaoAV2(@Param(value = "idAvaliacao") Long idAvaliacao);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao " +
		" where participante.avaliacao.id = :idAvaliacao and participante.limiteAv3 is not null order by participante.id")
	List<Participante> findByAvaliacaoAV3(@Param(value = "idAvaliacao") Long idAvaliacao);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao JOIN FETCH participante.avaliador " +
		" where participante.avaliacao.id = :idAvaliacao and participante.avaliador.unidade.id = :idUnidade and participante.limiteAv1 is not null order by participante.id")
	List<Participante> findByAvaliacaoAndUnidadeAV1(
		@Param(value = "idAvaliacao") Long idAvaliacao,
		@Param(value = "idUnidade") Long idUnidade);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao JOIN FETCH participante.avaliador " +
		" where participante.avaliacao.id = :idAvaliacao and participante.avaliador.unidade.id = :idUnidade and participante.limiteAv2 is not null order by participante.id")
	List<Participante> findByAvaliacaoAndUnidadeAV2(
		@Param(value = "idAvaliacao") Long idAvaliacao,
		@Param(value = "idUnidade") Long idUnidad);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao JOIN FETCH participante.avaliador " +
		" where participante.avaliacao.id = :idAvaliacao and participante.avaliador.departamento.id = :idDepartamento and participante.limiteAv1 is not null order by participante.id")
	List<Participante> findByAvaliacaoAndDepartamentoAV1(
		@Param(value = "idAvaliacao") Long idAvaliacao,
		@Param(value = "idDepartamento") Long idDepartamento);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao JOIN FETCH participante.avaliador " +
		" where participante.avaliacao.id = :idAvaliacao and participante.avaliador.departamento.id = :idDepartamento and participante.limiteAv2 is not null order by participante.id")
	List<Participante> findByAvaliacaoAndDepartamentoAV2(
		@Param(value = "idAvaliacao") Long idAvaliacao,
		@Param(value = "idDepartamento") Long idDepartamento);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao JOIN FETCH participante.avaliador " +
		" where participante.avaliacao.id = :idAvaliacao and participante.avaliador.unidade.id = :idUnidade and participante.limiteAv3 is not null order by participante.id")
	List<Participante> findByAvaliacaoAndUnidadeAV3(
		@Param(value = "idAvaliacao") Long idAvaliacao,
		@Param(value = "idUnidade") Long idUnidade);

	@Query("select participante from Participante participante JOIN FETCH participante.avaliacao JOIN FETCH participante.avaliador " +
		" where participante.avaliacao.id = :idAvaliacao and participante.avaliador.departamento.id = :idDepartamento and participante.limiteAv3 is not null order by participante.id")
	List<Participante> findByAvaliacaoAndDepartamentoAV3(
		@Param(value = "idAvaliacao") Long idAvaliacao,
		@Param(value = "idDepartamento") Long idDepartamento);

	@Query(value = "select count(pre.id) " +
		"from avaliacao_preenchimento pre " +
		"inner join avaliacao_participante ap on pre.id_participante = ap.id " +
		"inner join avaliacao av on ap.id_avaliacao = av.id " +
		"where ap.id_avaliacao = ?1 and pre.id_graduacao_av1 is not null and av.limite_av1 is not null", nativeQuery = true)
	Long countPreenchimentoAv1ByAvaliacao(Long idAvaliacao);

	@Query(value = "select count(pre.id) " +
		"from avaliacao_preenchimento pre " +
		"inner join avaliacao_participante ap on pre.id_participante = ap.id " +
		"inner join avaliacao av on ap.id_avaliacao = av.id " +
		"where ap.id_avaliacao = ?1 and pre.id_graduacao_av2 is not null and av.limite_av2 is not null", nativeQuery = true)
	Long countPreenchimentoAv2ByAvaliacao(Long idAvaliacao);

	@Query(value = "select count(pre.id) " +
		"from avaliacao_preenchimento pre " +
		"inner join avaliacao_participante ap on pre.id_participante = ap.id " +
		"inner join avaliacao av on ap.id_avaliacao = av.id " +
		"where ap.id_avaliacao = ?1 and pre.id_graduacao_av3 is not null and av.limite_av3 is not null", nativeQuery = true)
	Long countPreenchimentoAv3ByAvaliacao(Long idAvaliacao);

	@Query(value = "select count(ap.id) from avaliacao_participante ap where ap.id_avaliacao = ?1 and ap.id_perfil_agente = ?2 ", nativeQuery = true)
	Integer countPerfilAgenteByAvaliacao(Long idAvaliacao, Integer idPerfilAgente);

	@Modifying
	@Transactional()
	@Query("update Participante p set p.limiteAv1 = ?1, p.limiteAv2 = ?2, p.limiteAv3 = ?3 where p.id = ?4")
	void updateDataLimite(Date limiteAv1, Date limiteAv2, Date limiteAv3, Long id);

	@Modifying
	@Transactional()
	@Query("delete from Participante p where p.id = ?1")
	void excluirParticipante(Long id);

}
