package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.DimensaoCompetenciaDetalhamentoTO;
import br.com.fasolti.avd.transfer.DimensaoTO;

import java.util.List;

public interface DimensaoCompetenciaDetalhamentoService extends AvdService<DimensaoCompetenciaDetalhamentoTO, DimensaoCompetenciaDetalhamentoTO> {

	List<DimensaoCompetenciaDetalhamentoTO> obterDetalhamentosPorDimensao(DimensaoTO dimensao);

	DimensaoCompetenciaDetalhamentoTO obterDimensaoDetalhamentoPorCompDet(Long idCompetenciaDetalhemento);

}
