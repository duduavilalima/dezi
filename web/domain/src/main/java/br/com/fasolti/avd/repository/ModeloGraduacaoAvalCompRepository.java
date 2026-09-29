package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.ModeloGraduacaoAvalComp;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModeloGraduacaoAvalCompRepository extends QueryDslCrudRepository<ModeloGraduacaoAvalComp> {

//	ModeloGraduacaoAvalComp findByAvaliacaoCompetenciaIdAndModeloGraduacaoIdModeloGraduacao(Long avaliacaoCompetenciaId, Long modeloGraduacaoId);

	ModeloGraduacaoAvalComp findByAvaliacaoCompetenciaId(Long avaliacaoCompetenciaId);

	ModeloGraduacaoAvalComp findByAvaliacaoCompDetId(Long avaliacaoCompDetId);

	List<ModeloGraduacaoAvalComp> findByModeloGraduacaoCompId(Long modeloGraduacaoCompId);

}
