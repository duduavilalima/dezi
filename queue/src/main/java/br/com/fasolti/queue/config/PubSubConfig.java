package br.com.fasolti.queue.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("pubsub")
public class PubSubConfig {
	private String exchangeName;

	private String queueName;

	private String delayedExchangeName;

	private String routingKey;

	private String resendQueueName;

	private String resendRoutingKey;

	private Integer sendDelay;

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getDelayedExchangeName() {
		return delayedExchangeName;
	}

	public void setDelayedExchangeName(String delayedExchangeName) {
		this.delayedExchangeName = delayedExchangeName;
	}

	public String getRoutingKey() {
		return routingKey;
	}

	public void setRoutingKey(String routingKey) {
		this.routingKey = routingKey;
	}

	public String getResendQueueName() {
		return resendQueueName;
	}

	public void setResendQueueName(String resendQueueName) {
		this.resendQueueName = resendQueueName;
	}

	public String getResendRoutingKey() {
		return resendRoutingKey;
	}

	public void setResendRoutingKey(String resendRoutingKey) {
		this.resendRoutingKey = resendRoutingKey;
	}

	public Integer getSendDelay() {
		return sendDelay;
	}

	public void setSendDelay(Integer sendDelay) {
		this.sendDelay = sendDelay;
	}
}
