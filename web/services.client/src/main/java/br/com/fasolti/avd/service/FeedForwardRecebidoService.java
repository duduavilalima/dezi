package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.ApontamentoFuncionalTO;
import br.com.fasolti.avd.transfer.mobile.FeedForwardMobileTO;
import br.com.fasolti.avd.transfer.mobile.UsuarioMobileTO;

import java.util.List;

public interface FeedForwardRecebidoService extends AvdService<ApontamentoFuncionalTO, ApontamentoFuncionalTO> {

	List<FeedForwardMobileTO> obterFeedForwardsRecebidos(UsuarioMobileTO avaliado);

}
