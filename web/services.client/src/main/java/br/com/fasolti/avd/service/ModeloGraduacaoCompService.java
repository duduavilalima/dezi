package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.ModeloGraduacaoCompTO;

public interface ModeloGraduacaoCompService extends AvdService<ModeloGraduacaoCompTO, ModeloGraduacaoCompTO> {

	ModeloGraduacaoCompTO getByAvaliacaoCompetenciaId(Long avaliacaoCompetenciaId);

	ModeloGraduacaoCompTO fullObject(ModeloGraduacaoCompTO modeloGraduacao);

	ModeloGraduacaoCompTO obterModeloGraduacaoCompProjections(Long idModeloGraduacao, Long idAvaliacao);

	ModeloGraduacaoCompTO getByIdModeloGraduacao(Long IdModeloGraduacao);
}

