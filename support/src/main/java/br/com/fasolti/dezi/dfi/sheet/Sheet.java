package br.com.fasolti.dezi.dfi.sheet;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Value;

public abstract class Sheet implements GenericSheet {

	/*
	 * @SuppressWarnings("unused")
	 * 
	 * @Autowired private EntityManager entityManager;
	 * 
	 * @Autowired private JPAQueryFactory jPAQueryFactory;
	 */

	protected AtomicInteger at = new AtomicInteger(0);

	@Value("${dezi.createdby.name}")
	protected String createdByName;

}
