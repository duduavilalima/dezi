package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.ContratanteFasolti;
import br.com.fasolti.avd.repository.ContratanteFasoltiRepository;
import br.com.fasolti.avd.service.ContratanteFasoltiService;
import br.com.fasolti.avd.transfer.ContratanteFasoltiTO;
import br.com.fasolti.service.util.BaseAvdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContratanteFasoltiServiceImpl
	extends BaseAvdService<ContratanteFasolti, ContratanteFasoltiTO, ContratanteFasoltiTO>
	implements ContratanteFasoltiService {

	@Autowired
	private ContratanteFasoltiRepository contratanteFasoltiRepository;

	@Autowired
	public ContratanteFasoltiServiceImpl(ContratanteFasoltiRepository contratanteFasoltiRepository) {
		super(contratanteFasoltiRepository);
	}
}
