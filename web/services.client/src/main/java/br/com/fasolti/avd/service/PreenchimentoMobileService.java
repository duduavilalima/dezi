package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.mobile.AvaliacaoMobileTO;
import br.com.fasolti.avd.transfer.mobile.CompetenciaMobileTO;
import br.com.fasolti.avd.transfer.mobile.PreenchimentoMobileTO;

public interface PreenchimentoMobileService extends DeziService<PreenchimentoMobileTO> {


	AvaliacaoMobileTO listAvTipoCompetenciaPorAvaliacao(Long avaliacaoId);

	CompetenciaMobileTO listPreenchimentoPorCompetencia(Long competenciaId);
}
