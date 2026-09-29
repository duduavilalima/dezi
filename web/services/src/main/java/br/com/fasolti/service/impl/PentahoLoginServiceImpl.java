package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Empresa;
import br.com.fasolti.avd.repository.EmpresaRepository;
import br.com.fasolti.avd.service.PentahoLoginService;
import br.com.fasolti.avd.service.domain.SimpleCredentials;
import br.com.fasolti.avd.transfer.EmpresaTO;
import br.com.fasolti.avd.transfer.PentahoSessionTO;
import br.com.fasolti.service.lang.PentahoInvalidCredentialsException;
import br.com.fasolti.service.lang.PentahoLoginException;
import br.com.fasolti.service.lang.PentahoReusedSessionException;
import br.com.fasolti.service.util.BaseAvdService;
import org.apache.http.*;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PentahoLoginServiceImpl extends BaseAvdService<Empresa, EmpresaTO, EmpresaTO> implements PentahoLoginService {
	@Value("${pentaho.server}")
	private String pentahoServer;

	@Value("${pentaho.username}")
	private String pentahoUser;

	@Value("${pentaho.password}")
	private String pentahoPassword;

	@Autowired
	public PentahoLoginServiceImpl(EmpresaRepository repository) {
		super(repository);
	}

	@Override
	public PentahoSessionTO login() throws PentahoLoginException {
		String sessionId;

		try {
			try {
				sessionId = this.fetchSession();
			} catch (PentahoReusedSessionException e) {
				// Por algum motivo o Pentaho retorna esse erro quando há 2 logins seguidos, então tenta mais uma vez
				sessionId = this.fetchSession();
			}
		} catch (IOException e) {
			throw new PentahoLoginException("Request error", e);
		}

		return new PentahoSessionTO(pentahoServer, sessionId);
	}

	private String fetchSession() throws IOException, PentahoLoginException {
		SimpleCredentials credentials = loadCredentials();

		String loginEndpoint = this.pentahoServer + "/pentaho/j_spring_security_check";

		List<NameValuePair> loginData = Form.form()
			.add("j_username", credentials.getUsername())
			.add("j_password", credentials.getPassword())
			.build();

		Request request =  Request.Post(loginEndpoint).bodyForm(loginData);

		return request.execute().handleResponse(response -> {
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() != HttpStatus.SC_MOVED_TEMPORARILY) {
				throw new PentahoLoginException(String.format("Invalid status (%d)", statusLine.getStatusCode()));
			}

			Header locationHeader = response.getFirstHeader("Location");

			if (locationHeader == null) {
				throw new PentahoLoginException("Invalid response");
			}

			String locationValue =  locationHeader.getValue();

			if (locationValue.contains("login_error=1")) {
				throw new PentahoInvalidCredentialsException(credentials);
			}

			if (locationValue.contains("login_error=2")) {
				throw new PentahoReusedSessionException();
			}

			String sessionId = null;
			Header[] cookieHeaders = response.getHeaders("Set-Cookie");

			if (cookieHeaders.length == 0) {
				throw new PentahoLoginException("Login response without session");
			}

			for (Header header : cookieHeaders) {
				HeaderElement[] elements = header.getElements();
				if ("JSESSIONID".equals(elements[0].getName())) {
					sessionId = elements[0].getValue();
					break;
				}
			}

			if (sessionId == null) {
				throw new PentahoLoginException("Invalid session in response");
			}

			return sessionId;
		});
	}

	private SimpleCredentials loadCredentials() {
		String identificador = getLoggedUser().getEmpresa().getIdentificador();
		Integer versao = getLoggedUser().getEmpresa().getVersaoBi();

		String particularLogin = pentahoUser;
		if (versao.equals(1) && identificador != null && !identificador.isEmpty()) {
			particularLogin += "_" + identificador;
		}

		return new SimpleCredentials(particularLogin, pentahoPassword);
	}

}
