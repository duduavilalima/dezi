package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.ModeloGraduacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloGraduacaoRepository extends QueryDslCrudRepository<ModeloGraduacao> {

	ModeloGraduacao findByEmpresaIdAndDescricaoModelo(Long empresaId, String descricaoModelo);

	ModeloGraduacao findByEmpresaIdAndDescricaoModeloIgnoreCase(Long empresaId, String descricaoModelo);

}
