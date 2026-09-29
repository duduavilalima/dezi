package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.ModeloGraduacaoComp;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloGraduacaoCompRepository extends QueryDslCrudRepository<ModeloGraduacaoComp> {

	ModeloGraduacaoComp findByIdModeloGraduacao(Long modeloGraduacaoId);

}
