package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.*;
import br.com.fasolti.avd.repository.*;
import br.com.fasolti.avd.service.*;
import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.mobile.*;
import br.com.fasolti.avd.types.Situacao;
import br.com.fasolti.avd.types.SituacaoFuncionario;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.avd.types.TipoPerfil;
import br.com.fasolti.commons.Constants;
import br.com.fasolti.commons.PasswordUtils;
import br.com.fasolti.commons.lang.ServiceException;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.services.ServiceFilter;
import br.com.fasolti.service.util.BaseAvdService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FuncionarioServiceImpl
	extends BaseAvdService<Funcionario, FuncionarioTO, FuncionarioTO>
	implements FuncionarioService {

	private final String NOMEUSUARIO = "--nomeusuario--";
	private final String SENHA = "--senha--";
	private final String LINK = "--link--";
	private final String MENSAGEM = "--mensagem--";
	private final String CLASSIFICACAO = "--classificacao--";
	private final String NOMEREMETENTE = "--nomeremetente--";
	private final String PLATAFORMA = "--plataforma--";
	private final String EMAILREMETENTE = "--emailremetente--";

	private final String NOTIFICACAO_EMAIL_FUNCIONARIO_NOVO = "notificacao.email.funcionario.novo";
	private final String NOTIFICACAO_EMAIL_RESPOSTA_MENSAGEM = "notificacao.email.resposta.mensagem";
	private final String NOTIFICACAO_EMAIL_FUNCIONARIO_ESQUECI_SENHA = "notificacao.email.funcionario.esqueci.senha";
	private final String NOTIFICACAO_EMAIL_FUNCIONARIO_TROCA_SENHA = "notificacao.email.funcionario.troca.senha";
	private final String NOTIFICACAO_SMS_FUNCIONARIO_NOVO = "notificacao.sms.funcionario.novo";
	private final String NOTIFICACAO_SMS_FUNCIONARIO_ESQUECI_SENHA = "notificacao.sms.funcionario.esqueci.senha";
	private final String NOTIFICACAO_SMS_FUNCIONARIO_TROCA_SENHA = "notificacao.sms.funcionario.troca.senha";

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private PasswordUtils passwordUtils;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ConfiguracaoService configuracaoService;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private AvaliacaoParticipanteRepository avaliacaoParticipanteRepository;

	@Autowired
	private ParticipanteRepository participanteRepository;

	@Autowired
	private FotoService fotoService;

	@Autowired
	private CargoCompetenciaService cargoCompetenciaService;

	@Autowired
	private AvaliacaoParticipanteService avaliacaoParticipanteService;

	@Autowired
	private Mapper mapper;

	@Autowired
	private EmailControleService emailControleService;

	@Autowired
	private UnidadeDepartamentoService unidadeDepartamentoService;

	@Autowired
	private DepartamentoService departamentoService;

	@Autowired
	private CargoService cargoService;

	@Autowired
	private ApontamentoFuncionalService apontamentoFuncionalService;

	@Autowired
	private TipoCompetenciaRepository tipoCompetenciaRepository;

	@Autowired
	private DepartamentoGestorService departamentoGestorService;

	@Autowired
	public FuncionarioServiceImpl(FuncionarioRepository repository) {
		super(repository);
	}

	private static final QFuncionario qFuncionario = QFuncionario.funcionario;
	private static final QCargo qCargo = QCargo.cargo;
	private static final QDepartamento qDepartamento = QDepartamento.departamento;
	private static final QUnidade qUnidade = QUnidade.unidade;

	//TODO Integrar na biblioteca para que os métodos gerais retornem sempre a projeção padrão
	private static final QBean<Funcionario> DEFAULT_PROJECTION = Projections.fields(
		Funcionario.class,
		qFuncionario.id,
		qFuncionario.matricula,
		qFuncionario.nome,
		qFuncionario.dataCargo,
		qFuncionario.admissao,
		qFuncionario.tipoPerfil,
		qFuncionario.situacao,
		qFuncionario.situacaoFuncional,
		Projections.fields(
			Cargo.class,
			qCargo.id,
			qCargo.nome,
			qCargo.idCargoCliente,
			qCargo.situacao
		).as("cargo"),
		Projections.fields(
			Departamento.class,
			qDepartamento.id,
			qDepartamento.nome,
			qDepartamento.situacao
		).as("departamento"),
		Projections.fields(
			Unidade.class,
			qUnidade.unidade.id,
			qUnidade.unidade.nome
		).as("unidade")
	);

	@Override
	protected ServiceFilter<Funcionario> getServiceFilter() {
		// TODO Não pode cachear localmente pq o serviço é stateless
		BooleanBuilder mandatoryFilter = new BooleanBuilder();

		UserTO user = getLoggedUser();
		switch (user.getTipoPerfil()) {
			case ADMINISTRADOR:
			case GESTOR_RH:
				mandatoryFilter.and(qFuncionario.empresa.id.eq(user.getEmpresa().getId()));
				break;
			case CONSULTOR_RH: //TODO Ver todas as empresas da qual ele é gestor
				mandatoryFilter.and(qFuncionario.empresa.id.eq(user.getEmpresa().getId()));
				break;
			case GESTOR_AREA:
				//Caso tenha dois niveis de hierarquia
				if (qFuncionario.departamento.departamentoPai != null) {
					mandatoryFilter.and(qFuncionario.departamento.id.eq(user.getDepartamento().getId())
						.or(qFuncionario.departamento.departamentoPai.id.eq(user.getDepartamento().getId())));
					//mandatoryFilter.and(qDepartamento.id.eq(user.getDepartamento().getId()));
				} else {
					mandatoryFilter.and(qFuncionario.departamento.id.eq(user.getDepartamento().getId()));
				}
				break;
			default:
				mandatoryFilter.and(qFuncionario.id.eq(user.getId()));
				break;
		}

		return ServiceFilter.create(qFuncionario, mandatoryFilter);
	}

	//TODO Alterar na classe base para o default buildSearchQuery() retornar um select vazio e criar um defaultProjection()
	private JPAQuery<Funcionario> baseQuery() {
		JPAQuery<Funcionario> query = select();
		buildSearchQuery(query);
		return query;
	}

	@Override
	protected void buildSearchQuery(JPAQuery<Funcionario> query) {
		query.select(DEFAULT_PROJECTION)
			.join(qFuncionario.unidade, qUnidade)
			.join(qFuncionario.cargo, qCargo)
			.join(qFuncionario.departamento, qDepartamento);
	}

	@Override
	protected void buildSearchPredicate(BooleanBuilder builder, FuncionarioTO exampleObj) {
		if (exampleObj.getNome() != null) {
			builder.and(qFuncionario.nome.lower().trim().like(Constants.PERCENT + exampleObj.getNome().toLowerCase().trim() + Constants.PERCENT));
		}

		if (exampleObj.getSituacao() != null) {
			if (exampleObj.getSituacao() != null) {
				builder.and(qFuncionario.situacao.eq(exampleObj.getSituacao()));
			} else if (exampleObj.getSituacao() != null) {
				builder.and(qFuncionario.situacao.eq(exampleObj.getSituacao()));
			}
		}
	}

	@Override
	protected void doBeforeSave(FuncionarioTO object) throws ServiceException {
		validar(object);
	}

	@Override
	protected void doBeforeSave(Funcionario entity, FuncionarioTO object) {
		// Senha temporária
		String senha = passwordUtils.randomPassword();
		entity.setPrimeiroAcesso(Boolean.TRUE);
		entity.setSenhaCriptografada(passwordEncoder.encode(senha));
		entity.setSenhaOriginal(senha);
	}

	@Override
	protected void doAfterSave(Funcionario entity, FuncionarioTO object) throws ServiceException {
		sendEmailNovoCadastro(object, entity.getSenhaOriginal());
		Configuracao conf = new Configuracao();
		ConfiguracaoTO confTO = new ConfiguracaoTO();
		conf.setFuncionario(entity);
		mapper.map(conf, confTO);
		configuracaoService.save(confTO);

		UnidadeDepartamentoTO persistedUnidadeDepto = unidadeDepartamentoService.obterPorUnidadeEDepartamento(entity.getUnidade().getId(), entity.getDepartamento().getId());
		unidadeDepartamentoService.atualizaGestorUnidadeDepartamento(persistedUnidadeDepto, object);
		departamentoGestorService.atualizarDepartamentoGestor(entity.getDepartamento().getId(), entity.getId());
	}

	@Override
	protected void doAfterUpdate(Funcionario entity) throws ServiceException {
		departamentoGestorService.atualizarDepartamentoGestor(entity.getDepartamento().getId(), entity.getId());
	}

	@Override
	protected void doBeforeUpdate(Funcionario entity, FuncionarioTO object) throws ServiceException {
		validar(object, Optional.of(entity));

		UnidadeDepartamentoTO persistedUnidadeDepto = unidadeDepartamentoService.obterPorUnidadeEDepartamento(entity.getUnidade().getId(), entity.getDepartamento().getId());
		unidadeDepartamentoService.atualizaGestorUnidadeDepartamento(persistedUnidadeDepto, object);

		if (SituacaoFuncionario.findByNotRecesso().contains(object.getSituacaoFuncional())) {
			object.setDataInicioRecesso(null);
			object.setDataFimRecesso(null);
		}

		if (SituacaoFuncionario.DESLIGADO.equals(object.getSituacaoFuncional())) {
			object.setSituacao(Situacao.INATIVA);
			if (object.getDesligamento() == null) {
				object.setDesligamento(new Date());
			}
		} else {
			object.setDesligamento(null);
		}
	}

	private void validar(FuncionarioTO funcionario) {
		validar(funcionario, Optional.empty());
	}

	private void validar(FuncionarioTO funcionario, Optional<Funcionario> entity) {
		FuncionarioTO funcionarioExistente = obterFuncionarioPorMatriculaEmpresa(funcionario.getMatricula());
		FuncionarioTO funcionarioEmailExistente = findByEmailFuncionalAndEmpresaIdAndSituacao(funcionario.getEmailFuncional(), funcionario.getEmpresa().getId(), Situacao.ATIVA);

		if (funcionarioEmailExistente != null && !funcionarioEmailExistente.getId().equals(funcionario.getId())) {
			throw new ValidationException("validation.duplicate_email", "funcionário", "field.email", funcionario.getEmailFuncional());
		}

		if (Situacao.INATIVA.equals(funcionario.getSituacao())
			&& !SituacaoFuncionario.DESLIGADO.equals(funcionario.getSituacaoFuncional())
		) {
			throw new ValidationException("validation.invalidInactivation", "funcionario");
		}

		if (Situacao.INATIVA.equals(funcionario.getSituacao())
			&& !SituacaoFuncionario.DESLIGADO.equals(funcionario.getSituacaoFuncional())
		) {
			throw new ValidationException("validation.invalidInactivation", "funcionario");
		}

		if (funcionario.getDesligamento() != null) {
			Date dataAdmissao = funcionario.getAdmissao() != null
				? funcionario.getAdmissao()
				: entity.map(Funcionario::getAdmissao).orElse(null);

			if (dataAdmissao != null && funcionario.getDesligamento().compareTo(dataAdmissao) < 0) {
				throw new ValidationException("validation.invalidDesligamentoDate", "funcionario");
			}
		}

		// Usuáriuo do tipo administrador só podem ser editados por usuário administradores.
		// Se o perfil atual é administrador...
		if (funcionarioExistente != null && funcionarioExistente.getTipoPerfil().equals(TipoPerfil.ADMINISTRADOR)) {
			// Apenas usuário logados pode editar.
			if (!getLoggedUser().getTipoPerfil().equals(TipoPerfil.ADMINISTRADOR)) {
				throw new ValidationException("validation.permission", "funcionario");
			}
		}

		if (funcionarioExistente != null && !funcionarioExistente.getId().equals(funcionario.getId())) {
			/*
			 * Para apresentação da validação no frontend, precisamos informar qual entidade, o código do campo
			 * que está sendo validado e o valor a ser apresentado na validação. No exemplo abaixo, a mensagem
			 * genérica (no frontend) será apresentada como: Já existe matrícula com valor XXXXXXX.
			 */
			//FIXME: Passar a entidade para internacionalização
			throw new ValidationException("validation.duplicate", "funcionário", "field.matricula", funcionario.getMatricula());
		}

		if (SituacaoFuncionario.findByRecesso().contains(funcionario.getSituacaoFuncional())) {

			if (funcionario.getDataInicioRecesso() == null) {
				throw new ValidationException("validation.missingDataInicioRecesso", "funcionário");
			}

			if (funcionario.getDataFimRecesso() == null) {
				throw new ValidationException("validation.missingDataFimRecesso", "funcionário");
			}

			if (funcionario.getDataFimRecesso().compareTo(funcionario.getDataInicioRecesso()) < 0) {
				throw new ValidationException("validation.invalidPeriodoRecesso", "funcionário");
			}
		}
	}

	//	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public void sendEmailNovoCadastro(FuncionarioTO funcionario, String senha) {
		// Obter dos parâmetros se será enviado o email.
		Parametro parametro = parametroRepository.findOne(NOTIFICACAO_EMAIL_FUNCIONARIO_NOVO);
		Boolean sendMail = Boolean.parseBoolean(parametro.getValor());

		if (sendMail) {
			emailControleService.sendEmailNovoCadastro(funcionario, senha);
		}

		// Obter dos parâmetros se será enviado o SMS.
		parametro = parametroRepository.findOne(NOTIFICACAO_SMS_FUNCIONARIO_NOVO);
		Boolean sendSms = Boolean.parseBoolean(parametro.getValor() + "/" + getLoggedUser().getEmpresa().getId());
		if (sendSms) {
			// FIXME: Ainda não implementado.
			logger.warn("Envio de SMS não implementado");
		}
	}

	@Override
	public FuncionarioTO findByEmailFuncionalAndEmpresaIdAndSituacao(String emailFuncional, Long empresaId, Situacao situacao) {
		return map(funcionarioRepository.findByEmailFuncionalAndEmpresaIdAndSituacao(emailFuncional, empresaId, situacao));
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void forgotPassword(String email, Long empresaId) throws ValidationException {
		Optional<Funcionario> funcionarioOpt = Optional.ofNullable(funcionarioRepository.findByEmailFuncionalAndEmpresaIdAndSituacao(email, empresaId, Situacao.ATIVA));
		if (funcionarioOpt.isPresent()) {
			Funcionario funcionario = funcionarioOpt.get();
			String senha = passwordUtils.randomPassword();
			funcionario.setSenhaCriptografada(passwordEncoder.encode(senha));
			funcionario.setPrimeiroAcesso(Boolean.TRUE);
			funcionarioRepository.save(funcionario);
			if (Boolean.parseBoolean(parametroRepository.findOne(NOTIFICACAO_EMAIL_FUNCIONARIO_ESQUECI_SENHA).getValor())) {
				emailControleService.sendEmailforgotPassword(funcionario.getId(),
					funcionario.getEmailFuncional(), funcionario.getNome(), senha, funcionario.getEmpresa().getHost(),
					funcionario.getEmpresa().getId());
			}
			if (Boolean.parseBoolean(parametroRepository.findOne(NOTIFICACAO_SMS_FUNCIONARIO_ESQUECI_SENHA).getValor())) {
				// FIXME: Ainda não implementado.
				logger.warn("Envio de SMS não implementado");
			}
		} else {
			logger.warn("E-mail não encontrado em esqueci minha senha: " + email);
			throw new ValidationException("validation.not-found-email", email);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void trocarSenha(TrocaDeSenha trocaSenhaTO) throws ValidationException {
		// FIXME: Refatorar validações.

		if (trocaSenhaTO == null || trocaSenhaTO.getSenha() == null || trocaSenhaTO.getNovaSenha() == null) {
			String msg = "Senha nula.";
			logger.error(msg);
			throw new ValidationException("not-null", "funcionario", "field.senha");
		}

		if (trocaSenhaTO.getConfirmarSenha() == null) {
			String msg = "Confirmar senha nula.";
			logger.error(msg);
			throw new ValidationException("not-null", "funcionario", "field.confirmar_senha");
		}

		if (!trocaSenhaTO.getNovaSenha().equals(trocaSenhaTO.getConfirmarSenha())) {
			String msg = "Senha de confirmação não confere com a nova senha.";
			logger.error(msg);
				throw new ValidationException("password-not-match", "funcionario", "field.confirmar_senha");
		}

		//FIXME Simplificar esse código
		Funcionario funcionario = funcionarioRepository.findOne(trocaSenhaTO.getFuncionario().getId());

		if (!BCrypt.checkpw(trocaSenhaTO.getSenha(), funcionario.getSenhaCriptografada())) {
			String msg = "Senha atual incorreta.";
			logger.error(msg);
			throw new ValidationException("actual-password-not-match", "funcionario", "field.senha");
		}

		FuncionarioTO funcionarioTO = new FuncionarioTO();
		funcionarioTO.setId(funcionario.getId());

		ConfiguracaoTO configuracao = configuracaoService.findByFuncionarioId(funcionarioTO.getId());

		if (!passwordEncoder.matches(trocaSenhaTO.getSenha(), funcionario.getSenhaCriptografada())) {
			throw new ValidationException("login-password-invalid", "funcionario", "field.senha");
		}

		funcionario.setSenhaCriptografada(passwordEncoder.encode(trocaSenhaTO.getNovaSenha()));

		logger.info(String.format("Salvando nova senha do colaborador ID: %s", funcionario.getId()));

		funcionario.setPrimeiroAcesso(Boolean.FALSE);
		funcionarioRepository.save(funcionario);

		// Obter dos parâmetros o envio de email.
		Parametro parametro = parametroRepository.findOne(NOTIFICACAO_EMAIL_FUNCIONARIO_TROCA_SENHA);
		if (Boolean.parseBoolean(parametro.getValor())) {
			emailControleService.sendEmailTrocaSenha(
				funcionario.getId(),
				funcionario.getEmailFuncional(),
				funcionario.getNome(),
				trocaSenhaTO.getNovaSenha(),
				funcionario.getEmpresa().getHost(),
				funcionario.getEmpresa().getId());
		}

		// Obter dos parâmetros se será enviado o SMS.
		parametro = parametroRepository.findOne(NOTIFICACAO_SMS_FUNCIONARIO_TROCA_SENHA);
		Boolean sendSms = Boolean.parseBoolean(parametro.getValor());
		if (sendSms) {
			// FIXME: Ainda não implementado.
			logger.warn("Envio de SMS não implementado");
		}
	}

	@Override
	protected void doAfterLoad(FuncionarioTO object) throws ServiceException {
		//FIXME Por que está forçando a empresa aqui?
		object.setEmpresa(getLoggedUser().getEmpresa());
		super.doAfterLoad(object);
	}

	@Override
	protected FuncionarioTO doAfterCreate(FuncionarioTO object) throws br.com.fasolti.commons.lang.ServiceException {
		object.setEmpresa(getLoggedUser().getEmpresa());
		object.setSituacao(Situacao.ATIVA);
		return object;
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public FuncionarioTO obterFuncionarioPorMatriculaEmpresa(String matricula) {
		if (matricula == null || matricula.trim().isEmpty()) {
			return null;
		}

		Funcionario funcionario = select(
			Projections.fields(Funcionario.class,
				qFuncionario.id,
				qFuncionario.matricula,
				qFuncionario.nome,
				qFuncionario.tipoPerfil
			))
			.where(
				transformMatricula(qFuncionario.matricula).eq(transformMatricula(matricula)),
				qFuncionario.empresa.id.eq(getLoggedUser().getEmpresa().getId()))
			.fetchOne();

		return map(funcionario);
	}

	@Override
	public FuncionarioTO findById(Long id) {
		return map(funcionarioRepository.findOne(id));
	}

	@Override
	public FuncionarioTO findByNome(String nome) {
		return map(funcionarioRepository.findByNomeIgnoreCaseAndEmpresaId(nome, getLoggedUser().getEmpresa().getId()));
	}

	@Override
	public FuncionarioTO findByEmpresaIdAndMatricula(Long idEmpresa, String matricula) {
		return map(funcionarioRepository.findByEmpresaIdAndMatricula(idEmpresa, matricula));
	}

	@Override
	public List<FuncionarioMobileTO> obterAvaliadosAtivosPorFuncionarioDaEmpresa(UsuarioMobileTO avaliador) {

//		List<Funcionario> funcionarioList = null;
		//FIXME: Removido no issue AVD-699

		FuncionarioTO avaliadorTO = obterFuncionarioProjections(avaliador.getIdFuncionario());
		List<FuncionarioTO> funcionarioList = obterSubordinados(avaliadorTO);

		List<FuncionarioMobileTO> avaliadosList = new ArrayList<>();

		if (funcionarioList != null) {
			for (FuncionarioTO funcionarioTO : funcionarioList) {
				FuncionarioMobileTO avaliadoTO = converterParaMobileTO(funcionarioTO);
				avaliadosList.add(avaliadoTO);

				List<CompetenciaMobileTO> list = new ArrayList<CompetenciaMobileTO>();

				for (CargoCompetenciaTO cargoCompetenciaTO : cargoCompetenciaService.findAllCargoCompetenciaByCargoAndAtivos(funcionarioTO.getCargo().getId())) {
					converteCompetenciaMobile(list, cargoCompetenciaTO);
				}

				avaliadoTO.setListaCompetencias(list);
			}

		}
		Collections.sort(avaliadosList);
		return avaliadosList;
	}


	private void converteCompetenciaMobile(List<CompetenciaMobileTO> list, CargoCompetenciaTO cargoCompetenciaTO) {
		CompetenciaMobileTO competenciaTO = new CompetenciaMobileTO();

		competenciaTO.setId(cargoCompetenciaTO.getCompetencia().getId());
		competenciaTO.setDescricaoAbreviada(cargoCompetenciaTO.getCompetencia().getDescricaoAbreviada());

		TipoCompetenciaMobileTO tipoCompetencia = new TipoCompetenciaMobileTO();

		tipoCompetencia.setId(cargoCompetenciaTO.getCompetencia().getTipo().getId());
		tipoCompetencia.setDescricao(cargoCompetenciaTO.getCompetencia().getTipo().getDescricao());
		tipoCompetencia.setPaiId(cargoCompetenciaTO.getCompetencia().getTipo().getIdPai());
		tipoCompetencia.setNome(cargoCompetenciaTO.getCompetencia().getTipo().getNome());
		tipoCompetencia.setNomeCurto(cargoCompetenciaTO.getCompetencia().getTipo().getNomeCurto());

		list.add(competenciaTO);
	}

	//FIXME: refatorar
	private FuncionarioMobileTO converterParaMobileTO(FuncionarioTO funcionario) {
		FuncionarioMobileTO funcionarioMobileTO = mapper.map(funcionario, FuncionarioMobileTO.class);


		funcionarioMobileTO.setEmpresaId(funcionario.getEmpresa().getId());
		funcionarioMobileTO.setEmail(funcionario.getEmailFuncional());
//		if(funcionario.getUnidade() != null) {
//			funcionarioMobileTO.setUnidadeId(funcionario.getUnidade().getId());
//		}

		List<ApontamentoFuncionalMobileTO> listApontamentos = new ArrayList<ApontamentoFuncionalMobileTO>();

		List<ApontamentoFuncionalTO> apontamentoFuncionalList = apontamentoFuncionalService.apontamentosPorFuncionario(funcionario);

		if (apontamentoFuncionalList != null) {

			for (ApontamentoFuncionalTO apontamentoFuncionalTO : apontamentoFuncionalList) {

				ApontamentoFuncionalMobileTO apontamentoMobile = mapper.map(apontamentoFuncionalTO, ApontamentoFuncionalMobileTO.class);

				apontamentoMobile.setAvaliadorId(apontamentoFuncionalTO.getAvaliador().getId());
				apontamentoMobile.setAvaliadoId(apontamentoFuncionalTO.getAvaliado().getId());

				listApontamentos.add(apontamentoMobile);
			}
			funcionarioMobileTO.setListaApontamentos(listApontamentos);
		}


		FotoTO foto = fotoService.obterFotoPerfilFuncionario(funcionario.getId());
		if (foto != null) {
			funcionarioMobileTO.setFotoBase64(foto.getBase64());
		} else {
			funcionarioMobileTO.setFotoBase64("");
		}

		funcionarioMobileTO.setCargo(mapper.map(funcionario.getCargo(), CargoMobileTO.class));
		funcionarioMobileTO.setDepartamento(mapper.map(funcionario.getDepartamento(), DepartamentoMobileTO.class));
		return funcionarioMobileTO;
	}

	@Override
	public List<FuncionarioTO> funcionariosParaAvaliacao(AvaliacaoTO to) {
		JPAQuery<Funcionario> query = baseQuery()
			.where(
				qFuncionario.situacao.eq(Situacao.ATIVA),
				qFuncionario.tipoPerfil.ne(TipoPerfil.ADMINISTRADOR)
			);

		return map(query.fetch());
	}

	@Override
	public List<FuncionarioTO> obterSubordinados(FuncionarioTO avaliador) {
		List<FuncionarioTO> subordinadoList = new ArrayList<>();

		UnidadeDepartamentoTO unidadeDepartamento = unidadeDepartamentoService.obterDepartamentoUnidadePorGestorId(avaliador.getId());

		//verifica se o avaliador é gestor de algum departamento
		if (unidadeDepartamento != null && avaliador.getDepartamento() != null && unidadeDepartamento.getDepartamento() != null &&
			avaliador.getDepartamento().getId().equals(unidadeDepartamento.getDepartamento().getId())) {

			buscarSubordinados(avaliador, subordinadoList, unidadeDepartamento.getDepartamento().getId(), unidadeDepartamento.getUnidade().getId());

			//obtem todos os departamentos que estão abaixo do departamento do avaliador
			List<DepartamentoTO> departamentos = departamentoService.obterDepartamentosFilhos(unidadeDepartamento.getDepartamento().getId());

			FuncionarioTO gestor = null;

			for (DepartamentoTO departamentoTO : departamentos) {
				//obtem todos os gestores que estão em departamentos abaixo do avaliador.
				List<UnidadeDepartamentoTO> unidadeDepartamentoList = unidadeDepartamentoService.obterDepartamentoUnidadePorDepartamento(departamentoTO.getId());

				if (unidadeDepartamentoList != null && !unidadeDepartamentoList.isEmpty()) {
					for (UnidadeDepartamentoTO unidadeDepartamentoTO : unidadeDepartamentoList) {
						subordinadoList.add(unidadeDepartamentoTO.getGestor());
					}
				} else {
					//se o departamento "corrente" está abaixo do departamento do avaliador e não tem gestor, todos os funcionários do
					//departamento "corrente" serão inseridos como subordinados do avaliador
					buscarSubordinados(avaliador, subordinadoList, departamentoTO.getId(), unidadeDepartamento.getUnidade().getId());
				}
			}
		}

		return subordinadoList;
	}

	private void buscarSubordinados(FuncionarioTO avaliador, List<FuncionarioTO> subordinadoList, Long idDepartamento, Long idUnidade) {
		//obtem todos os funcionarios que trabalham naquela unidade e departamento
		List<FuncionarioTO> funcionarios = obterFuncionariosPorDepartamentoEUnidade(idDepartamento, idUnidade);

		if (funcionarios != null && !funcionarios.isEmpty()) {
			for (FuncionarioTO funcionarioTO : funcionarios) {

				if (!funcionarioTO.getNome().equals("Administrador")) {
					//se o funcionario for diferente do avaliador, ele entra na lista de subordinados
					if (!funcionarioTO.getId().equals(avaliador.getId()) && funcionarioTO.getSituacaoFuncional() != SituacaoFuncionario.DESLIGADO) {
						subordinadoList.add(funcionarioTO);
					}
				}
			}
		}
	}

	@Override
	public FuncionarioTO obterFuncionarioProjections(Long idFuncionario) {
		JPAQuery<Funcionario> query = baseQuery()
			.where(qFuncionario.id.eq(idFuncionario));

		return map(query.fetchOne());
	}

	@Override
	public List<FuncionarioTO> obterFuncionariosPorDepartamentoEUnidade(Long departamentoId, Long unidadeId) {
		JPAQuery<Funcionario> query = baseQuery()
			.where(
				qFuncionario.departamento.id.eq(departamentoId),
				qFuncionario.unidade.id.eq(unidadeId)
			);

		return map(query.fetch());
	}

	@Override
	public FuncionarioMobileTO obterFuncionarioMobile(FuncionarioMobileTO usuario) {
		FuncionarioTO funcionario = obterFuncionarioProjections(usuario.getId());
		FuncionarioMobileTO funcionarioMobileTO = converterParaMobileTO(funcionario);

		funcionarioMobileTO.setFotoBase64(null);
		List<CompetenciaMobileTO> list = new ArrayList<CompetenciaMobileTO>();

		for (CargoCompetenciaTO cargoCompetenciaTO : cargoCompetenciaService.findAllCargoCompetenciaByCargoAndAtivos(funcionario.getCargo().getId())) {
			converteCompetenciaMobile(list, cargoCompetenciaTO);
		}

		funcionarioMobileTO.setListaCompetencias(list);

		return funcionarioMobileTO;
	}

	@Override
	public List<FuncionarioTO> obterSubordinadosNaoSelecionadosPorAvaliadoEAvaliacao(FuncionarioTO avaliado, AvaliacaoTO avaliacao) {
		List<FuncionarioTO> subordinados = new ArrayList<>();

		List<FuncionarioTO> listaSubordinados = obterSubordinados(avaliado);
		List<ParticipanteTO> listaParticipantes = avaliacaoParticipanteService.obterParticipantesPorAvaliadoEAvaliacao(avaliado.getId(), avaliacao.getId());

		for (FuncionarioTO subordinado : listaSubordinados) {
			boolean encontrou = false;

			for (ParticipanteTO participante : listaParticipantes) {
				if (participante.getAvaliador().getId().equals(subordinado.getId())) {
					encontrou = true;
					break;
				}
			}

			if (!encontrou)
				subordinados.add(subordinado);
		}

		return subordinados;
	}

	@Override
	public List<FuncionarioTO> obterFuncionariosPorPerfilAndEmpresa(TipoPerfil perfil, Long idEmpresa) {
		return map(funcionarioRepository.findByTipoPerfilAndSituacaoAndEmpresaId(perfil, Situacao.ATIVA, idEmpresa));
	}

	@Override
	public List<FuncionarioMensagemTO> obterFuncionarioParticipantesDaAvaliacao(Long idAvaliacao) {
		List<FuncionarioMensagemTO> funcionarios = new ArrayList<>();

		Map<Long, TipoPerfilTO> idsFuncionarios = new HashMap<>();
		List<Participante> participantes = avaliacaoParticipanteRepository.findByAvaliacaoId(idAvaliacao);

		for (Participante participante : participantes) {

			if (!idsFuncionarios.keySet().contains(participante.getAvaliado().getId())) {
				TipoPerfilTO tipo = new TipoPerfilTO();
				tipo.setAutoAvaliacao(participante.getAvaliacao().getPreencheAv1());
				tipo.setPontoFocal(true);
				idsFuncionarios.put(participante.getAvaliado().getId(), tipo);
			} else {
				TipoPerfilTO tipo = idsFuncionarios.get(participante.getAvaliado().getId());
				tipo.setAutoAvaliacao(participante.getAvaliacao().getPreencheAv1());
				tipo.setPontoFocal(true);
			}

			if (!idsFuncionarios.keySet().contains(participante.getAvaliador().getId())) {
				TipoPerfilTO tipo = new TipoPerfilTO();

				if (participante.getPerfilAgente().equals(TipoAvaliacaoPerfil.GESTOR))
					tipo.setGestor(true);
				else if (participante.getPerfilAgente().equals(TipoAvaliacaoPerfil.SUBORDINADO))
					tipo.setSubordinado(true);
				else if (participante.getPerfilAgente().equals(TipoAvaliacaoPerfil.PARES))
					tipo.setPar(true);

				idsFuncionarios.put(participante.getAvaliador().getId(), tipo);
			} else {
				TipoPerfilTO tipo = idsFuncionarios.get(participante.getAvaliador().getId());

				if (participante.getPerfilAgente().equals(TipoAvaliacaoPerfil.GESTOR))
					tipo.setGestor(true);
				else if (participante.getPerfilAgente().equals(TipoAvaliacaoPerfil.SUBORDINADO))
					tipo.setSubordinado(true);
				else if (participante.getPerfilAgente().equals(TipoAvaliacaoPerfil.PARES))
					tipo.setPar(true);
			}


			if (participante.getRevisor() != null && participante.getRevisor().getId() != null) {

				if (!idsFuncionarios.keySet().contains(participante.getRevisor().getId())) {
					TipoPerfilTO tipo = new TipoPerfilTO();
					tipo.setGestor(true);
					idsFuncionarios.put(participante.getRevisor().getId(), tipo);
				} else {
					TipoPerfilTO tipo = idsFuncionarios.get(participante.getRevisor().getId());
					tipo.setGestor(true);
				}
			}
		}

		for (Long idFuncionario : idsFuncionarios.keySet()) {
			TipoPerfilTO tipoPerfilTO = idsFuncionarios.get(idFuncionario);
			FuncionarioTO funcionarioTO = findById(idFuncionario);
			FuncionarioMensagemTO funcionarioMensagemTO = mapper.map(funcionarioTO, FuncionarioMensagemTO.class);

			if (tipoPerfilTO.getPontoFocal() == null)
				tipoPerfilTO.setPontoFocal(false);

			if (tipoPerfilTO.getGestor() == null)
				tipoPerfilTO.setGestor(false);

			if (tipoPerfilTO.getSubordinado() == null)
				tipoPerfilTO.setSubordinado(false);

			if (tipoPerfilTO.getPar() == null)
				tipoPerfilTO.setPar(false);

			funcionarioMensagemTO.setTipoPerfis(tipoPerfilTO);
			funcionarios.add(funcionarioMensagemTO);
		}

		return funcionarios;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void faleConosco(FaleConoscoMobileTO to) {
		emailControleService.sendEmailFaleConosco(to);
	}

	@Override
	public List<FuncionarioTO> obterFuncionariosAtivosDoDepartamento(Long idDepartamento) {
		JPAQuery<Funcionario> query = baseQuery()
			.where(
				qFuncionario.departamento.id.eq(idDepartamento),
				qFuncionario.situacao.eq(Situacao.ATIVA),
				qFuncionario.empresa.id.eq(getLoggedUser().getEmpresa().getId())
			);

		return map(query.fetch());
	}

	@Override
	public List<FuncionarioTO> findByDepartamentoIdProjections(Long idDepartamento) {
		//TODO Verificar quem está usando isso, não parece fazer sentido pq é o filtro default de gerente de área
		JPAQuery<Funcionario> query = baseQuery()
			.where(qFuncionario.departamento.id.eq(idDepartamento));

		return map(query.fetch());
	}

	@Override
	public List<FuncionarioTO> findByCargoIdAndSituacaodProjections(Long idCargo, Situacao situacao) {
		JPAQuery<Funcionario> query = baseQuery()
			.where(
				qFuncionario.cargo.id.eq(idCargo),
				qFuncionario.situacao.eq(situacao)
			);

		return map(query.fetch());
	}

	@Override
	public List<FuncionarioTO> findBySituacaoProjections(SituacaoFuncionario situacao) {
		JPAQuery<Funcionario> query = baseQuery()
			.where(
				qFuncionario.situacaoFuncional.eq(situacao)
			).orderBy(qFuncionario.nome.asc());
		return map(query.fetch());
	}

	@Transactional
	public FuncionarioTO inactivate(FuncionarioTO object) throws ServiceException {
		object.setSituacaoFuncional(SituacaoFuncionario.DESLIGADO);
		return super.inactivate(object);
	}

	@Override
	@Transactional
	public FuncionarioTO activate(FuncionarioTO object) throws ServiceException {
		if (SituacaoFuncionario.DESLIGADO.equals(object.getSituacaoFuncional())) {
			object.setSituacaoFuncional(SituacaoFuncionario.EM_ATIVIDADE);
		}
		return super.activate(object);
	}

	private String transformMatricula(String matricula) {
		return matricula.toLowerCase();
	}

	private StringExpression transformMatricula(StringExpression matricula) {
		return matricula.lower();
	}

	/*
	Para o Gestor de Área, no PDI, serão exibidos os colaboradores que pertençam a área do gestor, e também os colaboradores
	de outros setores que foram avaliados como subordinados. Os dois métodos abaixo "obterColaboradoresPDI" e
	"loadColaboradorPDI" forma implementados para atender a essa demanda.
	 */
	@Override
	public List<FuncionarioTO> obterColaboradoresPDI() {
		UserTO user = getLoggedUser();

		JPAQuery<Funcionario> query = this.baseQuery();

		query.distinct()
				.where(qFuncionario.situacao.eq(Situacao.ATIVA));

		List<Funcionario> funcionariosSetor = query.fetch();

		Funcionario funcionario = new Funcionario();
		funcionario.setId(user.getId());
		List<Participante> participantes = participanteRepository.findByAvaliador(funcionario);
		Set<Funcionario> avaliados = participantes.stream()
				.filter(participante -> participante.getPerfilAgente().equals(TipoAvaliacaoPerfil.GESTOR))
				.filter(participante -> participante.getAvaliado().getSituacao().equals(Situacao.ATIVA))
				.map(Participante::getAvaliado).collect(Collectors.toSet());

		avaliados.addAll(funcionariosSetor);

		return map(avaliados);
	}

	@Override
	public FuncionarioTO loadColaboradorPDI(Long funcionarioId) {
		return map(repository.findOne(funcionarioId));
	}

	@Override
	protected OrderSpecifier<?>[] defaultOrder() {
		OrderSpecifier[] orderSpecifier = {new OrderSpecifier(Order.ASC, QFuncionario.funcionario.nome)};
		return orderSpecifier;
	}
}
