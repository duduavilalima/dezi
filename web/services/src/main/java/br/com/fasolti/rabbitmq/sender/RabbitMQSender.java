package br.com.fasolti.rabbitmq.sender;

import org.springframework.amqp.AmqpConnectException;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQSender {

	@Autowired
	private DirectExchange direct;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${dezi.routing.key}")
	private String routingkey;

	public void sendRMQ(String msg) throws AmqpConnectException {
		rabbitTemplate.convertAndSend(direct.getName(),routingkey,msg);
	}

}
