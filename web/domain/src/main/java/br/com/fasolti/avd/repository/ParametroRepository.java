package br.com.fasolti.avd.repository;

import br.com.fasolti.avd.domain.Parametro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametroRepository extends CrudRepository<Parametro, String> {
}
