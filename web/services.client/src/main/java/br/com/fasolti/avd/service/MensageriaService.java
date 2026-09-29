package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.MensageriaTO;
import br.com.fasolti.avd.transfer.search.MensageriaSearchTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;

import java.util.List;
import java.util.Map;

public interface MensageriaService extends AvdService<MensageriaTO, MensageriaSearchTO> {

	List<MensageriaTO> saveAndSendMensagensAvaliacao(List<MensageriaTO> listDeMensagens);

	//TODO: Ver o template do email
	void homeOfficeBrasilMensagemBoasVindas(FuncionarioSimpleTO to);

	//TODO: Ver o template do email e quais são os ID's e nome do administrador que enviará o email como remetente
	void homeOfficeBrasilMensagemAmbienteEmpresa(FuncionarioTO to);

	void mensagemLiberarAvaliacao(AvaliacaoTO to);

	void mensagemLiberarAvaliacaoConcluido(AvaliacaoTO to);

	void mensagemLiberarAvaliacaoErro(AvaliacaoTO to, Exception e);

	Map<String, List<MensageriaTO>> mensagensPorUsuario(Long userId);

	List<MensageriaTO> getAllMessagesRecebidasByAvaliacao(AvaliacaoTO avaliacao);

	List<MensageriaTO> getAllMessagesByAvaliacao(AvaliacaoTO avaliacao);

	List<MensageriaTO> destinatariosMensagem(MensageriaTO mensageria);

	MensageriaSearchTO quantidadeMensagensNaoLidas(Long userId);

	void removerMensageria(long id);

}
