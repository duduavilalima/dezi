package br.com.fasolti.queue.commons;

import java.io.Serializable;

import org.hibernate.service.spi.ServiceException;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface GenericService<E, ID extends Serializable, R extends QuerydslPredicateExecutor<E>> {

	Class<E> getEntityClass();

	R getRepository();

	E getById(ID id);

	E save(E entity) throws ServiceException;

	E update(E entity) throws ServiceException;

	void delete(E entity) throws ServiceException;

	void delete(ID id) throws ServiceException;

}
