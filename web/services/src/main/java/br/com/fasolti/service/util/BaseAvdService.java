package br.com.fasolti.service.util;

import br.com.fasolti.avd.domain.BaseEntity;
import br.com.fasolti.avd.service.AvdService;
import br.com.fasolti.avd.service.ConfiguracaoService;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.transfer.util.SituationalObject;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.SearchObject;
import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public abstract class BaseAvdService<E extends BaseEntity, T extends TransferObject, S extends SearchObject>
	extends BaseSearchService<E, T, S>
	implements AvdService<T, S> {

	@Autowired
	protected ConfiguracaoService configuracaoService;

	@Autowired
	protected SecurityService<UserTO> securityService;

	public BaseAvdService(QueryDslCrudRepository<E> repository) {
		super(repository);
	}

	protected Situacao situacaoDefault() {
		return Situacao.ATIVA;
	}

	@Override
	protected void doBeforeSave(E entity, T object) throws ServiceException {
		if (entity instanceof SituationalObject) {
			((SituationalObject) entity).setSituacao(situacaoDefault());
		}
	}

	@Override
	@Transactional
	public T activate(T object) throws ServiceException {
		if (!(object instanceof SituationalObject)) {
			throw new ServiceException("Objeto não pode ser ativado");
		}

		((SituationalObject) object).setSituacao(Situacao.ATIVA);

		return update(object, object.getId());
	}

	@Override
	@Transactional
	public T inactivate(T object) throws ServiceException {
		if (!(object instanceof SituationalObject)) {
			throw new ServiceException("Objeto não pode ser desativado");
		}

		((SituationalObject) object).setSituacao(Situacao.INATIVA);

		return update(object, object.getId());
	}

	@Override
	public UserTO getLoggedUser() {
		return securityService.getUser();
	}
}
