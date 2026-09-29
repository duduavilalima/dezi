package br.com.fasolti.queue.rabbitmq.config;

import br.com.fasolti.queue.config.PubSubConfig;
import org.springframework.amqp.core.*;
import org.springframework.amqp.core.Binding.DestinationType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class QueueFactory {
	@Autowired
	private PubSubConfig pubSubConfig;

	@Bean
	public Queue defaultQueue() {
		return QueueBuilder.durable(pubSubConfig.getQueueName()).build();
	}

	@Bean
	public Queue resendQueue() {
		return QueueBuilder.durable(pubSubConfig.getResendQueueName()).build();
	}

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(pubSubConfig.getExchangeName());
	}

	@Bean
	public CustomExchange delayedExchange() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-delayed-type", "direct");

		return new CustomExchange(
			pubSubConfig.getDelayedExchangeName(),
			"x-delayed-message",
			true,
			false,
			args
		);
	}

	@Bean
	public Binding defaultBinding(DirectExchange directExchange, Queue defaultQueue) {
		return BindingBuilder.bind(defaultQueue).to(directExchange).with(pubSubConfig.getRoutingKey());
	}

	@Bean
	public Binding resendBinding(CustomExchange delayedExchange, Queue resendQueue) {
		return new Binding(
			resendQueue.getName(),
			DestinationType.QUEUE,
			delayedExchange.getName(),
			pubSubConfig.getResendRoutingKey(),
			null
		);
	}

	@Bean
	public RabbitAdmin admin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}
}
