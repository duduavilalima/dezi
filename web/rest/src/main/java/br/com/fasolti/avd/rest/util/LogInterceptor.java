package br.com.fasolti.avd.rest.util;

import br.com.fasolti.avd.service.FuncionarioService;
import br.com.fasolti.avd.service.LogEntityService;
import br.com.fasolti.avd.transfer.LogEntityTO;
import br.com.fasolti.avd.transfer.UserTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class LogInterceptor extends HandlerInterceptorAdapter {
	private static final List<String> LOGGED_METHODS = Arrays.asList("POST", "PUT", "DELETE");

	@Autowired
	private FuncionarioService funcionarioService;

	@Autowired
	private LogEntityService logEntityService;

	@Autowired(required = false)
	private Logger logger;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		try {
			String requestMethod = request.getMethod();

			if (!LOGGED_METHODS.contains(requestMethod)) {
				return;
			}

			if (!(handler instanceof HandlerMethod)) {
				return;
			}

			HandlerMethod handlerMethod = (HandlerMethod) handler;
			LogRequired logRequired = handlerMethod.getMethod().getAnnotation(LogRequired.class);
			if (logRequired == null)
				return;

			ByteArrayOutputStream requestBody = (ByteArrayOutputStream) request.getAttribute(CustomJsonHttpMessageConverter.REQUEST_BODY_ATTRIBUTE_NAME);
			if (requestBody == null) {
				return;
			}

			String stringJson = requestBody.toString();
			if (stringJson == null) {
				return;
			}

			stringJson = stringJson.trim();

			UserTO funcionario = funcionarioService.getLoggedUser();

			if (isBulkOperation(stringJson)) {
				saveLog(new JSONArray(stringJson), funcionario, request, logRequired);
			} else if (isSingleOperation(stringJson)) {
				saveLog(new JSONObject(stringJson), funcionario, request, logRequired);
			} else {
				logger.warn("JSON de requisição ou configuração inválido para ação \"{}\". Verifique a anotação e/ou implementação em \"{}#{}\".",
					request.getRequestURI(),
					handlerMethod.getMethod().getClass().getName(), handlerMethod.getMethod().getName());
			}

		} catch (Exception writeEx) {
			logger.error("Erro ao criar entrada de log para requisição '{}'!", request.toString(), writeEx);
		}
	}

	private void saveLog(JSONArray jsonArray, UserTO funcionario, HttpServletRequest request, LogRequired logRequired) {
		long idx = 0;
		for (Object value : jsonArray) {
			if (value instanceof JSONObject) {
				try {
					saveLog((JSONObject) value, funcionario, request, logRequired);

				} catch (Exception ex) {
					logger.error("Erro ao criar entrada de log para elemento de posição \"{}\" na array de resposta à requisição '{}'!", idx, request.toString(), ex);
				}

			} else {
				logger.error("Tipo de valor JSON \"{}\" (na posição \"{}\" da array JSON de resposta à  URI \"{}\") não é um objeto válido para logging.", value.getClass().getTypeName(), idx, request.getRequestURI());
			}

			idx++;
		}
	}

	private void saveLog(JSONObject jsonObject, UserTO funcionario, HttpServletRequest request, LogRequired logRequired) {
		Integer entidadeId;

		if(logRequired.entity().equals("TrocaDeSenha")) {
			entidadeId = (Integer) jsonObject.getJSONObject("funcionario").get("id");
		} else {
			entidadeId = (Integer) jsonObject.get("id");
		}

		LogEntityTO logEntity = new LogEntityTO();
		logEntity.setEntidadeId(Long.parseLong(entidadeId.toString()));
		logEntity.setDataCriacao(LocalDateTime.now());
		logEntity.setNomeEntidade(logRequired.entity());
		logEntity.setUsuario(funcionario.getMatricula());
		logEntity.setObjeto(jsonObject.toString(0));
		logEntity.setUri(request.getRequestURI());

		logEntityService.save(logEntity);
	}

	private boolean isBulkOperation(String requestJson) {
		return requestJson.startsWith("[");
	}

	private boolean isSingleOperation(String requestJson) {
		return requestJson.startsWith("{");
	}
}
