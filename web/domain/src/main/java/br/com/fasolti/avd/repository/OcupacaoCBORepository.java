package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.OcupacaoCBO;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OcupacaoCBORepository extends QueryDslCrudRepository<OcupacaoCBO> {

    List<OcupacaoCBO> findByCodigoContainingIgnoreCaseOrDescricaoContainingIgnoreCase(String codigo, String descricao);
}
