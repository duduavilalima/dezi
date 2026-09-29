package br.com.fasolti.avd.rest;

import br.com.fasolti.avd.rest.properties.BuildProperties;
import br.com.fasolti.avd.service.EmpresaService;
import br.com.fasolti.avd.service.FotoService;
import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.service.LogAuthService;
import br.com.fasolti.avd.transfer.FotoTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.UserTO;
import br.com.fasolti.avd.transfer.mobile.FuncionarioMobileTO;
import br.com.fasolti.avd.transfer.simple.EmpresaAuthSimpleTO;
import br.com.fasolti.avd.transfer.util.AuthInfo;
import br.com.fasolti.avd.types.SituacaoAuth;
import br.com.fasolti.avd.util.UsernameParser;
import br.com.fasolti.commons.client.auth.TokenTransfer;
import br.com.fasolti.commons.lang.ValidationException;
import br.com.fasolti.commons.rest.BaseResource;
import br.com.fasolti.commons.security.token.TokenUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@RestController
@RequestMapping("auth")
public class AuthResource extends BaseResource<FuncionarioTO> {
	private static final String LOGIN = "login";
	private static final String FORGOTPASSWORD = "forgotpassword";
	private static final String EMPRESAS = "empresas";
	private static final String EMPRESAS_MOBILE = "mobile/empresas";

	@Value("${environment.name}")
	private String environmentName;

	@Autowired(required = false)
	private Logger logger;

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private LogAuthService logAuthService;

	@Autowired
	private TokenUtils tokenUtils;

	@Autowired
	private EmpresaService empresaService;

	@Autowired
	private FotoService fotoService;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private BuildProperties buildProperties;

	@Autowired
	public AuthResource(FuncionarioService service) {
		super(service);
	}

	/*
	 * FIXME: Após vários teste, descobrimos que o backend lança exceção na segunda chamada GET.
	 * Devido a isso, foi necessário criar um método getUser para o verbo POST. Após isso,
	 * não houve mais erro de acesso pelo mobile.
	 * MOBILE SÓ PODERÁ USAR MÉTODOS POST.
	 */
	@RequestMapping(method = RequestMethod.POST, path = "/mobile/user")
	public FuncionarioMobileTO getMobileUser() {
		UserTO usuario = funcionarioService.getLoggedUser();

		FuncionarioMobileTO funcionarioMobile = new FuncionarioMobileTO();

		FotoTO foto = fotoService.obterFotoPerfilFuncionario(usuario.getId());

		if (foto != null) {
			funcionarioMobile.setFotoBase64(foto.getBase64());
		}

		//TODO Usar o Dozer
		funcionarioMobile.setId(usuario.getId());
		funcionarioMobile.setNome(usuario.getNome());
		funcionarioMobile.setEmail(usuario.getEmailFuncional());

		return funcionarioMobile;
	}

	public Authentication authenticate(String username, String password) {
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(username, password);
		return authenticationManager.authenticate(authenticationToken);
	}

	@RequestMapping(method = RequestMethod.POST, path = LOGIN)
	public ResponseEntity<?> login(@RequestBody AuthInfo authInfo) {
		String username = UsernameParser.compose(authInfo.getEmpresaId(), authInfo.getMatricula());

		try {
			if (logAuthService.checkIpBlocked(username, request.getRemoteAddr())) {
				return ResponseEntity.status(404).body(null);
			}
			if (logAuthService.checkUsernameBlocked(username, request.getRemoteAddr())) {
				return ResponseEntity.status(403).body(null);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getCause());
		}

		try {
			authenticate(username, authInfo.getSenha());
		} catch(AuthenticationException e) {
			logAuthService.addLog(username, request.getRemoteAddr(), SituacaoAuth.AUTENTICACAO_INVALIDA);
			return ResponseEntity.status(401).body(null);
		}

		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		logAuthService.addLog(username, request.getRemoteAddr(), SituacaoAuth.LOGIN_OK);

		return ResponseEntity.ok(new TokenTransfer(tokenUtils.createAPIToken(userDetails)));
	}

