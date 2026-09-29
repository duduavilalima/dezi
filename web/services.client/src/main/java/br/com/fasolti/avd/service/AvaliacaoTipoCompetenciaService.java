package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.AvaliacaoTipoCompetenciaTO;

import java.util.List;

public interface AvaliacaoTipoCompetenciaService extends AvdService<AvaliacaoTipoCompetenciaTO, AvaliacaoTipoCompetenciaTO> {

	List<AvaliacaoTipoCompetenciaTO> obterAvaliacaoTipoCompetenciaProjections(Long idAvaliacao);

	List<AvaliacaoTipoCompetenciaTO> findIdsTiposCompetenciaByAvaliacaoId(Long idAvaliacao);

}
