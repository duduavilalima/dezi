package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Conceito;
import br.com.fasolti.avd.repository.ConceitoRepository;
import br.com.fasolti.avd.service.ConceitoService;
import br.com.fasolti.avd.transfer.ConceitoTO;
import br.com.fasolti.service.util.BaseAvdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConceitoServiceImpl
	extends BaseAvdService<Conceito, ConceitoTO, ConceitoTO>
	implements ConceitoService{

	@Autowired
	private ConceitoRepository conceitoRepository;

	@Autowired
	public ConceitoServiceImpl(ConceitoRepository repository) {
		super(repository);
	}

	@Override
	public List<ConceitoTO> findAllConceitosEmpresaLogada() {
		return map(conceitoRepository.findByEmpresaId(getLoggedUser().getEmpresa().getId()));
	}
}
