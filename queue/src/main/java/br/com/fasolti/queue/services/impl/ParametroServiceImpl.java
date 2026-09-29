package br.com.fasolti.queue.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.fasolti.queue.entities.Parametro;
import br.com.fasolti.queue.entities.QParametro;
import br.com.fasolti.queue.repositories.ParametroRepository;
import br.com.fasolti.queue.services.ParametroService;

@Service
public class ParametroServiceImpl implements ParametroService {

	@Autowired
	private ParametroRepository parametroRepository;

	QParametro qParametro = QParametro.parametro;

	@Override
	public Optional<Parametro> buscarPorChave(String chave) {
		return parametroRepository.findOne(qParametro.chave.eq(chave));
	}

	@Override
	public List<Parametro> buscarParametros(String... chaves) {
		return (List<Parametro>) parametroRepository.findAll(qParametro.chave.in(chaves));
	}

}