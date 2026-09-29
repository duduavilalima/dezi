package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.mobile.FaleConosco;
import br.com.fasolti.avd.repository.FaleConoscoRepository;
import br.com.fasolti.avd.service.EmailControleService;
import br.com.fasolti.avd.service.FaleConoscoService;
import br.com.fasolti.avd.transfer.mobile.FaleConoscoMobileTO;
import br.com.fasolti.service.util.BaseDeziService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FaleConoscoServiceImpl extends BaseDeziService<FaleConosco, FaleConoscoMobileTO> implements FaleConoscoService {

	@Autowired
	private EmailControleService emailControleService;

	@Autowired
	private FaleConoscoRepository faleConoscoRepository;

	@Autowired
	public FaleConoscoServiceImpl(FaleConoscoRepository faleConoscoRepository) {
		super(faleConoscoRepository);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void salvarFaleConosco(FaleConoscoMobileTO faleConoscoMobileTO) {
		emailControleService.sendEmailFaleConosco(faleConoscoMobileTO);
		return;
	}
}
