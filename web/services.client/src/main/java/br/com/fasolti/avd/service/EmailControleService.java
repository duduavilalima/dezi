package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.mobile.FaleConoscoMobileTO;
import br.com.fasolti.avd.types.TipoEmailStatus;

import java.util.List;

public interface EmailControleService extends AvdService<EmailControleTO, EmailControleTO> {

	void sendEmailforgotPassword(Long funcionarioID, String email, String nomeFuncionario, String senha, String empresaHost, Long empresaID);

	void sendEmailTrocaSenha(Long funcionarioId, String email, String nomeFuncionario, String senha, String empresaHost, Long empresaID);

	void sendEmailFaleConosco(FaleConoscoMobileTO faleConoscoMobile);

	void sendEmailEnviarMensagem(ContatoTO contato);

	void sendEmailNovoCadastro(FuncionarioTO funcionario, String senha);

	void sendEmailWelcomeMessage(EmailBoasVindasTO emailBoasVindas);

	void sendEmailException(EmailControleTO emailControle);

	void sendEmailNotificateBug(String email, String subject, String message);

	void sendEmailEnvioDeMensagensDeAcompanhamento(EnvioMensagemAcompanhamentoTO envioMensagemAcompanhamento);

	void sendEmailVerificaMensagem(MensageriaTO mensageria, Boolean avaliacao);

	void sendEmail(String email, String subject, String message);

	List<EmailControleTO> consultarEmailsComErro();

	List<EmailControleTO> consultarEmailsPendentes();

	void resolverEmail();

	void atualizarStatusEmail(EmailControleTO emailControle);
}
