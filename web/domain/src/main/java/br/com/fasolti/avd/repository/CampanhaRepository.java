package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Campanha;
import br.com.fasolti.avd.domain.Participante;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampanhaRepository extends QueryDslCrudRepository<Campanha> {

	Campanha findByEmpresaIdAndDescricaoIgnoreCase(Long empresaId, String descricao);

	List<Campanha> findByEmpresaId(Long empresaId);

}
