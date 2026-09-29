package br.com.fasolti.queue.commons.impl;

import java.io.Serializable;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;

import br.com.fasolti.queue.commons.GenericService;
import br.com.fasolti.queue.commons.GenericsInfo;
import br.com.fasolti.queue.commons.GenericsUtils;
import br.com.fasolti.queue.commons.Util;
import br.com.fasolti.queue.repositories.QueryDslCustomRepository;

public abstract class GenericServiceImpl<E, ID extends Serializable, R extends QueryDslCustomRepository<E, ID>>
		implements GenericService<E, ID, R> {

	private Logger log = LoggerFactory.getLogger(GenericServiceImpl.class);

	private static final String NAO_EXISTE_REGISTRO_COM_O_ID_INFORMADO = "Não existe registro com o ID informado.";
	private static final String ID_NAO_INFORMADO = "Id não informado.";
	private static final String ENTIDADE_NAO_INFORMADA = "Entidade não informada.";
	protected static final String EMAIL_NAO_ENCONTRADO = "E-mail não encontrada.";

	private R repository;

	@SuppressWarnings("unused")
	@Autowired
	private EntityManager entityManager;

	@Autowired
	private JPAQueryFactory jPAQueryFactory;

	private final EntityPath<E> entityPath;

	private Class<E> entityClass;

	public GenericServiceImpl(R repository) {
		this.repository = repository;
		GenericsInfo genericsInfo = GenericsUtils.getGenericsInfo(this);
		this.entityClass = genericsInfo.getType(0);
		this.entityPath = new PathBuilder<>(this.entityClass, Util.varName(this.entityClass));
	}

	@Override
	public Class<E> getEntityClass() {
		return entityClass;
	}

	@Override
	public R getRepository() {
		return repository;
	}

	protected JPAQueryFactory getJPAQueryFactory() {
		return jPAQueryFactory;
	}

	protected UpdateClause<?> jpaQueryUpdate() {
		return getJPAQueryFactory().update(this.entityPath);
	}

	protected DeleteClause<?> jpaQueryDelete() {
		return getJPAQueryFactory().delete(this.entityPath);
	}

	@Override
	@Transactional(readOnly = true)
	public E getById(ID id) {
		try {
			if (id == null) {
				return null;
			}
			log.info("Obter {} pelo id {}.", entityClass.getName(), id);
			Optional<E> optional = this.repository.findById(id);

			if (optional.isEmpty()) {
				throw new ServiceException(ENTIDADE_NAO_INFORMADA);
			}
			return optional.get();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	@Override
	@Transactional
	public E save(E entity) throws ServiceException {
		if (entity == null) {
			return null;
		}
		entity = this.repository.save(entity);
		return entity;
	}

	@Override
	@Transactional
	public E update(E entity) throws ServiceException {
		if (entity == null) {
			return null;
		}
		entity = this.repository.save(entity);
		return entity;
	}

	@Override
	@Transactional
	public void delete(E entity) throws ServiceException {
		if (entity == null) {
			throw new ServiceException(ENTIDADE_NAO_INFORMADA);
		}
		this.repository.delete(entity);
	}

	@Override
	@Transactional
	public void delete(ID id) throws ServiceException {
		if (id == null) {
			throw new ServiceException(ID_NAO_INFORMADO);
		}
		log.info("Deletando entidade id {}", id);
		Optional<E> obj = this.repository.findById(id);
		if (obj.isEmpty()) {
			throw new ServiceException(NAO_EXISTE_REGISTRO_COM_O_ID_INFORMADO);
		}
		this.repository.deleteById(id);
	}
}
