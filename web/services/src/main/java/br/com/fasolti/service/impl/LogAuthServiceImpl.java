package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.LogAuth;
import br.com.fasolti.avd.domain.QLogAuth;
import br.com.fasolti.avd.repository.LogAuthRepository;
import br.com.fasolti.avd.service.LogAuthService;
import br.com.fasolti.avd.transfer.LogAuthTO;
import br.com.fasolti.avd.types.SituacaoAuth;
import br.com.fasolti.commons.services.BaseService;
import com.querydsl.core.types.Projections;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class LogAuthServiceImpl	extends BaseService<LogAuth, LogAuthTO> implements LogAuthService {

	private final int USERNAME_ERROR_WINDOW = 10;
	private final int MAX_USERNAME_ERRROS = 5;
	private final int IP_ERROR_WINDOW = 20;
	private final int MAX_IP_ERRORS = 25;

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	public LogAuthServiceImpl(LogAuthRepository repository) {
		super(repository);
	}

	private final QLogAuth qLogAuth = QLogAuth.logAuth;

	@Override
	@Transactional
	public boolean checkUsernameBlocked(String user, String remoteAddr) throws Exception {
		List<LogAuth> situacaoListUser = situacaoListUser(user);
		if (situacaoListUser.stream().anyMatch(s -> s.getSituacao().equals(SituacaoAuth.USUARIO_BLOQUEADO))) {
			logger.warn("Usuário {} bloqueado",user);
			addLog(user,remoteAddr,SituacaoAuth.USUARIO_BLOQUEADO);
			return true;
		}

		if (situacaoListUser.stream().filter(s -> s.getSituacao().equals(SituacaoAuth.AUTENTICACAO_INVALIDA)).count() >= MAX_USERNAME_ERRROS) {
			logger.warn("Usuário {} com autenticação inválida",user);
			addLog(user,remoteAddr,SituacaoAuth.USUARIO_BLOQUEADO);
			return true;
		}

		return false;
	}

	@Override
	@Transactional
	public boolean checkIpBlocked(String user, String remoteAddr) throws Exception {
		if (situacaoIp(user, remoteAddr)) {
			logger.warn("Usuário {} com IP {} bloqueado",user,remoteAddr);
			addLog(user,remoteAddr,SituacaoAuth.IP_BLOQUEADO);
			return true;
		}
		return false;
	}

	private List<LogAuth> situacaoListUser(String user) throws Exception {
		Date agora = Date.from(Instant.now());
		Date aPartir = Date.from(Instant.now().minus(USERNAME_ERROR_WINDOW, ChronoUnit.MINUTES));
		return select(
			Projections.fields(LogAuth.class,qLogAuth.situacao))
		.where(
			qLogAuth.userName.eq(user)
				.and(qLogAuth.data.between(aPartir,agora)
					.and(qLogAuth.situacao.eq(SituacaoAuth.AUTENTICACAO_INVALIDA)
						.or(qLogAuth.situacao.eq(SituacaoAuth.USUARIO_BLOQUEADO))))
			)
		.fetch();
	}

	private Boolean situacaoIp (String user, String remoteAddr) throws Exception {
		Date agora = Date.from(Instant.now());
		Date aPartir = Date.from(Instant.now().minus(IP_ERROR_WINDOW, ChronoUnit.MINUTES));
		return select(
			Projections.fields(LogAuth.class,qLogAuth.situacao))
			.where(
				qLogAuth.ip.eq(remoteAddr)
					.and(qLogAuth.userName.eq(user))
					.and(qLogAuth.data.between(aPartir,agora)
					.and(qLogAuth.situacao.eq(SituacaoAuth.AUTENTICACAO_INVALIDA)
						.or(qLogAuth.situacao.eq(SituacaoAuth.USUARIO_BLOQUEADO))) )
					)
			.fetchCount() > MAX_IP_ERRORS;
	}

	@Override
	@Transactional
	public void addLog(String user, String remoteAddr, SituacaoAuth situacao) {
		repository.save(new LogAuth.Builder(user, situacao).setData(Date.from(Instant.now())).setIp(remoteAddr).build());
	}
}
