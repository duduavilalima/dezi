package br.com.fasolti.queue.services;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;

import br.com.fasolti.queue.dto.EmailDTO;
import br.com.fasolti.queue.entities.EmailControle;

public interface EmailService {

	void sendEmail(List<String> mails, String subject, String msg) throws MessagingException;

	void sendEmail(String toAddress, String fromAddress, String subject, String msgBody) throws MessagingException;

	void sendEmail(String toAddress, String subject, String msgBody) throws MessagingException;

	void sendEmail(String toAddress, String fromAddress, String subject, String msgBody, File... files)
			throws MessagingException;

	void sendEmail(String toAddress, String subject, String msgBody, File... files) throws MessagingException;

	CompletableFuture<EmailDTO> send(EmailDTO email) throws InterruptedException;

	CompletableFuture<EmailDTO> sendEmailDefault(EmailDTO email, String title, String message)
			throws InterruptedException;

	CompletableFuture<EmailDTO> sendEmailError(EmailDTO email, String title, Exception error)
			throws InterruptedException;

	void sendEmailError(String[] emails, String subject, String title, Exception error) throws InterruptedException;

	void sendEmailErrorDefault(String subject, String title, Exception error) throws InterruptedException;

	CompletableFuture<EmailControle> sendEmailDezi(EmailControle email, String templateEmail)
			throws InterruptedException;

}
