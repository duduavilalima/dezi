package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.CompetenciaPreenchimentoTO;
import br.com.fasolti.avd.transfer.ParticipanteTO;
import br.com.fasolti.avd.transfer.search.ParticipanteSearchTO;

import java.util.List;

public interface ParticipanteService extends AvdService<ParticipanteTO, ParticipanteSearchTO> {

	List<ParticipanteTO> listAllAvailable();

	List<ParticipanteTO> obterParticipantesPorAvaliacaoAndEmpresa(Long idAvaliacao);

	List<ParticipanteTO> obterParticipantesPorAvaliadorAndAvaliacao(Long idAvaliador, Long idAvaliacao);

	List<ParticipanteTO> obterParticipantesPorCampanhaAndEmpresa(Long idCampanha);

	List<ParticipanteTO> obterParticipantesPorAvaliadorUnidadeAndEmpresa(Long idUnidade);

	List<ParticipanteTO> obterParticipantesPorAvaliadorDepartamentoAndEmpresa(Long idUnidade);

	List<ParticipanteTO> obterParticipantesPorAvaliacaoAndUnidadeAndEmpresa(Long idAvaliacao, Long idUnidade);

	List<ParticipanteTO> obterParticipantesPorAvaliacaoAndUnidadeAndDepartamentoAndEmpresa(Long idAvaliacao, Long idUnidade, Long idDepartamento);

	List<ParticipanteTO> obterParticipantesPorAvaliacaoAndDepartamentoAndEmpresa(Long idAvaliacao, Long idDepartamento);

	CompetenciaPreenchimentoTO salvarCompetencia(CompetenciaPreenchimentoTO competenciaPreenchimentoTO, Long idParticipante);
}
