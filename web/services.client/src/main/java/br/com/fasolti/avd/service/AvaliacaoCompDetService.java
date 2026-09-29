package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.AvaliacaoCompDetTO;
import br.com.fasolti.avd.types.TipoAvaliacaoPerfil;

import java.util.List;

public interface AvaliacaoCompDetService extends AvdService<AvaliacaoCompDetTO, AvaliacaoCompDetTO> {

	List<AvaliacaoCompDetTO> listByAvaliacaoCompetenciaId(Long avaliacaoCompetenciaId);

	List<AvaliacaoCompDetTO> listByAvaliacaoCompetenciaAndAvaliadorWithPreenchimentoAndHint(Long avaliacaoCompetenciaId, Long avaliadorId);

	AvaliacaoCompDetTO obterAvaliacaoCompDetProjections(Long avalCompId, Long idCompDet, Long avaliacaoCompDetId);

}
