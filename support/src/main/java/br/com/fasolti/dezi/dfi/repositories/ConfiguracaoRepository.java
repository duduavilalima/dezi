package br.com.fasolti.dezi.dfi.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.avd.domain.Configuracao;

@Repository
public interface ConfiguracaoRepository extends QueryDslCustomerRepository<Configuracao, Long>  {

}
