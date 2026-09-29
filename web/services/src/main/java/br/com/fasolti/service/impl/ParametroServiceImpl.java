package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Parametro;
import br.com.fasolti.avd.repository.ParametroRepository;
import br.com.fasolti.avd.service.ParametroService;
import br.com.fasolti.avd.transfer.ParametroTO;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParametroServiceImpl implements ParametroService {

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private Mapper mapper;

	public ParametroServiceImpl() {
	}

	@Override
	public ParametroTO buscaPorChave(String chave) {
		return mapper.map(parametroRepository.findOne(chave), ParametroTO.class);
	}

	@Override
	public ParametroTO save(ParametroTO parametroTO){
		Parametro entity = parametroRepository.save(mapper.map(parametroTO, Parametro.class));
		return mapper.map(entity, ParametroTO.class);
	}

	@Override
	public List<ParametroTO> buscaTodos() {
		List<ParametroTO> listTO = new ArrayList<>();
		Iterable<Parametro> list = parametroRepository.findAll();

		for(Parametro parametro : list){
			ParametroTO parametroTO = mapper.map(parametro, ParametroTO.class);
			listTO.add(parametroTO);
		}

		return listTO;
	}
}
