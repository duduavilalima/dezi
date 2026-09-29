package br.com.fasolti.queue.rabbitmq;

import br.com.fasolti.queue.commons.EmailParametro;

public interface QueueSender {

	void send(EmailParametro emailParametro);

	void waitTimeSend(EmailParametro emailParametro);

}
