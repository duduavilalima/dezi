package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.EmailControle;
import br.com.fasolti.avd.domain.Funcionario;
import br.com.fasolti.avd.domain.QEmailControle;
import br.com.fasolti.avd.repository.EmailControleRepository;
import br.com.fasolti.avd.repository.FuncionarioRepository;
import br.com.fasolti.avd.repository.ParametroRepository;
import br.com.fasolti.avd.service.ConfiguracaoService;
import br.com.fasolti.avd.service.EmailControleService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.mobile.FaleConoscoMobileTO;
import br.com.fasolti.avd.transfer.simple.EmpresaSimpleTO;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.TipoEmailStatus;
import br.com.fasolti.commons.PasswordUtils;
import br.com.fasolti.rabbitmq.sender.RabbitMQSender;
import br.com.fasolti.service.util.BaseAvdService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class EmailControleServiceImpl extends BaseAvdService<EmailControle, EmailControleTO, EmailControleTO> implements EmailControleService {

	@Autowired(required = false)
	private Logger logger;

	private final String TIPO_EMAIL_BEMVINDO_FUNCIONARIO = "1";
	private final String TIPO_EMAIL_ESQUECEU_SENHA_FUNCIONARIO = "2";
	private final String TIPO_EMAIL_TROCA_SENHA_FUNCIONARIO = "3";
	private final String TIPO_EMAIL_NOVO_CADASTRO_FUNCIONARIO = "4";
	private final String TIPO_EMAIL_SEND_MSG_DEFAULT = "5";
	private final String TIPO_EMAIL_SUPORTE_FALECONOSCO = "6";
	private final String TIPO_EMAIL_SEND_CONTATO = "7";
	private final String TIPO_EMAIL_SEND_MSG_NOVA_AVALIACAO = "8";

	/*private final String EMAIL_TEMPL_BEMVINDO_FUNCIONARIO = "mail.template.bemvindo.funcionario";
	private final String EMAIL_TEMPL_ESQUECEU_SENHA_FUNCIONARIO = "mail.template.esqueceu.senha.funcionario";
	private final String EMAIL_TEMPL_TROCA_SENHA_FUNCIONARIO = "mail.template.troca.senha.funcionario";
	private final String EMAIL_TEMPL_NOVO_CADASTRO_FUNCIONARIO = "mail.template.novo.cadastro.funcionario";
	private final String EMAIL_TEMPL_SEND_MSG_DEFAULT = "mail.template.send.message.default";
	private final String EMAIL_TEMPL_SUPORTE_FALECONOSCO =  "mail.template.suporte.faleconosco";
	private final String EMAIL_TEMPL_SEND_CONTATO =  "mail.send.contato";
	private final String EMAIL_TEMPL_SEND_MSG_NOVA_AVALIACAO =  "mail.send.message.nova.avaliacao";*/

	private final String EMAIL_SUBJECT_BEMVINDO_FUNCIONARIO = "mail.subject.bemvindo.funcionario";
	private final String EMAIL_SUBJECT_ESQUECEU_SENHA_FUNCIONARIO = "mail.subject.esqueceu.senha.funcionario";
	private final String EMAIL_SUBJECT_NOVO_CADASTRO_FUNCIONARIO = "mail.subject.novo.cadastro.funcionario";
	private final String EMAIL_SUBJECT_TROCA_SENHA_FUNCIONARIO = "mail.subject.troca.senha.funcionario";

	private final String EMAIL_CONTATO_SUPORTE = "email.contato.suporte";

	private final String ERRO_EMAIL = "Erro ao enviar email para a fila RabbitMQ !";

	@Autowired
	private EmailControleRepository emailControleRepository;

	@Autowired
	private RabbitMQSender senderMQ;

	private final QEmailControle qEmailControle = QEmailControle.emailControle;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private PasswordUtils passwordUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ConfiguracaoService configuracaoService;


	@Autowired
	public EmailControleServiceImpl(EmailControleRepository repository) { super(repository); }

	private String mapToJson(Map<String,String> templateValues) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(templateValues);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void salvarEEnviarEmail(EmailControleTO emailControleTO) {
		logger.info(String.format("Enviando e-mail para: %s ", emailControleTO.getEmail()));
		try{
			emailControleTO.setDataCriacao(new Date());
			senderMQ.sendRMQ(this.save(emailControleTO).getId().toString());
		}catch (Exception e) { //TODO: Fazer exceptions mais específicas
			emailControleTO.setStatus(TipoEmailStatus.REENVIO);
			emailControleTO.setDataCriacao(new Date());
			logger.error(ERRO_EMAIL, e);
			emailControleTO.setMsgErro(ERRO_EMAIL);
			this.save(emailControleTO);
		}
	}

	private String parametroGetValor(String chave) {
		return parametroRepository.findOne(chave).getValor();
	}

	private String dateFormat(String format, Date date) {
		SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
		return dateFormatter.format(date);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendEmailforgotPassword(Long funcionarioID, String email, String nomeFuncionario, String senha, String empresaHost, Long empresaID) {
		Map<String,String> templateValues = new HashMap<>();
		templateValues.put("nomeusuario", nomeFuncionario);
		templateValues.put("senha", senha);
		templateValues.put("link", "http://"+empresaHost+"/index.html#/login/"+empresaID);
		EmailControleTO emailControleTO = EmailControleTO.builder(funcionarioID, email, TIPO_EMAIL_ESQUECEU_SENHA_FUNCIONARIO, TipoEmailStatus.PENDENTE)
			.setMensagem(mapToJson(templateValues))
			.setAssunto(EMAIL_SUBJECT_ESQUECEU_SENHA_FUNCIONARIO)
			.setCriador(nomeFuncionario)
			.build();
		salvarEEnviarEmail(emailControleTO);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendEmailTrocaSenha(Long funcionarioID, String email, String nomeFuncionario, String senha,  String empresaHost, Long empresaID) {
//		Map<String,String> templateValues = new HashMap<>();
//		templateValues.put("nomeusuario", nomeFuncionario);
//		templateValues.put("senha", senha);
//		templateValues.put("link", "http://"+empresaHost+"/index.html#/login/"+empresaID);
//		salvarEEnviarEmail(EmailControleTO.builder(funcionarioID, email, TIPO_EMAIL_TROCA_SENHA_FUNCIONARIO, TipoEmailStatus.PENDENTE)
//			.setMensagem(mapToJson(templateValues))
//			.setAssunto(EMAIL_SUBJECT_TROCA_SENHA_FUNCIONARIO)
//			.setCriador(nomeFuncionario)
//			.build());
		System.out.println("Troca de senha.");
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendEmailFaleConosco(FaleConoscoMobileTO to) {
		Map<String,String> templateValues = new HashMap<>();
		templateValues.put("MENSAGEM", to.getMensagem());
		templateValues.put("CLASSIFICACAO", to.getClassificacao());
		templateValues.put("NOMEREMETENTE", to.getNomeRemetente());
		templateValues.put("PLATAFORMA", to.getPlataforma());
		templateValues.put("EMAILREMETENTE", to.getEmailRemetente());
		salvarEEnviarEmail(EmailControleTO.builder(to.getId() , parametroGetValor(EMAIL_CONTATO_SUPORTE), TIPO_EMAIL_SUPORTE_FALECONOSCO, TipoEmailStatus.PENDENTE)
			.setMensagem(mapToJson(templateValues))
			.setAssunto(to.getAssunto())
			.setCriador(getLoggedUser().getMatricula())
			.build());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendEmailEnviarMensagem(ContatoTO contato) {
		Map<String,String> templateValues = new HashMap<>();
		templateValues.put("id", contato.getFuncionario().getId().toString());
		templateValues.put("matricula", contato.getFuncionario().getMatricula());
		templateValues.put("usuario", contato.getFuncionario().getNome());
		templateValues.put("empresa", contato.getEmpresa().getNome());
		templateValues.put("contato", contato.getFuncionario().getEmailFuncional());
		templateValues.put("assunto", contato.getAssunto());
		templateValues.put("classificacao", contato.getClassificacao());
		templateValues.put("mensagem", contato.getMensagem());
		salvarEEnviarEmail(EmailControleTO.builder(contato.getFuncionario().getId() , parametroGetValor(EMAIL_CONTATO_SUPORTE), TIPO_EMAIL_SEND_CONTATO, TipoEmailStatus.PENDENTE)
			.setMensagem(mapToJson(templateValues))
			.setCriador(getLoggedUser().getMatricula())
			.setAssunto(contato.getAssunto())
			.build());
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void sendEmailNovoCadastro(FuncionarioTO funcionario, String senha) {
		Map<String,String> templateValues = new HashMap<>();
		templateValues.put("nomeusuario", funcionario.getNome());
		templateValues.put("login", funcionario.getMatricula());
		templateValues.put("senha", senha);
		templateValues.put("link", "http://"+funcionario.getEmpresa().getHost()+"/tour");
		salvarEEnviarEmail(EmailControleTO.builder(funcionario.getId() , funcionario.getEmailFuncional(), TIPO_EMAIL_NOVO_CADASTRO_FUNCIONARIO, TipoEmailStatus.PENDENTE)
			.setMensagem(mapToJson(templateValues))
			.setCriador(getLoggedUser().getMatricula())
			.setAssunto(EMAIL_SUBJECT_NOVO_CADASTRO_FUNCIONARIO)
			.build());
	}

	@Override
	public void sendEmailWelcomeMessage(EmailBoasVindasTO emailBoasVindasTO) {
		EmpresaSimpleTO empresa = getLoggedUser().getEmpresa();
		List<FuncionarioTO> destinatarios = new ArrayList<>();

		emailBoasVindasTO.setEmpresa(empresa);

		if (emailBoasVindasTO.getColaborador() != null) {
			destinatarios.add(emailBoasVindasTO.getColaborador());
		}
		else {
			destinatarios = funcionarioService.obterFuncionariosPorPerfilAndEmpresa(emailBoasVindasTO.getPerfil(), empresa.getId());
		}

		destinatarios.forEach(funcionario -> {
			Funcionario colaborador = funcionarioRepository.findByEmpresaIdAndMatriculaIgnoreCaseAndSituacao(empresa.getId(), funcionario.getMatricula().toLowerCase(), Situacao.ATIVA);

			logger.info(String.format("Gerando nova senha para usuário: %s", funcionario.getMatricula()));
			String senha = passwordUtils.randomPassword();
			String senhaCriptografada = passwordEncoder.encode(senha);
			colaborador.setSenhaCriptografada(senhaCriptografada);
			colaborador.setSenhaOriginal(senha);

			colaborador.setPrimeiroAcesso(Boolean.TRUE);

			logger.info(String.format("Salvando usuário id: %d", colaborador.getId()));
			// Salvando a nova senha.
			funcionarioRepository.updateSenha(colaborador.getId(), senhaCriptografada, true);

			String msg = emailBoasVindasTO.getMensagem()
				.replace("#URL#", "http://" + emailBoasVindasTO.getEmpresa().getHost() + "/index.html" + "#login/" + emailBoasVindasTO.getEmpresa().getId())
				.replace("#LOGIN#", funcionario.getMatricula())
				.replace("#PASSWORD#", senha);

			String assunto = emailBoasVindasTO.getAssunto() != null && !emailBoasVindasTO.getAssunto().trim().equals("") ?
				emailBoasVindasTO.getAssunto() : EMAIL_SUBJECT_BEMVINDO_FUNCIONARIO;

			Map<String,String> templateValues = new HashMap<>();
			templateValues.put("MENSAGEM", msg);

			salvarEEnviarEmail(EmailControleTO.builder(funcionario.getId() , funcionario.getEmailFuncional(), TIPO_EMAIL_BEMVINDO_FUNCIONARIO, TipoEmailStatus.PENDENTE)
				.setMensagem(mapToJson(templateValues))
				.setAssunto(assunto)
				.setCriador(getLoggedUser().getMatricula())
				.build());
		});
	}

	@Override
	public void sendEmailException(EmailControleTO emailControleTO) {
		emailControleTO.setIdDestinatario(getLoggedUser().getId());
		emailControleTO.setCriador(getLoggedUser().getMatricula());
		salvarEEnviarEmail(emailControleTO);
	}

	@Override
	public void sendEmailNotificateBug(String email, String subject, String message) {
		sendEmail(email, subject, message);
	}

	@Override
	public void sendEmail(String email, String subject, String message) {
		salvarEEnviarEmail(EmailControleTO.builder(getLoggedUser().getId() , email, null, TipoEmailStatus.PENDENTE)
			.setMensagem(message)
			.setCriador(getLoggedUser().getMatricula())
			.setAssunto(subject)
			.build());
	}

	@Override
	public void sendEmailEnvioDeMensagensDeAcompanhamento(EnvioMensagemAcompanhamentoTO envioMensagemAcompanhamentoTO) {

		final String template = envioMensagemAcompanhamentoTO.getAvisoEmailInternacionalizado();
		Date dataEnvio = new Date();

		envioMensagemAcompanhamentoTO.getListaColaboradores().forEach( funcionarioSimpleTO -> {

			EmpresaSimpleTO empresa = getLoggedUser().getEmpresa();

			Map<String,String> templateValues = new HashMap<>();
			templateValues.put("link", "http://" + empresa.getHost() + "/index.html" + "#login/" + empresa.getId());
			templateValues.put("MSG_LINK", envioMensagemAcompanhamentoTO.getLinkEmailInternacionalizado());

			String templateReplace = template
				.replace("_enviada_", getLoggedUser().getNome())
				.replace("_data_", dateFormat("dd/MM/yyyy", dataEnvio))
				.replace("_horas_", dateFormat("HH:mm", dataEnvio))
				.replace("_assunto_", envioMensagemAcompanhamentoTO.getTitulo())
				.replace("&gt;",">")
				.replace("_enviadaAv_", "")
				.replace("&lt;", "<");

			templateValues.put("CONTENT", templateReplace);

			salvarEEnviarEmail(EmailControleTO.builder(getLoggedUser().getId() , funcionarioSimpleTO.getEmailFuncional(), TIPO_EMAIL_SEND_MSG_DEFAULT, TipoEmailStatus.PENDENTE)
				.setMensagem(mapToJson(templateValues))
				.setCriador(getLoggedUser().getMatricula())
				.setAssunto("Nova mensagem - DEZI")
				.build());
		});
	}

	@Override
	public void sendEmailVerificaMensagem(MensageriaTO mensageria, Boolean avaliacao) {
		String template =  mensageria.getAvisoEmailInternacionalizado();
		Date dataEnvio = new Date();

		EmpresaSimpleTO empresa = getLoggedUser().getEmpresa();

		Map<String,String> templateValues = new HashMap<>();
		templateValues.put("link", "http://" + empresa.getHost() + "/index.html" + "#login/" + empresa.getId());
		templateValues.put("MSG_LINK", mensageria.getLinkEmailInternacionalizado());
		template = template
			.replace("_enviada_", avaliacao ? mensageria.getAvaliacao().getDescricaoAbreviada() : mensageria.getUsuarioEnvio().getNome())
			.replace("_data_", dateFormat("dd/MM/yyyy", dataEnvio))
			.replace("_horas_", dateFormat("HH:mm", dataEnvio))
			.replace("_assunto_", mensageria.getTitulo())
			.replace("&gt;",">")
			.replace("_enviadaAv_", avaliacao ? mensageria.getAvaliacao().getDescricaoAbreviada() : "")
			.replace("&lt;", "<");

		templateValues.put("CONTENT", template);

		salvarEEnviarEmail(
			EmailControleTO.builder(
				getLoggedUser().getId() ,
				mensageria.getUsuarioDestino().getEmailFuncional(),
				TIPO_EMAIL_SEND_MSG_DEFAULT,
				TipoEmailStatus.PENDENTE
			)
			.setMensagem(mapToJson(templateValues))
			.setAssunto(mensageria.getTitulo())
			.setCriador(getLoggedUser().getMatricula())
			.build()
		);
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmailControleTO> consultarEmailsComErro() {
		JPAQuery<EmailControle> emailControleQuery = select(
			Projections.fields(EmailControle.class,
				qEmailControle.id,
				qEmailControle.email,
				qEmailControle.assunto,
				qEmailControle.status,
				qEmailControle.msgErro,
				qEmailControle.idDestinatario,
				qEmailControle.dataAtualizacao
			)
		).where(qEmailControle.status.eq(TipoEmailStatus.ERRO));
		return map(emailControleQuery.fetch());
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmailControleTO> consultarEmailsPendentes() {
		JPAQuery<EmailControle> emailControleQuery = select(
			Projections.fields(EmailControle.class,
				qEmailControle.id,
				qEmailControle.email,
				qEmailControle.assunto,
				qEmailControle.status,
				qEmailControle.idDestinatario,
				qEmailControle.dataAtualizacao
			)
		).where(qEmailControle.status.eq(TipoEmailStatus.PENDENTE));
		return map(emailControleQuery.fetch());
	}

	@Override
	public void resolverEmail() { }

	@Override
	public void atualizarStatusEmail(EmailControleTO emailControleTO) {
		this.save(emailControleTO);
	}

}
