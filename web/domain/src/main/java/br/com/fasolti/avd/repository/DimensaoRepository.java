package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Dimensao;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DimensaoRepository extends QueryDslCrudRepository<Dimensao> {
	Dimensao findByNomeAndEmpresaId(String nome, Long idEmpresa);

	List<Dimensao> findBySituacaoAndEmpresaId(Situacao situacao, Long idEmpresa);
}
