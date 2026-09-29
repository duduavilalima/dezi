package br.com.fasolti.service.impl;

import br.com.fasolti.avd.domain.Parametro;
import br.com.fasolti.avd.repository.ParametroRepository;
import br.com.fasolti.avd.service.BugReportService;
import br.com.fasolti.avd.service.EmailControleService;
import br.com.fasolti.commons.Constants;
import br.com.fasolti.commons.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BugReportServiceImpl implements BugReportService {

	@Autowired
	private EmailControleService emailControleService;

	@Autowired
	private ParametroRepository parametroRepository;

	@Override
	public final void notificateBug(String title, String description, Object... objects) {
		Parametro emailParam = parametroRepository.findOne("mail.receiving.notification.bug.report");
		List<String> emails = Util.stringTokenizer(emailParam.getValor(), Constants.SEMICOLON);

		Parametro assunto = parametroRepository.findOne("mail.bug.report.subject");

		StringBuilder subject = new StringBuilder();
		subject.append(assunto.getValor());

		if (title != null && !title.isEmpty()) {
			subject
					.append(Constants.SPACE)
					.append(Constants.HIFEN)
					.append(Constants.SPACE)
					.append(title);
		}

		StringBuilder message = new StringBuilder();
		message
			.append("Find a BUG!").append(Constants.HTML_LINE)
			.append(String.format("Title: %s", title)).append(Constants.HTML_LINE)
			.append(String.format("Description: %s", description)).append(Constants.HTML_LINE)
			.append(String.format("Date: %s", Util.dateStringNow())).append(Constants.HTML_LINE);

		if (objects != null) {
			for (Object obj : objects) {
				message
					.append(String.format("Class: %s", obj.getClass().getSimpleName()))
					.append(Constants.HTML_LINE)
					.append(String.format("ToString: %s", obj.toString()))
					.append(Constants.HTML_LINE).append(Constants.HTML_LINE).append(Constants.HTML_LINE);

				if (obj instanceof Throwable) {
					message.append("StackTraceStart start!").append(Constants.HTML_LINE);
					Throwable e = (Throwable) obj;
					List<Throwable> list = getFormatedStackTrace(e);
					for (Throwable t : list) {
						message
							.append(String.format("Exception: %s", t.getClass().getSimpleName()))
							.append(Constants.HTML_LINE)
							.append(String.format("Message: %s", t.getLocalizedMessage()))
							.append(Constants.HTML_LINE);
					}
					message.append("StackTraceStart end!").append(Constants.HTML_LINE);
				}
			}
		}

		emails.forEach(email -> emailControleService.sendEmailNotificateBug(email, subject.toString(), message.toString()));
	}

	private List<Throwable> getFormatedStackTrace(Throwable t) {
		List<Throwable> list = new ArrayList<>();

		int limit = 30;
		int num = 0;
		int nullMessageLimit = 3;
		int nullMessages = 0;

		boolean inLoop = true;

		while (inLoop) {
			list.add(t);
			num++;

			if (t.getLocalizedMessage() == null || t.getLocalizedMessage().isEmpty()) {
				nullMessages++;
			}

			if (t instanceof SQLException) {
				SQLException sqle = (SQLException) t;
				if (sqle.getNextException() != null) {
					list.add(sqle.getNextException());
				}
			}
			t = t.getCause();

			inLoop = (t != null && limit > num && nullMessageLimit > nullMessages);
		}

		return list;
	}

}
