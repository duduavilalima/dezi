package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.ArvoreTipoCompetenciaTO;
import br.com.fasolti.avd.transfer.TipoCompetenciaTO;
import br.com.fasolti.avd.transfer.mobile.AvaliacaoTipoCompetenciaMobileTO;
import br.com.fasolti.avd.transfer.mobile.AvaliacaoTipoCompetenciaSyncMobileTO;
import br.com.fasolti.avd.types.Situacao;

import java.util.List;

public interface TipoCompetenciaService extends AvdService<TipoCompetenciaTO, TipoCompetenciaTO> {

	List<TipoCompetenciaTO> obterFilhosDoTipoDeComepetencia(TipoCompetenciaTO object);

	void calcularNumeroDeFilhos(TipoCompetenciaTO object);

	List<TipoCompetenciaTO> obterTiposDeCompetenciaQueNaoTemPai();

	List<TipoCompetenciaTO> obterTipoCompetenciasPai();

	List<TipoCompetenciaTO> obterTiposCompetenciasFilhosAtivos(Long idPai);

	List<ArvoreTipoCompetenciaTO> obterArvoreDeTiposDeCompetencia();

	List<TipoCompetenciaTO> obterTiposDeCompetenciaAtivos();

	void obterFilhosCargoPai(ArvoreTipoCompetenciaTO tipoTreeTOPai);

	List<ArvoreTipoCompetenciaTO> obterArvoreDeTiposDeCompetencia(Long id);

	List<TipoCompetenciaTO> listBySituacao(Situacao situacao);

	//AvaliacaoTipoCompetenciaSyncMobileTO obterAvaliacaoTipoCompetenciaMobile(AvaliacaoTipoCompetenciaSyncMobileTO avaliacaoMobileTO);

	List<TipoCompetenciaTO> obterTipoCompentenciaAscendentes(TipoCompetenciaTO tipoCompetenciaTO);

	List<TipoCompetenciaTO> findByIdPaiAndIdEmpresaOrderByNome(Long idPai, Long idEmpresa);
}
