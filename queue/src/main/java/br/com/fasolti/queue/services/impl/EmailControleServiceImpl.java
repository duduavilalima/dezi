package br.com.fasolti.queue.services.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.SendFailedException;

import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.fasolti.queue.commons.EmailParametro;
import br.com.fasolti.queue.commons.impl.GenericServiceImpl;
import br.com.fasolti.queue.dto.EmailDTO;
import br.com.fasolti.queue.entities.EmailControle;
import br.com.fasolti.queue.entities.QEmailControle;
import br.com.fasolti.queue.enuns.TipoEmailStatus;
import br.com.fasolti.queue.rabbitmq.impl.EmailQueueSender;
import br.com.fasolti.queue.repositories.EmailControleRepository;
import br.com.fasolti.queue.services.EmailControleService;
import br.com.fasolti.queue.services.EmailService;
import br.com.fasolti.queue.services.ParametroService;
import br.com.fasolti.queue.util.Constant;

@Service
public class EmailControleServiceImpl extends GenericServiceImpl<EmailControle, Long, EmailControleRepository>
		implements EmailControleService {

	public EmailControleServiceImpl(EmailControleRepository repository) {
		super(repository);
	}

	private static final Logger log = LoggerFactory.getLogger(EmailControle.class);

	QEmailControle qEmailControle = QEmailControle.emailControle;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ParametroService parametroService;

	@Autowired
	private EmailQueueSender sendQueue;

	private EmailControle emailControle;

	@Override
	@Transactional(readOnly = true)
	public Optional<EmailControle> buscarPorEmail(String email) {
		return Optional.ofNullable(this.getRepository().findByEmail(email));
	}

	@Override
	@Transactional
	public Long atualizarStatus(Long id, TipoEmailStatus emailStatus) {
		return jpaQueryUpdate().where(qEmailControle.id.eq(id)).set(qEmailControle.status, emailStatus)
				.set(qEmailControle.dataAtualizacao, new Date()) // FIXME: deveria atualizar sozinho com @preUpdate
				.set(qEmailControle.atualizador, Constant.ATUALIZADOR).execute();
	}

	@Override
	@Transactional
	public Long atualizarStatus(Long id, TipoEmailStatus emailStatus, String msgErro) {
		return jpaQueryUpdate().where(qEmailControle.id.eq(id)).set(qEmailControle.status, emailStatus)
				.set(qEmailControle.msgErro, msgErro).set(qEmailControle.dataAtualizacao, new Date()) // FIXME: deveria
																										// atualizar
																										// sozinho com
																										// @preUpdate
				.set(qEmailControle.atualizador, Constant.ATUALIZADOR).execute();
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmailControle> consultarEmailsComErro() {
		return this.getRepository().findByStatus(TipoEmailStatus.ERRO);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmailControle> consultarPendentes() {
		return this.getRepository().findByStatus(TipoEmailStatus.PENDENTE);
	}

	@Override
	public List<EmailControle> consultarReenvio() {
		return this.getRepository().findByStatus(TipoEmailStatus.REENVIO);
	}

	@Override
	public void novoEmail(String emailControleID) {
		try {
			enviarEmail(emailControleID, TipoEmailStatus.ENVIADO);

		} catch (SendFailedException sfe) {
			sfe.printStackTrace();
		} catch (Exception e) {
			log.error("Novo Email a ser reenviado !");
			emailErroMsgAndUpdate(emailControleID, e.getMessage(), 0);
		}
	}

	@Override
	public void resolverEmail(String emailControleID) {
		try {
			enviarEmail(emailControleID, TipoEmailStatus.RESOLVIDO);
		} catch (Exception e) {
			log.error("Email a ser reslvido !");
			emailErroMsgAndUpdate(emailControleID, e.getMessage(), 0);
		}
	}

	@Override
	public void reEnviarEmail(String emailControleID, int tentativas) {
		try {
			enviarEmail(emailControleID, TipoEmailStatus.ENVIADO);
		} catch (SendFailedException sfe) {
			sfe.printStackTrace();
		} catch (Exception e) {
			log.error("Email a ser reenviado !");
			emailErroMsgAndUpdate(emailControleID, e.getMessage(), tentativas);
		}
	}

	private void emailErroMsgAndUpdate(String emailControleID, String erroMsg, int tentativas) {
		this.emailControle.setMsgErro(erroMsg);
		this.emailControle.setStatus(TipoEmailStatus.REENVIO);
		sendQueue.waitTimeSend(new EmailParametro(emailControleID, (tentativas + 1)));
		this.update(emailControle);
	}

	private void enviarEmail(String emailControleID, TipoEmailStatus status) throws InterruptedException, Exception {
		Optional<EmailControle> emailControleOp = getRepository().findById(Long.parseLong(emailControleID));

		if (emailControleOp.isPresent()) {
			this.emailControle = emailControleOp.get();

			if (this.emailControle.getTemplate() == null) {
				sendEmailDefault(emailControle);

			} else if (this.emailControle.getTemplate().equals(Constant.TIPO_EMAIL_ESQUECEU_SENHA_FUNCIONARIO)) {
				this.emailControle.setAssunto(parametroGetValor(this.emailControle.getAssunto(),
						Constant.EMAIL_SUBJECT_ESQUECEU_SENHA_FUNCIONARIO));
				sendEmail(emailControle, Constant.TEMPL_EMAILHTML_ESQUECEU_SENHA);

			} else if (this.emailControle.getTemplate().equals(Constant.TIPO_EMAIL_TROCA_SENHA_FUNCIONARIO)) {
				this.emailControle.setAssunto(
						parametroGetValor(this.emailControle.getAssunto(), Constant.EMAIL_SUBJECT_TROCA_SENHA_FUNCIONARIO));
				sendEmail(emailControle, Constant.TEMPL_EMAILHTML_TROCA_SENHA);

			} else if (this.emailControle.getTemplate().equals(Constant.TIPO_EMAIL_SUPORTE_FALECONOSCO)) {
				sendEmail(emailControle, Constant.TEMPL_EMAILHTML_FALECONOSCO);

			} else if (this.emailControle.getTemplate().equals(Constant.TIPO_EMAIL_SEND_CONTATO)) {
				sendEmail(emailControle, Constant.TEMPL_EMAILHTML_SENDCONTATO);

			} else if (this.emailControle.getTemplate().equals(Constant.TIPO_EMAIL_NOVO_CADASTRO_FUNCIONARIO)) {
				this.emailControle.setAssunto(parametroGetValor(this.emailControle.getAssunto(),
						Constant.EMAIL_SUBJECT_NOVO_CADASTRO_FUNCIONARIO));
				sendEmail(emailControle, Constant.TEMPL_EMAILHTML_NOVO_CADASTRO);

			} else if (this.emailControle.getTemplate().equals(Constant.TIPO_EMAIL_BEMVINDO_FUNCIONARIO)) {
				this.emailControle.setAssunto(
						parametroGetValor(this.emailControle.getAssunto(), Constant.EMAIL_SUBJECT_BEMVINDO_FUNCIONARIO));
				sendEmail(emailControle, Constant.TEMPL_EMAILHTML_BEMVINDO_FUNCIONARIO);

			} else if (this.emailControle.getTemplate().equals(Constant.TIPO_EMAIL_SEND_MSG_DEFAULT)) {
				sendEmail(emailControle, Constant.TEMPL_EMAILHTML_MESSAGE_DEFAULT);
			}

			this.emailControle.setStatus(status);
			this.update(emailControle);

		} else {
			throw new ServiceException(EMAIL_NAO_ENCONTRADO + " ID: " + emailControleID);
		}
	}

	private void sendEmailDefault(EmailControle emailControle) {
		EmailDTO email = new EmailDTO(this.emailControle.getEmail(), this.emailControle.getAssunto(),
				this.emailControle.getMensagem());
		try {
			emailService.send(email);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void sendEmail(EmailControle emailControle, String template) throws InterruptedException, Exception {
		emailService.sendEmailDezi(emailControle, template);
	}

	private String parametroGetValor(String assunto, String chave) {
		return chave.equals(assunto) ? parametroService.buscarPorChave(chave).get().getValor() : assunto;
	}

}