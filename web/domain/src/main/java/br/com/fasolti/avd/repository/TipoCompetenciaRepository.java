package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.TipoCompetencia;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;

import java.util.List;

public interface TipoCompetenciaRepository extends QueryDslCrudRepository<TipoCompetencia> {

	List<TipoCompetencia> findByIdPaiAndIdEmpresaOrderByNome(Long tipoCompetencia, Long idEmpresa);

	TipoCompetencia findByNomeAndIdEmpresa(String nome, Long idEmpresa);

	List<TipoCompetencia> findByIdPaiIsNullAndIdEmpresa(Long idEmpresa);

	List<TipoCompetencia> findBySituacaoAndIdEmpresa(Situacao situacao, Long idEmpresa);

}
