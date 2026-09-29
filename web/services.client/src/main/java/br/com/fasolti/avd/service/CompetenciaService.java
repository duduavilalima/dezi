package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.*;
import br.com.fasolti.avd.transfer.mobile.CompetenciaMobileTO;
import br.com.fasolti.avd.transfer.simple.AvaliacaoSimpleTO;

import java.util.Date;
import java.util.List;

public interface CompetenciaService extends AvdService<CompetenciaTO, CompetenciaTO> {

	List<CompetenciaTO> findByEmpresaIdAndDescricaoAbreviadaIgnoreCase(Long idEmpresa, String descricaoAbreviada);

	List<CompetenciaPreenchimentoTO> listByParticipante(ParticipanteTO participante);

	List<AvaliacaoSimpleTO> listAvaliacaoByCompetencia(CompetenciaTO competencia);

	List<CompetenciaAvaliacao> listCompetenciaAvaliacaoByAvaliadorAndPeriodo(FuncionarioTO avaliador, Date periodo);

	CompetenciaTO obterCompetenciaProjections(Long id);

	List<CompetenciaTO> buscarCompetenciasAtivas();

	List<CompetenciaTO> buscarCompetenciasAtivasPorTipoCompetencia(Long id);

	List<CompetenciaTO> obterCompetenciasPorTipoDeCompetencia(Long idTipoCompetencia);

	List<CompetenciaPorTipoCompTreeTO> obterArvoreDeCompetenciaPorTipoCompetencia();

	CompetenciaPorTipoCompTreeTO obterHierarquiaDeTiposDeCompetenciaDeUmaCompetencia(Long idCompetencia);

	List<CompetenciaPorTipoCompTreeTO> obterArvoreDeTiposDeCompetenciaComCompetencias();

//	CompetenciaMobileTO obterCompetenciaMobileTOFullObject(CompetenciaMobileTO competenciaMobileTO);
}
