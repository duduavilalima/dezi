package br.com.fasolti.queue.services;

import java.util.List;
import java.util.Optional;

import br.com.fasolti.queue.commons.GenericService;
import br.com.fasolti.queue.entities.EmailControle;
import br.com.fasolti.queue.enuns.TipoEmailStatus;
import br.com.fasolti.queue.repositories.EmailControleRepository;

public interface EmailControleService extends GenericService<EmailControle, Long, EmailControleRepository> {

	Optional<EmailControle> buscarPorEmail(String email);

	Long atualizarStatus(Long id, TipoEmailStatus emailStatus);

	Long atualizarStatus(Long id, TipoEmailStatus emailStatus, String msgErro);

	List<EmailControle> consultarEmailsComErro();

	List<EmailControle> consultarReenvio();

	List<EmailControle> consultarPendentes();

	void resolverEmail(String emailControleID);

	void novoEmail(String emailControleID);

	void reEnviarEmail(String emailControleID, int tentativas);

}