package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.*;
import br.com.fasolti.avd.service.CampanhaService;
import br.com.fasolti.avd.service.EmailControleService;
import br.com.fasolti.avd.service.MensageriaService;
import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.MensageriaTO;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.transfer.search.MensageriaSearchTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import br.com.fasolti.avd.transfer.simple.UserTOSimple;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.commons.PasswordUtils;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MensageriaServiceImpl
	extends BaseAvdService<Mensageria, MensageriaTO, MensageriaSearchTO>
	implements MensageriaService {

	public static final String NOTIFICACAO_EMAIL_AVALIACAO_NOVA_MENSAGEM = "notificacao.email.avaliacao.nova.mensagem";

	public static final String NOTIFICACAO_EMAIL_RESPOSTA_MENSAGEM = "notificacao.email.resposta.mensagem";

	public static final String EMAIL_TEMPL_SEND_MSG_NOVA_AVALIACAO = "mail.send.message.nova.avaliacao";

	public static final String EMAIL_FALE_CONOSCO = "faleconosco@dezi.com.br";

	private static final QMensageria qMensageria = QMensageria.mensageria;

	@Value("${homeoffice.user.id}")
	private Long adminId;

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private PasswordUtils passwordUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MensageriaRepository mensageriaRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private Mapper mapper;

	@Autowired
	private EmailControleService emailControleService;

	@Autowired
	private AvaliacaoRepository avaliacaoRepository;

	@Autowired
	public MensageriaServiceImpl(MensageriaRepository repository) {
		super(repository);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, MensageriaSearchTO exampleObj) {
		builder
			.and(dslHelper.eq(qMensageria.avaliacao, exampleObj.getAvaliacao()))
			.and(dslHelper.eq(qMensageria.usuarioDestino, exampleObj.getUsuarioDestino()))
			.and(dslHelper.eq(qMensageria.usuarioEnvio, exampleObj.getUsuarioEnvio()))
			.and(dslHelper.eq(qMensageria.mensagemOriginal, exampleObj.getMensagemOriginal()))
			.and(dslHelper.eq(qMensageria.titulo, exampleObj.getTitulo()))
			.and(dslHelper.isNull(qMensageria.idOriginal, exampleObj.getIdOriginalIsNull()))
			.and(dslHelper.eq(qMensageria.numMensagem, exampleObj.getNumMensagem()));
	}

	@Override
	protected void doAfterSave(Mensageria entity, MensageriaTO to) throws ServiceException {
		Parametro parametro = parametroRepository.findOne(NOTIFICACAO_EMAIL_AVALIACAO_NOVA_MENSAGEM);
		Parametro parametro_responder = parametroRepository.findOne(NOTIFICACAO_EMAIL_RESPOSTA_MENSAGEM);
		// FIXME: Repetição de código -- Refatorar
		if (Boolean.parseBoolean(parametro.getValor()) && to.getAvaliacao() != null) {
			if (Boolean.parseBoolean(parametro_responder.getValor()) && to.getAvaliacao() == null) {
				to.setEnvioEmail(true);
			}
			if (validateMensageria(to)) {
				emailControleService.sendEmailVerificaMensagem(to, false);
			}
		}
	}

	@Override
	@Transactional
	public List<MensageriaTO> saveAndSendMensagensAvaliacao(List<MensageriaTO> listDeMensagens) throws ValidationException {
		Optional<String> templateOpt = Optional.ofNullable(parametroRepository.findOne(NOTIFICACAO_EMAIL_AVALIACAO_NOVA_MENSAGEM).getValor());
		if (templateOpt.isPresent()) {
			listDeMensagens.stream()
				.map(this::createUser)
				.forEach(mensageriaTO -> {
						if (validateMensageria(mensageriaTO)) {
							emailControleService.sendEmailVerificaMensagem(mensageriaTO, true);
							mensageriaRepository.save(map(mensageriaTO));
						} else {
							logger.warn("Erro na validação da mensageriaTO com o título: " +
								mensageriaTO.getTitulo() +
								" da avaliação " +
								mensageriaTO.getAvaliacao().getDescricao() +
								" da empresa " +
								getLoggedUser().getEmpresa().getNome());
							throw new ValidationException("validation.mensageria", "empty-fields", mensageriaTO);
						}
					}
				);
		}

		return listDeMensagens;
	}

	private MensageriaTO createUser(MensageriaTO mensageriaTO) {
		UserTO loggedUser = getLoggedUser();
		UserTOSimple user = new UserTOSimple(loggedUser.getId(), loggedUser.getNome());
		mensageriaTO.setDataHoraEnvio(new Date());

		mensageriaTO.setUsuarioEnvio(user);
		mensageriaTO.setNumMensagem(getNumeroMensagem());
		return mensageriaTO;
	}

	private boolean validateMensageria(MensageriaTO mensageriaTO) {
		if (Optional.ofNullable(mensageriaTO.getAvisoEmailInternacionalizado()).isPresent() &&
			Optional.ofNullable(mensageriaTO.getUsuarioDestino().getEmailFuncional()).isPresent() &&
			getLoggedUser().getEmailFuncional() != null) {
			return true;
		} else {
			return false;
		}
	}

	//TODO: Ver o link do pwa para download!
	// Ver o template do email e quais são os ID's e nome do administrador que enviará o email como remetente
	@Override
	@Transactional
	public void homeOfficeBrasilMensagemBoasVindas(FuncionarioSimpleTO to) {
		Funcionario funcionario = funcionarioRepository.findOne(to.getId());
		if (funcionario.getPrimeiroAcesso() != null && funcionario.getPrimeiroAcesso()) {
			String senha = passwordUtils.randomPassword();
			funcionario.setSenhaCriptografada(passwordEncoder.encode(senha));
			funcionario.setPrimeiroAcesso(Boolean.TRUE);

			funcionarioRepository.save(funcionario);
			String templateNovoAcesso = "Olá!<br/>\n" +
				"Agradecemos sua participação na nossa <b>Pesquisa Nacional de Clima Home Office!</b> Ao respondê-la, esperamos lhe propiciar uma experiência inovadora na maneira de realizar pesquisas e avaliações, um dos principais diferenciais da plataforma MentorApp Dezi.<br/>\n" +
				"<br/>\n" +
				"Para expandir esta experiência para todos os colaboradores da sua empresa, basta enviar um e-mail para #EMAILSUPORTE# manifestando seu desejo. Disponibilizaremos gratuitamente um ambiente exclusivo e privado para o acompanhamento dos impactos do trabalho remoto em sua equipe, de forma funcional e assertiva.<br/>\n" +
				"<br/>\n" +
				"Seguem os dados para acesso ao sistema:<br/>\n" +
				"Endereço Web: #WEBSITE#<br/>\n" +
				"Login: #LOGIN#<br/>\n" +
				"Senha: #PASSWORD#<br/>\n" +
				"Acesse também o aplicativo de qualquer lugar no link abaixo, na versão mobile:<br/>\n" +
				"#LINKPWA#<br/>\n" +
				"<br/>\n" +
				"Atenciosamente,<br/>\n" +
				"MentorApp Dezi";
			templateNovoAcesso = templateNovoAcesso
				.replace("#WEBSITE#", "https://homeofficebrasil.dezi.com.br/")
				.replace("#EMAILSUPORTE#", EMAIL_FALE_CONOSCO)
				.replace("#LINKPWA#", "https://homeofficebrasilmobile.dezi.com.br/index.html")
				.replace("#LOGIN#", funcionario.getMatricula())
				.replace("#PASSWORD#", senha);
			emailControleService.sendEmail(to.getEmailFuncional(), "Bem-vindo(a) à Pesquisa Nacional de Clima Home Office!", templateNovoAcesso);
		}
		Optional<String> template = Optional.of(parametroRepository.findOne(EMAIL_TEMPL_SEND_MSG_NOVA_AVALIACAO).getValor());
		MensageriaTO mensageriaTO = new MensageriaTO();
		mensageriaTO.setDataHoraEnvio(new Date());

		UserTOSimple user = new UserTOSimple();
		user.setNome("HomeOffice");
		user.setId(adminId);
		mensageriaTO.setUsuarioEnvio(user);
		mensageriaTO.setIdDestino(funcionario.getId());
		mensageriaTO.setNumMensagem(getNumeroMensagem());
		mensageriaTO.setTitulo("Bem-vindo(a) à Pesquisa Nacional de Clima Home Office!");
		mensageriaTO.setTexto("Texto boas vindas - Home Office");
		if (template.isPresent()) {
			if (mensageriaTO.getAvisoEmailInternacionalizado() != null && !mensageriaTO.getAvisoEmailInternacionalizado().isEmpty()) {
				if (mensageriaTO.getUsuarioDestino().getEmailFuncional() != null && getLoggedUser().getEmailFuncional() != null
					&& mensageriaTO.getEnvioEmail() != null && mensageriaTO.getEnvioEmail()) {
					emailControleService.sendEmailVerificaMensagem(mensageriaTO, true);
				}
			}
		}
		mensageriaRepository.save(map(mensageriaTO));
	}

	//TODO: Ver o template do email e quais são os ID's e nome do administrador que enviará o email como remetente
	@Override
	@Transactional
	public void homeOfficeBrasilMensagemAmbienteEmpresa(FuncionarioTO to) {
		String templateAmbienteEmpresarial = "Olá!<br/>\n" +
			"Agradecemos seu interesse na plataforma Mentor App Dezi!<br/>\n" +
			"<br/>\n" +
			"Para obter o ambiente gratuito e exclusivo ao realizar a <b>Pesquisa Home Office</b> em sua empresa, precisaremos de algumas informações de seus colaboradores. Para ativarmos seu ambiente de forma rápida e prática, disponibilizamos esta planilha: #PLANILHA#. Nela, solicitamos poucos dados preliminares, como nome, gênero, e-mail e data de nascimento. A indicação da(o) responsável pela gestão do RH também é importante, pois caberá a ela(e) controlar a frequência com que as avaliações serão realizadas, a data de encerramento dos ciclos de avaliação, entre outras atribuições gerenciais.<br/>\n" +
			"<br/>\n" +
			"Nosso e-mail para o envio ou dúvidas: #EMAILSUPORTE#<br/>\n" +
			"<br/>\n" +
			"Não se preocupe: nossa política de privacidade já está aderente à Lei Geral de Proteção de Dados (LGPD), Queremos que sua experiência seja positiva!<br/>\n" +
			"<br/>\n" +
			"Até já!<br/>\n" +
			"MentorApp Dezi";
		templateAmbienteEmpresarial = templateAmbienteEmpresarial
			.replace("#EMAILSUPORTE#", EMAIL_FALE_CONOSCO)
			.replace("#PLANILHA#", "https://s3-dezi-aws.s3.amazonaws.com/Carga_Mentor_App_Dezi.xlsx");

		emailControleService.sendEmail(to.getEmailFuncional(), "Você está a um passo de sua Pesquisa Home Office personalizada", templateAmbienteEmpresarial);

		Optional<String> template = Optional.of(parametroRepository.findOne(EMAIL_TEMPL_SEND_MSG_NOVA_AVALIACAO).getValor());
		MensageriaTO mensageriaTO = new MensageriaTO();
		mensageriaTO.setDataHoraEnvio(new Date());
		UserTOSimple user = new UserTOSimple();
		user.setNome("HomeOffice");
		user.setId(adminId);
		mensageriaTO.setUsuarioEnvio(user);
		mensageriaTO.setIdDestino(to.getId());
		mensageriaTO.setNumMensagem(getNumeroMensagem());
		mensageriaTO.setTitulo("Você está a um passo de sua Pesquisa Home Office personalizada");
		mensageriaTO.setTexto("Texto ambiente empresarial - Home Office");
		if (template.isPresent()) {
			if (mensageriaTO.getAvisoEmailInternacionalizado() != null && !mensageriaTO.getAvisoEmailInternacionalizado().isEmpty()) {
				if (mensageriaTO.getUsuarioDestino().getEmailFuncional() != null && getLoggedUser().getEmailFuncional() != null
					&& mensageriaTO.getEnvioEmail() != null && mensageriaTO.getEnvioEmail()) {
					emailControleService.sendEmailVerificaMensagem(mensageriaTO, true);
				}
			}
		}
		mensageriaRepository.save(map(mensageriaTO));
	}

	@Override
	@Transactional
	public void mensagemLiberarAvaliacao(AvaliacaoTO to) {
		String templateAmbienteEmpresarial =
			"A empresa #EMPRESA#, comandou a liberação da avaliação #AVALIACAO#, da campanha #CAMPANHA#.<br/><br/>\n" +
			"<span style='font-weight: bold;'>MentorApp Dezi</span>";

		Avaliacao avaliacao = avaliacaoRepository.findOne(to.getId());
		templateAmbienteEmpresarial = templateAmbienteEmpresarial
			.replace("#AVALIACAO#", "<span style='font-weight: bold;'>" + to.getDescricaoAbreviada() + "</span>")
			.replace("#EMPRESA#", "<span style='font-weight: bold;'>" + getLoggedUser().getEmpresa().getNome() + "</span>")
			.replace("#CAMPANHA#", "<span style='font-weight: bold;'>" + avaliacao.getCampanha().getDescricao() + "</span>")
		;

		emailControleService.sendEmail(EMAIL_FALE_CONOSCO, "Liberação DEZI", templateAmbienteEmpresarial);

		Optional<String> template = Optional.of(parametroRepository.findOne(EMAIL_TEMPL_SEND_MSG_NOVA_AVALIACAO).getValor());
		MensageriaTO mensageriaTO = new MensageriaTO();
		mensageriaTO.setDataHoraEnvio(new Date());
		UserTOSimple user = new UserTOSimple();
		user.setNome("HomeOffice");
		user.setId(adminId);
		mensageriaTO.setUsuarioEnvio(user);
//		mensageriaTO.setIdDestino(to.getId());
		mensageriaTO.setNumMensagem(getNumeroMensagem());
		mensageriaTO.setTitulo("Liberação DEZI");
		mensageriaTO.setTexto(templateAmbienteEmpresarial);
		if (template.isPresent()) {
			if (mensageriaTO.getAvisoEmailInternacionalizado() != null && !mensageriaTO.getAvisoEmailInternacionalizado().isEmpty()) {
				if (mensageriaTO.getUsuarioDestino().getEmailFuncional() != null && getLoggedUser().getEmailFuncional() != null
					&& mensageriaTO.getEnvioEmail() != null && mensageriaTO.getEnvioEmail()) {
					emailControleService.sendEmailVerificaMensagem(mensageriaTO, true);
				}
			}
		}
		mensageriaRepository.save(map(mensageriaTO));
	}

	@Override
	@Transactional
	public void mensagemLiberarAvaliacaoConcluido(AvaliacaoTO to) {
		Avaliacao avaliacao = avaliacaoRepository.findOne(to.getId());
		List<Funcionario> gestoresRH = funcionarioRepository.findByTipoPerfilAndSituacaoAndEmpresaId(
					TipoPerfil.GESTOR_RH,
					Situacao.ATIVA,
					avaliacao.getCampanha().getEmpresa().getId()
				);

		for (Funcionario gestor : gestoresRH) {
			String templateAmbienteEmpresarial =
				"Prezado(a) #GESTOR#,<br/>\n" +
					"<br/>\n" +
					"A liberação de sua avaliação foi processada com sucesso.<br/><br/>\n" +
					"<span style='font-weight: bold;'>MentorApp Dezi</span>";

			templateAmbienteEmpresarial = templateAmbienteEmpresarial.replace("#GESTOR#", gestor.getNome());

			String assunto = "Liberação Empresa #EMPRESA# - Campanha #CAMPANHA# - Avaliação #AVALIACAO#";

			assunto = assunto
				.replace("#AVALIACAO#", to.getDescricaoAbreviada())
				.replace("#EMPRESA#", getLoggedUser().getEmpresa().getNome())
				.replace("#CAMPANHA#", avaliacao.getCampanha().getDescricao())
			;

			emailControleService.sendEmail(gestor.getEmailFuncional(), assunto, templateAmbienteEmpresarial);

			Optional<String> template = Optional.of(parametroRepository.findOne(EMAIL_TEMPL_SEND_MSG_NOVA_AVALIACAO).getValor());
			MensageriaTO mensageriaTO = new MensageriaTO();
			mensageriaTO.setDataHoraEnvio(new Date());
			UserTOSimple user = new UserTOSimple();
			user.setNome("HomeOffice");
			user.setId(adminId);
			mensageriaTO.setUsuarioEnvio(user);
			mensageriaTO.setIdDestino(to.getId());
			mensageriaTO.setNumMensagem(getNumeroMensagem());
			mensageriaTO.setTitulo(assunto);
			mensageriaTO.setTexto(templateAmbienteEmpresarial);
			if (template.isPresent()) {
				if (mensageriaTO.getAvisoEmailInternacionalizado() != null && !mensageriaTO.getAvisoEmailInternacionalizado().isEmpty()) {
					if (mensageriaTO.getUsuarioDestino().getEmailFuncional() != null && getLoggedUser().getEmailFuncional() != null
						&& mensageriaTO.getEnvioEmail() != null && mensageriaTO.getEnvioEmail()) {
						emailControleService.sendEmailVerificaMensagem(mensageriaTO, true);
					}
				}
			}
			mensageriaRepository.save(map(mensageriaTO));
		}
	}

	@Override
	@Transactional
	public void mensagemLiberarAvaliacaoErro(AvaliacaoTO to, Exception e) {
		StringWriter erros = new StringWriter();
		e.printStackTrace(new PrintWriter(erros));

		String templateAmbienteEmpresarial =
			"Erro na liberação da avaliação #AVALIACAO#, da campanha #CAMPANHA#, empresa #EMPRESA#.<br/><br/>\n" +
			"#ERRO#<br/><br/><br/>" +
			"<span style='font-weight: bold;'>MentorApp Dezi</span>";

		Avaliacao avaliacao = avaliacaoRepository.findOne(to.getId());
		templateAmbienteEmpresarial = templateAmbienteEmpresarial
			.replace("#AVALIACAO#", "<span style='font-weight: bold;'>" + to.getDescricaoAbreviada() + "</span>")
			.replace("#EMPRESA#", "<span style='font-weight: bold;'>" + getLoggedUser().getEmpresa().getNome() + "</span>")
			.replace("#CAMPANHA#", "<span style='font-weight: bold;'>" + avaliacao.getCampanha().getDescricao() + "</span>")
			.replace("#ERRO#", erros.toString().replaceAll(";", ";\n<br/>").replaceAll(" at ", "<br/> at "));
		;

		emailControleService.sendEmail(EMAIL_FALE_CONOSCO, "ERRO - Liberação DEZI", templateAmbienteEmpresarial);

		Optional<String> template = Optional.of(parametroRepository.findOne(EMAIL_TEMPL_SEND_MSG_NOVA_AVALIACAO).getValor());
		MensageriaTO mensageriaTO = new MensageriaTO();
		mensageriaTO.setDataHoraEnvio(new Date());
		UserTOSimple user = new UserTOSimple();
		user.setNome("HomeOffice");
		user.setId(adminId);
		mensageriaTO.setUsuarioEnvio(user);
//		mensageriaTO.setIdDestino(to.getId());
		mensageriaTO.setNumMensagem(getNumeroMensagem());
		mensageriaTO.setTitulo("ERRO na Liberação DEZI");
		mensageriaTO.setTexto(templateAmbienteEmpresarial);
		if (template.isPresent()) {
			if (mensageriaTO.getAvisoEmailInternacionalizado() != null && !mensageriaTO.getAvisoEmailInternacionalizado().isEmpty()) {
				if (mensageriaTO.getUsuarioDestino().getEmailFuncional() != null && getLoggedUser().getEmailFuncional() != null
					&& mensageriaTO.getEnvioEmail() != null && mensageriaTO.getEnvioEmail()) {
					emailControleService.sendEmailVerificaMensagem(mensageriaTO, true);
				}
			}
		}
		mensageriaRepository.save(map(mensageriaTO));
	}

	private Long getNumeroMensagem() {
		Long numeroMensagem = mensageriaRepository.findMaxNumMensagem();

		if (numeroMensagem == null)
			numeroMensagem = 1L;
		else
			numeroMensagem = numeroMensagem + 1;
		return numeroMensagem;
	}

	@Override
	public Map<String, List<MensageriaTO>> mensagensPorUsuario(Long userId) {
		List<Mensageria> listMensageriaEnvio = mensageriaRepository.findByUsuarioEnvioIdOrderByDataHoraEnvioAsc(userId);
		List<Mensageria> listMensageriaDestino = mensageriaRepository.findByUsuarioDestinoIdOrderByDataHoraEnvioAsc(userId);

		Map<String, List<MensageriaTO>> map = new TreeMap<>();

		map.put("envio", map(listMensageriaEnvio));
		map.put("destino", map(listMensageriaDestino));

		return map;
	}

	private List<Mensageria> buscarAvaliacaoPorId(Long id) {
		return select(
			Projections.fields(
				Mensageria.class,
				qMensageria.id,
				qMensageria.idOriginal,
				qMensageria.numMensagem,
				qMensageria.titulo,
				qMensageria.dataHoraEnvio,
				qMensageria.texto,
				Projections.fields(
					Avaliacao.class,
					qMensageria.avaliacao.id
				).as("avaliacao"),
				Projections.fields(
					Avaliacao.class,
					qMensageria.avaliacaoDestino.id
				).as("avaliacaoDestino"),
				Projections.fields(
					Avaliacao.class,
					qMensageria.avaliacaoEnvio.id
				).as("avaliacaoEnvio"),
				Projections.fields(
					Mensageria.class,
					qMensageria.mensagemOriginal.id
				).as("mensagemOriginal"),
				Projections.fields(
					Funcionario.class,
					qMensageria.usuarioDestino.id,
					qMensageria.usuarioDestino.nome
				).as("funcionarioDestino"),
				Projections.fields(
					Funcionario.class,
					qMensageria.usuarioEnvio.id,
					qMensageria.usuarioEnvio.nome
				).as("funcionarioEnvio")
			)
		)
			.leftJoin(qMensageria.avaliacao, QAvaliacao.avaliacao)
			.leftJoin(qMensageria.avaliacaoDestino, QAvaliacao.avaliacao)
			.leftJoin(qMensageria.avaliacaoEnvio, QAvaliacao.avaliacao)
			.leftJoin(qMensageria.usuarioDestino, QFuncionario.funcionario)
			.leftJoin(qMensageria.usuarioEnvio, QFuncionario.funcionario)
			.where(qMensageria.avaliacao.id.eq(id))
			.orderBy(qMensageria.dataHoraEnvio.asc())
			.fetch();
	}

	@Override
	public List<MensageriaTO> getAllMessagesRecebidasByAvaliacao(AvaliacaoTO avaliacao) {
		return buscarAvaliacaoPorId(avaliacao.getId()).stream()
			.filter(msg -> msg.getAvaliacaoDestino() != null && msg.getAvaliacaoDestino().getId() != null)
			.map(msg -> map(msg))
			.collect(Collectors.toList());
	}

	@Override
	public List<MensageriaTO> getAllMessagesByAvaliacao(AvaliacaoTO avaliacao) {
		List<Mensageria> list = buscarAvaliacaoPorId(avaliacao.getId());
		Map<String, List<Mensageria>> map = new TreeMap<>();
		list.forEach(entity -> {
			String titulo = entity.getTitulo().trim() + (entity.getNumMensagem() != null ? entity.getNumMensagem().toString() : "");
			List<Mensageria> mensagerias = map.get(titulo);
			if (mensagerias == null) {
				mensagerias = new ArrayList<>();
				map.put(titulo, mensagerias);
			}
			//Somente mensagens enviadas de uma avaliação
			if (entity.getAvaliacaoEnvio() != null) {
				mensagerias.add(entity);
			}
		});

		List<MensageriaTO> listTO = new ArrayList<>();

		map.keySet().forEach(
			key -> {
				if (map.get(key) != null && !map.get(key).isEmpty()) {
					MensageriaTO msgTO = map(map.get(key).get(0));
					msgTO.setTotalEnviados(map.get(key).size());
					listTO.add(msgTO);
				}
			}
		);
		return listTO;
	}

	@Override
	public List<MensageriaTO> destinatariosMensagem(MensageriaTO mensageria) {
		return map(mensageriaRepository.findByAvaliacaoIdAndTituloOrderByDataHoraEnvioAsc(mensageria.getAvaliacao().getId(), mensageria.getTitulo()));
	}

	@Override
	public MensageriaSearchTO quantidadeMensagensNaoLidas(Long userId) {
		BooleanBuilder criteria = new BooleanBuilder();
		criteria
			.and(qMensageria.usuarioDestino.id.eq(userId))
			.and(qMensageria.usuarioEnvio.id.ne(userId))
			.and(qMensageria.dataHoraAbertura.isNull());

		Long count = mensageriaRepository.count(criteria);

		MensageriaSearchTO mensageria = new MensageriaSearchTO();
		mensageria.setMensagensNaoLidas(count.intValue());

		return mensageria;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false)
	public void removerMensageria(long id) {
		mensageriaRepository.findByAvaliacaoId(id).forEach((mensageria) -> {
			mensageriaRepository.delete(mensageria);
		});
	}

}
