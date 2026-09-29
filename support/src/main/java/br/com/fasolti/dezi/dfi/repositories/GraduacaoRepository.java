package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.Graduacao;

@Repository
public interface GraduacaoRepository extends QueryDslCustomerRepository<Graduacao, Long> {

}