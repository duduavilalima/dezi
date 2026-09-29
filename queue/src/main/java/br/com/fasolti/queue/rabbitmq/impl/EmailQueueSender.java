package br.com.fasolti.queue.rabbitmq.impl;

import br.com.fasolti.queue.commons.EmailParametro;
import br.com.fasolti.queue.config.PubSubConfig;
import br.com.fasolti.queue.rabbitmq.QueueSender;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailQueueSender implements QueueSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private CustomExchange delayExchange;

	@Autowired
	private PubSubConfig pubSubConfig;

	@Override
	public void send(EmailParametro emailParametro) {
		rabbitTemplate.convertAndSend(delayExchange.getName(), pubSubConfig.getResendRoutingKey(), emailParametro);
	}

	@Override
	public void waitTimeSend(EmailParametro emailParametro) {
		rabbitTemplate.convertAndSend(delayExchange.getName(), pubSubConfig.getResendRoutingKey(), emailParametro, m -> {
			m.getMessageProperties().getHeaders().put("x-delay", calcularWaitTime(emailParametro.getTentativa()));
			return m;
		});
	}

	private long calcularWaitTime(int tentativas) {
		if (tentativas > 0) {
			return pubSubConfig.getSendDelay() * tentativas;
		}
		return 0L;
	}

}
