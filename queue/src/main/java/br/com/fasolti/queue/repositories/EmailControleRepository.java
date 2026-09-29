package br.com.fasolti.queue.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;

import br.com.fasolti.queue.entities.EmailControle;
import br.com.fasolti.queue.enuns.TipoEmailStatus;

@Repository
public interface EmailControleRepository extends QueryDslCustomRepository<EmailControle, Long> {

	EmailControle findByEmail(String email);

	List<EmailControle> findByStatus(TipoEmailStatus status);
}
