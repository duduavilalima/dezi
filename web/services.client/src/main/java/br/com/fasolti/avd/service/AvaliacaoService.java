package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.CampanhaTO;
import br.com.fasolti.avd.transfer.FuncionarioTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.campanhasrapidas.HomeOfficeBrasilTO;
import br.com.fasolti.avd.transfer.mobile.AvaliacaoSyncMobileTO;
import br.com.fasolti.avd.transfer.search.AvaliacaoSearchTO;
import br.com.fasolti.avd.transfer.simple.FuncionarioSimpleTO;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.List;

public interface AvaliacaoService extends AvdService<AvaliacaoTO, AvaliacaoSearchTO> {

	List<AvaliacaoTO> listByCampanha(Long id);

	List<AvaliacaoTO> listarAvaliacoesLiberadasDaCampanha(Long id);

	List<AvaliacaoTO> listByCampanha(CampanhaTO campanha);

	AvaliacaoTO copiar(AvaliacaoTO avaliacaoTO);

	List<ParticipanteTO> obterSubordinadosPorAvaliado(FuncionarioSimpleTO avaliado, AvaliacaoTO avaliacao);

	FuncionarioSimpleTO homeOfficeBrasilSalvarParticipante(FuncionarioTO funcTO) throws CloneNotSupportedException;

	FuncionarioTO homeOfficeBrasilCriarFuncionario(HomeOfficeBrasilTO objectTO);

	void removerAvaliado(List<ParticipanteTO> listaParticipantes, AvaliacaoTO avaliacao);

	void removerTodosParticipantes(AvaliacaoTO avaliacaoTO);

	void removeragliberacao(Long id);

	Long count(CampanhaTO campanhaTO);

	AvaliacaoSyncMobileTO obterAvaliacoesDoAvaliadorMobile(AvaliacaoSyncMobileTO avaliacaoMobileTO);

	void salvarParticipantes(AvaliacaoTO avaliacao) throws CloneNotSupportedException;

	void salvarAvaliadores(AvaliacaoTO avaliacao) throws CloneNotSupportedException;

	void salvarAvaliadoresLiberacao(AvaliacaoTO avaliacao) throws CloneNotSupportedException;

	void liberar(AvaliacaoTO avaliacao);

	List<ParticipanteTO> importarPares(InputStream file);

	List<ParticipanteTO> salvarPares(List<ParticipanteTO> pares);

	List<AvaliacaoTO> obterAvaliacoesPDI();

	void ajustarAvalPesoCargoComp();
}
