package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Competencia;
import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompetenciaRepository extends QueryDslCrudRepository<Competencia> {

	List<Competencia> findByEmpresa(Empresa empresa);

	List<Competencia> findByEmpresaIdAndDescricaoAbreviadaIgnoreCase(Long empresaId, String descricaoAbreviada);

	Competencia findByEmpresaIdAndDescricaoAbreviada(Long empresaId, String descricaoAbreviada);

}
