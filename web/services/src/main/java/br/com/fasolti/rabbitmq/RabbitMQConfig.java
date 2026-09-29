package br.com.fasolti.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

	@Value("${dezi.queue.name}")
	private String queueName;

	@Value("${dezi.exchange.name}")
	private String exchange;

	@Value("${dezi.routing.key}")
	private String routingkey;

	@Value("${spring.rabbitmq.host}")
	private String host;

	@Value("${spring.rabbitmq.username}")
	private String userMQ;

	@Value("${spring.rabbitmq.password}")
	private String passwordMq;

	@Bean
	public Queue queue() {
		return new Queue(queueName, true);
	}

	@Bean
	public DirectExchange direct() {
		return new DirectExchange(exchange);
	}

	@Bean
	public Binding binding(DirectExchange direct, Queue Queue) {
		return BindingBuilder.bind(Queue)
			.to(direct)
			.with(routingkey);
	}

	@Bean
	public ConnectionFactory rabbitMQConnectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
		connectionFactory.setUsername(userMQ);
		connectionFactory.setPassword(passwordMq);
		connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);
		connectionFactory.setChannelCacheSize(25);
		return connectionFactory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate() {
		return new RabbitTemplate(rabbitMQConnectionFactory());
	}

}
