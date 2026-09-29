package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Avaliacao;
import br.com.fasolti.avd.domain.Participante;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository
public interface AvaliacaoRepository extends QueryDslCrudRepository<Avaliacao> {

	List<Avaliacao> findByCampanhaId(Long campanhaId);

	List<Avaliacao> findByCampanhaIdAndDescricaoAbreviadaIgnoreCase(Long campanhaId, String descricaoAbreviada);

	List<Avaliacao> findByCampanhaEmpresaIdAndIdIn(Long idEmpresa, Set<Long> listDeIds);

	@Modifying
	@Transactional()
	@Query("update Avaliacao a set a.situacao = ?1 where a.id = ?2")
	void updateStatus(SituacaoAvaliacao liberada, Long id);


	@Query(value = "SELECT DISTINCT ON (ap.id_avaliacao) av.id, av.descricao_abreviada, av.limite_av2" +
		" from avaliacao_participante ap INNER JOIN avaliacao av ON ap.id_avaliacao = av.id" +
		" INNER JOIN campanha ca ON av.id_campanha = ca.id" +
		" where (ap.id_avaliador = ?1 or ap.id_avaliado = ?1) and av.id_situacao = 2" +
		" and av.limite_av2 >= current_date" +
		" and ca.id_empresa = ?3" +
		" and " +
		" CASE WHEN av.data_atualizacao is not null" +
		" THEN av.data_atualizacao > ?2" +
		" ELSE av.data_criacao > ?2" +
		" END", nativeQuery = true)
	List<Object[]> obterAvaliacoesMobile(@Param(value = "idAvaliador") Long idAvaliador,
										 @Param(value = "dataUltimoSync") Date dataUltimoSync,
										 @Param(value = "idEmpresa") Long idEmpresa);

	@Query(value = "SELECT DISTINCT ON (ap.id_avaliacao) av.id, av.descricao_abreviada, av.limite_av2" +
		" from avaliacao_participante ap INNER JOIN avaliacao av ON ap.id_avaliacao = av.id" +
		" where (ap.id_avaliador = :idAvaliador or ap.id_avaliado = :idAvaliador) " +
		" and av.id_situacao = 2 ", nativeQuery = true)
	List<Object[]> obterAvaliacoesUsuarioLogado(@Param(value = "idAvaliador") Long idAvaliador);
}
