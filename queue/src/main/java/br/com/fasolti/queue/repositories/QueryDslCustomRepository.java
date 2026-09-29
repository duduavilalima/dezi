package br.com.fasolti.queue.repositories;

import java.io.Serializable;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface QueryDslCustomRepository<E, ID extends Serializable>
		extends CrudRepository<E, ID>, QuerydslPredicateExecutor<E> {
}
