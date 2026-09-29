package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.types.SituacaoAvaliacao;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;
import br.com.fasolti.commons.lang.ServiceException;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.Date;
import java.util.List;

public interface PreenchimentoService extends AvdService<PreenchimentoTO, PreenchimentoTO> {

	List<PreenchimentoTO> findByAvaliacao(AvaliacaoTO avaliacao);

	List<PreenchimentoTO> listByParticipante(ParticipanteTO participante);

	List<PreenchimentoTO> reset(ParticipanteTO participante) throws ServiceException;

	List<PreenchimentoTO> findByCargo(CargoTO cargo);

	PreenchimentoTO findByAvaliacaoCompetencia(AvaliacaoCompetenciaTO avaliacaoCompetencia);

	List<PreenchimentoTO> listByAvaliacaoCompDetId(Long avaliacaoCompetenciaId);

	List<PreenchimentoTO> listByAvaliacaoCompDetAndAvaliador(Long avaliacaoCompetenciaId, Long avaliadorId);

	List<PreenchimentoTO> listByAvaliacaoCompDetAndAvaliado(Long avaliacaoCompetenciaId, Long avaliadoId);

	Long count(BooleanExpression eq);

	PreenchimentoTO obterPreenchimentoProjections(Long participanteId, Long avaliacaoCompetenciaId);

	List<PreenchimentoTO> obterListaDePreenchimentoDoParticipante(ParticipanteTO participanteTO);

	List<PreenchimentoTO> preenchimentosByParticipante(Long participanteId);

	List<PreenchimentoTO> listByAvaliadoAndLimiteAndCompetenciaProjections(Long avaliadoId, Date data, SituacaoAvaliacao situacao, Long avaliacaoCompetenciaId);

	List<PreenchimentoTO> listByAvaliadorAndLimiteAndCompetenciaProjections(Long avaliadoId, Date data, SituacaoAvaliacao situacao, Long avaliacaoCompetenciaId);

	List<PreenchimentoTO> listByParticipanteSemAvalPesoCargoComp(Long idParticipante);

}
