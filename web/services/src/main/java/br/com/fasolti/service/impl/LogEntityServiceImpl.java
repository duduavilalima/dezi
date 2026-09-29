package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.LogEntity;
import br.com.fasolti.avd.repository.LogEntityRepository;
import br.com.fasolti.avd.service.LogEntityService;
import br.com.fasolti.avd.transfer.LogEntityTO;
import br.com.fasolti.commons.services.BaseCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//TODO Refatorar o commons para usar composição, para facilitar os serviços de entidades sem ID
@Service
public class LogEntityServiceImpl
	extends BaseCrudService<LogEntity, LogEntityTO>
	implements LogEntityService
{
	@Autowired
	public LogEntityServiceImpl(LogEntityRepository repository) {
		super(repository);
	}
}
