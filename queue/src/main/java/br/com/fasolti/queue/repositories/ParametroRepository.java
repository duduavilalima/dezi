package br.com.fasolti.queue.repositories;

import org.springframework.stereotype.Repository;

import br.com.fasolti.queue.entities.Parametro;

@Repository
public interface ParametroRepository extends QueryDslCustomRepository<Parametro, Long> {

}
