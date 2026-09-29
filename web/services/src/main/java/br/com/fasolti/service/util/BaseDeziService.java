package br.com.fasolti.service.util;

import br.com.fasolti.avd.domain.BaseEntity;
import br.com.fasolti.avd.service.ConfiguracaoService;
import br.com.fasolti.avd.service.DeziService;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.transfer.util.SituationalObject;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.commons.client.transfer.TransferObject;
import br.com.fasolti.commons.domain.QueryDslCrudRepository;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.security.user.SecurityService;
import br.com.fasolti.commons.services.BaseCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;

@Transactional(readOnly = true)
public abstract class BaseDeziService<E extends BaseEntity, T extends TransferObject>
	extends BaseCrudService<E, T>
	implements DeziService<T> {

	@Autowired
	protected ConfiguracaoService configuracaoService;

	@Autowired
	protected SecurityService<UserTO> securityService;

	public BaseDeziService(QueryDslCrudRepository<E> repository) {
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
	public final T activate(@Nonnull Long id) throws ServiceException {
		E entity = repository.findOne(id);

		if (!(entity instanceof SituationalObject)) {
			throw new ServiceException("Objeto não pode ser ativado");
		}

		((SituationalObject) entity).setSituacao(Situacao.ATIVA);

		return map(repository.save(entity));
	}

	@Override
	@Transactional
	public final T deactivate(@Nonnull Long id) throws ServiceException {
		E entity = repository.findOne(id);

		if (!(entity instanceof SituationalObject)) {
			throw new ServiceException("Objeto não pode ser desativado");
		}

		((SituationalObject) entity).setSituacao(Situacao.INATIVA);

		return map(repository.save(entity));
	}

	@Override
	public UserTO getLoggedUser() {
		return securityService.getUser();
	}
}
