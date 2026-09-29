package br.com.fasolti.avd.service;

import br.com.fasolti.avd.transfer.ModeloGraduacaoAvalCompTO;

public interface ModeloGraduacaoAvalCompService extends AvdService<ModeloGraduacaoAvalCompTO, ModeloGraduacaoAvalCompTO> {


	ModeloGraduacaoAvalCompTO obterModeloGraduacaoAvalCompProjections(Long avaliacaoCompetenciaId, Long modeloGraduacaoId, Long avaliacaoCompDetId);

}
