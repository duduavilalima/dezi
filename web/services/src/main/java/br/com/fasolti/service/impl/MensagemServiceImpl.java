package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Mensagem;
import br.com.fasolti.avd.domain.Parametro;
import br.com.fasolti.avd.repository.MensagemRepository;
import br.com.fasolti.avd.repository.ParametroRepository;
import br.com.fasolti.avd.service.EmailControleService;
import br.com.fasolti.avd.service.MensagemService;
import br.com.fasolti.avd.transfer.EmailBoasVindasTO;
import br.com.fasolti.avd.transfer.MensagemTO;
import br.com.fasolti.service.util.BaseAvdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MensagemServiceImpl extends BaseAvdService<Mensagem, MensagemTO, MensagemTO> implements MensagemService {

	private final String NOTIFICACAO_EMAIL_FUNCIONARIO_BEMVINDO = "notificacao.email.funcionario.bemvindo";

	@Autowired
	public MensagemServiceImpl(MensagemRepository repository) {
		super(repository);
	}

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private EmailControleService emailControleService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendWelcomeMessage(EmailBoasVindasTO emailBoasVindasTO) {
		// Obter dos parâmetros se será enviado o email.
		Parametro parametro = parametroRepository.findOne(NOTIFICACAO_EMAIL_FUNCIONARIO_BEMVINDO);
		Boolean sendMail = Boolean.parseBoolean(parametro.getValor());
		if (sendMail) {
			emailControleService.sendEmailWelcomeMessage(emailBoasVindasTO);
		}
	}
}
