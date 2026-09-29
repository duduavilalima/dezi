package br.com.fasolti.queue.services.impl;

import br.com.fasolti.queue.dto.EmailDTO;
import br.com.fasolti.queue.entities.EmailControle;
import br.com.fasolti.queue.services.EmailService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class EmailServiceImpl implements EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

	@Value("${default.email-from}")
	private String emailFrom;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private ObjectMapper objectMapper;

	@Async
	@Override
	public CompletableFuture<EmailDTO> send(EmailDTO email) {
		log.info(String.format("Received <%s>", email.getTo()));
		try {
			sendEmail(email.getTo(), email.getSubject(), email.getBody());
		} catch (MessagingException e) {
			e.printStackTrace();
			log.error("Erro ao enviar e-mail", e);
		}
		return CompletableFuture.completedFuture(email);
	}

	public String build(String title, String message) {
		Context context = new Context();
		context.setVariable("title", title);
		context.setVariable("message", message);
		return templateEngine.process("mailDefault", context);
	}

	public String build(String htmlTemplateEmail, Map<String, String> templateVariaveis) {
		Context context = new Context();
		templateVariaveis.forEach(context::setVariable);
		return templateEngine.process(htmlTemplateEmail, context);
	}

	private Map<String, String> jsonToMap(String json) throws IOException {
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		TypeReference<Map<String, String>> mapType = new TypeReference<>() {
		};

		return objectMapper.readValue(json, mapType);
	}

	@Override
	public CompletableFuture<EmailControle> sendEmailDezi(EmailControle email, String templateEmail) {
		try {
			sendEmail(email.getEmail(), email.getAssunto(), build(templateEmail, jsonToMap(email.getMensagem())));
		} catch (MessagingException | IOException e) {
			//FIXME Logar ou tratar corretamente
			e.printStackTrace();
		} catch (MailException e) {
			//FIXME Logar ou tratar corretamente
			e.printStackTrace();
			log.error("Erro nova exception : {}", e.getMessage());
		}

		return CompletableFuture.completedFuture(email);
	}

	@Async
	@Override
	public CompletableFuture<EmailDTO> sendEmailDefault(EmailDTO email, String title, String message) {
		log.info(String.format("Received <%s>", email.getTo()));
		try {
			sendEmail(email.getTo(), email.getSubject(), build(title, message));
		} catch (MessagingException e) {
			e.printStackTrace();
			log.error("Erro ao enviar e-mail", e);
		}
		return CompletableFuture.completedFuture(email);
	}

	@Async
	@Override
	public CompletableFuture<EmailDTO> sendEmailError(EmailDTO email, String title, Exception error) {
		log.info(String.format("Received <%s>", email.getTo()));
		try {
			sendEmail(email.getTo(), email.getSubject(), build(title, getStackTrace(error)));
		} catch (MessagingException e) {
			e.printStackTrace();
			log.error("Erro ao enviar e-mail", e);
		}
		return CompletableFuture.completedFuture(email);
	}

	@Override
	public final void sendEmail(List<String> mails, String subject, String msg) throws MessagingException {
		for (String mail : mails) {
			sendEmail(mail, subject, msg);
		}
	}

	@Override
	public final void sendEmail(String toAddress, String fromAddress, String subject, String msgBody)
			throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(fromAddress);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		helper.setText(msgBody, true);

		mailSender.send(message);
	}

	@Override
	public final void sendEmail(String toAddress, String subject, String msgBody) throws MessagingException {
		sendEmail(toAddress, emailFrom, subject, msgBody);
	}

	@Override
	public final void sendEmail(String toAddress, String fromAddress, String subject, String msgBody, File... files)
			throws MessagingException {
		try {
			MimeMessage message = mailSender.createMimeMessage();

			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(fromAddress);
			helper.setTo(toAddress);
			helper.setSubject(subject);

			helper.setText(msgBody, true);

			for (File file : files) {
				FileSystemResource res = new FileSystemResource(file);
				helper.addInline(file.getName(), res);
			}

			mailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
			log.error("Erro envio de email", e);
			throw new MessagingException(e.getMessage());
		}
	}

	@Override
	public final void sendEmail(String toAddress, String subject, String msgBody, File... files)
			throws MessagingException {
		sendEmail(toAddress, emailFrom, subject, msgBody, files);
	}

	@Override
	public void sendEmailErrorDefault(String subject, String title, Exception error) {
		String[] emails = { emailFrom };
		sendEmailError(emails, subject, title, error);
	}

	@Async
	@Override
	public void sendEmailError(String[] emails, String subject, String title, Exception error) {
		for (String email : emails) {

			log.info(String.format("Received <%s>", email));
			try {
				sendEmail(email, subject, build(title, getStackTrace(error)));
			} catch (MessagingException e) {
				e.printStackTrace();
				log.error("Erro ao enviar e-mail", e);
			}
		}
	}

	private String getStackTrace(final Throwable throwable) {
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		throwable.printStackTrace(pw);
		return sw.getBuffer().toString();
	}
}
