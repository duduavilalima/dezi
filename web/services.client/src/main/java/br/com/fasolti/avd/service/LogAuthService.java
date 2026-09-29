package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.LogAuthTO;
import br.com.fasolti.avd.types.SituacaoAuth;
import br.com.fasolti.commons.client.service.Service;

public interface LogAuthService extends Service<LogAuthTO> {

	boolean checkUsernameBlocked(String user, String remoteAddr) throws Exception;
	boolean checkIpBlocked(String user, String remoteAddr) throws Exception;
	void addLog(String user, String remoteAddr, SituacaoAuth situacao);

}
