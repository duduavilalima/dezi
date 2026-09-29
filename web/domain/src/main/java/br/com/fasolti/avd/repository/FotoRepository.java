package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Foto;
import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.types.TipoFoto;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FotoRepository extends QueryDslCrudRepository<Foto> {
	Foto findByTipo(TipoFoto tipo);

	Foto findByFuncionario(Funcionario funcionario);

	Foto findByTipoAndFuncionario(TipoFoto tipo, Funcionario funcionario);

	@Query(value = "SELECT DISTINCT on (ap.id_avaliado) ft.id_funcionario, ft.base64 from foto ft " +
		"INNER JOIN avaliacao_participante ap on ft.id_funcionario = ap.id_avaliado " +
		"INNER JOIN funcionario f on ft.id_funcionario = f.id " +
		"INNER JOIN avaliacao av on ap.id_avaliacao = av.id " +
		"WHERE f.id_empresa = ?2 " +
		"AND (ap.id_avaliador = ?1 or ap.id_avaliado = ?1) " +
		"AND (av.limite_av1 >= current_date OR av.limite_av2 >= current_date)", nativeQuery = true)
	List<Object[]> obterFotoSubordinadoAvaliacaoAtiva(Long avaliadorId, Long empresaId);

}
