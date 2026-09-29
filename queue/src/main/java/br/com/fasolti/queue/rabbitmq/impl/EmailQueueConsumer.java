package br.com.fasolti.queue.rabbitmq.impl;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import br.com.fasolti.queue.commons.EmailParametro;
import br.com.fasolti.queue.enuns.TipoEmailStatus;
import br.com.fasolti.queue.services.EmailControleService;

@Component
public class EmailQueueConsumer {

	@Autowired
	private EmailControleService emailControleService;

	@RabbitListener(queues = "${pubsub.queue-name}")
	public void receive(@Payload String emailControleId) throws ListenerExecutionFailedException {
		emailControleService.novoEmail(emailControleId);
	}

	@RabbitListener(queues = "${pubsub.resend-queue-name}")
	public void receive(final EmailParametro em) throws ListenerExecutionFailedException {
		if (em.getTentativa() <= 3) {
			emailControleService.reEnviarEmail(em.getId(), em.getTentativa());
		} else {
			emailControleService.atualizarStatus(Long.parseLong(em.getId()), TipoEmailStatus.ERRO);
		}
	}
}
