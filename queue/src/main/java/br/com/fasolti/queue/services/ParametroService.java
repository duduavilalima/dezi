package br.com.fasolti.queue.services;

import java.util.List;
import java.util.Optional;

import br.com.fasolti.queue.entities.Parametro;

public interface ParametroService {

	Optional<Parametro> buscarPorChave(String chave);

	List<Parametro> buscarParametros(String... chaves);

}