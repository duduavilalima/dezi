package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.ModeloGraduacao;

@Repository
public interface ModeloGraduacaoRepository extends QueryDslCustomerRepository<ModeloGraduacao, Long> {

}