	@RequestMapping(method = RequestMethod.POST, path = "seclogin")
	public TokenTransfer secondaryLogin(@RequestBody AuthInfo authInfo) {
		String username = UsernameParser.compose(authInfo.getEmpresaId(), authInfo.getMatricula());
		try {
			authenticate(username, authInfo.getSenha());
		} catch(AuthenticationException e) {
			logger.error(e.getMessage(),e);
			throw new ValidationException("password-not-match", "consenso");
		}
		/*
		 * Reload user as password of authentication principal will be null after authorization and
		 * password is needed for token generation
		 */
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		return new TokenTransfer(tokenUtils.createAPIToken(userDetails));
	}

	@RequestMapping(method = RequestMethod.POST, value = FORGOTPASSWORD)
	public final ResponseEntity<Void> forgotPassword(@RequestBody FuncionarioTO funcionario) {
		funcionarioService.forgotPassword(funcionario.getEmailFuncional(), funcionario.getEmpresa().getId());
		return ResponseEntity.noContent().build();
	}

	@RequestMapping(method = RequestMethod.GET, path = EMPRESAS)
	public List<EmpresaAuthSimpleTO> empresas(
		@RequestHeader(value = "Origin", required = false) String originHeader,
		@RequestHeader(value = "Referer", required = false) String refererHeader,
		@RequestHeader(value = "Host", required = false) String hostHeader
	) {
		return listEmpresasByHostInfo(originHeader, refererHeader, hostHeader, false);
	}

	@RequestMapping(method = RequestMethod.GET, path = EMPRESAS_MOBILE)
	public List<EmpresaAuthSimpleTO> empresasMobile(
		@RequestHeader(value = "Origin", required = false) String originHeader,
		@RequestHeader(value = "Referer", required = false) String refererHeader,
		@RequestHeader(value = "Host", required = false) String hostHeader
	) {
		return listEmpresasByHostInfo(originHeader, refererHeader, hostHeader, true);
	}

	@RequestMapping(method = RequestMethod.GET, path = "version")
	public String version() throws IOException {
		ClassLoader classLoader = AuthResource.class.getClassLoader();
		InputStream stream = classLoader.getResourceAsStream("build.properties");

		if (stream == null)
			return "ERROR";

		Properties prop = new Properties();
		prop.load(stream);

		return prop.getProperty("version") + "-" + prop.getProperty("build.date");
	}

	@RequestMapping(method = RequestMethod.GET, path = "buildInfo")
	public Map<String, BuildProperties> buildInfo() {
		Map<String, BuildProperties> buildInfo = new HashMap<>();
		buildInfo.put(String.format("%s/%s", buildProperties.getProjectGroup(), buildProperties.getProjectName()), buildProperties);
		return buildInfo;
	}

	private String extractHostname(String headerValue) {
		String hostName = null;

		try {
			URL url = new URL(headerValue);
			hostName = url.getHost();
		} catch (MalformedURLException ex) {}

		if (hostName != null && !hostName.isEmpty()) {
			return hostName;
		}

		return null;
	}

	private String removeMobileHostnameSuffix(String hostName) {
		String[] hostParts = hostName.split("\\.");
		hostParts[0] = hostParts[0].replaceFirst("mobile$", "");
		return String.join(".", hostParts);
	}

	private List<EmpresaAuthSimpleTO> listEmpresasByHostInfo(
		String originHeader,
		String refererHeader,
		String hostHeader,
		boolean forMobile
	) {
		String hostName = null;
		List<EmpresaAuthSimpleTO> empresas;

		if (originHeader != null) {
			hostName = extractHostname(originHeader);
		}

		if (hostName == null && refererHeader != null) {
			hostName = extractHostname(refererHeader);
		}

		if (hostName == null && hostHeader != null && !hostHeader.isEmpty()) {
			hostName = hostHeader;
		}

		if ("production".equals(environmentName) && hostName != null && !hostName.isEmpty()) {
			if (forMobile) {
				hostName = removeMobileHostnameSuffix(hostName);
			}

			// Se torna necessário listar todas as empresas se uma empresa com o hostname exato não for encontrada
			// por conta dos apps híbridos (compilados para APK/IPA). Eles não possuem Origin/etc, efetivamente.
			// Nestes casos, todas as empresas devem ser listadas.
			empresas = empresaService.empresasAuthAtivasByHost(hostName, forMobile);
		} else {
			empresas = empresaService.empresasAuthAtivas();
		}

		return empresas;
	}
}
