package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.AvaliacaoTO;
import br.com.fasolti.avd.transfer.CompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.DetalhamentoCargoCompetenciaTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.search.ParticipanteSearchTO;

import java.util.List;
import java.util.Set;

public interface AvaliacaoParticipanteService extends AvdService<ParticipanteTO, ParticipanteSearchTO> {

	Long obterTotalAvaliacoesPorAvaliacao(AvaliacaoTO avaliacao);

	List<ParticipanteTO> obterListaParticipantesPorAvaliacao(AvaliacaoTO avaliacao);

	ParticipanteTO liberaAvaliacaoConsenso(ParticipanteTO participante);

	ParticipanteTO atualizaAvaliadorAndRevisor(ParticipanteTO participante);

	List<AvaliacaoTO> listarAvaliacoesPorAvaliado(Long avaliadoId);

	List<ParticipanteTO> listarAvaliacoesComPDIPorAvaliado(Long avaliadoId);

	List<ParticipanteTO> obterParticipantesPorAvaliadoEAvaliacao(Long avaliadoId, Long avaliacaoId);

    List<ParticipanteTO> obterParticipanteProjections(Long avaliadoId, Long avaliadorId, Long revidorId, Long avaliacaoId);

    void removerParticipanteAgLiberacao(Long id);

	void removerTodasPerguntasDoParticipante(ParticipanteTO participante);

	List<ParticipanteTO> obterAvaliadosPorAvaliacao(Long idAvaliacao);

	void salvarPerguntas(ParticipanteTO object, List<DetalhamentoCargoCompetenciaTO> perguntas, Set<CompetenciaDetalhamentoTO> detalhamentosSalvos);

	Integer qtdAvaliadosPorAvaliacao(Long idAvaliacao);

	boolean temAvaliadores(Long avaliacaoId, Long avaliadoId);

	//TODO: Passar isso para o serviceFilter e ver se nao tera side-effects
	List<ParticipanteTO> obterAvaliacaoParticipantePDI();
}
