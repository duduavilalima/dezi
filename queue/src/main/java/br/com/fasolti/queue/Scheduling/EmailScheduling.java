package br.com.fasolti.queue.Scheduling;

import org.springframework.stereotype.Component;

@Component
public class EmailScheduling {

	/*
	 * private static final Logger log =
	 * LoggerFactory.getLogger(EmailControle.class);
	 * 
	 * @Autowired private EmailControleService service;
	 * 
	 * private static final SimpleDateFormat dateFormat = new
	 * SimpleDateFormat("HH:mm:ss");
	 * 
	 * @Autowired private RabbitMQSender send;
	 */

	/*
	 * @Scheduled(fixedRate = 30000) // cada 30s 30000 public void
	 * resolverEmailComErro() { log.info("Resolvendo E-mail com erro, agora {}",
	 * dateFormat.format(new Date())); service.consultarReenvio().forEach(email -> {
	 * service.resolverEmail(email.getId().toString()); });
	 * log.info(dateFormat.format(new Date())); EmailParametro em = new
	 * EmailParametro(); em.setId(ran()); em.setTentativa(1); send.send(em);
	 * 
	 * }
	 */

	/*
	 * @Scheduled(fixedRate = 30000) // cada 1h 3600000 public void
	 * enviarEmailPendentes() { //TODO: caso dezi não consiga enviar id email para
	 * fila do rabbitMQ, log.info("Verificando E-mails pendentes, agora {}",
	 * dateFormat.format(new Date())); service.consultarReenvio().forEach(email -> {
	 * service.resolverEmail(email.getId().toString()); }); }
	 */

	/*
	 * public Long ran() { long leftLimit = 1L; long rightLimit = 10L; return
	 * leftLimit + (long) (Math.random() * (rightLimit - leftLimit)); }
	 */

}
