package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.*;

import java.util.List;
import java.util.Set;

public interface AvaliacaoCompetenciaService extends AvdService<AvaliacaoCompetenciaTO, AvaliacaoCompetenciaTO> {

	List<AvaliacaoCompetenciaTO> listByParticipante(ParticipanteTO participante);

	List<AvaliacaoCompetenciaTO> listByCompetenciaDetalhamento(CompetenciaDetalhamentoTO competenciaDetalhamento);

	List<AvaliacaoCompetenciaTO> listByAvaliador(FuncionarioTO funcionario);

	List<AvaliacaoCompetenciaTO> listByAvaliacaoAndAvaliadorAndCompetencia(AvaliacaoTO avaliacao, FuncionarioTO funcionario, CompetenciaTO competencia);

	List<AvaliacaoCompetenciaTO> listByParticipanteAndDetalhamento(ParticipanteTO participante, Set<CompetenciaDetalhamentoTO> detalhamentos);

	AvaliacaoCompetenciaTO getByAvaliacaoIdAndIdCompetencia(Long avaliacaoId, Long competenciaId);

	List<AvaliacaoCompetenciaTO> listByIdCompetencia(Long competenciaId);

	AvaliacaoCompetenciaTO obterAvaliacaoCompetenciaProjections(Long avaliacaoId, Long competenciaId);
}
